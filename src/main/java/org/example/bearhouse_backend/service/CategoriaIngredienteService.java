package org.example.bearhouse_backend.service;

import org.example.bearhouse_backend.dto.categoriaingrediente.CategoriaIngredienteRequestDTO;
import org.example.bearhouse_backend.dto.categoriaingrediente.CategoriaIngredienteResponseDTO;
import org.example.bearhouse_backend.entity.CategoriaIngrediente;
import org.example.bearhouse_backend.exception.RecursoDuplicadoException;
import org.example.bearhouse_backend.exception.RecursoNoEncontradoException;
import org.example.bearhouse_backend.repository.CategoriaIngredienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaIngredienteService {

    private final CategoriaIngredienteRepository categoriaRepository;

    public CategoriaIngredienteService(CategoriaIngredienteRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }


    private CategoriaIngredienteResponseDTO convertirAResponseDTO(CategoriaIngrediente categoria) {
        CategoriaIngredienteResponseDTO dto = new CategoriaIngredienteResponseDTO();
        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        return dto;
    }


    public CategoriaIngredienteResponseDTO crearCategoria(CategoriaIngredienteRequestDTO requestDTO) {
        if (categoriaRepository.existsByNombre(requestDTO.getNombre())) {
            throw new RecursoDuplicadoException(
                    "Ya existe una categoría con el nombre: " + requestDTO.getNombre());
        }
        CategoriaIngrediente categoria = new CategoriaIngrediente();
        categoria.setNombre(requestDTO.getNombre());
        CategoriaIngrediente guardada = categoriaRepository.save(categoria);
        return convertirAResponseDTO(guardada);
    }

    @Transactional(readOnly = true)
    public List<CategoriaIngredienteResponseDTO> listarCategorias() {
        return categoriaRepository.findAll()
                .stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaIngredienteResponseDTO buscarPorId(Integer id) {
        CategoriaIngrediente categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una categoría con el id: " + id));
        return convertirAResponseDTO(categoria);
    }


    public CategoriaIngredienteResponseDTO actualizarCategoria(
            Integer id, CategoriaIngredienteRequestDTO requestDTO) {
        CategoriaIngrediente categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una categoría con el id: " + id));

        if (categoriaRepository.existsByNombreAndIdNot(requestDTO.getNombre(), id)) {
            throw new RecursoDuplicadoException(
                    "Ya existe otra categoría con el nombre: " + requestDTO.getNombre());
        }

        categoria.setNombre(requestDTO.getNombre());
        CategoriaIngrediente actualizada = categoriaRepository.save(categoria);
        return convertirAResponseDTO(actualizada);
    }


    public void eliminarCategoria(Integer id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RecursoNoEncontradoException(
                    "No existe una categoría con el id: " + id);
        }
        categoriaRepository.deleteById(id);
    }
}