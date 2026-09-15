package org.example.bearhouse_backend.security;

import org.example.bearhouse_backend.entity.Empleado;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class EmpleadoUserDetails implements UserDetails {

    private final Empleado empleado;

    public EmpleadoUserDetails(Empleado empleado) {
        this.empleado = empleado;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // El rol del empleado se convierte en una "autoridad" con prefijo ROLE_
        return List.of(new SimpleGrantedAuthority("ROLE_" + empleado.getRol().getNombre().toUpperCase()));
    }

    @Override
    public String getPassword() {
        return empleado.getPassword_hash();
    }

    @Override
    public String getUsername() {
        return empleado.getUsuario();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return empleado.getActivo();   // solo empleados activos pueden entrar
    }

    // Acceso al empleado original, por si lo necesitamos
    public Empleado getEmpleado() {
        return empleado;
    }
}