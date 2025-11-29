package UserFunction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UserReservationCancelController {
    private UserReservationCancelModel model;
    private UserReservationCancelView view;
    private UserReservationListView parentView;
    
    private String name;
    private String userId;
    private String room;
    private String date;
    private String startTime;
    private String endTime;
    private int selectedRow;
    
    public UserReservationCancelController(UserReservationListView parentView, int selectedRow) {
        this.model = new UserReservationCancelModel();
        this.parentView = parentView;
        this.selectedRow = selectedRow;
        
        JTable table = parentView.getTable();
        
        // 테이블 컬럼 순서에 맞춰 데이터 가져오기
        // {"이름", "학번", "강의실", "날짜", "요일", "시작시간", "종료시간", "승인상태"}
        this.name = table.getValueAt(selectedRow, 0).toString();
        this.userId = table.getValueAt(selectedRow, 1).toString();
        this.room = table.getValueAt(selectedRow, 2).toString();
        this.date = table.getValueAt(selectedRow, 3).toString();
        // 요일(4) 건너뜀
        this.startTime = table.getValueAt(selectedRow, 5).toString();
        this.endTime = table.getValueAt(selectedRow, 6).toString();
        
        // 뷰 생성
        Window parentWindow = SwingUtilities.getWindowAncestor(parentView);
        if (parentWindow instanceof Frame) {
            this.view = new UserReservationCancelView((Frame) parentWindow);
        } else {
            this.view = new UserReservationCancelView(null);
        }
        
        this.view.setReservationInfo(name, userId, room, date, startTime, endTime);
        this.view.addConfirmListener(e -> handleCancelConfirm());
    }
    
    public void showView() {
        view.setVisible(true);
    }
    
    private void handleCancelConfirm() {
        String reason = view.getCancelReason();
        
        if (reason.isEmpty()) {
            view.showError("취소 사유를 입력해주세요.");
            return;
        }
        
        // 1. 예약 취소 요청 (startTime 포함)
        boolean cancelSuccess = model.cancelReservation(userId, date, room, startTime);
        
        if (!cancelSuccess) {
            view.showError("예약 취소 처리에 실패했습니다.\n(이미 취소되었거나 정보를 찾을 수 없습니다.)");
            return;
        }
        
        // 2. 취소 사유 저장 (cancel.txt)
        model.saveCancelReason(userId, reason);
        
        // 3. 화면 갱신 (테이블 행 삭제)
        DefaultTableModel tableModel = (DefaultTableModel) parentView.getTable().getModel();
        tableModel.removeRow(selectedRow);
        
        JOptionPane.showMessageDialog(view, "예약이 정상적으로 취소되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
        view.dispose();
    }
}