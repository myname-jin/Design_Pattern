/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ruleagreementTest;

import rulemanagement.RuleManagementModel;  // ← 여기를 꼭 추가하세요
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class RuleAgreementControllerTest {

    @TempDir
    Path tempDir;

    Path rulesFile;
    HeadlessRuleAgreementController controller;

    @BeforeEach
    void setUp() throws Exception {
        // 1) 임시 파일 생성 및 초기 데이터 쓰기
        rulesFile = tempDir.resolve("rules.txt");
        Files.write(rulesFile, Arrays.asList("ruleA", "ruleB"));

        // 2) 헤드리스 컨트롤러 생성
        controller = new HeadlessRuleAgreementController(rulesFile.toString());
    }

    @Test
    void addRule_appendsToFileAndRulesList() throws IOException {
        // 새 규칙 추가
        controller.addRule("ruleC");

        // 파일에도 추가되었는지 확인
        List<String> fileLines = Files.readAllLines(rulesFile);
        assertTrue(fileLines.contains("ruleC"), "파일에 ruleC가 추가되어야 합니다.");

        // 내부 리스트에도 반영되었는지 확인
        assertTrue(controller.getRules().contains("ruleC"), "컨트롤러 리스트에 ruleC가 있어야 합니다.");
    }

    @Test
    void deleteRules_removesFromFileAndRulesList() throws IOException {
        // ruleA 삭제
        controller.deleteRules(Collections.singletonList("ruleA"));

        // 파일에서 제거되었는지 확인
        List<String> fileLines = Files.readAllLines(rulesFile);
        assertFalse(fileLines.contains("ruleA"), "파일에서 ruleA가 제거되어야 합니다.");

        // 내부 리스트에서도 제거되었는지 확인
        assertFalse(controller.getRules().contains("ruleA"), "컨트롤러 리스트에서 ruleA가 제거되어야 합니다.");
    }

    /**
     * 테스트 전용, Swing 뷰 없이 모델의 add/delete만 감싸는 헤드리스 컨트롤러
     */
    static class HeadlessRuleAgreementController {
        private final RuleManagementModel model;
        private final List<String> rules;

        HeadlessRuleAgreementController(String filePath) throws IOException {
            this.model = new RuleManagementModel(filePath);
            this.rules = new ArrayList<>(model.getRules());
        }

        void addRule(String rule) throws IOException {
            model.addRule(rule);
            rules.add(rule);
        }

        void deleteRules(List<String> toDelete) throws IOException {
            model.deleteRules(toDelete);
            rules.removeAll(toDelete);
        }

        List<String> getRules() {
            return new ArrayList<>(rules);
        }
    }
}