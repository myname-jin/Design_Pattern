/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package management;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author suk22
 */
public class ClassroomModelTest {

    @Test
    void testConstructorAndGetters() {
        ClassroomModel classroom = new ClassroomModel("101", "1층", "30", "화이트보드 있음");

        assertEquals("101", classroom.getRoom());
        assertEquals("1층", classroom.getLocation());
        assertEquals("30", classroom.getCapacity());
        assertEquals("화이트보드 있음", classroom.getNote());
    }

    @Test
    void testToFileString() {
        ClassroomModel classroom = new ClassroomModel("202", "2층", "40", "빔프로젝터 있음");

        String expected = "202,2층,40,빔프로젝터 있음";
        assertEquals(expected, classroom.toFileString());
    }

    @Test
    void testEmptyFields() {
        ClassroomModel classroom = new ClassroomModel("", "", "", "");

        assertEquals("", classroom.getRoom());
        assertEquals("", classroom.getLocation());
        assertEquals("", classroom.getCapacity());
        assertEquals("", classroom.getNote());
        assertEquals(",,,", classroom.toFileString());
    }
}
