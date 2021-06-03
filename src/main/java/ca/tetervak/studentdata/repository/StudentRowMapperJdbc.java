package ca.tetervak.studentdata.repository;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

final class StudentRowMapperJdbc implements RowMapper<StudentEntityJdbc> {

    @Override
    public StudentEntityJdbc mapRow(ResultSet rs, int rowNum) throws SQLException {
        StudentEntityJdbc form = new StudentEntityJdbc();
        form.setId(rs.getInt("id"));
        form.setFirstName(rs.getString("first_name"));
        form.setLastName(rs.getString("last_name"));
        form.setProgramName(rs.getString("program_name"));
        form.setProgramYear(rs.getInt("program_year"));
        form.setProgramCoop(rs.getBoolean("program_coop"));
        form.setProgramInternship(rs.getBoolean("program_internship"));
        return form;
    }
}
