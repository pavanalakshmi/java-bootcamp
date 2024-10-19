package hibernate;

import hibernate.entity.Address;
import hibernate.entity.Order;
import hibernate.entity.Product;
import hibernate.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HibernateCRUD {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Address.class)
                .addAnnotatedClass(Order.class)
                .addAnnotatedClass(Product.class)
                .buildSessionFactory();
        Session session = factory.openSession();
//        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
//        Session session = factory.getCurrentSession();
        try {
            User user = persistUserWithOrdersAndProducts(session);
            System.out.println("Saved User ID=" + user.getId());
            readEntities(factory, user);
            updateEntities(factory, user);
            deleteEntities(factory, user);

        } finally {
            factory.close();
        }
    }



    private static User persistUserWithOrdersAndProducts(Session session) {
        User user = new User();
        user.setUsername("Pavana");

        Address address = new Address();
        address.setStreet("584 mclevin avenue");
        address.setCity("Toronto");
        user.setAddress(address);

        Order order1 = new Order();
        order1.setOrderDate(LocalDate.now());

        Order order2 = new Order();
        order2.setOrderDate(LocalDate.now().plusDays(1));

        Product product1 = new Product();
        product1.setName("Laptop");
        product1.setPrice(800.0);

        Product product2 = new Product();
        product2.setName("iPhone");
        product2.setPrice(1500.0);

        order1.setUser(user);
        order2.setUser(user);

        order1.setProducts(Arrays.asList(product1,product2));
        order2.setProducts(Collections.singletonList(product1));

        user.setOrders(Arrays.asList(order1,order2));

        session.beginTransaction();
        session.persist(user); // saves the object/entity into database.

        session.getTransaction().commit();
        return user;
    }

    private static void readEntities(SessionFactory factory, User user) {
        Session session = null;
        try{
            // read
            session = factory.openSession();
            session.beginTransaction();

            List<User> users = session.createQuery("from User u where u.id=" + user.getId(),
                    User.class).getResultList();
            for (User x : users) {
                System.out.println(x.toString());
            }

            User user2 = session.get(User.class, user.getId()); // Fetch user by ID
            System.out.println("User Details: " + user2.getUsername());
            System.out.println("Address: " + user2.getAddress().getStreet());
            session.getTransaction().commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private static void updateEntities(SessionFactory factory, User user) {
        Session session = null;
        try {
            session = factory.openSession();
            session.beginTransaction();

            User user3 = session.get(User.class, user.getId());
            user3.setUsername("PL" + user.getId()); // Update username
            user3.getAddress().setCity("Scarborough" + user.getId()); // Update address
            session.getTransaction().commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private static void deleteEntities(SessionFactory factory, User user) {
        Session session = null;
        try {
            session = factory.openSession();
            session.beginTransaction();

            User user4 = session.get(User.class, user.getId());
            if (user4 != null) {
                session.remove(user4); // This will delete the user and associated address, orders (cascade)
            } else {
                System.out.println("User not found!");
            }
            session.getTransaction().commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}

















