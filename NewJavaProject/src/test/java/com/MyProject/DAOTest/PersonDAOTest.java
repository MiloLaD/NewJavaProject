package com.MyProject.DAOTest;



import com.MyProject.newjavaproject.dao.personDAO;
import com.MyProject.newjavaproject.DatabaseManager;
import com.MyProject.newjavaproject.Person;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersonDAOTest {

    private personDAO dao;
    private Connection connection;

    @BeforeEach
    void setUp() throws Exception {
        connection = DatabaseManager.connect();
        dao = new personDAO();
    }

    @Test
    void testGetAllPersons() throws Exception {
        List<Person> persons = dao.getAllPersons();
        assertNotNull(persons);
    }

    @Test
    void testAddPerson() throws Exception {
        Person person = new Person(1, "Martin", "Sophie", "Soso", "0756789012", "25 Avenue des Champs", "sophie.martin@example.com", new Date(1000000), "profil.jpg");
        dao.addPerson(person);
        List<Person> persons = dao.getAllPersons();
        assertTrue(persons.stream().anyMatch(p -> p.getLastname().equals("Martin")));
    }

    @Test
    void testDeletePerson() throws Exception {
        Person person = new Person(2, "Durand", "Paul", "Paulo", "0611223344", "12 Rue Lafayette", "paul.durand@example.com", new Date(2000000), "profil2.jpg");
        dao.addPerson(person);
        dao.deletePerson(2);
        List<Person> persons = dao.getAllPersons();
       assertFalse(persons.stream().anyMatch(p -> p.getIdperson() == 2));
    }

    @Test
    void testUpdatePerson() throws Exception {
        Person person = new Person(3, "Lemoine", "Claire", "Clai", "0623456789", "50 Boulevard Haussmann", "claire.lemoine@example.com", new Date(3000000), "profil3.jpg");
        dao.addPerson(person);
        person.setLastname("Dubois");
        boolean result = dao.updatePerson(person);
        assertTrue(result);
        List<Person> persons = dao.getAllPersons();
        assertTrue(persons.stream().anyMatch(p -> p.getLastname().equals("Dubois")));
    }
}

