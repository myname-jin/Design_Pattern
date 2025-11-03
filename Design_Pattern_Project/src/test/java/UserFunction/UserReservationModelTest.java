/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package UserFunction;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author jms5310
 */
public class UserReservationModelTest {
    
    public UserReservationModelTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testGetStudentId() {
        String studentId = "20211111";
        String department = "컴퓨터학과";
        String name = "홍길동";
        String room = "101";
        String time = "10:00~12:00";
        String status = "승인";
        
        UserReservationModel instance = new UserReservationModel(studentId, department, name, room, time, status);
        String result = instance.getStudentId();
        assertEquals("20211111", result);
    }

    @Test
    public void testGetDepartment() {
        UserReservationModel instance = new UserReservationModel("20211111", "컴퓨터학과", "홍길동", "101", "10:00~12:00", "승인");
        String result = instance.getDepartment();
        assertEquals("컴퓨터학과", result);
    }

    @Test
    public void testGetName() {
        UserReservationModel instance = new UserReservationModel("20211111", "컴퓨터학과", "홍길동", "101", "10:00~12:00", "승인");
        String result = instance.getName();
        assertEquals("홍길동", result);
    }

    @Test
    public void testGetRoom() {
        UserReservationModel instance = new UserReservationModel("20211111", "컴퓨터학과", "홍길동", "101", "10:00~12:00", "승인");
        String result = instance.getRoom();
        assertEquals("101", result);
    }

    @Test
    public void testGetTime() {
        UserReservationModel instance = new UserReservationModel("20211111", "컴퓨터학과", "홍길동", "101", "10:00~12:00", "승인");
        String result = instance.getTime();
        assertEquals("10:00~12:00", result);
    }

    @Test
    public void testGetStatus() {
        UserReservationModel instance = new UserReservationModel("20211111", "컴퓨터학과", "홍길동", "101", "10:00~12:00", "승인");
        String result = instance.getStatus();
        assertEquals("승인", result);
    }

    @Test
    public void testToString() {
        UserReservationModel instance = new UserReservationModel("20211111", "컴퓨터학과", "홍길동", "101", "10:00~12:00", "승인");
        String result = instance.toString();
        String expected = "20211111,컴퓨터학과,홍길동,101,10:00~12:00,승인";
        assertEquals(expected, result);
    }
}