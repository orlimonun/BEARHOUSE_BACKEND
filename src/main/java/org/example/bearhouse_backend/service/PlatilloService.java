package org.example.bearhouse_backend.service;

import org.example.bearhouse_backend.dto.platillo.PlatilloRequestDTO;
import org.example.bearhouse_backend.dto.platillo.PlatilloResponseDTO;
import org.example.bearhouse_backend.entity.CategoriaPlatillo;
import org.example.bearhouse_backend.entity.Platillo;
import org.example.bearhouse_backend.exception.RecursoNoEncontradoException;
import org.example.bearhouse_backend.repository.CategoriaPlatilloRepository;
import org.example.bearhouse_backend.repository.PlatilloRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlatilloService {

    private final PlatilloRepository platilloRepository;
    private final CategoriaPlatilloRepository categoriaRepository;

    public PlatilloService(PlatilloRepository platilloRepository,
                           CategoriaPlatilloRepository categoriaRepository) {
        this.platilloRepository = platilloRepository;
        this.categoriaRepository = categoriaRepository;
    }

    private PlatilloResponseDTO convertirAResponseDTO(Platillo platillo) {
        PlatilloResponseDTO dto = new PlatilloResponseDTO();
        dto.setId(platillo.getId());
        dto.setNombre(platillo.getNombre());
        dto.setDescripcion(platillo.getDescripcion());
        dto.setPrecio(platillo.getPrecio());
        dto.setDisponible(platillo.getDisponible());
        dto.setCreadoEn(platillo.getCreadoEn());
        if (platillo.getCategoria() != null) {
            dto.setCategoriaId(platillo.getCategoria().getId());
            dto.setCategoriaNombre(platillo.getCategoria().getNombre());
        }
        return dto;
    }

    // Método de ayuda para no repetir la búsqueda de categoría
    private void asignarCategoria(Platillo platillo, Integer categoriaId) {
        if (categoriaId != null) {
            CategoriaPlatillo categoria = categoriaRepository.findById(categoriaId)
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No existe una categoría de platillo con el id: " + categoriaId));
            platillo.setCategoria(categoria);
        } else {
            platillo.setCategoria(null);
        }
    }

    @Transactional
    public PlatilloResponseDTO crearPlatillo(PlatilloRequestDTO requestDTO) {
        Platillo platillo = new Platillo();
        platillo.setNombre(requestDTO.getNombre());
        platillo.setDescripcion(requestDTO.getDescripcion());
        platillo.setPrecio(requestDTO.getPrecio());
        asignarCategoria(platillo, requestDTO.getCategoriaId());
        if (requestDTO.getDisponible() != null) {
            platillo.setDisponible(requestDTO.getDisponible());
        }
        return convertirAResponseDTO(platilloRepository.save(platillo));
    }

    @Transactional(readOnly = true)
    public List<PlatilloResponseDTO> listarPlatillos() {
        return platilloRepository.findAll()
                .stream().map(this::convertirAResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<PlatilloResponseDTO> listarDisponibles() {
        return platilloRepository.findByDisponibleTrue()
                .stream().map(this::convertirAResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public PlatilloResponseDTO buscarPorId(Integer id) {
        Platillo platillo = platilloRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un platillo con el id: " + id));
        return convertirAResponseDTO(platillo);
    }

    @Transactional
    public PlatilloResponseDTO actualizarPlatillo(Integer id, PlatilloRequestDTO requestDTO) {
        Platillo platillo = platilloRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un platillo con el id: " + id));
        platillo.setNombre(requestDTO.getNombre());
        platillo.setDescripcion(requestDTO.getDescripcion());
        platillo.setPrecio(requestDTO.getPrecio());
        asignarCategoria(platillo, requestDTO.getCategoriaId());
        platillo.setDisponible(requestDTO.getDisponible() != null ? requestDTO.getDisponible() : true);
        return convertirAResponseDTO(platilloRepository.save(platillo));
    }

    @Transactional
    public void eliminarPlatillo(Integer id) {
        if (!platilloRepository.existsById(id)) {
            throw new RecursoNoEncontradoException(
                    "No existe un platillo con el id: " + id);
        }
        platilloRepository.deleteById(id);
    }
}