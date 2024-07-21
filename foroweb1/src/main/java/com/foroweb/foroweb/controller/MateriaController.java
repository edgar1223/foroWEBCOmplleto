package com.foroweb.foroweb.controller;
import com.foroweb.foroweb.dto.MateriaDTO;
import com.foroweb.foroweb.model.Alumno;
import com.foroweb.foroweb.model.Materia;
import com.foroweb.foroweb.service.MateriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api/materia")
public class MateriaController {
    @Autowired
    private MateriaService materiaService;
    @GetMapping("/materia")
    public ResponseEntity<List<MateriaDTO>> getAllMateria() {
        List<MateriaDTO> materias = materiaService.findAll();
        return ResponseEntity.ok(materias);
    }

}