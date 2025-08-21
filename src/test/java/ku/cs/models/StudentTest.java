package ku.cs.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {
    private static Student s;

    @BeforeEach
    void setup() {
        s = new Student("6710405516", "StudentTest");
    }

    @Test
    @DisplayName("ทดสอบการเพิ่มคะแนน 45.15 คะแนน")
    void testAddScore() {
        s.addScore(45.15);
        assertEquals(45.15, s.getScore());
    }

    @Test
    @DisplayName("ทดสอบการเพิ่มคะแนน 85 คะแนน และให้ Object คำนวนเกรดออกมา")
    void testCalculateGrade(){
        s.addScore(85);
        assertEquals("A", s.grade());
    }

    @Test
    @DisplayName("ทดสอบว่า ID ที่ระบุถูกต้องหรือไม่")
    void testIsId() {
        final String testId = "6710405516";
        assertTrue(s.isId(testId));
    }

    @Test
    @DisplayName("ทดสอบว่าชื่อมีคำที่ระบุหรือไม่")
    void testIsNameContains() {
        final String testName = "Test";
        assertTrue(s.isNameContains(testName));
    }

    @Test
    @DisplayName("ทดสอบการดึงข้อมูลรหัสนักศึกษา")
    void testGetStudentInfo() {
        s.addScore(70);
        assertEquals("6710405516", s.getId());
        assertEquals("StudentTest", s.getName());
        assertEquals(70, s.getScore());
        assertEquals("B", s.getGrade());
    }

    @Test
    @DisplayName("ทดสอบการแปลงข้อมูล Object เป็น String")
    void testToString() {
        s.addScore(80);
        assertEquals(
                "{id: '6710405516', name: 'StudentTest', score: 80.0}",
                s.toString()
        );
    }
}