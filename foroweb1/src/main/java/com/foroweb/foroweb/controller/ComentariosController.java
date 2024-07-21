package com.foroweb.foroweb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foroweb.foroweb.config.pdf;
import com.foroweb.foroweb.dto.ComentariosDTO;
import com.foroweb.foroweb.dto.ComentariosDTORe;
import com.foroweb.foroweb.model.Comentarios;
import com.foroweb.foroweb.model.Post;
import com.foroweb.foroweb.model.Usuario;
import com.foroweb.foroweb.service.ComentariosService;
import com.foroweb.foroweb.service.FileStorageService;
import com.foroweb.foroweb.service.PostService;
import com.foroweb.foroweb.service.UsuarioService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/comentario")
public class ComentariosController {

    @Autowired
    private ComentariosService comentariosservice;

    @Autowired
    private WebSocketController webSocketController;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private pdf convertir;

    @Autowired
    private PostService postService;

    private final Key key;

    @Autowired
    public ComentariosController(Key key) {
        this.key = key;
    }

    @PostMapping("/comentarios")
    public ResponseEntity<Object> createComentarios(
            @Valid @ModelAttribute ComentariosDTO comenta,
            @RequestParam String token) {

        String imgPath = "";
        String archivoPath = "";
        Date todayDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        try {
            Long userId = getUserIdFromToken(token);
            Usuario usuario = usuarioService.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            Post post = postService.findById(comenta.getPost())
                    .orElseThrow(() -> new IllegalArgumentException("Post no encontrado"));

            if (comenta.getImg() != null && !comenta.getImg().isEmpty()) {
                imgPath = fileStorageService.storeFile(comenta.getImg());
            }
            if (comenta.getArchivo() != null && !comenta.getArchivo().isEmpty()) {
                archivoPath = convertir.convertToPdf(comenta.getArchivo());
            }

            Comentarios comentario = new Comentarios();
            comentario.setId(comenta.getId());
            comentario.setTitulo(comenta.getTitulo());
            comentario.setContenido(comenta.getContenido());
            comentario.setImg(imgPath);
            comentario.setArchivo(archivoPath);
            comentario.setFecha(todayDate);
            comentario.setUsuario(usuario);
            comentario.setPost(post);

            Comentarios comentarioGuardado = comentariosservice.createComentarios(comentario);
            postService.updatevista(post.getId(),true);

            ComentariosDTORe repuesta = new ComentariosDTORe();
            repuesta.setId(comentarioGuardado.getId());
            repuesta.setTitulo(comentarioGuardado.getTitulo());
            repuesta.setContenido(comentarioGuardado.getContenido());
            repuesta.setFecha(comentarioGuardado.getFecha());
            repuesta.setImg(comentarioGuardado.getImg());
            repuesta.setArchivo(comentarioGuardado.getArchivo());
            ComentariosDTORe.UsuarioDTO usuarioDTO = new ComentariosDTORe.UsuarioDTO();
            usuarioDTO.setNombre(usuario.getNombre());
            usuarioDTO.setApellido(usuario.getApellido());
            usuarioDTO.setEmail(usuario.getEmail());
            usuarioDTO.setImgUrl(usuario.getImg());
            repuesta.setUsuario(usuarioDTO);

            ComentariosDTORe.PostDTO postDTO = new ComentariosDTORe.PostDTO();
            postDTO.setId(post.getId());
            repuesta.setPost(postDTO);

            // Notificar al autor del post
            usuarioService.notificaciones(post.getUsuario().getId(),post.getUsuario().getNotificaciones()+1);
            webSocketController.notifyUser(post.getUsuario().getId(), repuesta);
            return ResponseEntity.ok(repuesta);
        } catch (IllegalArgumentException | IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/comentarios/{id}")
    public ResponseEntity<List<ComentariosDTORe>> findByPosdId(@PathVariable Long id, @RequestParam String token) {
        Long userId = getUserIdFromToken(token);
        Usuario usuario = usuarioService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        List<ComentariosDTORe> comentarios = comentariosservice.findByPosdId(id);
        return ResponseEntity.ok(comentarios);
    }

    private Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("userId", Long.class);
    }
}
