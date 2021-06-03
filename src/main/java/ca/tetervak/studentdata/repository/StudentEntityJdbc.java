package ca.tetervak.studentdata.repository;

import javax.validation.constraints.*;

public class StudentEntityJdbc {

    private int id = 0;
    private String firstName = "";
    private String lastName = "";
    private String programName = "";
    private int programYear = 1;
    private boolean programCoop = false;
    private boolean programInternship = false;

    public StudentEntityJdbc() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String program) {
        this.programName = program;
    }

    public int getProgramYear() {
        return programYear;
    }

    public void setProgramYear(int year) {
        this.programYear = year;
    }

    public boolean isProgramCoop() {
        return programCoop;
    }

    public void setProgramCoop(boolean coop) {
        this.programCoop = coop;
    }

    public boolean isProgramInternship() {
        return programInternship;
    }

    public void setProgramInternship(boolean internship) {
        this.programInternship = internship;
    }

}
