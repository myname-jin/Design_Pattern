/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserFunction;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.net.Socket;
import ServerClient.LogoutUtil;


/**
 *
 * @author jms5310
 */
public class UserReservationListController {

    private UserReservationListView view;
    private final JTable table;
    private  String userId;
    private final String filePath = "src/main/resources/reservation.txt";
    private String userType;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    
    
    
     public UserReservationListController(String userId, String userType, Socket socket, BufferedReader in, BufferedWriter out) {
        this.userId = userId; // 조건분기로 처리
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.userType = userType;

        //  로그아웃 자동 처리 등록
       
        
        view = new UserReservationListView();
    view.setController(this);  // 컨트롤러 참조 설정
    
    table = view.getTable();
    
    // 로그아웃 처리 등록
    if (socket != null && out != null) {
        LogoutUtil.attach(view, userId, socket, out);
    }
    
    loadReservationData();
    view.setVisible(true);  // 화면 표시 - 이 부분이 있는지 확인
    }
    
     public void backToMainPage() {
    // 메인 페이지로 돌아가기
    new UserFunction.UserMainController(userId, userType, socket, in, out);

}
    
    public void loadReservationData() {
        String[] columns = {"이름", "학번", "강의실", "날짜", "요일", "시작시간", "종료시간", "승인상태"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        System.out.println("현재 사용자 ID: " + userId); // 디버깅용

        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean foundReservation = false;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
               if (parts.length >= 12 && parts[2].equals(userId)) {
             model.addRow(new Object[]{
                parts[0], // 이름
                parts[2], // 학번
                parts[5], // 강의실
                parts[6], // 날짜 (추가)
                parts[7], // 요일
                parts[8], // 시작시간
                parts[9], // 종료시간
                parts[11] // 승인상태
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "예약 정보를 불러오는 중 오류가 발생했습니다.");
        }

        table.setModel(model);
        table.setAutoCreateRowSorter(true);

        
        
        // 승인 상태 컬러 적용
      table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // 기본 색상
        c.setForeground(Color.BLACK);

       // 승인 상태 컬럼 색상
            if (column == 7) {
                String status = table.getValueAt(row, column).toString();
                if (status.equals("승인")) {
                    c.setForeground(new Color(0, 128, 0)); // 초록
                } else if (status.equals("거절")) {
                    c.setForeground(Color.RED); // 빨강
                } else {
                    c.setForeground(Color.BLACK); // 대기
                }
            } else {
                c.setForeground(Color.BLACK);
            }

            return c;
    }
});
       if (table.getColumnCount() >= 8) { // 컬럼 수 확인
        table.getColumnModel().getColumn(0).setPreferredWidth(80);  // 이름
        table.getColumnModel().getColumn(1).setPreferredWidth(90);  // 학번
        table.getColumnModel().getColumn(2).setPreferredWidth(70);  // 강의실
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // 날짜
        table.getColumnModel().getColumn(4).setPreferredWidth(40);  // 요일
        table.getColumnModel().getColumn(5).setPreferredWidth(80);  // 시작시간
        table.getColumnModel().getColumn(6).setPreferredWidth(80);  // 종료시간
        table.getColumnModel().getColumn(7).setPreferredWidth(80);  // 승인상태
    }
    }
}
