package org.example.bearhouse_backend.service;

import org.example.bearhouse_backend.dto.auth.LoginRequestDTO;
import org.example.bearhouse_backend.dto.auth.LoginResponseDTO;
import org.example.bearhouse_backend.entity.Empleado;
import org.example.bearhouse_backend.repository.EmpleadoRepository;
import org.example.bearhouse_backend.security.EmpleadoUserDetails;
import org.example.bearhouse_backend.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmpleadoRepository empleadoRepository;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       EmpleadoRepository empleadoRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.empleadoRepository = empleadoRepository;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        // 1. Autenticar: verifica usuario y contraseña (lanza excepción si son incorrectos)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsuario(),
                        request.getPassword()));

        // 2. Si la autenticación pasó, buscar el empleado y generar el token
        Empleado empleado = empleadoRepository.findByUsuario(request.getUsuario())
                .orElseThrow();  // no debería fallar: si autenticó, existe
        EmpleadoUserDetails userDetails = new EmpleadoUserDetails(empleado);

        String token = jwtService.generarToken(userDetails);

        return new LoginResponseDTO(
                token,
                empleado.getUsuario(),
                empleado.getRol().getNombre());
    }
}