package ca.tetervak.studentdata.controller;

import ca.tetervak.studentdata.model.StudentForm;
import ca.tetervak.studentdata.data.service.StudentDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class StudentDataController {

    private final Logger logger = LoggerFactory.getLogger(StudentDataController.class);

    private static final String[] programs = {
            "--- Select Program ---",
            "Computer Programmer", "Systems Technology",
            "Engineering Technician", "Systems Technician"};

    private final StudentDataService studentDataService;

    public StudentDataController(StudentDataService studentDataService){
        this.studentDataService = studentDataService;
    }

    @GetMapping(value={"/", "/index"})
    public String index(){
        logger.trace("index() is called");
        return "Index";
    }

    @GetMapping("/add-student")
    public ModelAndView addStudent(){
        logger.trace("addStudent() is called");
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
        logger.trace("insertStudent() is called");
        logger.debug("form = " + form);
        // checking for the input validation errors
        if (bindingResult.hasErrors()) {
            logger.trace("input validation errors");
            //model.addAttribute("form", form);
            model.addAttribute("programs", programs);
            return "AddStudent";
        } else {
            logger.trace("the user inputs are correct");
            studentDataService.insertStudentForm(form);
            logger.debug("id = " + form.getId());
            return "redirect:confirm-insert/" + form.getId();
        }
    }

    @GetMapping("/confirm-insert/{id}")
    public String confirmInsert(@PathVariable(name = "id") String strId, Model model){
        logger.trace("confirmInsert() is called");
        logger.debug("id = " + strId);
        try {
            int id = Integer.parseInt(strId);
            logger.trace("looking for the data in the database");
            StudentForm form = studentDataService.getStudentForm(id);
            if (form == null) {
                logger.trace("no data for this id=" + id);
                return "DataNotFound";
            } else {
                logger.trace("showing the data");
                model.addAttribute("form", form);
                return "ConfirmInsert";
            }
        } catch (NumberFormatException e) {
            logger.trace("the id in not an integer");
            return "DataNotFound";
        }
    }

    @GetMapping("/list-students")
    public ModelAndView listStudents() {
        logger.trace("listStudents() is called");
        List<StudentForm> list = studentDataService.getAllStudentForms();
        logger.debug("list size = " + list.size());
        return new ModelAndView("ListStudents",
                                "students", list);
    }

    @GetMapping("/delete-all")
    public String deleteAll(){
        logger.trace("deleteAll() is called");
        studentDataService.deleteAllStudentForms();
        return "redirect:list-students";
    }

    @GetMapping("student-details/{id}")
    public String studentDetails(@PathVariable String id, Model model){
        logger.trace("studentDetails() is called");
        logger.debug("id = " + id);
        try {
            StudentForm form = studentDataService.getStudentForm(Integer.parseInt(id));
            if (form != null) {
                model.addAttribute("student", form);
                return "StudentDetails"; // show the student data in the form to edit
            } else {
                logger.trace("no data for this id=" + id);
                return "DataNotFound";
            }
        } catch (NumberFormatException e) {
            logger.trace("the id is missing or not an integer");
            return "DataNotFound";
        }
    }

    // a user clicks "Delete" link (in the table) to "DeleteStudent"
    @GetMapping("/delete-student")
    public String deleteStudent(@RequestParam String id, Model model) {
        logger.trace("deleteStudent() is called");
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
        logger.trace("removeStudent() is called");
        logger.debug("id = " + id);
        try {
            studentDataService.deleteStudentForm(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            logger.trace("the id is missing or not an integer");
        }
        return "redirect:list-students";
    }

    // a user clicks "Edit" link (in the table) to "EditStudent"
    @GetMapping("/edit-student")
    public String editStudent(@RequestParam String id, Model model) {
        logger.trace("editStudent() is called");
        logger.debug("id = " + id);
        try {
            StudentForm form = studentDataService.getStudentForm(Integer.parseInt(id));
            if (form != null) {
                model.addAttribute("form", form);
                model.addAttribute("programs", programs);
                return "EditStudent";
            } else {
                logger.trace("no data for this id=" + id);
                return "redirect:list-students";
            }
        } catch (NumberFormatException e) {
            logger.trace("the id is missing or not an integer");
            return "redirect:list-students";
        }
    }

    // the form submits the data to "UpdateStudent"
    @PostMapping("/update-student")
    public String updateStudent(
            @Validated @ModelAttribute("form") StudentForm form,
            BindingResult bindingResult,
            Model model) {
        logger.trace("updateStudent() is called");
        logger.debug("form = " + form);
        // checking for the input validation errors
        if (bindingResult.hasErrors()) {
            logger.trace("input validation errors");
            //model.addAttribute("form", form);
            model.addAttribute("programs", programs);
            return "EditStudent";
        } else {
            logger.trace("the user inputs are correct");
            studentDataService.updateStudentForm(form);
            logger.debug("id = " + form.getId());
            return "redirect:student-details/" + form.getId();
        }
    }
}
