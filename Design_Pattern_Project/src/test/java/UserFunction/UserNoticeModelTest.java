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
import notice.Notice;
import java.util.List;

/**
 *
 * @author jms5310
 */
public class UserNoticeModelTest {
    
    public UserNoticeModelTest() {
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
        String userId = "test123";
        UserNoticeModel instance = new UserNoticeModel(userId, null, null, null);
        
        assertNotNull(instance);
        assertEquals("test123", instance.getUserId());
    }

    @Test
    public void testGetUserId() {
        String userId = "test123";
        UserNoticeModel instance = new UserNoticeModel(userId, null, null, null);
        
        String result = instance.getUserId();
        assertEquals("test123", result);
    }

    @Test
    public void testGetUserName() {
        UserNoticeModel instance = new UserNoticeModel("testUser", null, null, null);
        
        String result = instance.getUserName();
        assertNotNull(result);
    }

    @Test
    public void testGetUserType() {
        UserNoticeModel instance = new UserNoticeModel("testUser", null, null, null);
        
        String result = instance.getUserType();
        assertNotNull(result);
    }

    @Test
    public void testGetAllNotices() {
        UserNoticeModel instance = new UserNoticeModel("testUser", null, null, null);
        
        List<Notice> result = instance.getAllNotices();
        assertNotNull(result);
    }

    @Test
    public void testGetFilteredNotices() {
        UserNoticeModel instance = new UserNoticeModel("testUser", null, null, null);
        
        List<Notice> result = instance.getFilteredNotices("", "전체");
        assertNotNull(result);
    }

    @Test
    public void testGetUnreadNoticeCount() {
        UserNoticeModel instance = new UserNoticeModel("testUser", null, null, null);
        
        int result = instance.getUnreadNoticeCount();
        assertTrue(result >= 0);
    }
}