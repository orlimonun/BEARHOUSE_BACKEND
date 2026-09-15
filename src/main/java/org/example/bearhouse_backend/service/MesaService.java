package org.example.bearhouse_backend.service;

import org.example.bearhouse_backend.dto.mesa.MesaRequestDTO;
import org.example.bearhouse_backend.dto.mesa.MesaResponseDTO;
import org.example.bearhouse_backend.entity.Mesa;
import org.example.bearhouse_backend.enums.EstadoMesa;
import org.example.bearhouse_backend.exception.RecursoDuplicadoException;
import org.example.bearhouse_backend.exception.RecursoNoEncontradoException;
import org.example.bearhouse_backend.repository.MesaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MesaService {

    private final MesaRepository mesaRepository;

    public MesaService(MesaRepository mesaRepository) {
        this.mesaRepository = mesaRepository;
    }

    private MesaResponseDTO convertirAResponseDTO(Mesa mesa) {
        MesaResponseDTO dto = new MesaResponseDTO();
        dto.setId(mesa.getId());
        dto.setNumero(mesa.getNumero());
        dto.setCapacidad(mesa.getCapacidad());
        dto.setEstado(mesa.getEstado().name());   // enum
        return dto;
    }

    // Convierte el String del DTO al enum, validando que sea un valor válido
    private EstadoMesa parsearEstado(String estadoTexto) {
        if (estadoTexto == null) {
            return EstadoMesa.LIBRE;   // por defecto
        }
        try {
            return EstadoMesa.valueOf(estadoTexto.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Estado de mesa inválido: " + estadoTexto + ". Valores válidos: LIBRE, OCUPADA, RESERVADA");
        }
    }

    @Transactional
    public MesaResponseDTO crearMesa(MesaRequestDTO requestDTO) {
        if (mesaRepository.existsByNumero(requestDTO.getNumero())) {
            throw new RecursoDuplicadoException(
                    "Ya existe una mesa con el número: " + requestDTO.getNumero());
        }
        Mesa mesa = new Mesa();
        mesa.setNumero(requestDTO.getNumero());
        mesa.setCapacidad(requestDTO.getCapacidad());
        mesa.setEstado(parsearEstado(requestDTO.getEstado()));
        return convertirAResponseDTO(mesaRepository.save(mesa));
    }

    @Transactional(readOnly = true)
    public List<MesaResponseDTO> listarMesas() {
        return mesaRepository.findAll()
                .stream().map(this::convertirAResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public MesaResponseDTO buscarPorId(Integer id) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una mesa con el id: " + id));
        return convertirAResponseDTO(mesa);
    }

    @Transactional
    public MesaResponseDTO actualizarMesa(Integer id, MesaRequestDTO requestDTO) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una mesa con el id: " + id));
        if (mesaRepository.existsByNumeroAndIdNot(requestDTO.getNumero(), id)) {
            throw new RecursoDuplicadoException(
                    "Ya existe otra mesa con el número: " + requestDTO.getNumero());
        }
        mesa.setNumero(requestDTO.getNumero());
        mesa.setCapacidad(requestDTO.getCapacidad());
        mesa.setEstado(parsearEstado(requestDTO.getEstado()));
        return convertirAResponseDTO(mesaRepository.save(mesa));
    }

    @Transactional
    public void eliminarMesa(Integer id) {
        if (!mesaRepository.existsById(id)) {
            throw new RecursoNoEncontradoException(
                    "No existe una mesa con el id: " + id);
        }
        mesaRepository.deleteById(id);
    }
}