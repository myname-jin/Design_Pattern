/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package rulemanagement;

/**
 *
 * @author adsd3
 */
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JTextField;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class RuleManagementControllerTest {

    @TempDir Path tempDir;
    private RuleManagementController controller;
    private RuleManagementModel  model;
    private RuleManagementView   view;

    @BeforeEach
    void setUp() throws Exception {
        // 임시 rules.txt 생성 및 초기화
        Path rulesFile = tempDir.resolve("rules.txt");
        Files.write(rulesFile, List.of("rule1", "rule2"),
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE);

        // 컨트롤러/모델/뷰 초기화
        controller = new RuleManagementController(rulesFile.toString());
        Field mf = RuleManagementController.class.getDeclaredField("model");
        mf.setAccessible(true);
        model = (RuleManagementModel) mf.get(controller);
        Field vf = RuleManagementController.class.getDeclaredField("view");
        vf.setAccessible(true);
        view = (RuleManagementView) vf.get(controller);
    }

    @Test
    void testInitialLoad_viaController() {
        List<String> fromModel = model.getRules();
        List<String> fromView  = view.getRuleCheckBoxes().stream()
                                      .map(AbstractButton::getText)
                                      .collect(Collectors.toList());

        assertEquals(2, fromModel.size());
        assertTrue(fromView.containsAll(fromModel));
    }

    @Test
    void testAddButtonAction() throws Exception {
        // 리플렉션으로 private JTextField newRuleField 꺼내 세팅
        Field tfField = RuleManagementView.class.getDeclaredField("newRuleField");
        tfField.setAccessible(true);
        JTextField newRuleField = (JTextField) tfField.get(view);
        newRuleField.setText("새룰");

        // Add 버튼 클릭
        JButton addBtn = view.getAddButton();
        addBtn.doClick();

        // 모델에 반영 확인
        assertTrue(model.getRules().contains("새룰"),
                   "Add 클릭 후 모델에 '새룰'이 추가되어야 한다");
    }

    @Test
    void testDeleteButtonAction() throws IOException {
        // 모델에 직접 추가 & 뷰 갱신
        model.addRule("toDelete");
        view.updateRules(model.getRules());

        // 체크박스에서 toDelete 선택
        view.getRuleCheckBoxes().forEach(b -> {
            if (b.getText().equals("toDelete")) {
                b.setSelected(true);
            }
        });

        // Delete 버튼 클릭
        JButton delBtn = view.getDeleteButton();
        delBtn.doClick();

        // 모델에서 삭제 확인
        assertFalse(model.getRules().contains("toDelete"),
                    "Delete 클릭 후 모델에서 'toDelete'가 제거되어야 한다");
    }
}