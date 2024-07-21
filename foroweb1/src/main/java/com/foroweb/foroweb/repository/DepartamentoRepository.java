package com.foroweb.foroweb.repository;

import com.foroweb.foroweb.model.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
    List<Departamento> findAll();
    List<Departamento> findByInstitucionId(Long institucionId);

    @Query("SELECT d FROM Departamento d WHERE d.nombre = :nombre")
    Optional<Departamento> findByNombre(@Param("nombre") String nombre);
}
