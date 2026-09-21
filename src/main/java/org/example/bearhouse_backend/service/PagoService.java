package org.example.bearhouse_backend.service;

import org.example.bearhouse_backend.dto.pago.*;
import org.example.bearhouse_backend.entity.*;
import org.example.bearhouse_backend.enums.EstadoOrden;
import org.example.bearhouse_backend.enums.MetodoPago;
import org.example.bearhouse_backend.exception.RecursoNoEncontradoException;
import org.example.bearhouse_backend.repository.EmpleadoRepository;
import org.example.bearhouse_backend.repository.OrdenRepository;
import org.example.bearhouse_backend.repository.PagoRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final OrdenRepository ordenRepository;
    private final EmpleadoRepository empleadoRepository;

    public PagoService(PagoRepository pagoRepository,
                       OrdenRepository ordenRepository,
                       EmpleadoRepository empleadoRepository) {
        this.pagoRepository = pagoRepository;
        this.ordenRepository = ordenRepository;
        this.empleadoRepository = empleadoRepository;
    }

    // --- Registrar un pago (parcial o total) seleccionando ítems ---
    @Transactional
    public PagoResponseDTO registrarPago(Integer ordenId, PagoRequestDTO dto) {
        Orden orden = buscarOrden(ordenId);
        validarOrdenCobrable(orden);

        Pago pago = new Pago();
        pago.setOrden(orden);
        pago.setMetodoPago(parsearMetodoPago(dto.getMetodoPago()));
        pago.setEmpleado(empleadoActual());

        BigDecimal monto = BigDecimal.ZERO;

        for (ItemPagoRequestDTO item : dto.getItems()) {
            DetalleOrden linea = buscarLineaDeLaOrden(orden, item.getDetalleOrdenId());

            if (item.getCantidad() > linea.getCantidadPendiente()) {
                throw new IllegalArgumentException(
                        "De " + linea.getPlatillo().getNombre() + " solo quedan "
                                + linea.getCantidadPendiente() + " unidades pendientes de pago");
            }

            BigDecimal subtotal = linea.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(item.getCantidad()));

            // Marcar esas unidades como pagadas en la línea de la orden
            linea.setCantidadPagada(linea.getCantidadPagada() + item.getCantidad());

            DetallePago detalle = new DetallePago();
            detalle.setPago(pago);
            detalle.setDetalleOrden(linea);
            detalle.setCantidad(item.getCantidad());
            detalle.setSubtotal(subtotal);
            pago.getDetalles().add(detalle);

            monto = monto.add(subtotal);
        }

        return cerrarPago(orden, pago, monto);
    }

    // --- Pagar todo lo pendiente de una vez (el caso "todo junto") ---
    @Transactional
    public PagoResponseDTO pagarTodo(Integer ordenId, String metodoPago) {
        Orden orden = buscarOrden(ordenId);
        validarOrdenCobrable(orden);

        List<ItemPagoRequestDTO> items = new ArrayList<>();
        for (DetalleOrden linea : orden.getDetalles()) {
            if (linea.getCantidadPendiente() > 0) {
                ItemPagoRequestDTO item = new ItemPagoRequestDTO();
                item.setDetalleOrdenId(linea.getId());
                item.setCantidad(linea.getCantidadPendiente());
                items.add(item);
            }
        }

        PagoRequestDTO dto = new PagoRequestDTO();
        dto.setMetodoPago(metodoPago);
        dto.setItems(items);
        return registrarPago(ordenId, dto);
    }

    // --- Historial de pagos de una orden ---
    @Transactional(readOnly = true)
    public List<PagoResponseDTO> listarPagosDeOrden(Integer ordenId) {
        buscarOrden(ordenId); // 404 si no existe
        return pagoRepository.findByOrdenIdOrderByFechaAsc(ordenId)
                .stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    // ================= Métodos de ayuda =================

    private PagoResponseDTO cerrarPago(Orden orden, Pago pago, BigDecimal monto) {
        boolean ordenSaldada = orden.getDetalles().stream()
                .allMatch(linea -> linea.getCantidadPendiente() == 0);

        if (ordenSaldada) {
            // El transporte se cobra con el pago que salda la orden
            BigDecimal transporte = orden.getTransporte() != null ? orden.getTransporte() : BigDecimal.ZERO;
            monto = monto.add(transporte);
            orden.setEstado(EstadoOrden.PAGADA);
        } else {
            orden.setEstado(EstadoOrden.PARCIALMENTE_PAGADA);
        }

        // Solo fijamos el método en la orden si es el único usado;
        // si hubo métodos distintos, queda en OTRO (pago mixto)
        List<Pago> previos = pagoRepository.findByOrdenIdOrderByFechaAsc(orden.getId());
        boolean hayOtroMetodo = previos.stream()
                .anyMatch(p -> p.getMetodoPago() != pago.getMetodoPago());
        orden.setMetodoPago(hayOtroMetodo ? MetodoPago.OTRO : pago.getMetodoPago());
        pago.setMonto(monto);

        Pago guardado = pagoRepository.save(pago);
        return convertirAResponseDTO(guardado);
    }

    private Orden buscarOrden(Integer ordenId) {
        return ordenRepository.findById(ordenId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una orden con el id: " + ordenId));
    }

    private void validarOrdenCobrable(Orden orden) {
        if (orden.getEstado() == EstadoOrden.CANCELADA) {
            throw new IllegalArgumentException("No se puede cobrar una orden cancelada");
        }
        if (orden.getEstado() == EstadoOrden.PAGADA) {
            throw new IllegalArgumentException("Esta orden ya está pagada por completo");
        }
    }

    private DetalleOrden buscarLineaDeLaOrden(Orden orden, Integer detalleOrdenId) {
        return orden.getDetalles().stream()
                .filter(linea -> linea.getId().equals(detalleOrdenId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "La línea " + detalleOrdenId + " no pertenece a esta orden"));
    }

    private Empleado empleadoActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        return empleadoRepository.findByUsuario(auth.getName()).orElse(null);
    }

    private MetodoPago parsearMetodoPago(String texto) {
        try {
            return MetodoPago.valueOf(texto.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException(
                    "Método de pago inválido: " + texto + ". Válidos: EFECTIVO, TARJETA, SINPE, OTRO");
        }
    }

    private PagoResponseDTO convertirAResponseDTO(Pago pago) {
        PagoResponseDTO dto = new PagoResponseDTO();
        dto.setId(pago.getId());
        dto.setOrdenId(pago.getOrden().getId());
        dto.setMonto(pago.getMonto());
        dto.setMetodoPago(pago.getMetodoPago().name());
        dto.setFecha(pago.getFecha());

        if (pago.getEmpleado() != null) {
            dto.setEmpleadoNombre(pago.getEmpleado().getNombre() + " " + pago.getEmpleado().getApellidos());
        }

        BigDecimal sumaItems = BigDecimal.ZERO;
        List<DetallePagoResponseDTO> detalles = new ArrayList<>();
        for (DetallePago d : pago.getDetalles()) {
            DetallePagoResponseDTO dd = new DetallePagoResponseDTO();
            dd.setDetalleOrdenId(d.getDetalleOrden().getId());
            dd.setPlatilloNombre(d.getDetalleOrden().getPlatillo().getNombre());
            dd.setCantidad(d.getCantidad());
            dd.setSubtotal(d.getSubtotal());
            detalles.add(dd);
            sumaItems = sumaItems.add(d.getSubtotal());
        }
        dto.setDetalles(detalles);
        dto.setTransporteCobrado(pago.getMonto().subtract(sumaItems));

        return dto;
    }
}