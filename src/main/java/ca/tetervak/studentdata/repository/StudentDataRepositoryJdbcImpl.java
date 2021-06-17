/* Alex Tetervak, Sheridan College, Ontario */
package ca.tetervak.studentdata.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class StudentDataRepositoryJdbcImpl implements StudentDataRepositoryJdbc {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    public StudentDataRepositoryJdbcImpl(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            JdbcTemplate jdbcTemplate){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insert(StudentEntityJdbc student) {
        String update = "INSERT INTO student "
                + "(first_name, last_name, program_name, program_year, program_coop, program_internship) "
                + "VALUES "
                + "(:first_name, :last_name, :program_name, :program_year, :program_coop, :program_internship)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        GeneratedKeyHolder keys = new GeneratedKeyHolder();
        params.addValue("first_name", student.getFirstName().trim());
        params.addValue("last_name", student.getLastName().trim());
        params.addValue("program_name", student.getProgramName());
        params.addValue("program_year", student.getProgramYear());
        params.addValue("program_coop", student.isProgramCoop());
        params.addValue("program_internship", student.isProgramInternship());
        namedParameterJdbcTemplate.update(update, params, keys);
        student.setId(keys.getKey()!=null?keys.getKey().intValue():0);
    }

    @Override
    public StudentEntityJdbc get(int id) {
        String query = "SELECT * FROM student WHERE ID = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        StudentEntityJdbc student = null;
        params.addValue("id", id);
        try {
            student = namedParameterJdbcTemplate.queryForObject(
                    query, params, new StudentRowMapperJdbc());
        } catch (DataAccessException e) {
            // the code above throws an exception if the record is not found
        }
        return student;
    }

    @Override
    public List<StudentEntityJdbc> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM student ORDER BY last_name, first_name",
                new StudentRowMapperJdbc());
    }

    @Override
    public void update(StudentEntityJdbc student) {
        jdbcTemplate.update(
        "UPDATE student SET "
                + "first_name = ?, last_name = ?, "
                + "program_name = ?, program_year = ?, "
                + "program_coop = ?, program_internship = ? "
                + "WHERE id = ?",
                student.getFirstName().trim(), student.getLastName().trim(),
                student.getProgramName(), student.getProgramYear(),
                student.isProgramCoop(), student.isProgramInternship(),
                student.getId());
    }

    @Override
    public void delete(int id) {
        String update = "DELETE FROM student WHERE id = ?";
        jdbcTemplate.update(update, id);
    }

    @Override
    public void deleteAll() {
        String update = "TRUNCATE TABLE student";
        jdbcTemplate.update(update);
    }

}
