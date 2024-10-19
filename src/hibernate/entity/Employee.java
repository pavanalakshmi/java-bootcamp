package hibernate.entity;

import javax.persistence.*;

@Entity
@Table(name="employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "details_id") // Foreign key in employee table
    private EmployeeDetails details;

    @ManyToOne
    @JoinColumn(name = "department_id") // Foreign key in employee table
    private Department department;

    public Employee() {
    }
//
//    public Employee(int id, String firstName, String lastName, EmployeeDetails details) {
//        this.id = id;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.details = details;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public EmployeeDetails getDetails() {
        return details;
    }

    public void setDetails(EmployeeDetails details) {
        this.details = details;
    }
}
