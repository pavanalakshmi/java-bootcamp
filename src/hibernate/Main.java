package hibernate;

import hibernate.entity.Employee;
import hibernate.entity.EmployeeDetails;
import hibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Main {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        EmployeeDetails details = new EmployeeDetails();
        details.setAddress("584 McLevin Avenue");
        details.setPhoneNumber("647-567-7890");

        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setDetails(details); // Set the One-to-One relationship

        session.save(employee);
        transaction.commit();

        session.close();
        HibernateUtil.shutdown();
    }
}
