package org.example.bearhouse_backend.controller;

import jakarta.validation.Valid;
import org.example.bearhouse_backend.dto.rol.RolRequestDTO;
import org.example.bearhouse_backend.dto.rol.RolResponseDTO;
import org.example.bearhouse_backend.service.RolService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService){
        this.rolService = rolService;
    }

    @PostMapping
    public ResponseEntity<RolResponseDTO> crearRol(
            @Valid @RequestBody RolRequestDTO requestDTO){
        RolResponseDTO rolCreado = rolService.crearRol(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(rolCreado);
    }

    @GetMapping
    public ResponseEntity<List<RolResponseDTO>> listarRoles(){
        return ResponseEntity.ok(rolService.listarRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolResponseDTO> buscarPorId(@PathVariable Integer id){
        return ResponseEntity.ok(rolService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolResponseDTO> actualizarRol(@PathVariable Integer id, @Valid @RequestBody RolRequestDTO requestDTO){
        return ResponseEntity.ok(rolService.actualizarRol(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRol(@PathVariable Integer id){
        rolService.eliminarRol(id);
        return ResponseEntity.noContent().build();
    }

}
