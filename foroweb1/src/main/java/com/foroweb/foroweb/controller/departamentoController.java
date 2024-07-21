package com.foroweb.foroweb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foroweb.foroweb.dto.DepartamentoDTO;
import com.foroweb.foroweb.model.Departamento;
import com.foroweb.foroweb.repository.DepartamentoRepository;
import com.foroweb.foroweb.service.departamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api/deparamentos")
public class departamentoController {
    @Autowired
    private departamentoService departamentoService;

    @GetMapping("/institucion/{institucionId}")
    public ResponseEntity<List<DepartamentoDTO>> getDepartamentosByInstitucionId(@PathVariable Long institucionId) {
        List<DepartamentoDTO> departamentos = departamentoService.getDepartamentosByInstitucionId(institucionId);
        return ResponseEntity.ok(departamentos);
    }
}
