package org.example.bearhouse_backend.service;

import org.example.bearhouse_backend.dto.rol.RolRequestDTO;
import org.example.bearhouse_backend.dto.rol.RolResponseDTO;
import org.example.bearhouse_backend.entity.Rol;
import org.example.bearhouse_backend.exception.RecursoDuplicadoException;
import org.example.bearhouse_backend.exception.RecursoNoEncontradoException;
import org.example.bearhouse_backend.repository.RolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository){
        this.rolRepository = rolRepository;
    }

    private RolResponseDTO convertirResponseDTO (Rol rol){
        RolResponseDTO dto = new RolResponseDTO();
        dto.setId(rol.getId());
        dto.setNombre(rol.getNombre());
        dto.setDescripcion(rol.getDescripcion());
        return dto;
    }

    private Rol convertirEntidad(RolRequestDTO dto){
        Rol rol = new Rol();
        rol.setNombre(dto.getNombre());
        rol.setDescripcion(dto.getDescripcion());
        return rol;
    }

    public RolResponseDTO crearRol(RolRequestDTO requestDTO){
        if(rolRepository.existsByNombre(requestDTO.getNombre())){
            throw new RecursoDuplicadoException(
                    "Ya existe un rol con el nombre: " + requestDTO.getNombre());
        }
        Rol rol = convertirEntidad(requestDTO);
        Rol rolGuardado = rolRepository.save(rol);
        return convertirResponseDTO(rolGuardado);

    }

    @Transactional(readOnly = true)
    public List<RolResponseDTO> listarRoles(){
        return rolRepository.findAll().stream().
                map(this::convertirResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public RolResponseDTO buscarPorId(Integer id){
        Rol rol = rolRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException(
                "No existe un rol con este id: " + id));
        return convertirResponseDTO(rol);
    }

    public RolResponseDTO actualizarRol (Integer id, RolRequestDTO requestDTO){

        Rol rol = rolRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("No existe un rol con el Id: " + id)
        );
        if(rolRepository.existsByNombreAndIdNot(requestDTO.getNombre(), id)){
            throw new RecursoNoEncontradoException(
                    "Ya existe otro rol con el nombre: " + requestDTO.getNombre());
        }
        rol.setNombre(requestDTO.getNombre());
        rol.setDescripcion(requestDTO.getDescripcion());

        Rol rolActualizado = rolRepository.save(rol);
        return convertirResponseDTO(rolActualizado);
    }

    public void eliminarRol( Integer id){
        if(!rolRepository.existsById(id)){
            throw new RecursoNoEncontradoException(
                    "No existe un rol con el id: " + id
            );
        }
        rolRepository.deleteById(id);
    }

}
