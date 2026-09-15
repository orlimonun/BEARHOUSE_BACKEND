package org.example.bearhouse_backend.service;

import org.example.bearhouse_backend.dto.empleado.EmpleadoRequestDTO;
import org.example.bearhouse_backend.dto.empleado.EmpleadoResponseDTO;
import org.example.bearhouse_backend.entity.Empleado;
import org.example.bearhouse_backend.entity.Rol;
import org.example.bearhouse_backend.exception.RecursoDuplicadoException;
import org.example.bearhouse_backend.exception.RecursoNoEncontradoException;
import org.example.bearhouse_backend.repository.EmpleadoRepository;
import org.example.bearhouse_backend.repository.RolRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public EmpleadoService(EmpleadoRepository empleadoRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder) {
        this.empleadoRepository = empleadoRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private EmpleadoResponseDTO convierteResponseDTO(Empleado empleado){
        EmpleadoResponseDTO dto = new EmpleadoResponseDTO();
        dto.setId(empleado.getId());
        dto.setNombre(empleado.getNombre());
        dto.setApellidos(empleado.getApellidos());
        dto.setCedula(empleado.getCedula());
        dto.setTelefono(empleado.getTelefono());
        dto.setCorreo(empleado.getCorreo());
        dto.setUsuario(empleado.getUsuario());
        dto.setSalario(empleado.getSalario());
        dto.setFechaIngreso(empleado.getFechaIngreso());
        dto.setActivo(empleado.getActivo());
        dto.setRolId(empleado.getRol().getId());
        dto.setRolNombre(empleado.getRol().getNombre());

        return dto;
    }

    public EmpleadoResponseDTO crearEmpleado(EmpleadoRequestDTO requestDTO){
        if(empleadoRepository.existsByUsuario(requestDTO.getUsuario())){
            throw new RecursoDuplicadoException(
                    "Ya existe un empleado con el usuario: " + requestDTO.getUsuario()
            );
        }
        if(requestDTO.getCedula() != null && empleadoRepository.existsByCedula(requestDTO.getCedula())){
            throw new RecursoDuplicadoException(
                    "Ya existe un empleado con la cedula: " + requestDTO.getCedula()
            );
        }
        if(requestDTO.getCorreo() != null && empleadoRepository.existsByCorreo(requestDTO.getCorreo())){
            throw new RecursoDuplicadoException(
                    "Ya existe un empleado con la correo: " + requestDTO.getCorreo()
            );
        }

        Rol rol = rolRepository.findById(requestDTO.getRolId()).orElseThrow(
                () -> new RecursoNoEncontradoException(
                        "No existe un rol con el id: " + requestDTO.getRolId()
                )
        );

        Empleado empleado = new Empleado();
        empleado.setNombre(requestDTO.getNombre());
        empleado.setApellidos(requestDTO.getApellidos());
        empleado.setCedula(requestDTO.getCedula());
        empleado.setTelefono(requestDTO.getTelefono());
        empleado.setCorreo(requestDTO.getCorreo());
        empleado.setUsuario(requestDTO.getUsuario());
        empleado.setSalario(requestDTO.getSalario());
        empleado.setFechaIngreso(requestDTO.getFechaIngreso());
        empleado.setRol(rol);

        empleado.setPassword_hash(passwordEncoder.encode(requestDTO.getPassword()));

        Empleado empleadoGuardado = empleadoRepository.save(empleado);
        return convierteResponseDTO(empleadoGuardado);
    }
    @Transactional(readOnly = true)
    public List<EmpleadoResponseDTO> listarEmpleados() {
        return empleadoRepository.findAll()
                .stream()
                .map(this::convierteResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public EmpleadoResponseDTO buscarPorId(Integer id) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un empleado con el id: " + id));
        return convierteResponseDTO(empleado);
    }


    public void eliminarEmpleado(Integer id) {
        if (!empleadoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException(
                    "No existe un empleado con el id: " + id);
        }
        empleadoRepository.deleteById(id);
    }

    public EmpleadoResponseDTO actualizarEmpleado(Integer id, EmpleadoRequestDTO requestDTO){
        Empleado empleado = empleadoRepository.findById(id).orElseThrow(()-> new RecursoNoEncontradoException(
                "No existe un empleado con el id: " + id
        ));
        if(empleadoRepository.existsByUsuarioAndIdNot(requestDTO.getUsuario(),id)){
            throw new RecursoDuplicadoException(
                    "Ya existe un empleado con el usuario: " + requestDTO.getUsuario()
            );
        }
        if(requestDTO.getCedula() != null && empleadoRepository.existsByCedulaAndIdNot(requestDTO.getCedula(), id)){
            throw new RecursoDuplicadoException(
                    "Ya existe un empleado con la cedula: " + requestDTO.getCedula()
            );
        }
        if(requestDTO.getCorreo() != null && empleadoRepository.existsByCorreoAndIdNot(requestDTO.getCorreo(), id)){
            throw new RecursoDuplicadoException(
                    "Ya existe un empleado con la correo: " + requestDTO.getCorreo()
            );
        }
        Rol rol = rolRepository.findById(requestDTO.getRolId()).orElseThrow(
                () -> new RecursoNoEncontradoException(
                        "No existe un rol con el id: " + requestDTO.getRolId()
                )
        );

        empleado.setNombre(requestDTO.getNombre());
        empleado.setApellidos(requestDTO.getApellidos());
        empleado.setCedula(requestDTO.getCedula());
        empleado.setTelefono(requestDTO.getTelefono());
        empleado.setCorreo(requestDTO.getCorreo());
        empleado.setUsuario(requestDTO.getUsuario());
        empleado.setSalario(requestDTO.getSalario());
        empleado.setFechaIngreso(requestDTO.getFechaIngreso());
        empleado.setRol(rol);

        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isBlank()){
            empleado.setPassword_hash(passwordEncoder.encode(requestDTO.getPassword()));
        }
        Empleado empleadoActualizado = empleadoRepository.save(empleado);
        return convierteResponseDTO(empleadoActualizado);
    }

}
