package ku.cs.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class StudentListTest {
    private static StudentList students;

    @BeforeEach
    void setup() {
        students = new StudentList();

        students.addNewStudent("6710405501", "John Cena");
        students.addNewStudent("6710405502", "Kapao Mookrop");
        students.addNewStudent("6710405516", "Sonsiwawong Suklert");
    }

    @Test
    void testAddNewStudent() {
        students.addNewStudent("6710405516", "Robloxian");
        students.addNewStudent("6710405503", "Lenovo Kung", 75);

        assertEquals("Sonsiwawong Suklert", students.findStudentById("6710405516").getName());
        assertEquals(75.0, students.findStudentById("6710405503").getScore());
    }

    @Test
    void testFindStudent() {
        final String idToFind = "6710405516";
        assertEquals("Sonsiwawong Suklert", students.findStudentById(idToFind).getName());
    }

    @Test
    void testFilterByName() {
        boolean isFound = false;
        StudentList results = students.filterByName("Sonsiwawong");

        for(Student result : results.getStudents()) {
            if (result.getName().equals("Sonsiwawong Suklert")) {
                isFound = true;
                break;
            }
        }

        assertTrue(isFound);
    }
}