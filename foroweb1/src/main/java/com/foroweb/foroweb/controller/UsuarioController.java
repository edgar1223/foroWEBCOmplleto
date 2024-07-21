package com.foroweb.foroweb.controller;

import com.foroweb.foroweb.config.ValidationException;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foroweb.foroweb.dto.*;
import com.foroweb.foroweb.model.Alumno;
import com.foroweb.foroweb.model.Profesor;
import com.foroweb.foroweb.model.Usuario;
import com.foroweb.foroweb.service.FileStorageService;
import com.foroweb.foroweb.service.UsuarioService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Key;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private  FileStorageService fileStorageService;
    private final Key key;
    private  ObjectMapper objectMapper;
    @Autowired
    public UsuarioController(UsuarioService usuarioService, FileStorageService fileStorageService, Key key) {
        this.usuarioService = usuarioService;
        this.fileStorageService = fileStorageService;
        this.key = key;
        this.objectMapper = objectMapper;
    }
    @PostMapping("/alumno")
    public  ResponseEntity<Object> createAlumn(@Valid @RequestBody AlumnoDTO alumnoDTO) {
        Map<String, String> usuario = new HashMap<>();
        Map<String, Map> response = new HashMap<>();
        Map<String, String> token = new HashMap<>();
        try {
            Alumno alumno = usuarioService.createAlumno(alumnoDTO);
            String tok = usuarioService.login(alumno.getEmail(), alumnoDTO.getPassword());
            usuario.put("id", alumno.getId().toString());
            usuario.put("Tipo",alumno.getUser_type());
            response.put("usuario", usuario);

            token.put("token", tok);
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(401).body(errorResponse);
        }
    }
    private Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("userId", Long.class);
    }
    @PostMapping("/profesor")
    public  ResponseEntity<Object> createProfe(@Valid @RequestBody ProfesorDTO profeDto,@RequestParam String token) {

        try {

            Long userId = getUserIdFromToken(token);
            Usuario usuarioValido = usuarioService.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            Profesor profe = usuarioService.createProfesor(profeDto);
            List<String> materiasIdsList = usuarioService.getMateriasIdsAsList(profe);
            ProfesorDTO profeRespuesta=new ProfesorDTO(profe.getId(),profe.getNombre()
            ,profe.getApellido(),profe.getEmail(),profe.getPassword(),profe.getDepartamento().getNombre(),profe.getImg()
            ,materiasIdsList);


            return ResponseEntity.ok(profeRespuesta);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(401).body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Map<String, String> credentials) {
        Map<String, String> usuario = new HashMap<>();

        try {
            String email = credentials.get("email");
            String password = credentials.get("password");
            String tok = usuarioService.login(email, password);
            usuario =usuarioService.obtenerId(email, password);
            Map<String, Map> response = new HashMap<>();
            response.put("usuario", usuario);
            Map<String, String> token = new HashMap<>();
            token.put("token", tok);
            response.put("token", token);
            return ResponseEntity.ok(response);
        }catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Credenciales inválidas");

            return ResponseEntity.status(401).body(errorResponse);
        }
    }

    @GetMapping("/img")
    public ResponseEntity<Object> img(@RequestParam String token) {
        Map<String, String> usuario = new HashMap<>();
        Long userId = getUserIdFromToken(token);
        Usuario usuarioValido = usuarioService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        try {
            String img=usuarioService.usuarioImg(userId);
            usuario.put("img",img);
            return ResponseEntity.ok(usuario);
        }catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Credenciales inválidas");

            return ResponseEntity.status(401).body(errorResponse);
        }
    }

    @GetMapping("/token")
    public ResponseEntity<UsuarioResponseDTO> getUsuarioByToken(@RequestParam String token) {
        try {
            Long userId = getUserIdFromToken(token);
            UsuarioResponseDTO usuario = usuarioService.getIdUsuario(userId);
            return ResponseEntity.ok(usuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Object> updateUsuario(@RequestParam(required = false) MultipartFile img,
                                                @RequestParam String token,@PathVariable Long id,
                                                @ModelAttribute UsuarioDTO usuarioDTO) {
        try {
            if (img != null) {
                usuarioDTO.setImg(img);
            }
            System.out.println(usuarioDTO.getImg());
            Long userId = getUserIdFromToken(token);
            if (userId.equals(id)) {
                System.out.println("Error"+usuarioDTO);
                UsuarioResponseDTO updatedUsuario = usuarioService.updateUsuario(userId, usuarioDTO);
                return ResponseEntity.ok(updatedUsuario);
            } else {
                return ResponseEntity.status(403).body("No puedes actualizar el usuario con ID " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al actualizar el usuario: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id,@RequestParam String token) {
        Long userId = getUserIdFromToken(token);
        if (userId.equals(id)) {
        usuarioService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }
    }

    @GetMapping("/id")
    public ResponseEntity<Object>idUser(@RequestParam String token){
        Long userId = getUserIdFromToken(token);
        Map<String, Long> response = new HashMap<>();
        response.put("id",userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/notificaciones")
    public ResponseEntity<Object>notificaciones(@RequestParam String token){
        Long userId = getUserIdFromToken(token);
        UsuarioResponseDTO usuario = usuarioService.getIdUsuario(userId);
        Map<String, Integer> response = new HashMap<>();
        response.put("notificaciones",usuario.getNotificaciones());
        return ResponseEntity.ok(response);
    }
    @GetMapping("/notificaciones/{noti} ")
    public ResponseEntity<Object>updateNotificaciones(@RequestParam String token,@PathVariable int noti){
        Long userId = getUserIdFromToken(token);
        usuarioService.notificaciones(userId,noti);
        Map<String, String> response = new HashMap<>();
        response.put("notificaciones","exitoso");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/Profesor/{departamentoId}")
    public List<ProfesorDTO> getProfesores(@PathVariable Long departamentoId,@RequestParam String token) {
        Long userId = getUserIdFromToken(token);
        Usuario usuarioValido = usuarioService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return usuarioService.getAllProfesores(departamentoId);
    }

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        try {
            List<Profesor> ac= usuarioService.processExcell(file);
            response.put("message", "File processed successfully");
            return ResponseEntity.ok(response);
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getValidationErrors());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file: " + e.getMessage());
        }
    }
}

