package ca.tetervak.studentdata.data.repository;

import lombok.Data;

@Data
public class StudentEntityJdbc {

    private int id = 0;
    private String firstName = "";
    private String lastName = "";
    private String programName = "";
    private int programYear = 1;
    private boolean programCoop = false;
    private boolean programInternship = false;
}
