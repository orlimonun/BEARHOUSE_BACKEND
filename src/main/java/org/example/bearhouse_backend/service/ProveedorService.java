package org.example.bearhouse_backend.service;

import org.example.bearhouse_backend.dto.proveedor.ProveedorRequestDTO;
import org.example.bearhouse_backend.dto.proveedor.ProveedorResponseDTO;
import org.example.bearhouse_backend.entity.Proveedor;
import org.example.bearhouse_backend.exception.RecursoNoEncontradoException;
import org.example.bearhouse_backend.repository.ProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    private ProveedorResponseDTO convertirAResponseDTO(Proveedor proveedor) {
        ProveedorResponseDTO dto = new ProveedorResponseDTO();
        dto.setId(proveedor.getId());
        dto.setNombre(proveedor.getNombre());
        dto.setContacto(proveedor.getContacto());
        dto.setTelefono(proveedor.getTelefono());
        dto.setCorreo(proveedor.getCorreo());
        dto.setDireccion(proveedor.getDireccion());
        dto.setActivo(proveedor.getActivo());
        return dto;
    }

    public ProveedorResponseDTO crearProveedor(ProveedorRequestDTO requestDTO) {
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(requestDTO.getNombre());
        proveedor.setContacto(requestDTO.getContacto());
        proveedor.setTelefono(requestDTO.getTelefono());
        proveedor.setCorreo(requestDTO.getCorreo());
        proveedor.setDireccion(requestDTO.getDireccion());
        Proveedor guardado = proveedorRepository.save(proveedor);
        return convertirAResponseDTO(guardado);
    }

    @Transactional(readOnly = true)
    public List<ProveedorResponseDTO> listarProveedores() {
        return proveedorRepository.findAll()
                .stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProveedorResponseDTO buscarPorId(Integer id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un proveedor con el id: " + id));
        return convertirAResponseDTO(proveedor);
    }

    public ProveedorResponseDTO actualizarProveedor(Integer id, ProveedorRequestDTO requestDTO) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un proveedor con el id: " + id));
        proveedor.setNombre(requestDTO.getNombre());
        proveedor.setContacto(requestDTO.getContacto());
        proveedor.setTelefono(requestDTO.getTelefono());
        proveedor.setCorreo(requestDTO.getCorreo());
        proveedor.setDireccion(requestDTO.getDireccion());
        Proveedor actualizado = proveedorRepository.save(proveedor);
        return convertirAResponseDTO(actualizado);
    }

    public void eliminarProveedor(Integer id) {
        if (!proveedorRepository.existsById(id)) {
            throw new RecursoNoEncontradoException(
                    "No existe un proveedor con el id: " + id);
        }
        proveedorRepository.deleteById(id);
    }
}
