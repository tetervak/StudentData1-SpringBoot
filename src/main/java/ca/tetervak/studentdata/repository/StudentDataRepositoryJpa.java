package ca.tetervak.studentdata.repository;


import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentDataRepositoryJpa extends JpaRepository<StudentEntity, Integer> {
}
