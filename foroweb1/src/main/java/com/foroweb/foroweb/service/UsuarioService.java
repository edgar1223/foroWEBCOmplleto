package com.foroweb.foroweb.service;

import com.foroweb.foroweb.config.ValidationException;
import com.foroweb.foroweb.dto.*;
import com.foroweb.foroweb.model.*;
import com.foroweb.foroweb.repository.MateriaRepository;
import com.foroweb.foroweb.repository.ProfesorRepository;
import com.foroweb.foroweb.repository.UsuarioRepository;
import  com.foroweb.foroweb.repository.DepartamentoRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.Key;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    @Autowired
    private ProfesorRepository profesorRepository;
    @Autowired
    private MateriaRepository materiaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final Key key;
    @Autowired
    private DepartamentoRepository departamentoRepository;
    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    public UsuarioService(Key key) {
        this.key = key;
    }

    public String login(String email, String password) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        if (usuario.isPresent() && passwordEncoder.matches(password, usuario.get().getPassword())) {
            return generateToken(usuario.get());
        } else {
            throw new IllegalArgumentException("Credenciales inválidas");
        }
    }

    public Map<String, String> obtenerId(String email, String password) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        Map<String, String> response = new HashMap<>();

        if (usuario.isPresent() && passwordEncoder.matches(password, usuario.get().getPassword())) {

            response.put("id", usuario.get().getId().toString());
            response.put("Tipo", usuario.get().getUser_type());
            return response;   // Ensure this returns a String
        } else {
            throw new IllegalArgumentException("Credenciales inválidas");
        }
    }

    private String generateToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("userId", usuario.getId())
                .claim("userType", usuario.getEmail())
                .signWith(key)
                .compact();
    }

    @Transactional
    public Alumno createAlumno(AlumnoDTO alumnoDTO) {
        Long id = alumnoDTO.getId();
        String id2 = alumnoDTO.getId().toString();
        String email = alumnoDTO.getEmail();
        Optional<Usuario> existingUsuario = usuarioRepository.findById(id);
        Map<String, String> response = new HashMap<>();
        if (alumnoDTO.getId() == null) {
            response.put("ID", "El campo 'id' es requerido. ");
        }
        if (alumnoDTO.getNombre() == null || alumnoDTO.getNombre().isBlank()) {
            response.put("nombre", "El campo 'nombre' es requerido. ");
        }
        if (alumnoDTO.getApellido() == null || alumnoDTO.getApellido().isBlank()) {
            response.put("apellido", "El campo 'apellido' es requerido. ");
        }
        if (alumnoDTO.getEmail() == null || alumnoDTO.getEmail().isBlank()) {
            response.put("email", "El campo 'email' es requerido. ");
        }
        if (alumnoDTO.getPassword() == null || alumnoDTO.getPassword().isBlank()) {
            response.put("password", "El campo 'password' es requerido. ");
        }
        if (alumnoDTO.getDepartamento_id() == null) {
            response.put("departamento", "El campo 'departamentoId' es requerido. ");
        }


        if (!id2.matches("\\d{8}")) {
            response.put("control", "Número de control no válido (" + id + ")");
        }
        if (!email.matches("\\d{8}@itoaxaca\\.edu\\.mx")) {
            response.put("formato", "Email no válido (" + email + "), formato valdio @itoaxaca.edu.mx ");
        }
        if (usuarioRepository.existsById(id)) {
            response.put("idRegistrado", "ID ya registrado");
        }
        if (usuarioRepository.findByEmail(alumnoDTO.getEmail()).isPresent()) {
            response.put("emailRegistrado", "Email ya registrado");
        }
        if (response.size() > 0) {
            throw new ValidationException(response);
        }


        Alumno alumno = new Alumno();
        alumno.setId(alumnoDTO.getId());
        alumno.setNombre(alumnoDTO.getNombre());
        alumno.setApellido(alumnoDTO.getApellido());
        alumno.setEmail(alumnoDTO.getEmail());
        alumno.setPassword(passwordEncoder.encode(alumnoDTO.getPassword()));
        alumno.setSemestre(alumnoDTO.getSemestre());
        Departamento departamento = departamentoRepository.findById(alumnoDTO.getDepartamento_id())
                .orElseThrow(() -> new IllegalArgumentException("Departamento no encontrado"));
        alumno.setDepartamento(departamento);
        String token = generateToken(alumno);
        return usuarioRepository.save(alumno);
    }

    public String usuarioImg(Long id) {
        Optional<Usuario> existingUsuario = usuarioRepository.findById(id);
        return existingUsuario.get().getImg();

    }

    @Transactional
    public Profesor createProfesor(ProfesorDTO profesorDTO) {
        StringBuilder mensajeError = new StringBuilder();
        String id2 = profesorDTO.getId().toString();
        Map<String, String> response = new HashMap<>();
        Long id = profesorDTO.getId();
        String email = profesorDTO.getEmail();
        String mensajeError2 = null;

        if (profesorDTO.getId() == null) {
            response.put("id", "El campo 'id' es requerido. ");
        }
        if (profesorDTO.getNombre() == null || profesorDTO.getNombre().isBlank()) {
            response.put("nombre", "El campo 'nombre' es requerido. ");
        }
        if (profesorDTO.getApellido() == null || profesorDTO.getApellido().isBlank()) {
            response.put("apellido", "El campo 'apellido' es requerido. ");
        }
        if (profesorDTO.getEmail() == null || profesorDTO.getEmail().isBlank()) {
            response.put("email", "El campo 'email' es requerido. ");
        }
        if (profesorDTO.getPassword() == null || profesorDTO.getPassword().isBlank()) {
            response.put("password", "El campo 'password' es requerido. ");
        }
        if (profesorDTO.getDepartamentoId() == null) {
            response.put("departamento", "El campo 'departamento' es requerido. ");
        }


        if (usuarioRepository.existsById(id)) {
            response.put("idRegistrado", "ID ya registrado");
        }
        if (usuarioRepository.findByEmail(profesorDTO.getEmail()).isPresent()) {
            response.put("emailRegistrado", "Email ya registrado");
        }
        if (!id2.matches("\\d{8}")) {
            response.put("controlNO", "Número de control no válido (" + id + ")");
        }
        if (!email.matches("^[a-zA-Z]+\\.[a-zA-Z]+@itoaxaca\\.edu\\.mx$")) {
            response.put("EmailNo", "Email no válido (" + email + "), formato válido nombre.apellido@itoaxaca.edu.mx ");
        }
        if (response.size() > 0) {
            throw new ValidationException(response);
        }

        Departamento departamento = departamentoRepository.findById(profesorDTO.getDepartamentoId())
                .orElseThrow(() -> new IllegalArgumentException("Departamento no encontrado"));


        Profesor profesor = new Profesor();
        profesor.setId(profesorDTO.getId());
        profesor.setNombre(profesorDTO.getNombre());
        profesor.setApellido(profesorDTO.getApellido());
        profesor.setEmail(profesorDTO.getEmail());
        profesor.setPassword(passwordEncoder.encode(profesorDTO.getPassword()));

        Optional<Departamento> departamentoOptional = departamentoRepository.findById(profesorDTO.getDepartamentoId());
        if (departamentoOptional.isPresent()) {
            profesor.setDepartamento(departamentoOptional.get());
        } else {
            throw new RuntimeException("Departamento no encontrado");
        }

        Set<Materia> materias = new HashSet<>();
        for (Long materiaId : profesorDTO.getMateriaIds()) {
            Optional<Materia> materiaOptional = materiaRepository.findById(materiaId);
            materiaOptional.ifPresent(materias::add);
        }
        profesor.setMaterias(materias);

        return profesorRepository.save(profesor);
    }

    public List<Alumno> getAllAlumnos() {
        return usuarioRepository.findAllAlumnos();
    }


    public List<ProfesorDTO> getAllProfesores(Long departamentoId) {
        List<Profesor> profesor = usuarioRepository.findAllProfesores(departamentoId);
        List<ProfesorDTO> profesorDTOS = new ArrayList<ProfesorDTO>();
        for (Profesor profesores : profesor) {
            List<String> materiasIdsList = getMateriasIdsAsList(profesores);
            ProfesorDTO profeAxuliar = new ProfesorDTO(
                    profesores.getId(),
                    profesores.getNombre(),
                    profesores.getApellido(),
                    profesores.getEmail(),
                    profesores.getPassword(),
                    profesores.getDepartamento().getNombre(),
                    profesores.getImg(),
                    materiasIdsList
            );
            System.out.println(profesores.getDepartamento().getNombre());
            profesorDTOS.add(profeAxuliar);
        }//Long id, String nombre, String apellido, String email, String password, Long departamentoId, String img
        return profesorDTOS;
    }

    public UsuarioResponseDTO getIdUsuario(Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            return new UsuarioResponseDTO(
                    usuario.getId(),
                    usuario.getNombre(),
                    usuario.getApellido(),
                    usuario.getEmail(),
                    usuario.getImg(),
                    usuario.getNotificaciones(),
                    usuario instanceof Profesor ? ((Profesor) usuario).getDepartamento().getId() : null,
                    usuario instanceof Alumno ? ((Alumno) usuario).getSemestre() : null
            );
        } else {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }
    @Transactional
    public void notificaciones(Long id,int notificaciones){
        try {
            usuarioRepository.updateUsuarioNotificaciones(id,notificaciones);

        }catch (Exception e) {
            throw new RuntimeException("Error al actualizar el usuario: " + e.getMessage(), e);
        }
    }
    @Transactional
    public UsuarioResponseDTO updateUsuario(Long id, UsuarioDTO usuarioDTO) {
        String imgPath = "";
        try {
            System.out.println(usuarioDTO);
            if (usuarioDTO.getImg() != null && !usuarioDTO.getImg().isEmpty()) {
                imgPath = fileStorageService.storeFile(usuarioDTO.getImg());
                usuarioRepository.updateUsuario(id,
                        usuarioDTO.getNombre(),
                        usuarioDTO.getApellido(),
                        usuarioDTO.getEmail(),
                        passwordEncoder.encode(usuarioDTO.getPassword()),
                        imgPath);
            } else {
                usuarioRepository.updateUsuarioImg(id,
                        usuarioDTO.getNombre(),
                        usuarioDTO.getApellido(),
                        usuarioDTO.getEmail(),
                        passwordEncoder.encode(usuarioDTO.getPassword()));
            }


            Usuario usuarioActualizado = usuarioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Mapear los datos al DTO
            UsuarioResponseDTO responseDTO = new UsuarioResponseDTO();
            responseDTO.setId(usuarioActualizado.getId());
            responseDTO.setNombre(usuarioActualizado.getNombre());
            responseDTO.setApellido(usuarioActualizado.getApellido());
            responseDTO.setEmail(usuarioActualizado.getEmail());
            responseDTO.setImgUrl(usuarioActualizado.getImg());

            UsuarioResponseDTO.DepartamentoDTO departamentoDTO = new UsuarioResponseDTO.DepartamentoDTO();
            departamentoDTO.setId(usuarioActualizado.getDepartamento().getId());
            departamentoDTO.setNombre(usuarioActualizado.getDepartamento().getNombre());

            responseDTO.setDepartamento(departamentoDTO);

            return responseDTO;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el usuario: " + e.getMessage(), e);
        }
    }

    public void deleteUserById(Long id) {
        usuarioRepository.deleteById(id);
    }

    public List<String> getMateriasIdsAsList(Profesor profesor) {
        Set<Materia> materiasSet = profesor.getMaterias();
        return materiasSet.stream()
                .map(Materia::getMateria) // Suponiendo que Materia tiene un método getId()
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Profesor> processExcell(MultipartFile file) throws IOException {
        Map<String, String> response = new HashMap<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (!rows.hasNext()) {
                throw new IllegalArgumentException("The file is empty");
            }

            Row headerRow = rows.next();
            List<String> headers = new ArrayList<>();
            headerRow.forEach(cell -> headers.add(cell.getStringCellValue()));

            // Verifica los encabezados
            List<String> expectedHeaders = Arrays.asList("numero_control", "nombre", "apellido", "departamento", "correo", "Materias");
            if (!headers.containsAll(expectedHeaders)) {
                response.put("Filas","error al leer los encabezado del excel");
                throw new ValidationException(response);
            }

            List<Profesor> entities = new ArrayList<>();
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                Profesor entity = new Profesor();

                // Verifica el tipo de celda antes de leer
                Cell cell = currentRow.getCell(0);
                if (cell.getCellType() == CellType.NUMERIC) {
                    long cellValue = (long) cell.getNumericCellValue();
                    String cellValueStr = String.valueOf(cellValue);
                    if (!cellValueStr.matches("\\d{8}")) {
                        response.put("control"+cellValue, "Número de control no válido (" + (long) cell.getNumericCellValue() + ")");
                        continue;
                    } else {

                    if (usuarioRepository.existsById((long) cell.getNumericCellValue())) {
                        response.put("id " + (long) cell.getNumericCellValue(), "ID  ya registrado");
                        continue;
                    } else {
                        entity.setId((long) cell.getNumericCellValue());
                    }
                }
                } else {
                    response.put("control "+ (long) cell.getNumericCellValue(), "Número de control no válido ");
                    continue;
                }

                entity.setNombre(currentRow.getCell(1).getStringCellValue());
                entity.setApellido(currentRow.getCell(2).getStringCellValue());

                Optional<Departamento> departamentoOptional = departamentoRepository.findByNombre(currentRow.getCell(3).getStringCellValue());
                if (departamentoOptional.isPresent()) {
                    entity.setDepartamento(departamentoOptional.get());
                } else {
                  response.put("Departamento "+ currentRow.getCell(3).getStringCellValue(),"Departamento , no encontrado");
                    continue;
                }

                if (!currentRow.getCell(4).getStringCellValue().matches("^[a-zA-Z]+\\.[a-zA-Z]+@itoaxaca\\.edu\\.mx$")) {
                    response.put("formato" +currentRow.getCell(4).getStringCellValue(), "Email no válido , formato valdio nombre.apellido@itoaxaca.edu.mx");
                }else{
                    if (usuarioRepository.findByEmail(currentRow.getCell(4).getStringCellValue()).isPresent()) {
                        response.put("email "+currentRow.getCell(4).getStringCellValue(), "Email ya registrado");
                    }
                    entity.setEmail(currentRow.getCell(4).getStringCellValue());

                }


                String materiasString = currentRow.getCell(5).getStringCellValue();
                String[] materiasArray = materiasString.split(",");
                Set<String> nombresMaterias = new HashSet<>(Arrays.asList(materiasArray));
                Set<Materia> materias = materiaRepository.findByNombres(nombresMaterias);
                entity.setMaterias(materias);

                // Añadir más campos según sea necesario
                entities.add(entity);
            }

            if (!response.isEmpty()) {
                throw new ValidationException(response);
            }

            usuarioRepository.saveAll(entities);
            return entities;
        }
    }
}
