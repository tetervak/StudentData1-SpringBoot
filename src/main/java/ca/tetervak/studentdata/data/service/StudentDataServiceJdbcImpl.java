/* Alex Tetervak, Sheridan College, Ontario */
package ca.tetervak.studentdata.data.service;


import ca.tetervak.studentdata.model.StudentForm;
import ca.tetervak.studentdata.data.repository.StudentDataRepositoryJdbc;
import ca.tetervak.studentdata.data.repository.StudentEntityJdbc;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentDataServiceJdbcImpl implements StudentDataService {

    StudentDataRepositoryJdbc studentDataRepositoryJdbc;

    public StudentDataServiceJdbcImpl(StudentDataRepositoryJdbc repository){
        this.studentDataRepositoryJdbc = repository;
    }

    private static void copyFormToEntity(StudentForm form, StudentEntityJdbc student){
        //student.setId(form.getId());
        student.setFirstName(form.getFirstName());
        student.setLastName(form.getLastName());
        student.setProgramName(form.getProgramName());
        student.setProgramYear(form.getProgramYear());
        student.setProgramCoop(form.isProgramCoop());
        student.setProgramInternship(form.isProgramInternship());
    }

    private static void copyEntityToForm(StudentEntityJdbc student, StudentForm form){
        form.setId(student.getId());
        form.setFirstName(student.getFirstName());
        form.setLastName(student.getLastName());
        form.setProgramName(student.getProgramName());
        form.setProgramYear(student.getProgramYear());
        form.setProgramCoop(student.isProgramCoop());
        form.setProgramInternship(student.isProgramInternship());
    }


    public void insertStudentForm(StudentForm form){
        StudentEntityJdbc student = new StudentEntityJdbc();
        copyFormToEntity(form, student);
        studentDataRepositoryJdbc.insert(student);
        form.setId(student.getId());
    }
    
    public StudentForm getStudentForm(int id){
        StudentEntityJdbc student = studentDataRepositoryJdbc.get(id);
        if(student != null){
            StudentForm form = new StudentForm();
            copyEntityToForm(student, form);
            return form;
        }else{
            return null;
        }

    }
    
    public List<StudentForm> getAllStudentForms(){
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
        StudentEntityJdbc student = studentDataRepositoryJdbc.get(form.getId());
        if(student != null){
            copyFormToEntity(form, student);
            studentDataRepositoryJdbc.update(student);
        }
    }
    
    public void deleteStudentForm(int id){
       studentDataRepositoryJdbc.delete(id);
    }
    
    public void deleteAllStudentForms(){
        studentDataRepositoryJdbc.deleteAll();
    }
}
