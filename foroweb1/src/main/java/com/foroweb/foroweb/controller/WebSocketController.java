package com.foroweb.foroweb.controller;

import com.foroweb.foroweb.dto.ComentariosDTORe;
import com.foroweb.foroweb.model.Comentarios;
import com.foroweb.foroweb.model.Post;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate template;

    public WebSocketController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/comment")
    @SendTo("/topic/comments")
    public Comentarios notifyComment(Comentarios comment) {
        return comment;
    }

    public void notifyUser(Long userId, ComentariosDTORe comment) {
        this.template.convertAndSend("/topic/comments/" + userId, comment);
    }
}

