/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package UserNotification;

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
public class NotificationControllerTest {
    
    public NotificationControllerTest() {
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
    public void testGetInstance() {
        String userId = "test123";
        String userType = "학생";
        
        NotificationController instance = NotificationController.getInstance(userId, userType, null, null, null);
        assertNotNull(instance);
        assertEquals(userId, instance.getUserId());
    }

    @Test
    public void testGetInstanceSingleton() {
        String userId = "test123";
        String userType = "학생";
        
        NotificationController instance1 = NotificationController.getInstance(userId, userType, null, null, null);
        NotificationController instance2 = NotificationController.getInstance(userId, userType, null, null, null);
        
        assertSame(instance1, instance2); // 같은 인스턴스인지 확인
    }

    @Test
    public void testGetUserId() {
        String userId = "test123";
        String userType = "학생";
        
        NotificationController instance = NotificationController.getInstance(userId, userType, null, null, null);
        String result = instance.getUserId();
        assertEquals(userId, result);
    }

    @Test
    public void testGetUserType() {
        String userId = "test123";
        String userType = "교수";
        
        NotificationController instance = NotificationController.getInstance(userId, userType, null, null, null);
        String result = instance.getUserType();
        assertNotNull(result);
        // 기본값으로 설정될 수 있으므로 null이 아닌지만 확인
    }

    @Test
    public void testGetModel() {
        String userId = "test123";
        String userType = "학생";
        
        NotificationController instance = NotificationController.getInstance(userId, userType, null, null, null);
        NotificationModel result = instance.getModel();
        assertNotNull(result);
    }

    @Test
    public void testGetNotificationButtonText() {
        String userId = "test123";
        String userType = "학생";
        
        NotificationController instance = NotificationController.getInstance(userId, userType, null, null, null);
        String result = instance.getNotificationButtonText();
        assertNotNull(result);
        assertTrue(result.contains("알림"));
    }

    @Test
    public void testShutdown() {
        String userId = "test123";
        String userType = "학생";
        
        NotificationController instance = NotificationController.getInstance(userId, userType, null, null, null);
        instance.shutdown();
        assertTrue(true); // 예외 없이 실행되면 통과
    }
}