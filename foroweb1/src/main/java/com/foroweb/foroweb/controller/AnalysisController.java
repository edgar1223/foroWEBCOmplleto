package com.foroweb.foroweb.controller;

import com.foroweb.foroweb.model.Usuario;
import com.foroweb.foroweb.service.AIService;
import com.foroweb.foroweb.service.UsuarioService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Key;

@RestController
@RequestMapping("/api/analisis")
public class AnalysisController {
    private final AIService aiService;
    private final Key key;
    private final UsuarioService usuarioService;

    @Autowired
    public AnalysisController(AIService aiService,Key key,UsuarioService usuarioService) {
        this.aiService = aiService;
        this.key = key;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/analyze-posts")
    public String analyzePosts(@RequestParam String toke) {
        Long userId = getUserIdFromToken(toke);
        Usuario usuarioValido = usuarioService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return aiService.analyzePosts(userId);
    }
    @GetMapping("/profesores")
    public ResponseEntity<String>profesores(@RequestParam String toke) {
        Long userId = getUserIdFromToken(toke);
        Usuario usuarioValido = usuarioService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return aiService.profesores();
    }
    private Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("userId", Long.class);
    }
    @GetMapping("/tendencias")
    public ResponseEntity<String>tendencias(@RequestParam String toke) {
        Long userId = getUserIdFromToken(toke);
        Usuario usuarioValido = usuarioService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return aiService.materias(userId);
    }
}
