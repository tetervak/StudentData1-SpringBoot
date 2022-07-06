/* Alex Tetervak, Sheridan College, Ontario */

package ca.tetervak.studentdata.data.repository;

import java.util.List;

public interface StudentDataRepositoryJdbc {
    void insert(StudentEntityJdbc student);
    StudentEntityJdbc get(int id);
    List<StudentEntityJdbc> getAll();
    void update(StudentEntityJdbc student);
    void delete(int id);
    void deleteAll();
}
