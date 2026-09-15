package org.example.bearhouse_backend.security;

import org.example.bearhouse_backend.entity.Empleado;
import org.example.bearhouse_backend.repository.EmpleadoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final EmpleadoRepository empleadoRepository;

    public UserDetailsServiceImpl(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usuario) throws UsernameNotFoundException {
        Empleado empleado = empleadoRepository.findByUsuarioConRol(usuario)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "No existe un empleado con el usuario: " + usuario));
        return new EmpleadoUserDetails(empleado);
    }
}