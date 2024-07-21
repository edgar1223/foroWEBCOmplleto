package com.foroweb.foroweb.service;

import com.foroweb.foroweb.dto.DepartamentoDTO;
import com.foroweb.foroweb.model.Departamento;
import com.foroweb.foroweb.repository.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class departamentoService {
    @Autowired
    private DepartamentoRepository departamentoRepository;
    @Transactional
    public List<Departamento> getAlldepartamentos() {
        return departamentoRepository.findAll();
    }
    @Transactional
    public List<DepartamentoDTO> getDepartamentosByInstitucionId(Long institucionId) {
        return departamentoRepository.findByInstitucionId(institucionId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private DepartamentoDTO convertToDTO(Departamento departamento) {
        DepartamentoDTO dto = new DepartamentoDTO();
        dto.setId(departamento.getId());
        dto.setNombre(departamento.getNombre());
        dto.setInstitucion(departamento.getInstitucion());
        return dto;
    }
}
