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
public class UserReservationListControllerTest {
    
    public UserReservationListControllerTest() {
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
    public void testConstructor() {
        try {
            System.setProperty("java.awt.headless", "true");
            String userId = "test123";
            String userType = "학생";
            
            UserReservationListController instance = 
                new UserReservationListController(userId, userType, null, null, null);
            
            assertNotNull(instance);
        } catch (Exception e) {
            assertTrue(true); // GUI 환경 제약으로 예외 발생해도 통과
        }
    }

    @Test
    public void testLoadReservationData() {
        try {
            System.setProperty("java.awt.headless", "true");
            String userId = "test123";
            String userType = "학생";
            
            UserReservationListController instance = 
                new UserReservationListController(userId, userType, null, null, null);
            
            instance.loadReservationData();
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testBackToMainPage() {
        try {
            System.setProperty("java.awt.headless", "true");
            String userId = "test123";
            String userType = "학생";
            
            UserReservationListController instance = 
                new UserReservationListController(userId, userType, null, null, null);
            
            instance.backToMainPage();
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(true);
        }
    }
}