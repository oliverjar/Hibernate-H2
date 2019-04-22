package com.treehouse.contactmgr;

import com.treehouse.contactmgr.model.Contact;
import com.treehouse.contactmgr.model.Contact.ContactBuilder;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public class Application {
    //Hold a reusable reference for session factory  (since we need only one)
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {

        //Create a StandardServiceRegistry
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {
        Contact contact = new ContactBuilder("James", "James")
                .withEmail("james@gmail.com")
                .withPhone(333333333L)
                .build();
        int id = save(contact);

        //Display list of contacts before update
        System.out.printf("%n%nBefore Update %n%n");
        fetchAllContats().stream().forEach(System.out::println);

        //Get the persisted contact
        Contact c = findContactById(id);
        //update the contact
        c.setFirstName("Beatrix");
        //persist the changes
        System.out.printf("%n%n Updating %n%n");
        update(c);
        System.out.printf("%n%n Updating %n%n");
        //Display list of contacts after the update
        System.out.printf("%n%n after Update %n%n");
        fetchAllContats().stream().forEach(System.out::println);

        delete(c);
        fetchAllContats().stream().forEach(System.out::println);
    }

    private static Contact findContactById(int id) {
        //open a session
        Session session = sessionFactory.openSession();

        //retrieve the persistent object
        Contact contact = session.get(Contact.class, id);

        //close the session
        session.close();

        //return the object
        return contact;
    }

    private static void update(Contact contact) {
        //open a session
        Session session = sessionFactory.openSession();

        //begin a transaction
        session.beginTransaction();
        //use the session to update the contact
        session.update(contact);

        //commit the transaction
        session.getTransaction().commit();

        //close the session
        session.close();
    }

    private static void delete(Contact contact) {
        //open a session
        Session session = sessionFactory.openSession();

        //begin a transaction
        session.beginTransaction();
        //use the session to update the contact
        session.delete(contact);

        //commit the transaction
        session.getTransaction().commit();

        //close the session
        session.close();
    }



    @SuppressWarnings("unchecked")
    private static List<Contact> fetchAllContats() {
        //open a session
        Session session = sessionFactory.openSession();

        //Create the criteria object
        Criteria criteria = session.createCriteria(Contact.class);
        List<Contact> contacts = criteria.list();

        //get a list of contact objects according to the criteria object we just created


        //close the session
        session.close();

        return contacts;
    }

    private static int save(Contact contact) {
        //open a session
        Session session = sessionFactory.openSession();

        //begin a transaction
        session.beginTransaction();

        //use the session to save the contact
        int id = (int) session.save(contact);

        //commit the session
        session.getTransaction().commit();

        //close the session
        session.close();

        return id;
    }
}
