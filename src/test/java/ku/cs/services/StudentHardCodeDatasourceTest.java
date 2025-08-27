package ku.cs.services;

import ku.cs.models.Student;
import ku.cs.models.StudentList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentHardCodeDatasourceTest {
    private StudentHardCodeDatasource datasource;

    @BeforeEach
    void setUp() {
        datasource = new StudentHardCodeDatasource();
    }

    @Test
    @DisplayName("ทดสอบการอ่านข้อมูลนักศึกษาจาก Hard Code")
    void testReadData() {
        StudentList studentList = datasource.readData();

        assertNotNull(studentList);
        assertEquals(4, studentList.getStudents().size());

        Student first = studentList.findStudentById("6710400001");
        assertNotNull(first);
        assertEquals("6710400001", first.getId());
        assertEquals("First", first.getName());
        assertEquals(0.0, first.getScore());

        Student second = studentList.findStudentById("6710400002");
        assertNotNull(second);
        assertEquals("6710400002", second.getId());
        assertEquals("Second", second.getName());

        Student third = studentList.findStudentById("6710400003");
        assertNotNull(third);
        assertEquals("6710400003", third.getId());
        assertEquals("Third", third.getName());

        Student fourth = studentList.findStudentById("6710400004");
        assertNotNull(fourth);
        assertEquals("6710400004", fourth.getId());
        assertEquals("Fourth", fourth.getName());
    }

    @Test
    @DisplayName("ทดสอบว่าข้อมูล StudentList เป็นข้อมูลที่ถูกต้อง")
    void testReadDataReturnType() {
        StudentList result = datasource.readData();

        assertTrue(result instanceof StudentList);
        assertNotNull(result.getStudents());
    }
}