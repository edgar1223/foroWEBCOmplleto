package com.foroweb.foroweb.service;
import com.foroweb.foroweb.dto.MateriaDTO;
import com.foroweb.foroweb.model.Materia;
import com.foroweb.foroweb.repository.MateriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MateriaService {

    @Autowired
    private MateriaRepository materiaRepository;

    public List<MateriaDTO> findAll() {
        List<Materia> materias = materiaRepository.findAllMateria();
        return materias.stream()
                .map(materia -> new MateriaDTO(materia.getId(), materia.getMateria()))
                .collect(Collectors.toList());
    }

    public Optional<Materia> findById(Long id) {
        return materiaRepository.findById(id);
    }

    public Materia save(Materia materia) {
        return materiaRepository.save(materia);
    }

    public void deleteById(Long id) {
        materiaRepository.deleteById(id);
    }
}
