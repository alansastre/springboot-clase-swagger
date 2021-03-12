package com.example.springbootclaseswagger.model;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
@Table(name = "employees")
public class Employee {

    // atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue
    @ApiModelProperty("Clave primaria tipo Long")
    private Long id;
    @ApiModelProperty("Nombre en formato texto mínimo 5 letras y máximo 50")
    private String name;
    @ApiModelProperty("Correo electrónico")
    private String email;
    @ApiModelProperty("Estado matrimonial")
    private Boolean married;

    private Integer age;

    private String country;

    private Double salary;

    @Column(name="years_in_company")
    private Integer yearsInCompany;

    public Employee() {
    }

    public Employee(String name, String email, Boolean married, Integer age, String country, Double salary) {
        this.name = name;
        this.email = email;
        this.married = married;
        this.age = age;
        this.country = country;
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getMarried() {
        return married;
    }

    public void setMarried(Boolean married) {
        this.married = married;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Integer getYearsInCompany() {
        return yearsInCompany;
    }

    public void setYearsInCompany(Integer yearsInCompany) {
        this.yearsInCompany = yearsInCompany;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", married=" + married +
                ", age=" + age +
                ", country='" + country + '\'' +
                ", salary=" + salary +
                ", yearsInCompany=" + yearsInCompany +
                '}';
    }
}
