package org.example.bearhouse_backend.service;

import org.example.bearhouse_backend.dto.receta.RecetaRequestDTO;
import org.example.bearhouse_backend.dto.receta.RecetaResponseDTO;
import org.example.bearhouse_backend.entity.Ingrediente;
import org.example.bearhouse_backend.entity.Platillo;
import org.example.bearhouse_backend.entity.Receta;
import org.example.bearhouse_backend.exception.RecursoDuplicadoException;
import org.example.bearhouse_backend.exception.RecursoNoEncontradoException;
import org.example.bearhouse_backend.repository.IngredienteRepository;
import org.example.bearhouse_backend.repository.PlatilloRepository;
import org.example.bearhouse_backend.repository.RecetaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecetaService {

    private final RecetaRepository recetaRepository;
    private final PlatilloRepository platilloRepository;
    private final IngredienteRepository ingredienteRepository;

    public RecetaService(RecetaRepository recetaRepository,
                         PlatilloRepository platilloRepository,
                         IngredienteRepository ingredienteRepository) {
        this.recetaRepository = recetaRepository;
        this.platilloRepository = platilloRepository;
        this.ingredienteRepository = ingredienteRepository;
    }

    private RecetaResponseDTO convertirAResponseDTO(Receta receta) {
        RecetaResponseDTO dto = new RecetaResponseDTO();
        dto.setId(receta.getId());
        dto.setPlatilloId(receta.getPlatillo().getId());
        dto.setPlatilloNombre(receta.getPlatillo().getNombre());
        dto.setIngredienteId(receta.getIngrediente().getId());
        dto.setIngredienteNombre(receta.getIngrediente().getNombre());
        dto.setUnidadMedida(receta.getIngrediente().getUnidadMedida());
        dto.setCantidad(receta.getCantidad());
        return dto;
    }

    // Metodo de ayuda: busca platillo e ingrediente, valida que existan, y arma la entidad
    private Receta construirReceta(RecetaRequestDTO dto) {
        Platillo platillo = platilloRepository.findById(dto.getPlatilloId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un platillo con el id: " + dto.getPlatilloId()));
        Ingrediente ingrediente = ingredienteRepository.findById(dto.getIngredienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un ingrediente con el id: " + dto.getIngredienteId()));

        Receta receta = new Receta();
        receta.setPlatillo(platillo);
        receta.setIngrediente(ingrediente);
        receta.setCantidad(dto.getCantidad());
        return receta;
    }

    // --- Agregar un ingrediente a la receta de un platillo ---
    @Transactional
    public RecetaResponseDTO agregarIngrediente(RecetaRequestDTO requestDTO) {
        // Validar la unicidad compuesta: ese ingrediente no puede estar ya en la receta de ese platillo
        if (recetaRepository.existsByPlatilloIdAndIngredienteId(
                requestDTO.getPlatilloId(), requestDTO.getIngredienteId())) {
            throw new RecursoDuplicadoException(
                    "Ese ingrediente ya está en la receta de ese platillo");
        }
        Receta receta = construirReceta(requestDTO);
        return convertirAResponseDTO(recetaRepository.save(receta));
    }

    // --- Ver la receta completa de un platillo ---
    @Transactional(readOnly = true)
    public List<RecetaResponseDTO> verRecetaDePlatillo(Integer platilloId) {
        // Validamos que el platillo exista, para dar un 404 claro si no
        if (!platilloRepository.existsById(platilloId)) {
            throw new RecursoNoEncontradoException(
                    "No existe un platillo con el id: " + platilloId);
        }
        return recetaRepository.findByPlatilloId(platilloId)
                .stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    // --- Actualizar la cantidad de una línea de receta ---
    @Transactional
    public RecetaResponseDTO actualizarCantidad(Integer id, RecetaRequestDTO requestDTO) {
        Receta receta = recetaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una línea de receta con el id: " + id));

        // Validar unicidad compuesta excluyendo la propia línea
        if (recetaRepository.existsByPlatilloIdAndIngredienteIdAndIdNot(
                requestDTO.getPlatilloId(), requestDTO.getIngredienteId(), id)) {
            throw new RecursoDuplicadoException(
                    "Ese ingrediente ya está en la receta de ese platillo");
        }

        Platillo platillo = platilloRepository.findById(requestDTO.getPlatilloId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un platillo con el id: " + requestDTO.getPlatilloId()));
        Ingrediente ingrediente = ingredienteRepository.findById(requestDTO.getIngredienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un ingrediente con el id: " + requestDTO.getIngredienteId()));

        receta.setPlatillo(platillo);
        receta.setIngrediente(ingrediente);
        receta.setCantidad(requestDTO.getCantidad());
        return convertirAResponseDTO(recetaRepository.save(receta));
    }

    // --- Quitar una línea de receta ---
    @Transactional
    public void quitarIngrediente(Integer id) {
        if (!recetaRepository.existsById(id)) {
            throw new RecursoNoEncontradoException(
                    "No existe una línea de receta con el id: " + id);
        }
        recetaRepository.deleteById(id);
    }
}