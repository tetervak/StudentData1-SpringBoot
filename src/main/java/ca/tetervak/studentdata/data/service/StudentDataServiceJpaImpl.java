package ca.tetervak.studentdata.data.service;

import ca.tetervak.studentdata.data.repository.StudentDataRepositoryJpa;
import ca.tetervak.studentdata.data.repository.StudentEntityJpa;
import ca.tetervak.studentdata.model.StudentForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class StudentDataServiceJpaImpl implements StudentDataService {

    private final StudentDataRepositoryJpa studentDataRepositoryJpa;

    StudentDataServiceJpaImpl(StudentDataRepositoryJpa studentDataRepositoryJpa){
        log.trace("constructor() is called");
        this.studentDataRepositoryJpa = studentDataRepositoryJpa;
    }

    private static void copyFormToEntity(StudentForm form, StudentEntityJpa student){
        log.trace("copyFormToEntity() is called");
        //student.setId(form.getId());
        student.setFirstName(form.getFirstName());
        student.setLastName(form.getLastName());
        student.setProgramName(form.getProgramName());
        student.setProgramYear(form.getProgramYear());
        student.setProgramCoop(form.isProgramCoop());
        student.setProgramInternship(form.isProgramInternship());
    }

    private static void copyEntityToForm(StudentEntityJpa student, StudentForm form){
        log.trace("copyEntityToForm() is called");
        form.setId(student.getId());
        form.setFirstName(student.getFirstName());
        form.setLastName(student.getLastName());
        form.setProgramName(student.getProgramName());
        form.setProgramYear(student.getProgramYear());
        form.setProgramCoop(student.getProgramCoop());
        form.setProgramInternship(student.getProgramInternship());
    }

    public void insertStudentForm(StudentForm form) {
        log.trace("insertStudentForm() is called");
        log.debug("insert student form " + form);
        StudentEntityJpa student = new StudentEntityJpa();
        copyFormToEntity(form, student);
        student = studentDataRepositoryJpa.save(student);
        form.setId(student.getId());
    }

    public List<StudentForm> getAllStudentForms() {
        log.trace("getAllStudentForms() is called");
        List<StudentForm> formList = new ArrayList<>();
        List<StudentEntityJpa> studentList = studentDataRepositoryJpa.findAll();
        for(StudentEntityJpa student: studentList){
            StudentForm form = new StudentForm();
            copyEntityToForm(student, form);
            formList.add(form);
        }
        log.trace("retrieved {} form objects", formList.size());
        return formList;
    }

    public void deleteAllStudentForms() {
        log.trace("deleteAllStudentForms() is called");
        log.debug("deleting all student forms");
        studentDataRepositoryJpa.deleteAll();
    }

    public void deleteStudentForm(int id) {
        log.trace("deleteStudentForm() is called");
        log.debug("deleting student form for id=" + id);
        studentDataRepositoryJpa.deleteById(id);
    }

    public StudentForm getStudentForm(int id) {
        log.trace("getStudentForm() is called");
        log.debug("getting student form for id=" + id);
        Optional<StudentEntityJpa> result = studentDataRepositoryJpa.findById(id);
        if(result.isPresent()){
            StudentForm form = new StudentForm();
            StudentEntityJpa student = result.get();
            copyEntityToForm(student, form);
            log.debug("the form for id={} is retrieved", id);
            return form;
        }
        log.debug("the form for id={} is not found", id);
        return null;
    }

    public void updateStudentForm(StudentForm form) {
        log.trace("updateStudentForm() is called");
        log.debug("form=" + form);
        Optional<StudentEntityJpa> result = studentDataRepositoryJpa.findById(form.getId());
        if(result.isPresent()){
            StudentEntityJpa student = result.get();
            copyFormToEntity(form, student);
            studentDataRepositoryJpa.save(student);
            //studentRepository.flush();
        }
    }
}
