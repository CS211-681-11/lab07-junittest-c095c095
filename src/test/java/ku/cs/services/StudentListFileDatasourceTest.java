package ku.cs.services;

import ku.cs.models.Student;
import ku.cs.models.StudentList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class StudentListFileDatasourceTest {
    private StudentListFileDatasource datasource;

    @TempDir
    Path tempDir;

    private String testFileName = "student-list.csv";

    @BeforeEach
    void setUp() {
        datasource = new StudentListFileDatasource(tempDir.toString(), testFileName);
    }

    @Test
    @DisplayName("ทดสอบการอ่านไฟล์ที่มีข้อมูลนักศึกษา")
    void testReadDataFromFile() throws IOException {
        File testFile = tempDir.resolve(testFileName).toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("6710400001,John Doe,85.5\n");
            writer.write("6710400002,Jane Smith,92.0\n");
            writer.write("6710400003,Bob Johnson,78.25\n");
        }

        StudentList studentList = datasource.readData();

        assertNotNull(studentList);
        assertEquals(3, studentList.getStudents().size());

        Student john = studentList.findStudentById("6710400001");
        assertNotNull(john);
        assertEquals("6710400001", john.getId());
        assertEquals("John Doe", john.getName());
        assertEquals(85.5, john.getScore());

        Student jane = studentList.findStudentById("6710400002");
        assertNotNull(jane);
        assertEquals("6710400002", jane.getId());
        assertEquals("Jane Smith", jane.getName());
        assertEquals(92.0, jane.getScore());
    }

    @Test
    @DisplayName("ทดสอบการอ่านไฟล์ว่าง")
    void testReadEmptyFile() {
        StudentList studentList = datasource.readData();

        assertNotNull(studentList);
        assertEquals(0, studentList.getStudents().size());
    }

    @Test
    @DisplayName("ทดสอบการเขียนข้อมูลลงไฟล์")
    void testWriteDataToFile() {
        StudentList studentList = new StudentList();
        studentList.addNewStudent("6710400001", "Test Student", 75.0);
        studentList.addNewStudent("6710400002", "Another Student", 88.5);

        datasource.writeData(studentList);

        StudentList readData = datasource.readData();

        assertNotNull(readData);
        assertEquals(2, readData.getStudents().size());

        Student student1 = readData.findStudentById("6710400001");
        assertNotNull(student1);
        assertEquals("Test Student", student1.getName());
        assertEquals(75.0, student1.getScore());

        Student student2 = readData.findStudentById("6710400002");
        assertNotNull(student2);
        assertEquals("Another Student", student2.getName());
        assertEquals(88.5, student2.getScore());
    }

    @Test
    @DisplayName("ทดสอบการอ่านไฟล์ที่มีบรรทัดว่าง")
    void testReadFileWithEmptyLines() throws IOException {
        File testFile = tempDir.resolve(testFileName).toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("6710400001,John Doe,85.5\n");
            writer.write("\n");
            writer.write("6710400002,Jane Smith,92.0\n");
            writer.write("\n");
        }

        StudentList studentList = datasource.readData();

        assertNotNull(studentList);
        assertEquals(2, studentList.getStudents().size());
    }

    @Test
    @DisplayName("ทดสอบการสร้างไฟล์ใหม่เมื่อไฟล์ไม่มีอยู่")
    void testFileCreation() {
        String newFileName = "new-test-file.csv";
        StudentListFileDatasource newDatasource = new StudentListFileDatasource(tempDir.toString(), newFileName);

        File expectedFile = tempDir.resolve(newFileName).toFile();
        assertTrue(expectedFile.exists());

        StudentList result = newDatasource.readData();
        assertNotNull(result);
        assertEquals(0, result.getStudents().size());
    }

    @Test
    @DisplayName("ทดสอบการทำงานของ Datasource interface")
    void testDatasourceInterface() {
        assertTrue(datasource instanceof Datasource);

        Datasource<StudentList> genericDatasource = datasource;
        StudentList result = genericDatasource.readData();
        assertNotNull(result);
    }

    @Test
    @DisplayName("ทดสอบการเขียนและอ่านข้อมูลแบบครบวงจร")
    void testWriteAndReadCycle() {
        StudentList originalData = new StudentList();
        originalData.addNewStudent("6710400001", "Student One", 95.0);
        originalData.addNewStudent("6710400002", "Student Two", 87.5);
        originalData.addNewStudent("6710400003", "Student Three", 76.0);

        datasource.writeData(originalData);

        StudentList readData = datasource.readData();

        assertEquals(originalData.getStudents().size(), readData.getStudents().size());

        for (Student original : originalData.getStudents()) {
            Student read = readData.findStudentById(original.getId());
            assertNotNull(read);
            assertEquals(original.getName(), read.getName());
            assertEquals(original.getScore(), read.getScore());
        }
    }
}