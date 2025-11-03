/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rulemanagement;

/**
 *
 * @author adsd3
 */
import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import management.ReservationMgmtView;

/**
 * Model과 View를 연결, 버튼 이벤트 처리. 메인 분리되어 showView()로 UI 표시를 수행합니다.
 */
public class RuleManagementController {

    final RuleManagementModel model;
    private final RuleManagementView view;

    /**
     * 모델과 뷰를 초기화하고 리스너를 연결합니다.
     *
     * @param filePath 규칙 파일 경로
     * @throws IOException 파일 입출력 오류 시
     */
    public RuleManagementController(String filePath) throws IOException {
        model = new RuleManagementModel(filePath);
        view = new RuleManagementView(model.getRules());
        attachListeners();
    }

    /**
     * 버튼 클릭 이벤트를 모델에 연결하고 뷰를 갱신하는 리스너를 설정합니다.
     */
    private void attachListeners() {
        view.getAddButton().addActionListener(e -> {
            String text = view.getNewRuleText();
            if (!text.isEmpty()) {
                try {
                    model.addRule(text);
                    view.updateRules(model.getRules());
                    view.clearNewRuleField();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(view,
                            "규칙 추가 실패: " + ex.getMessage(),
                            "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        view.getDeleteButton().addActionListener(e -> {
            List<String> toDelete = view.getRuleCheckBoxes().stream()
                    .filter(AbstractButton::isSelected)
                    .map(AbstractButton::getText)
                    .collect(Collectors.toList());
            if (!toDelete.isEmpty()
                    && JOptionPane.showConfirmDialog(view,
                            "선택한 규칙을 삭제하시겠습니까?",
                            "확인", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    model.deleteRules(toDelete);
                    view.updateRules(model.getRules());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(view,
                            "규칙 삭제 실패: " + ex.getMessage(),
                            "오류", JOptionPane.ERROR_MESSAGE);
                }
            }

        });
        view.getBackButton().addActionListener(e -> {
    try {
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
    } catch (Exception ignored) {}

    SwingUtilities.invokeLater(() -> {
        try {
            ReservationMgmtView mgmtView = new ReservationMgmtView();
            mgmtView.setLocationRelativeTo(null);
            mgmtView.setVisible(true); // 👈 꼭 있어야 함
        } catch (Exception ex) {
            ex.printStackTrace(); // 에러 확인
        }
    });

    view.dispose();
});

    }

    /**
     * UI를 표시합니다. Main 클래스에서 호출하세요.
     */
    public void showView() {
        SwingUtilities.invokeLater(() -> view.setVisible(true));
    }
}
