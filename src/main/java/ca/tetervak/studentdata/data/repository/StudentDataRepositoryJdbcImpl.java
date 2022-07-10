/* Alex Tetervak, Sheridan College, Ontario */
package ca.tetervak.studentdata.data.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Repository
public class StudentDataRepositoryJdbcImpl implements StudentDataRepositoryJdbc {

    private final Logger log = LoggerFactory.getLogger(StudentDataRepositoryJdbc.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    public StudentDataRepositoryJdbcImpl(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            JdbcTemplate jdbcTemplate) {
        log.trace("constructor is called");
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insert(StudentEntityJdbc student) {
        log.trace("insert() is called");
        String update = "INSERT INTO student "
                + "(first_name, last_name, program_name, program_year, program_coop, program_internship) "
                + "VALUES "
                + "(:firstName, :lastName, :programName, :programYear, :programCoop, :programInternship)";
        SqlParameterSource params = new BeanPropertySqlParameterSource(student);
        GeneratedKeyHolder keys = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(update, params, keys);
        student.setId(keys.getKey() != null ? keys.getKey().intValue() : 0);
    }

    @Override
    public StudentEntityJdbc get(int id) {
        log.trace("get() is called");
        log.debug("retrieving student record for id=" + id);
        String query = "SELECT * FROM student WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        StudentEntityJdbc student = null;
        params.addValue("id", id);
        RowMapper<StudentEntityJdbc> rowMapper = new BeanPropertyRowMapper<>(StudentEntityJdbc.class);
        try {
            student = namedParameterJdbcTemplate.queryForObject(
                    query, params, rowMapper);
        } catch (DataAccessException e) {
            log.debug("the student data for given id={} could not be retrieved", id);
            // the code above throws an exception if the record is not found
        }
        return student;
    }

    @Override
    public List<StudentEntityJdbc> getAll() {
        log.trace("getAll() is called");
        RowMapper<StudentEntityJdbc> rowMapper = new BeanPropertyRowMapper<>(StudentEntityJdbc.class);
        List<StudentEntityJdbc> list = jdbcTemplate.query(
                "SELECT * FROM student ORDER BY last_name, first_name",
                rowMapper);
        log.debug("retrieved list of {} student records", list.size());
        return list;
    }

    @Override
    public void update(StudentEntityJdbc student) {
        log.trace("update() is called");
        log.debug("updating student record id=" + student.getId());
        SqlParameterSource params = new BeanPropertySqlParameterSource(student);
        namedParameterJdbcTemplate.update(
                "UPDATE student SET "
                        + "first_name = :firstName, last_name = :lastName, "
                        + "program_name = :programName, program_year = :programYear, "
                        + "program_coop = :programCoop, program_internship = :programIntership "
                        + "WHERE id = :id", params);
    }

    @Override
    public void delete(int id) {
        log.trace("delete() is called");
        log.debug("deleting student record for id=" + id);
        String update = "DELETE FROM student WHERE id = ?";
        jdbcTemplate.update(update, id);
    }

    @Override
    public void deleteAll() {
        log.trace("deleteAll() is called");
        log.debug("deleting all student records");
        String update = "TRUNCATE TABLE student";
        jdbcTemplate.update(update);
    }

}
