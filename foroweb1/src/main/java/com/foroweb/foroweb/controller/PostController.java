package com.foroweb.foroweb.controller;

import com.foroweb.foroweb.dto.MateriaDTO;
import com.foroweb.foroweb.dto.MateriaPostCountDTO;
import com.foroweb.foroweb.dto.PostDTO;
import com.foroweb.foroweb.dto.PostResponseDTO;
import com.foroweb.foroweb.model.Post;
import com.foroweb.foroweb.model.Profesor;
import com.foroweb.foroweb.model.Usuario;
import com.foroweb.foroweb.model.Materia;
import com.foroweb.foroweb.service.PostService;
import com.foroweb.foroweb.service.UsuarioService;
import com.foroweb.foroweb.service.MateriaService;
import com.foroweb.foroweb.service.FileStorageService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.foroweb.foroweb.config.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private  pdf convertir;
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private MateriaService materiaService;

    @Autowired
    private FileStorageService fileStorageService;

    private final Key key;

    @Autowired
    public PostController(Key key) {
        this.key = key;
    }
    @PostMapping("/post")
    public ResponseEntity<Object> createPost(@Valid @ModelAttribute PostDTO postDTO,@RequestParam String token) {
        String imgPath="";
        String archivoPath="";
        Date todayDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            Long userId = getUserIdFromToken(token);
            Usuario usuario = usuarioService.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            Materia materia = materiaService.findById(postDTO.getMateriaId())
                    .orElseThrow(() -> new IllegalArgumentException("Materia no encontrada"));
            if (postDTO.getImg()!=null ) {
                imgPath = fileStorageService.storeFile(postDTO.getImg());
            }
            if(postDTO.getArchivo()!=null){
                archivoPath = convertir.convertToPdf(postDTO.getArchivo());

            }

            Post post = new Post();
            post.setTitulo(postDTO.getTitulo());
            post.setContenido(postDTO.getContenido());
            post.setImg(imgPath);
            post.setArchivo(archivoPath);
            post.setUsuario(usuario);
            post.setMateria(materia);
            post.setFecha(todayDate);
            Post savedPost = postService.save(post);

            PostResponseDTO responseDTO = new PostResponseDTO();
            responseDTO.setId(savedPost.getId());
            responseDTO.setTitulo(savedPost.getTitulo());
            responseDTO.setContenido(savedPost.getContenido());
            responseDTO.setImg(savedPost.getImg());
            responseDTO.setArchivo(savedPost.getArchivo());

            PostResponseDTO.UsuarioDTO usuarioDTO = new PostResponseDTO.UsuarioDTO();
            usuarioDTO.setNombre(usuario.getNombre());
            usuarioDTO.setApellido(usuario.getApellido());
            usuarioDTO.setEmail(usuario.getEmail());
            usuarioDTO.setImgUrl(usuario.getImg());
            responseDTO.setUsuario(usuarioDTO);

            PostResponseDTO.MateriaDTO materiaDTO = new PostResponseDTO.MateriaDTO();
            materiaDTO.setMateria(materia.getMateria());
            responseDTO.setMateria(materiaDTO);

            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException | IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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
    @GetMapping("/search")
    public ResponseEntity<List<PostDTO>> searchPosts(@RequestParam String query) {
        List<Post> posts = postService.findByTitleOrContentLike(query);

        List<PostDTO> postDTOs = posts.stream().map(post -> {
            PostDTO postDTO = new PostDTO();
            postDTO.setId(post.getId());
            postDTO.setTitulo(post.getTitulo());
            postDTO.setContenido(post.getContenido());

            postDTO.setUsuarioId(post.getUsuario().getId());
            postDTO.setMateriaId(post.getMateria().getId());
            return postDTO;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(postDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long id,@RequestParam String token) {
        Long userId = getUserIdFromToken(token);

        Post post = postService.findById(id).orElseThrow(() -> new IllegalArgumentException("Post no encontrado"));
        postService.updateVista(userId,post,false);
        PostResponseDTO responseDTO = new PostResponseDTO();
        responseDTO.setId(post.getId());
        responseDTO.setTitulo(post.getTitulo());
        responseDTO.setContenido(post.getContenido());
        responseDTO.setImg(post.getImg());
        responseDTO.setArchivo(post.getArchivo());
        responseDTO.setFecha(post.getFecha());
        PostResponseDTO.UsuarioDTO usuarioDTO = new PostResponseDTO.UsuarioDTO();
        Usuario usuario = post.getUsuario();
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setApellido(usuario.getApellido());
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setImgUrl(usuario.getImg());
        responseDTO.setUsuario(usuarioDTO);

        PostResponseDTO.MateriaDTO materiaDTO = new PostResponseDTO.MateriaDTO();
        Materia materia = post.getMateria();
        materiaDTO.setMateria(materia.getMateria());
        responseDTO.setMateria(materiaDTO);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/post")
    public ResponseEntity<List<PostDTO>> getAllPost() {
        List<Post> posts = postService.findAllPost();
        List<PostDTO> postDTOs = posts.stream()
                .map(post -> new PostDTO(post.getId(), post.getMateria().getId(), post.getTitulo(), post.getContenido()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(postDTOs);
    }

    @GetMapping("/usuarioPost")
    public ResponseEntity<List<PostResponseDTO>> getPostUsuario(@RequestParam String token) {
        Long userId = getUserIdFromToken(token);
        Usuario usuario = usuarioService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        List<Post> posts = postService.findusuario_idPost(userId);
        if (posts.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron publicaciones para este usuario");
        }

        // Crear una lista para almacenar los DTOs de respuesta
        List<PostResponseDTO> responseDTOs = new ArrayList<>();

        // Iterar sobre la lista de publicaciones y crear un DTO para cada una
        for (Post post : posts) {
            PostResponseDTO responseDTO = new PostResponseDTO();
            responseDTO.setId(post.getId());
            responseDTO.setTitulo(post.getTitulo());
            responseDTO.setContenido(post.getContenido());
            responseDTO.setImg(post.getImg());
            responseDTO.setArchivo(post.getArchivo());

            Materia materia = post.getMateria();
            if (materia != null) {
                PostResponseDTO.MateriaDTO materiaDTO = new PostResponseDTO.MateriaDTO();
                materiaDTO.setMateria(materia.getMateria());
                responseDTO.setMateria(materiaDTO);
            }
            // Agregar el DTO a la lista de respuestas
            responseDTOs.add(responseDTO);
        }

        // Devolver la lista de DTOs como ResponseEntity
        return ResponseEntity.ok(responseDTOs);
    }
    @GetMapping("/usuarioMaterias")
    public ResponseEntity<List<MateriaPostCountDTO>> getMateriayUsuario(@RequestParam String token) {
        Long userId = getUserIdFromToken(token);
        Usuario usuario = usuarioService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        List<MateriaPostCountDTO> materias = postService.findMateriaByUsuario(userId);
        if (materias.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron publicaciones para este usuario");
        }

        return ResponseEntity.ok(materias);}

    @GetMapping("/Vista")
    public ResponseEntity<List<PostResponseDTO>> findAllByUsuarioIdAndVistaTrue(@RequestParam String token) {
        Long userId = getUserIdFromToken(token);
        Usuario usuario = usuarioService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        List<Post> postAux = postService.findAllByUsuarioIdAndVistaTrue(userId);
        List<PostResponseDTO> postRepuesta = new ArrayList<>();

        for (Post aux : postAux) {
            PostResponseDTO postAuxiliar = new PostResponseDTO();
            postAuxiliar.setId(aux.getId());
            postAuxiliar.setTitulo(aux.getTitulo());
            postAuxiliar.setContenido(aux.getContenido());
            PostResponseDTO.MateriaDTO materiaDTO = new PostResponseDTO.MateriaDTO();
            Materia materia = aux.getMateria();
            materiaDTO.setMateria(materia.getMateria());
            postAuxiliar.setMateria(materiaDTO);
            postAuxiliar.setFecha(aux.getFecha());

            postRepuesta.add(postAuxiliar);
        }

        return ResponseEntity.ok(postRepuesta);
    }

}