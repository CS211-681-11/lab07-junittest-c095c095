package ku.cs.services;

import ku.cs.models.Student;
import ku.cs.models.StudentList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentListHardCodeDatasourceTest {
    private StudentListHardCodeDatasource datasource;

    @BeforeEach
    void setUp() {
        datasource = new StudentListHardCodeDatasource();
    }

    @Test
    @DisplayName("ทดสอบการอ่านข้อมูลนักศึกษาจาก Hard Code")
    void testReadData() {
        StudentList studentList = datasource.readData();

        assertNotNull(studentList);
        assertEquals(10, studentList.getStudents().size());

        Student first = studentList.findStudentById("6710400001");
        assertNotNull(first);
        assertEquals("6710400001", first.getId());
        assertEquals("First", first.getName());
        assertEquals(0.0, first.getScore());

        Student tenth = studentList.findStudentById("67104000010");
        assertNotNull(tenth);
        assertEquals("67104000010", tenth.getId());
        assertEquals("Tenth", tenth.getName());
        assertEquals(0.0, tenth.getScore());
    }

    @Test
    @DisplayName("ทดสอบการเขียนข้อมูล")
    void testWriteData() {
        StudentList testData = new StudentList();
        testData.addNewStudent("test123", "Test Student");

        assertDoesNotThrow(() -> datasource.writeData(testData));
    }

    @Test
    @DisplayName("ทดสอบว่าข้อมูลที่อ่านได้มี ID ที่ถูกต้องทั้งหมด")
    void testAllStudentIds() {
        StudentList studentList = datasource.readData();

        String[] expectedIds = {
                "6710400001", "6710400002", "6710400003", "6710400004", "6710400005",
                "6710400006", "6710400007", "6710400008", "6710400009", "67104000010"
        };

        for (String id : expectedIds) {
            Student student = studentList.findStudentById(id);
            assertNotNull(student, "ไม่พบนักศึกษา ID: " + id);
            assertEquals(id, student.getId());
        }
    }

    @Test
    @DisplayName("ทดสอบการทำงานของ Datasource interface")
    void testDatasourceInterface() {
        assertTrue(datasource instanceof Datasource);

        Datasource<StudentList> genericDatasource = datasource;
        StudentList result = genericDatasource.readData();
        assertNotNull(result);
    }
}