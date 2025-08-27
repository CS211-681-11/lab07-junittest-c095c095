package ku.cs.services;

import ku.cs.models.Student;
import ku.cs.models.StudentList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class StudentListFileDatasourceTest {

    @Test
    @DisplayName("ทดสอบการอ่านไฟล์ที่มีข้อมูลนักศึกษา")
    void testReadDataFromFile() throws IOException {
        Path systemTempDir = Files.createTempDirectory("junit-test-");
        String uniqueTestFileName = "test-students-" + System.currentTimeMillis() + ".csv";
        StudentListFileDatasource testDatasource = new StudentListFileDatasource(systemTempDir.toString(), uniqueTestFileName);
        
        File testFile = systemTempDir.resolve(uniqueTestFileName).toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("6710400001,John Doe,85.5\n");
            writer.write("6710400002,Jane Smith,92.0\n");
            writer.write("6710400003,Bob Johnson,78.25\n");
        }

        StudentList studentList = testDatasource.readData();

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
        
        testFile.delete();
        systemTempDir.toFile().delete();
    }

    @Test
    @DisplayName("ทดสอบการอ่านไฟล์ว่าง")
    void testReadEmptyFile() throws IOException {
        Path systemTempDir = Files.createTempDirectory("junit-test-empty-");
        String emptyFileName = "empty-student-list.csv";
        StudentListFileDatasource emptyDatasource = new StudentListFileDatasource(systemTempDir.toString(), emptyFileName);
        
        StudentList studentList = emptyDatasource.readData();

        assertNotNull(studentList);
        assertEquals(0, studentList.getStudents().size());
        
        File emptyFile = systemTempDir.resolve(emptyFileName).toFile();
        emptyFile.delete();
        systemTempDir.toFile().delete();
    }

    @Test
    @DisplayName("ทดสอบการอ่านข้อมูลจากไฟล์ student-list.csv จริง")
    void testReadActualStudentListFile() {
        StudentListFileDatasource actualDatasource = new StudentListFileDatasource("data", "student-list.csv");
        StudentList studentList = actualDatasource.readData();

        assertNotNull(studentList);
        assertEquals(5, studentList.getStudents().size());

        Student tim = studentList.findStudentById("6410450001");
        assertNotNull(tim);
        assertEquals("6410450001", tim.getId());
        assertEquals("Tim Berners-Lee", tim.getName());
        assertEquals(145.5, tim.getScore());

        Student barbara = studentList.findStudentById("6410450002");
        assertNotNull(barbara);
        assertEquals("6410450002", barbara.getId());
        assertEquals("Barbara Liskov", barbara.getName());
        assertEquals(60.7, barbara.getScore());

        Student alan = studentList.findStudentById("6410450003");
        assertNotNull(alan);
        assertEquals("6410450003", alan.getId());
        assertEquals("Alan Turing", alan.getName());
        assertEquals(346.2, alan.getScore());

        Student john = studentList.findStudentById("6410450004");
        assertNotNull(john);
        assertEquals("6410450004", john.getId());
        assertEquals("John McCarthy", john.getName());
        assertEquals(63.8, john.getScore());

        Student alanKay = studentList.findStudentById("6410450005");
        assertNotNull(alanKay);
        assertEquals("6410450005", alanKay.getId());
        assertEquals("Alan Kay", alanKay.getName());
        assertEquals(68.0, alanKay.getScore());
    }

    @Test
    @DisplayName("ทดสอบการเขียนข้อมูลลงไฟล์")
    void testWriteDataToFile() throws IOException {
        Path systemTempDir = Files.createTempDirectory("junit-test-write-");
        String writeTestFileName = "write-test-students.csv";
        StudentListFileDatasource writeDatasource = new StudentListFileDatasource(systemTempDir.toString(), writeTestFileName);
        
        StudentList studentList = new StudentList();
        studentList.addNewStudent("6710400001", "Test Student", 75.0);
        studentList.addNewStudent("6710400002", "Another Student", 88.5);

        writeDatasource.writeData(studentList);

        StudentList readData = writeDatasource.readData();

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
        
        File writeFile = systemTempDir.resolve(writeTestFileName).toFile();
        writeFile.delete();
        systemTempDir.toFile().delete();
    }

    @Test
    @DisplayName("ทดสอบการอ่านไฟล์ที่มีบรรทัดว่าง")
    void testReadFileWithEmptyLines() throws IOException {
        Path systemTempDir = Files.createTempDirectory("junit-test-empty-lines-");
        String uniqueTestFileName = "test-empty-lines-" + System.currentTimeMillis() + ".csv";
        StudentListFileDatasource testDatasource = new StudentListFileDatasource(systemTempDir.toString(), uniqueTestFileName);
        
        File testFile = systemTempDir.resolve(uniqueTestFileName).toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("6710400001,John Doe,85.5\n");
            writer.write("\n");
            writer.write("6710400002,Jane Smith,92.0\n");
            writer.write("\n");
        }

        StudentList studentList = testDatasource.readData();

        assertNotNull(studentList);
        assertEquals(2, studentList.getStudents().size());
        
        testFile.delete();
        systemTempDir.toFile().delete();
    }

    @Test
    @DisplayName("ทดสอบการสร้างไฟล์ใหม่เมื่อไฟล์ไม่มีอยู่")
    void testFileCreation() throws IOException {
        Path systemTempDir = Files.createTempDirectory("junit-test-creation-");
        String newFileName = "new-test-file.csv";
        StudentListFileDatasource newDatasource = new StudentListFileDatasource(systemTempDir.toString(), newFileName);

        File expectedFile = systemTempDir.resolve(newFileName).toFile();
        assertTrue(expectedFile.exists());

        StudentList result = newDatasource.readData();
        assertNotNull(result);
        assertEquals(0, result.getStudents().size());
        
        expectedFile.delete();
        systemTempDir.toFile().delete();
    }

    @Test
    @DisplayName("ทดสอบการทำงานของ Datasource interface")
    void testDatasourceInterface() throws IOException {
        Path systemTempDir = Files.createTempDirectory("junit-test-interface-");
        String interfaceTestFileName = "interface-test.csv";
        StudentListFileDatasource interfaceDatasource = new StudentListFileDatasource(systemTempDir.toString(), interfaceTestFileName);
        
        assertTrue(interfaceDatasource instanceof Datasource);

        Datasource<StudentList> genericDatasource = interfaceDatasource;
        StudentList result = genericDatasource.readData();
        assertNotNull(result);
        
        File interfaceFile = systemTempDir.resolve(interfaceTestFileName).toFile();
        interfaceFile.delete();
        systemTempDir.toFile().delete();
    }

    @Test
    @DisplayName("ทดสอบการเขียนและอ่านข้อมูลทั้งหมด")
    void testWriteAndReadCycle() throws IOException {
        Path systemTempDir = Files.createTempDirectory("junit-test-cycle-");
        String cycleTestFileName = "cycle-test.csv";
        StudentListFileDatasource cycleDatasource = new StudentListFileDatasource(systemTempDir.toString(), cycleTestFileName);
        
        StudentList originalData = new StudentList();
        originalData.addNewStudent("6710400001", "Student One", 95.0);
        originalData.addNewStudent("6710400002", "Student Two", 87.5);
        originalData.addNewStudent("6710400003", "Student Three", 76.0);

        cycleDatasource.writeData(originalData);

        StudentList readData = cycleDatasource.readData();

        assertEquals(originalData.getStudents().size(), readData.getStudents().size());

        for (Student original : originalData.getStudents()) {
            Student read = readData.findStudentById(original.getId());
            assertNotNull(read);
            assertEquals(original.getName(), read.getName());
            assertEquals(original.getScore(), read.getScore());
        }
        
        File cycleFile = systemTempDir.resolve(cycleTestFileName).toFile();
        cycleFile.delete();
        systemTempDir.toFile().delete();
    }
}