package org.example.bearhouse_backend.service;

import org.example.bearhouse_backend.dto.ingrediente.IngredienteRequestDTO;
import org.example.bearhouse_backend.dto.ingrediente.IngredienteResponseDTO;
import org.example.bearhouse_backend.entity.CategoriaIngrediente;
import org.example.bearhouse_backend.entity.Ingrediente;
import org.example.bearhouse_backend.exception.RecursoDuplicadoException;
import org.example.bearhouse_backend.exception.RecursoNoEncontradoException;
import org.example.bearhouse_backend.repository.IngredienteRepository;
import org.example.bearhouse_backend.repository.CategoriaIngredienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IngredienteService {

    private final IngredienteRepository ingredienteRepository;
    private final CategoriaIngredienteRepository categoriaIngredienteRepository;

    public IngredienteService(IngredienteRepository ingredienteRepository, CategoriaIngredienteRepository categoriaIngredienteRepository) {
        this.ingredienteRepository = ingredienteRepository;
        this.categoriaIngredienteRepository = categoriaIngredienteRepository;
    }

    private IngredienteResponseDTO convertirResponseDTO(Ingrediente ingrediente){
        IngredienteResponseDTO dto = new IngredienteResponseDTO();
        dto.setId(ingrediente.getId());
        dto.setNombre(ingrediente.getNombre());
        dto.setUnidadMedida(ingrediente.getUnidadMedida());
        dto.setStockActual(ingrediente.getStockActual());
        dto.setStockMinimo(ingrediente.getStockMinimo());
        dto.setCostoUnitario(ingrediente.getCostoUnitario());
        dto.setActivo(ingrediente.getActivo());

        if(ingrediente.getCategoria() != null){
            dto.setCategoriaId(ingrediente.getCategoria().getId());
            dto.setCategoriaNombre(ingrediente.getCategoria().getNombre());
        }
        return dto;
    }
    public IngredienteResponseDTO crearIngrediente(IngredienteRequestDTO requestDTO){
        if(ingredienteRepository.existsByNombre(requestDTO.getNombre())){
            throw new RecursoDuplicadoException(
                    "Ya existe un ingrediente con el nombre: " + requestDTO.getNombre()
            );
        }
        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setNombre(requestDTO.getNombre());
        ingrediente.setUnidadMedida(requestDTO.getUnidadMedida());
        ingrediente.setStockMinimo(requestDTO.getStockMinimo());
        ingrediente.setCostoUnitario(requestDTO.getCostoUnitario());

        if(requestDTO.getCategoriaId() != null){
            CategoriaIngrediente categoria = categoriaIngredienteRepository.findById(requestDTO.getCategoriaId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No existe una categoria con el id: " + requestDTO.getCategoriaId()
                    ));
        }
        Ingrediente guardado = ingredienteRepository.save(ingrediente);
        return convertirResponseDTO(guardado);
    }

    @Transactional(readOnly = true)
    public List<IngredienteResponseDTO> listarIngredientes(){
        return ingredienteRepository.findAll().stream().map(this::convertirResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<IngredienteResponseDTO> listarBajoStock() {
        return ingredienteRepository.findIngredientesBajoStock()
                .stream()
                .map(this::convertirResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public IngredienteResponseDTO buscarPorId(Integer id){
        Ingrediente ingrediente = ingredienteRepository.findById(id).orElseThrow(()-> new RecursoNoEncontradoException(
                "No existe un ingrediente con el id: " + id
        ));
        return convertirResponseDTO(ingrediente);
    }

    public IngredienteResponseDTO actualizarIngrediente(Integer id, IngredienteRequestDTO requestDTO){
        Ingrediente ingrediente = ingredienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un ingrediente con el id: " + id));

        if (ingredienteRepository.existsByNombreAndIdNot(requestDTO.getNombre(), id)) {
            throw new RecursoDuplicadoException(
                    "Ya existe otro ingrediente con el nombre: " + requestDTO.getNombre());
        }

        ingrediente.setNombre(requestDTO.getNombre());
        ingrediente.setUnidadMedida(requestDTO.getUnidadMedida());
        ingrediente.setStockMinimo(requestDTO.getStockMinimo());
        ingrediente.setCostoUnitario(requestDTO.getCostoUnitario());

        if (requestDTO.getCategoriaId() != null) {
            CategoriaIngrediente categoria = categoriaIngredienteRepository.findById(requestDTO.getCategoriaId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No existe una categoría con el id: " + requestDTO.getCategoriaId()));
            ingrediente.setCategoria(categoria);
        } else {
            ingrediente.setCategoria(null);
        }

        Ingrediente actualizado = ingredienteRepository.save(ingrediente);
        return convertirResponseDTO(actualizado);
    }

    public void eliminarIngrediente(Integer id) {
        if (!ingredienteRepository.existsById(id)) {
            throw new RecursoNoEncontradoException(
                    "No existe un ingrediente con el id: " + id);
        }
        ingredienteRepository.deleteById(id);
    }

}
