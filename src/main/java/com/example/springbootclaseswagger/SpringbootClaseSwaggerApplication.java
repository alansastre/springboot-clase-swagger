package com.example.springbootclaseswagger;

import com.example.springbootclaseswagger.model.Employee;
import com.example.springbootclaseswagger.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootClaseSwaggerApplication implements CommandLineRunner {

    @Autowired
    private EmployeeRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(SpringbootClaseSwaggerApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {

        Employee employeeOfTheMonth = new Employee("Bob Esponja",
                "bob@crustaceo.com",
                false,
                23,
                "Fondo de Bikini",
                50000D);
        repository.save(employeeOfTheMonth);
    }
}
