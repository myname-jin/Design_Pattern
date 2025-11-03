/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ruleagreementTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import ruleagreement.RuleAgreementModel;

/**
 * RuleAgreementModel 테스트 클래스
 * 
 * @author jms5310
 */
public class RuleAgreementModelTest {
    
    private static File testFile;
    private RuleAgreementModel model;
    
    @BeforeAll
    public static void setUpClass() {
        // 테스트용 임시 파일 생성
        try {
            testFile = File.createTempFile("test_rules", ".txt");
            try (FileWriter writer = new FileWriter(testFile)) {
                writer.write("1. 테스트 규칙 1\n");
                writer.write("2. 테스트 규칙 2\n");
                writer.write("3. 테스트 규칙 3\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @AfterAll
    public static void tearDownClass() {
        // 테스트 파일 삭제
        if (testFile != null) {
            testFile.delete();
        }
    }
    
    @BeforeEach
    public void setUp() throws IOException {
        model = new RuleAgreementModel(testFile.getAbsolutePath());
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * getRules 메서드 테스트.
     */
    @Test
    public void testGetRules() {
        System.out.println("getRules 테스트");
        List<String> rules = model.getRules();
        
        // 검증
        assertEquals(3, rules.size(), "규칙이 3개 있어야 함");
        assertEquals("1. 테스트 규칙 1", rules.get(0));
        assertEquals("2. 테스트 규칙 2", rules.get(1));
        assertEquals("3. 테스트 규칙 3", rules.get(2));
    }
}