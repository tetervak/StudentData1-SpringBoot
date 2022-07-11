package ca.tetervak.studentdata.controller;

import ca.tetervak.studentdata.model.StudentForm;
import ca.tetervak.studentdata.data.service.StudentDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@Controller
public class StudentDataController {

    private static final String[] programs = {
            "--- Select Program ---",
            "Computer Programmer", "Systems Technology",
            "Engineering Technician", "Systems Technician"};

    private final StudentDataService studentDataService;

    public StudentDataController(StudentDataService studentDataService){
        log.trace("constructor is called");
        this.studentDataService = studentDataService;
    }

    @GetMapping(value={"/", "/index"})
    public String index(){
        log.trace("index() is called");
        return "Index";
    }

    @GetMapping("/add-student")
    public ModelAndView addStudent(){
        log.trace("addStudent() is called");
        ModelAndView modelAndView =
                new ModelAndView("AddStudent",
                                    "form", new StudentForm());
        modelAndView.addObject("programs", programs);
        return modelAndView;
    }

    @PostMapping("/insert-student")
    public String insertStudent(
            @Validated @ModelAttribute("form") StudentForm form,
            BindingResult bindingResult,
            Model model){
        log.trace("insertStudent() is called");
        log.debug("form=" + form);
        // checking for the input validation errors
        if (bindingResult.hasErrors()) {
            log.trace("input validation errors");
            //model.addAttribute("form", form);
            model.addAttribute("programs", programs);
            return "AddStudent";
        } else {
            log.trace("the user inputs are correct");
            studentDataService.insertStudentForm(form);
            log.debug("id=" + form.getId());
            return "redirect:confirm-insert/" + form.getId();
        }
    }

    @GetMapping("/confirm-insert/{id}")
    public String confirmInsert(@PathVariable(name = "id") String strId, Model model){
        log.trace("confirmInsert() is called");
        log.debug("id=" + strId);
        try {
            int id = Integer.parseInt(strId);
            log.trace("looking for the data in the database");
            StudentForm form = studentDataService.getStudentForm(id);
            if (form == null) {
                log.trace("no data for this id=" + id);
                return "DataNotFound";
            } else {
                log.trace("showing the data");
                model.addAttribute("form", form);
                return "ConfirmInsert";
            }
        } catch (NumberFormatException e) {
            log.trace("the id in not an integer");
            return "DataNotFound";
        }
    }

    @GetMapping("/list-students")
    public ModelAndView listStudents() {
        log.trace("listStudents() is called");
        List<StudentForm> list = studentDataService.getAllStudentForms();
        log.debug("displaying list size = " + list.size());
        return new ModelAndView("ListStudents",
                                "students", list);
    }

    @GetMapping("/delete-all")
    public String deleteAll(){
        log.trace("deleteAll() is called");
        studentDataService.deleteAllStudentForms();
        return "redirect:list-students";
    }

    @GetMapping("student-details/{id}")
    public String studentDetails(@PathVariable String id, Model model){
        log.trace("studentDetails() is called");
        log.debug("id=" + id);
        try {
            StudentForm form = studentDataService.getStudentForm(Integer.parseInt(id));
            if (form != null) {
                model.addAttribute("student", form);
                return "StudentDetails"; // show the student data in the form to edit
            } else {
                log.trace("no data for this id=" + id);
                return "DataNotFound";
            }
        } catch (NumberFormatException e) {
            log.trace("the id is missing or not an integer");
            return "DataNotFound";
        }
    }

    // a user clicks "Delete" link (in the table) to "DeleteStudent"
    @GetMapping("/delete-student")
    public String deleteStudent(@RequestParam String id, Model model) {
        log.trace("deleteStudent() is called");
        try {
            StudentForm form = studentDataService.getStudentForm(Integer.parseInt(id));
            if (form != null) {
                model.addAttribute("student", form);
                return "DeleteStudent"; // ask "Do you really want to remove?"
            } else {
                return "redirect:list-students";
            }
        } catch (NumberFormatException e) {
            return "redirect:list-students";
        }
    }

    // a user clicks "Remove Record" button in "DeleteStudent" page,
    // the form submits the data to "RemoveStudent"
    @PostMapping("/remove-student")
    public String removeStudent(@RequestParam String id) {
        log.trace("removeStudent() is called");
        log.debug("id=" + id);
        try {
            studentDataService.deleteStudentForm(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            log.trace("the id is missing or not an integer");
        }
        return "redirect:list-students";
    }

    // a user clicks "Edit" link (in the table) to "EditStudent"
    @GetMapping("/edit-student")
    public String editStudent(@RequestParam String id, Model model) {
        log.trace("editStudent() is called");
        log.debug("id=" + id);
        try {
            StudentForm form = studentDataService.getStudentForm(Integer.parseInt(id));
            if (form != null) {
                model.addAttribute("form", form);
                model.addAttribute("programs", programs);
                return "EditStudent";
            } else {
                log.trace("no data for this id=" + id);
                return "redirect:list-students";
            }
        } catch (NumberFormatException e) {
            log.trace("the id is missing or not an integer");
            return "redirect:list-students";
        }
    }

    // the form submits the data to "UpdateStudent"
    @PostMapping("/update-student")
    public String updateStudent(
            @Validated @ModelAttribute("form") StudentForm form,
            BindingResult bindingResult,
            Model model) {
        log.trace("updateStudent() is called");
        log.debug("form=" + form);
        // checking for the input validation errors
        if (bindingResult.hasErrors()) {
            log.trace("input validation errors");
            //model.addAttribute("form", form);
            model.addAttribute("programs", programs);
            return "EditStudent";
        } else {
            log.trace("the user inputs are correct");
            studentDataService.updateStudentForm(form);
            log.debug("id=" + form.getId());
            return "redirect:student-details/" + form.getId();
        }
    }
}
