package ca.tetervak.studentdata.repository;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

final class StudentRowMapperJdbc implements RowMapper<StudentEntityJdbc> {

    @Override
    public StudentEntityJdbc mapRow(ResultSet rs, int rowNum) throws SQLException {
        StudentEntityJdbc entityJdbc = new StudentEntityJdbc();
        entityJdbc.setId(rs.getInt("id"));
        entityJdbc.setFirstName(rs.getString("first_name"));
        entityJdbc.setLastName(rs.getString("last_name"));
        entityJdbc.setProgramName(rs.getString("program_name"));
        entityJdbc.setProgramYear(rs.getInt("program_year"));
        entityJdbc.setProgramCoop(rs.getBoolean("program_coop"));
        entityJdbc.setProgramInternship(rs.getBoolean("program_internship"));
        return entityJdbc;
    }
}
