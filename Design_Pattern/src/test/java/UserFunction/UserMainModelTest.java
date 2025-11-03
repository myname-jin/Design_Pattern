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
public class UserMainModelTest {
    
    public UserMainModelTest() {
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
    public void testGetUserId() {
        String userId = "test123";
        String userType = "학생";
        UserMainModel instance = new UserMainModel(userId, userType, null, null, null);
        
        String result = instance.getUserId();
        assertEquals("test123", result);
    }

    @Test
    public void testGetUserName() {
        UserMainModel instance = new UserMainModel("testUser", "학생", null, null, null);
        
        String result = instance.getUserName();
        assertNotNull(result);
    }

    @Test
    public void testGetUserType() {
        UserMainModel instance = new UserMainModel("testUser", "교수", null, null, null);
        
        String result = instance.getUserType();
        assertNotNull(result);
    }
}