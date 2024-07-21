package com.foroweb.foroweb.service;


import com.foroweb.foroweb.dto.MateriaPostCountDTO;
import com.foroweb.foroweb.model.Post;
import com.foroweb.foroweb.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UsuarioService usuarioService;

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Optional<Post> findById(Long id) {
        postRepository.updateVista(id,false);
        return postRepository.findById(id);
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }
    public void updatevista(Long id, boolean vista){

        postRepository.updateVista(id,vista);
    }

    public List<Post> findByTitleOrContentLike(String query) {
        String pattern = "%" + query + "%";
        return postRepository.findAll((Specification<Post>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.like(root.get("titulo"), pattern),
                        criteriaBuilder.like(root.get("contenido"), pattern)
                )
        );
    }
    public List<Post> findIdPost(Long id) {
        return postRepository.findIdPost(id);
    }

    public List<Post> findAllPost(){
        return  postRepository.findAllPost();
    }

    public List<Post> findAllByUsuarioIdAndVistaTrue(Long usuario_id){
        return  postRepository.findAllByUsuarioIdAndVistaTrue(usuario_id);
    }

    public  List<Post> findusuario_idPost(Long usuario_id){
        return postRepository.findUsuariIdPost(usuario_id);
    }
    public List<MateriaPostCountDTO> findMateriaByUsuario(Long id) {
        List<Object[]> result = postRepository.findMateriaByUsuario(id);
        List<MateriaPostCountDTO> materiaPostCounts = new ArrayList<>();

        for (Object[] row : result) {
            Long materiaId = (Long) row[0];
            String materiaNombre = (String) row[1];
            Long postCount = (Long) row[2];
            materiaPostCounts.add(new MateriaPostCountDTO(materiaId, materiaNombre, postCount));
        }

        return materiaPostCounts;
    }


    public void updateVista(Long id,Post post, boolean b) {
        if (Objects.equals(id, post.getUsuario().getId())){
            boolean bandera=false;
            boolean postVisto = postRepository.existsByIdAndUsuarioIdAndVista(post.getId(), id, true);
            System.out.println(postVisto+"vistp"+post.getId());
            if (postVisto) {
                System.out.println("entro");
                // Actualizar la vista del post si está marcado como visto
                postRepository.updateVista(post.getId(), false);
                usuarioService.notificaciones(id, post.getUsuario().getNotificaciones() - 1);
            }
        }
    }
}