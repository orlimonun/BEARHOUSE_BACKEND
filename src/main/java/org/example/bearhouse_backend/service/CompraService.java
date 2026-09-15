package org.example.bearhouse_backend.service;

import org.example.bearhouse_backend.dto.compra.CompraRequestDTO;
import org.example.bearhouse_backend.dto.compra.CompraResponseDTO;
import org.example.bearhouse_backend.dto.compra.DetalleCompraRequestDTO;
import org.example.bearhouse_backend.dto.compra.DetalleCompraResponseDTO;
import org.example.bearhouse_backend.entity.*;
import org.example.bearhouse_backend.enums.TipoMovimiento;
import org.example.bearhouse_backend.exception.RecursoNoEncontradoException;
import org.example.bearhouse_backend.repository.CompraRepository;
import org.example.bearhouse_backend.repository.IngredienteRepository;
import org.example.bearhouse_backend.repository.ProveedorRepository;
import org.example.bearhouse_backend.repository.EmpleadoRepository;
import org.example.bearhouse_backend.repository.MovimientoInventarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CompraService {

    private final CompraRepository compraRepository;
    private final ProveedorRepository proveedorRepository;
    private final EmpleadoRepository empleadoRepository;
    private final IngredienteRepository ingredienteRepository;
    private final MovimientoInventarioRepository movimientoRepository;

    public CompraService(CompraRepository compraRepository,
                         ProveedorRepository proveedorRepository,
                         EmpleadoRepository empleadoRepository,
                         IngredienteRepository ingredienteRepository,
                         MovimientoInventarioRepository movimientoRepository) {
        this.compraRepository = compraRepository;
        this.proveedorRepository = proveedorRepository;
        this.empleadoRepository = empleadoRepository;
        this.ingredienteRepository = ingredienteRepository;
        this.movimientoRepository = movimientoRepository;
    }


    private DetalleCompraResponseDTO convertirDetalleAResponseDTO(DetalleCompra detalle) {
        DetalleCompraResponseDTO dto = new DetalleCompraResponseDTO();
        dto.setId(detalle.getId());
        dto.setIngredienteId(detalle.getIngrediente().getId());
        dto.setIngredienteNombre(detalle.getIngrediente().getNombre());
        dto.setCantidad(detalle.getCantidad());
        dto.setCostoUnitario(detalle.getCostoUnitario());
        dto.setSubtotal(detalle.getSubtotal());
        return dto;
    }


    private CompraResponseDTO convertirAResponseDTO(Compra compra) {
        CompraResponseDTO dto = new CompraResponseDTO();
        dto.setId(compra.getId());
        dto.setFecha(compra.getFecha());
        dto.setNumFactura(compra.getNumFactura());
        dto.setTotal(compra.getTotal());
        dto.setObservaciones(compra.getObservaciones());

        dto.setProveedorId(compra.getProveedor().getId());
        dto.setProveedorNombre(compra.getProveedor().getNombre());

        if (compra.getEmpleado() != null) {
            dto.setEmpleadoId(compra.getEmpleado().getId());
            dto.setEmpleadoNombre(compra.getEmpleado().getNombre());
        }

        List<DetalleCompraResponseDTO> detallesDTO = compra.getDetalles()
                .stream()
                .map(this::convertirDetalleAResponseDTO)
                .toList();
        dto.setDetalles(detallesDTO);

        return dto;
    }

    @Transactional
    public CompraResponseDTO crearCompra(CompraRequestDTO requestDTO) {
        // 1. Validar y obtener el proveedor (obligatorio)
        Proveedor proveedor = proveedorRepository.findById(requestDTO.getProveedorId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un proveedor con el id: " + requestDTO.getProveedorId()));

        // 2. Crear la compra (encabezado)
        Compra compra = new Compra();
        compra.setProveedor(proveedor);
        compra.setNumFactura(requestDTO.getNumFactura());
        compra.setObservaciones(requestDTO.getObservaciones());

        // 3. Empleado (opcional): solo si vino un id
        if (requestDTO.getEmpleadoId() != null) {
            Empleado empleado = empleadoRepository.findById(requestDTO.getEmpleadoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No existe un empleado con el id: " + requestDTO.getEmpleadoId()));
            compra.setEmpleado(empleado);
        }

        // 4. Procesar cada detalle: calcular subtotal, acumular total, subir stock, registrar movimiento
        BigDecimal totalCompra = BigDecimal.ZERO;

        for (DetalleCompraRequestDTO detalleDTO : requestDTO.getDetalles()) {
            // 4a. Buscar el ingrediente de esta línea
            Ingrediente ingrediente = ingredienteRepository.findById(detalleDTO.getIngredienteId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No existe un ingrediente con el id: " + detalleDTO.getIngredienteId()));

            // 4b. Calcular el subtotal de la línea (cantidad × costo unitario)
            BigDecimal subtotal = detalleDTO.getCantidad()
                    .multiply(detalleDTO.getCostoUnitario());

            // 4c. Crear el detalle y asociarlo a la compra
            DetalleCompra detalle = new DetalleCompra();
            detalle.setCompra(compra);
            detalle.setIngrediente(ingrediente);
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setCostoUnitario(detalleDTO.getCostoUnitario());
            detalle.setSubtotal(subtotal);
            compra.getDetalles().add(detalle);

            // 4d. Acumular el total de la compra
            totalCompra = totalCompra.add(subtotal);

            // 4e. SUBIR el stock del ingrediente
            BigDecimal nuevoStock = ingrediente.getStockActual().add(detalleDTO.getCantidad());
            ingrediente.setStockActual(nuevoStock);
            // actualizar el costo unitario del ingrediente al más reciente
            ingrediente.setCostoUnitario(detalleDTO.getCostoUnitario());
            ingredienteRepository.save(ingrediente);

            // 4f. Registrar el movimiento de ENTRADA en la bitácora
            MovimientoInventario movimiento = new MovimientoInventario();
            movimiento.setIngrediente(ingrediente);
            movimiento.setTipo(TipoMovimiento.ENTRADA);
            movimiento.setCantidad(detalleDTO.getCantidad());
            movimiento.setMotivo("Compra a proveedor");
            movimiento.setEmpleado(compra.getEmpleado());
            movimiento.setReferenciaTipo("COMPRA");
            // referenciaId se setea después de guardar la compra (cuando tenga id)
            movimientoRepository.save(movimiento);
        }

        // 5. Asignar el total calculado y guardar la compra (con sus detalles en cascada)
        compra.setTotal(totalCompra);
        Compra compraGuardada = compraRepository.save(compra);

        // 6. Devolver el Response
        return convertirAResponseDTO(compraGuardada);
    }


    @Transactional(readOnly = true)
    public List<CompraResponseDTO> listarCompras() {
        return compraRepository.findAll()
                .stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public CompraResponseDTO buscarPorId(Integer id) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una compra con el id: " + id));
        return convertirAResponseDTO(compra);
    }


}