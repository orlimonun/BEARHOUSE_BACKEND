package org.example.bearhouse_backend.service;

import org.example.bearhouse_backend.dto.categoriaplatillo.CategoriaPlatilloRequestDTO;
import org.example.bearhouse_backend.dto.categoriaplatillo.CategoriaPlatilloResponseDTO;
import org.example.bearhouse_backend.entity.CategoriaPlatillo;
import org.example.bearhouse_backend.exception.RecursoDuplicadoException;
import org.example.bearhouse_backend.exception.RecursoNoEncontradoException;
import org.example.bearhouse_backend.repository.CategoriaPlatilloRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaPlatilloService {

    private final CategoriaPlatilloRepository categoriaRepository;

    public CategoriaPlatilloService(CategoriaPlatilloRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    private CategoriaPlatilloResponseDTO convertirAResponseDTO(CategoriaPlatillo categoria) {
        CategoriaPlatilloResponseDTO dto = new CategoriaPlatilloResponseDTO();
        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        return dto;
    }

    @Transactional
    public CategoriaPlatilloResponseDTO crearCategoria(CategoriaPlatilloRequestDTO requestDTO) {
        if (categoriaRepository.existsByNombre(requestDTO.getNombre())) {
            throw new RecursoDuplicadoException(
                    "Ya existe una categoría con el nombre: " + requestDTO.getNombre());
        }
        CategoriaPlatillo categoria = new CategoriaPlatillo();
        categoria.setNombre(requestDTO.getNombre());
        return convertirAResponseDTO(categoriaRepository.save(categoria));
    }

    @Transactional(readOnly = true)
    public List<CategoriaPlatilloResponseDTO> listarCategorias() {
        return categoriaRepository.findAll()
                .stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaPlatilloResponseDTO buscarPorId(Integer id) {
        CategoriaPlatillo categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una categoría con el id: " + id));
        return convertirAResponseDTO(categoria);
    }

    @Transactional
    public CategoriaPlatilloResponseDTO actualizarCategoria(Integer id, CategoriaPlatilloRequestDTO requestDTO) {
        CategoriaPlatillo categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una categoría con el id: " + id));
        if (categoriaRepository.existsByNombreAndIdNot(requestDTO.getNombre(), id)) {
            throw new RecursoDuplicadoException(
                    "Ya existe otra categoría con el nombre: " + requestDTO.getNombre());
        }
        categoria.setNombre(requestDTO.getNombre());
        return convertirAResponseDTO(categoriaRepository.save(categoria));
    }

    @Transactional
    public void eliminarCategoria(Integer id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RecursoNoEncontradoException(
                    "No existe una categoría con el id: " + id);
        }
        categoriaRepository.deleteById(id);
    }
}