package org.example.bearhouse_backend.service;

import org.example.bearhouse_backend.dto.orden.DetalleOrdenRequestDTO;
import org.example.bearhouse_backend.dto.orden.DetalleOrdenResponseDTO;
import org.example.bearhouse_backend.dto.orden.OrdenRequestDTO;
import org.example.bearhouse_backend.dto.orden.OrdenResponseDTO;
import org.example.bearhouse_backend.entity.*;
import org.example.bearhouse_backend.enums.EstadoOrden;
import org.example.bearhouse_backend.enums.MetodoPago;
import org.example.bearhouse_backend.enums.TipoMovimiento;
import org.example.bearhouse_backend.enums.TipoOrden;
import org.example.bearhouse_backend.exception.RecursoNoEncontradoException;
import org.example.bearhouse_backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class OrdenService {

    private final OrdenRepository ordenRepository;
    private final PlatilloRepository platilloRepository;
    private final RecetaRepository recetaRepository;
    private final IngredienteRepository ingredienteRepository;
    private final MovimientoInventarioRepository movimientoRepository;
    private final EmpleadoRepository empleadoRepository;
    private final MesaRepository mesaRepository;

    private static final BigDecimal TARIFA_TRANSPORTE = new BigDecimal("1000");
    private static final BigDecimal KM_POR_BLOQUE = new BigDecimal("3");

    public OrdenService(OrdenRepository ordenRepository,
                        PlatilloRepository platilloRepository,
                        RecetaRepository recetaRepository,
                        IngredienteRepository ingredienteRepository,
                        MovimientoInventarioRepository movimientoRepository,
                        EmpleadoRepository empleadoRepository,
                        MesaRepository mesaRepository) {
        this.ordenRepository = ordenRepository;
        this.platilloRepository = platilloRepository;
        this.recetaRepository = recetaRepository;
        this.ingredienteRepository = ingredienteRepository;
        this.movimientoRepository = movimientoRepository;
        this.empleadoRepository = empleadoRepository;
        this.mesaRepository = mesaRepository;
    }

    // Calcula el cargo de transporte según los kilómetros.
    private BigDecimal calcularTransporte(BigDecimal kilometros) {
        if (kilometros == null || kilometros.compareTo(BigDecimal.ZERO) <= 0) {
            return TARIFA_TRANSPORTE;   // mínimo: 1 bloque aunque sea muy cerca
        }

        BigDecimal bloques = kilometros.divide(KM_POR_BLOQUE, 0, RoundingMode.CEILING);
        return bloques.multiply(TARIFA_TRANSPORTE);
    }


    private DetalleOrdenResponseDTO convertirDetalleAResponseDTO(DetalleOrden detalle) {
        DetalleOrdenResponseDTO dto = new DetalleOrdenResponseDTO();
        dto.setId(detalle.getId());
        dto.setPlatilloId(detalle.getPlatillo().getId());
        dto.setPlatilloNombre(detalle.getPlatillo().getNombre());
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setSubtotal(detalle.getSubtotal());
        return dto;
    }


    private OrdenResponseDTO convertirAResponseDTO(Orden orden) {
        OrdenResponseDTO dto = new OrdenResponseDTO();
        dto.setId(orden.getId());
        dto.setTipo(orden.getTipo().name());
        dto.setEstado(orden.getEstado().name());
        dto.setMetodoPago(orden.getMetodoPago() != null ? orden.getMetodoPago().name() : null);
        dto.setFecha(orden.getFecha());
        dto.setSubtotal(orden.getSubtotal());
        dto.setImpuesto(orden.getImpuesto());
        dto.setTransporte(orden.getTransporte());
        dto.setKilometros(orden.getKilometros());
        dto.setTotal(orden.getTotal());
        dto.setObservaciones(orden.getObservaciones());

        if (orden.getMesa() != null) {
            dto.setMesaId(orden.getMesa().getId());
            dto.setMesaNumero(orden.getMesa().getNumero());
        }

        if (orden.getEmpleado() != null) {
            dto.setEmpleadoId(orden.getEmpleado().getId());
            dto.setEmpleadoNombre(orden.getEmpleado().getNombre());
        }

        dto.setDetalles(orden.getDetalles()
                .stream()
                .map(this::convertirDetalleAResponseDTO)
                .toList());
        return dto;
    }

    @Transactional
    public OrdenResponseDTO crearOrden(OrdenRequestDTO requestDTO) {
        // 1. Parsear y validar el tipo de orden
        TipoOrden tipo = parsearTipo(requestDTO.getTipo());

        // 2. Crear la orden (encabezado)
        Orden orden = new Orden();
        orden.setTipo(tipo);
        orden.setObservaciones(requestDTO.getObservaciones());

        // 3. Metodo de pago (opcional)
        if (requestDTO.getMetodoPago() != null) {
            orden.setMetodoPago(parsearMetodoPago(requestDTO.getMetodoPago()));
        }

        // 4. Empleado (opcional)
        if (requestDTO.getEmpleadoId() != null) {
            Empleado empleado = empleadoRepository.findById(requestDTO.getEmpleadoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No existe un empleado con el id: " + requestDTO.getEmpleadoId()));
            orden.setEmpleado(empleado);
        }

        // 5. Mesa (opcional, solo tiene sentido para LOCAL)
        if (requestDTO.getMesaId() != null) {
            Mesa mesa = mesaRepository.findById(requestDTO.getMesaId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No existe una mesa con el id: " + requestDTO.getMesaId()));
            orden.setMesa(mesa);
        }

        // 6. Procesar cada platillo: precio, subtotal, descuento de stock vía receta
        BigDecimal subtotalOrden = BigDecimal.ZERO;

        for (DetalleOrdenRequestDTO detalleDTO : requestDTO.getDetalles()) {
            // 6a. Buscar el platillo y su precio oficial
            Platillo platillo = platilloRepository.findById(detalleDTO.getPlatilloId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No existe un platillo con el id: " + detalleDTO.getPlatilloId()));

            // 6b. Calcular el subtotal de la línea (precio × cantidad)
            BigDecimal cantidad = new BigDecimal(detalleDTO.getCantidad());
            BigDecimal subtotalLinea = platillo.getPrecio().multiply(cantidad);

            // 6c. Crear el detalle y asociarlo a la orden
            DetalleOrden detalle = new DetalleOrden();
            detalle.setOrden(orden);
            detalle.setPlatillo(platillo);
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setPrecioUnitario(platillo.getPrecio());
            detalle.setSubtotal(subtotalLinea);
            orden.getDetalles().add(detalle);

            // 6d. Acumular el subtotal de la orden
            subtotalOrden = subtotalOrden.add(subtotalLinea);

            // 6e. DESCONTAR EL STOCK recorriendo la receta del platillo
            List<Receta> receta = recetaRepository.findByPlatilloId(platillo.getId());
            for (Receta lineaReceta : receta) {
                Ingrediente ingrediente = lineaReceta.getIngrediente();

                // Cantidad a descontar = cantidad en receta × cuántos platillos se venden
                BigDecimal cantidadDescontar = lineaReceta.getCantidad()
                        .multiply(new BigDecimal(detalleDTO.getCantidad()));

                // Bajar el stock
                BigDecimal nuevoStock = ingrediente.getStockActual().subtract(cantidadDescontar);
                ingrediente.setStockActual(nuevoStock);
                ingredienteRepository.save(ingrediente);

                // Registrar el movimiento de SALIDA
                MovimientoInventario movimiento = new MovimientoInventario();
                movimiento.setIngrediente(ingrediente);
                movimiento.setTipo(TipoMovimiento.SALIDA);
                movimiento.setCantidad(cantidadDescontar);
                movimiento.setMotivo("Venta - " + platillo.getNombre());
                movimiento.setEmpleado(orden.getEmpleado());
                movimiento.setReferenciaTipo("ORDEN");
                movimientoRepository.save(movimiento);
            }
        }

        // 7. Calcular el transporte (solo si es EXPRESS)
        BigDecimal transporte = BigDecimal.ZERO;
        if (tipo == TipoOrden.EXPRESS) {
            transporte = calcularTransporte(requestDTO.getKilometros());
            orden.setKilometros(requestDTO.getKilometros());
        }
        orden.setTransporte(transporte);

        // 8. Calcular totales: subtotal + impuesto (0) + transporte
        orden.setSubtotal(subtotalOrden);
        orden.setImpuesto(BigDecimal.ZERO);
        orden.setTotal(subtotalOrden.add(transporte));

        // 9. Guardar la orden (con detalles en cascada) y devolver
        Orden ordenGuardada = ordenRepository.save(orden);
        return convertirAResponseDTO(ordenGuardada);
    }

    private TipoOrden parsearTipo(String texto) {
        try {
            return TipoOrden.valueOf(texto.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Tipo de orden inválido: " + texto + ". Válidos: LOCAL, PARA_LLEVAR, EXPRESS");
        }
    }

    private MetodoPago parsearMetodoPago(String texto) {
        try {
            return MetodoPago.valueOf(texto.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Método de pago inválido: " + texto + ". Válidos: EFECTIVO, TARJETA, SINPE, OTRO");
        }
    }


    @Transactional(readOnly = true)
    public List<OrdenResponseDTO> listarOrdenes() {
        return ordenRepository.findAll()
                .stream().map(this::convertirAResponseDTO).toList();
    }


    @Transactional(readOnly = true)
    public OrdenResponseDTO buscarPorId(Integer id) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una orden con el id: " + id));
        return convertirAResponseDTO(orden);
    }

    // --- Marcar una orden como PAGADA ---
    @Transactional
    public OrdenResponseDTO pagarOrden(Integer id, String metodoPago) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una orden con el id: " + id));

        if (orden.getEstado() != EstadoOrden.ABIERTA) {
            throw new IllegalArgumentException(
                    "Solo se pueden pagar órdenes ABIERTAS. Estado actual: " + orden.getEstado());
        }

        orden.setEstado(EstadoOrden.PAGADA);
        if (metodoPago != null) {
            orden.setMetodoPago(parsearMetodoPago(metodoPago));
        }
        return convertirAResponseDTO(ordenRepository.save(orden));
    }

    @Transactional
    public OrdenResponseDTO cancelarOrden(Integer id) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una orden con el id: " + id));

        if (orden.getEstado() == EstadoOrden.CANCELADA) {
            throw new IllegalArgumentException("La orden ya está cancelada");
        }

        // Devolver al stock los ingredientes que la orden había descontado
        for (DetalleOrden detalle : orden.getDetalles()) {
            Platillo platillo = detalle.getPlatillo();
            List<Receta> receta = recetaRepository.findByPlatilloId(platillo.getId());

            for (Receta lineaReceta : receta) {
                Ingrediente ingrediente = lineaReceta.getIngrediente();

                // Cantidad a devolver = cantidad en receta × platillos de ese detalle
                BigDecimal cantidadDevolver = lineaReceta.getCantidad()
                        .multiply(new BigDecimal(detalle.getCantidad()));

                // SUMAR de vuelta al stock (lo opuesto a la venta)
                BigDecimal nuevoStock = ingrediente.getStockActual().add(cantidadDevolver);
                ingrediente.setStockActual(nuevoStock);
                ingredienteRepository.save(ingrediente);

                // Registrar el movimiento de devolución (tipo AJUSTE)
                MovimientoInventario movimiento = new MovimientoInventario();
                movimiento.setIngrediente(ingrediente);
                movimiento.setTipo(TipoMovimiento.AJUSTE);
                movimiento.setCantidad(cantidadDevolver);
                movimiento.setMotivo("Cancelación de orden #" + orden.getId() + " - " + platillo.getNombre());
                movimiento.setEmpleado(orden.getEmpleado());
                movimiento.setReferenciaTipo("CANCELACION");
                movimiento.setReferenciaId(orden.getId());
                movimientoRepository.save(movimiento);
            }
        }

        orden.setEstado(EstadoOrden.CANCELADA);
        return convertirAResponseDTO(ordenRepository.save(orden));
    }

}