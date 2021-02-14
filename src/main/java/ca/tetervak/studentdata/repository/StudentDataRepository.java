package ca.tetervak.studentdata.repository;


import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentDataRepository extends JpaRepository<StudentEntity, Integer> {
}
