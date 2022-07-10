/* Alex Tetervak, Sheridan College, Ontario */
package ca.tetervak.studentdata.data.service;

import ca.tetervak.studentdata.model.StudentForm;
import ca.tetervak.studentdata.data.repository.StudentDataRepositoryJdbc;
import ca.tetervak.studentdata.data.repository.StudentEntityJdbc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentDataServiceJdbcImpl implements StudentDataService {

    private final Logger log = LoggerFactory.getLogger(StudentDataService.class);

    StudentDataRepositoryJdbc studentDataRepositoryJdbc;

    public StudentDataServiceJdbcImpl(StudentDataRepositoryJdbc repository){
        log.trace("constructor is called");
        this.studentDataRepositoryJdbc = repository;
    }

    private void copyFormToEntity(StudentForm form, StudentEntityJdbc student){
        log.trace("copyFormToEntity() is called");
        //student.setId(form.getId());
        student.setFirstName(form.getFirstName());
        student.setLastName(form.getLastName());
        student.setProgramName(form.getProgramName());
        student.setProgramYear(form.getProgramYear());
        student.setProgramCoop(form.isProgramCoop());
        student.setProgramInternship(form.isProgramInternship());
    }

    private void copyEntityToForm(StudentEntityJdbc student, StudentForm form){
        log.trace("copyEntityToForm() is called");
        form.setId(student.getId());
        form.setFirstName(student.getFirstName());
        form.setLastName(student.getLastName());
        form.setProgramName(student.getProgramName());
        form.setProgramYear(student.getProgramYear());
        form.setProgramCoop(student.isProgramCoop());
        form.setProgramInternship(student.isProgramInternship());
    }


    public void insertStudentForm(StudentForm form){
        log.trace("insertStudentForm() is called");
        StudentEntityJdbc student = new StudentEntityJdbc();
        copyFormToEntity(form, student);
        studentDataRepositoryJdbc.insert(student);
        form.setId(student.getId());
    }
    
    public StudentForm getStudentForm(int id){
        log.trace("getStudentForm() is called");
        StudentEntityJdbc student = studentDataRepositoryJdbc.get(id);
        if(student != null){
            StudentForm form = new StudentForm();
            copyEntityToForm(student, form);
            return form;
        }else{
            log.debug("the retrieved data for student id={} is null", id);
            return null;
        }

    }
    
    public List<StudentForm> getAllStudentForms(){
        log.trace("getAllStudentForms() is called");
        List<StudentForm> forms = new ArrayList<>();
        List<StudentEntityJdbc> students = studentDataRepositoryJdbc.getAll();
        for(StudentEntityJdbc entity: students){
            StudentForm form = new StudentForm();
            copyEntityToForm(entity, form);
            forms.add(form);
        }
        return forms;
    }
    
    public void updateStudentForm(StudentForm form){
        log.trace("updateStudentForm() is called");
        StudentEntityJdbc student = studentDataRepositoryJdbc.get(form.getId());
        if(student != null){
            copyFormToEntity(form, student);
            studentDataRepositoryJdbc.update(student);
        }
    }
    
    public void deleteStudentForm(int id){
        log.trace("deleteStudentForm() is called");
        studentDataRepositoryJdbc.delete(id);
    }
    
    public void deleteAllStudentForms(){
        log.trace("deleteAllStudentForms() is called");
        studentDataRepositoryJdbc.deleteAll();
    }
}
