package com.foroweb.foroweb.service;

import com.foroweb.foroweb.dto.ComentariosDTO;
import com.foroweb.foroweb.dto.ComentariosDTORe;
import com.foroweb.foroweb.dto.PostResponseDTO;
import com.foroweb.foroweb.model.Comentarios;
import com.foroweb.foroweb.model.Usuario;
import com.foroweb.foroweb.repository.ComentariosRepository;
import com.foroweb.foroweb.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service

public class ComentariosService {
    @Autowired
    private ComentariosRepository comentariosRepository;
    @Autowired
    private UsuarioService usuarioService;
public Comentarios createComentarios( Comentarios comentarios){
    return comentariosRepository.save(comentarios);
}
public List<ComentariosDTORe> findByPosdId(Long posdId){
    List<Comentarios>comentarioAux=comentariosRepository.findByPostId(posdId);
    List<ComentariosDTORe> comentarioRepu = new ArrayList<>();
    for(Comentarios come:comentarioAux){
        ComentariosDTORe aux=new ComentariosDTORe();
        aux.setId(come.getId());
        aux.setTitulo(come.getTitulo());
        aux.setContenido(come.getContenido());
        aux.setFecha(come.getFecha());
        aux.setImg(come.getImg());
        aux.setArchivo(come.getArchivo());
        Usuario usuario = usuarioService.findById(come.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Usuario not found with id: " + come.getUsuario().getId()));
        ComentariosDTORe.UsuarioDTO usuarioDTO = new ComentariosDTORe.UsuarioDTO();
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setApellido(usuario.getApellido());
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setImgUrl(usuario.getImg());
        aux.setUsuario(usuarioDTO);
        comentarioRepu.add(aux);
    }
    return comentarioRepu;
}
}
