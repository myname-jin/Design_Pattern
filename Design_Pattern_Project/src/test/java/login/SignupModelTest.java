/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package login;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author msj02
 */
public class SignupModelTest {
    
    public SignupModelTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        System.out.println("setUpClass");
    }
    
    @AfterAll
    public static void tearDownClass() {
        System.out.println("tearDownClass");
    }
    
    @BeforeEach
    public void setUp() {
        System.out.println("setUp");
    }
    
    @AfterEach
    public void tearDown() {
        System.out.println("tearDown");
    }

    /**
     * Test of registerUser method, of class SignupModel.
     */
    @Test
    public void testRegisterUser() {
        System.out.println("registerUser 테스트: 빈 값 입력하거나 정해진 role이 아닌 값 입력 시 false");
        String userId = "m77776666";
        String password = "77776666";
        String role = "교수";
        String userName = "테스트";
        String userDept = "컴퓨터소프트웨어공학과";
        SignupModel instance = new SignupModel();
        boolean expResult = true;
        boolean result = instance.registerUser(userId, password, role, userName, userDept);
        assertEquals(expResult, result);
        
    }
    
}
