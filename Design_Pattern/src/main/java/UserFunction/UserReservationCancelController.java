/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserFunction;

/**
 *
 * @author jms5310
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UserReservationCancelController {
    private UserReservationCancelModel model;
    private UserReservationCancelView view;
    private UserReservationListView parentView;
    
    private String name;
    private String userId;
    private String room;
    private String date;
    private String day;
    private String startTime;
    private String endTime;
    private int selectedRow;
    
    public UserReservationCancelController(UserReservationListView parentView, int selectedRow) {
        this.model = new UserReservationCancelModel();
        this.parentView = parentView;
        this.selectedRow = selectedRow;
        
        // 선택한 예약 정보 가져오기
        JTable table = parentView.getTable();
        this.name = table.getValueAt(selectedRow, 0).toString();
        this.userId = table.getValueAt(selectedRow, 1).toString();
        this.room = table.getValueAt(selectedRow, 2).toString();
        this.date = table.getValueAt(selectedRow, 3).toString();
        this.day = table.getValueAt(selectedRow, 4).toString();
        this.startTime = table.getValueAt(selectedRow, 5).toString();
        this.endTime = table.getValueAt(selectedRow, 6).toString();
        
        // 뷰 초기화 및 이벤트 연결
    this.view = new UserReservationCancelView((JFrame)SwingUtilities.getWindowAncestor(parentView));
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
        
        // 예약 취소 처리
        boolean cancelSuccess = model.cancelReservation(userId, date, room);
        if (!cancelSuccess) {
            view.showError("예약 취소 처리 중 오류가 발생했습니다.");
            return;
        }
        
        // 취소 이유 저장
        boolean reasonSuccess = model.saveCancelReason(userId, reason);
        if (!reasonSuccess) {
            view.showError("취소 사유 저장 중 오류가 발생했습니다.");
            return;
        }
        
        // 테이블에서 행 삭제
        DefaultTableModel tableModel = (DefaultTableModel) parentView.getTable().getModel();
        tableModel.removeRow(selectedRow);
        
        JOptionPane.showMessageDialog(view, "예약이 취소되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
        view.dispose();
    }
}