package ca.tetervak.studentdata.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;

@Data
public class StudentForm implements Serializable {

    private int id = 0;

    @NotBlank
    @Size(max = 30)
    @Pattern(regexp = "[A-Za-z]*")
    private String firstName = "";

    @NotBlank
    @Size(max = 30)
    @Pattern(regexp = "[A-Za-z]*")
    private String lastName = "";

    @NotBlank
    @Pattern(regexp = "(Computer Programmer|Systems Technology|Engineering Technician|Systems Technician)?")
    private String programName = "";

    @NotNull
    @Min(1)
    @Max(3)
    private int programYear = 1;

    private boolean programCoop = false;

    private boolean programInternship = false;

}

