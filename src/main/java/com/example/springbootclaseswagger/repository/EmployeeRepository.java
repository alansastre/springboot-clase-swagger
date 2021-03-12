package com.example.springbootclaseswagger.repository;

import com.example.springbootclaseswagger.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Query DSL creación consultas vía nombre de métodos

    Optional<Employee> findByEmail(String email);

    List<Employee> findByMarried(Boolean married);

    List<Employee> findAllByAgeAfter(Integer age);
}
