package hibernate.entity;

import javax.persistence.*;

@Entity
@Table(name = "employee_details")
public class EmployeeDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToOne(mappedBy = "details")
    private Employee employee;

    public EmployeeDetails() {
    }

    public EmployeeDetails(int id, String address, String phoneNumber, Employee employee) {
        this.id = id;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.employee = employee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
