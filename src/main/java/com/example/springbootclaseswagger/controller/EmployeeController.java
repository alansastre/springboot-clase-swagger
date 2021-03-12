package com.example.springbootclaseswagger.controller;

import com.example.springbootclaseswagger.model.Employee;
import com.example.springbootclaseswagger.repository.EmployeeRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController // Define que esto es un controlador REST
// @Controller // Define que es un controlador MVC
@RequestMapping("/api") // Prefijo api para todos los resources o endpoints
public class EmployeeController {

    /*

        @GetMapping - recuperar
        @PostMapping - crear
        @PutMapping - actualizar
        @DeleteMapping - borrar

     */
    // atributos
    private final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeRepository repository;

    public EmployeeController(EmployeeRepository repository){
        this.repository = repository;
    }


    /**
     * RETRIEVE ALL
     * It return all employees
     * @return List of employees from database
     */
    @GetMapping("/employees") // GET - recupera informacion - recuperar todos los empleados
    @ApiOperation("Encuentra todos los empleados sin filtro ni paginación")
    public List<Employee> findEmployees(){
        log.debug("REST request to find all Employees");
        return repository.findAll();
    }

    /**
     * RETRIEVE ONE
     * @param id
     * @return
     */
    @GetMapping("/employees/{id}")  // GET - recupera informacion - recuperar un empleado
    @ApiOperation("Encuentra un empleado por su id")
    public ResponseEntity<Employee> findOne(@ApiParam("Clave primaria del empleado en formato Long") @PathVariable Long id){
        log.info("REST request to find one employee by id: {}", id);
        Optional<Employee> employeeOpt = repository.findById(id);
        return employeeOpt.map(employee -> ResponseEntity.ok().body(employee))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * RETRIEVE BY PROPERTY - unique
     * @param email
     * @return
     */
    @GetMapping("/employees/email/{email}")
    @ApiOperation("Encuentra un empleado por su id")
    public ResponseEntity<Employee> filtrarPorEmail(@ApiParam("Correo electrónico en formato cadena de texto") @PathVariable String email){
        log.info("REST request to find one employee by email: {}", email);
        Optional<Employee> employeeOptional = repository.findByEmail(email);
//        if(employeeOptional.isPresent())
//            return ResponseEntity.ok().body(employeeOptional.get());
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return employeeOptional.map(
                employee -> ResponseEntity.ok().body(employee)).orElseGet(
                        () -> ResponseEntity.notFound().build());

    }

    /**
     * RETRIEVE BY PROPERTY - multiple results
     * @param married
     * @return
     */
    @GetMapping("/employees/married/{married}")
    @ApiOperation("Filtra todos por estado matrimonial")
    public ResponseEntity<List<Employee>> filterByMarried(@ApiParam("Boolean que representa si está casado o no") @PathVariable Boolean married){
        log.debug("Filter all employees by married status: {}", married);

        List<Employee> employees = repository.findByMarried(married);

        if (employees.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return ResponseEntity.ok().body(employees);
    }

    // FILTRAR POR AGE
    @GetMapping("/employees/age-greater/{age}")
    @ApiOperation("Filtra todos por edad mayor que")
    public ResponseEntity<List<Employee>> filterByAgeGreater(@PathVariable Integer age){
        log.debug("REST request to filter employees by age: {}", age);

        List<Employee> employees = repository.findAllByAgeAfter(age);
        if (employees.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return ResponseEntity.ok().body(employees);
    }



    // CALCULAR SALARIO
    /*

        1 - Recibir id del empleado sobre el que calcular el salario
        2 - Calculo salario
            if yearsInCompany > 10
                setSalary
        3 - persistir empleado con el nuevo salario en base de datos
        4 - devolver empleado con el salario calculado
     */
    @GetMapping("/employees/calculate-salary/{id}")
    public ResponseEntity<Employee> calculateSalary(@PathVariable Long id){
        log.debug("REST request to calculate salary of employee id: {}", id);

        // Retrieve employee
        Optional<Employee> employeeOpt = repository.findById(id);
        if (!employeeOpt.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        // calculate price
        Employee employee = employeeOpt.get();

        // null
        if (employee.getYearsInCompany() == null)
            return ResponseEntity.ok().body(employee);

        if (employee.getYearsInCompany() <5){
            employee.setSalary(24000D);
        }else if(employee.getYearsInCompany() > 5 && employee.getYearsInCompany() < 20){
            employee.setSalary(40000D);
        }else{
            employee.setSalary(60000D);
        }

        // persist in db and return it
        return ResponseEntity.ok().body(repository.save(employee));
    }



    /**
     * CREATE NEW
     * @param employee
     * @return
     * @throws URISyntaxException
     */
    @PostMapping("/employees")  // POST (CREAR) - recibe informacion - crear un nuevo empleado
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) throws URISyntaxException {
        log.debug("REST request to save an Employee: {} ", employee);
        if (employee.getId() != null) // != null means there is an employee in database
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Employee employeeDB = repository.save(employee);
        return ResponseEntity
                .created(new URI("/api/employees/" + employeeDB.getId()))
                .body(employeeDB);
    }

    // UPDATE ONE

    /**
     * It updates one employee
     * @param employee Employee to update
     * @return Updated employee
     */
    @PutMapping("/employees") // PUT (ACTUALIZAR) - recibe informacion - actualiza un empleado existente
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee){
        log.debug("REST request to update an Employee: {}", employee);

        if (employee.getId() == null){ // == null means want to create a new employee
            log.warn("Updating employee without id");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // Employee employeeDB = repository.save(employee);
        // return ResponseEntity.ok().body(employeeDB);
        return ResponseEntity.ok().body(repository.save(employee));
    }

    // DELETE ONE

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id){
        log.debug("REST request to delete an employee by id {}", id);
        if(!repository.existsById(id)) // Check if exist
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // DELETE ALL
    //@GetMapping("/employees/delete-all")
    // @ApiIgnore
    @DeleteMapping("/employees")
    public ResponseEntity<Void> deleteEmployees(){
        log.debug("REST request to delete all employee");
        repository.deleteAll();
        return ResponseEntity.noContent().build();
    }


    // Ejemplo Controlador MVC
//    @GetMapping
//    public String helloMVC(Model model){
//        model.addAttribute("employee", new Employee());
//        return "employee-list";
//    }
//    <span>${employee.name}</span>
}
