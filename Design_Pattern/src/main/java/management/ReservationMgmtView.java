/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package management;

import calendar.CalendarController;
import calendar.ReservationRepositoryModel;
import calendar.ReservationServiceModel;
import visualization.MainView;
import visualization.ReservationModel;
import visualization.ReservationController;
import rulemanagement.RuleManagementController;
import statistics.StatsView;

import java.io.IOException;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * Observer Pattern이 적용된 관리자 예약 관리 화면
 * 기존 기능을 모두 유지하며 구조를 개선함.
 */
public class ReservationMgmtView extends javax.swing.JFrame implements ReservationObserver {

    private boolean isReverting = false; //  무한루프 방지용
    
    // 1. 옵저버 패턴을 위한 모델
    private AdminReservationModel model;
    
    // 2.  기존 기능을 위한 컨트롤러들 (유지 - 제한 기능 등에서 사용)
    private ReservationMgmtController oldController = new ReservationMgmtController(); 
    private NotificationController notificationController = new NotificationController();
    
    // 백업 매니저
    private BackupManager backupManager = new BackupManager();
    
    // Invoker (커맨드 패턴용)
    private ReservationInvoker invoker = new ReservationInvoker();

    // --- 생성자 ---
    public ReservationMgmtView() {
        // 1. 모델 초기화 및 옵저버 등록
        this.model = new AdminReservationModel();
        this.model.addObserver(this); // "나(View)를 관찰자로 등록해줘"

        // 2. UI 초기화 
        initComponents();
        
        // 검색창 클릭 시 텍스트 자동 삭제/복구 기능 추가
        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                // 클릭했을 때, "검색해 주세요"가 적혀있으면 지워준다.
                if (jTextField1.getText().equals("검색해 주세요")) {
                    jTextField1.setText("");
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                // 다른 곳을 클릭했을 때, 비어있으면 다시 "검색해 주세요"를 채워준다.
                if (jTextField1.getText().isEmpty()) {
                    jTextField1.setText("검색해 주세요");
                }
            }
        });
        
        // 3. 추가 설정 
        setTableColumns();
        setupTableListener();
        setupApprovalColumnEditor();
        
        setTitle("관리자 예약 목록");
        setLocationRelativeTo(null);

        // 4. 데이터 로드 (이제 모델이 데이터를 로드하고, Observer인 View에게 알림을 줌)
        model.loadData(); 

        // 5. 알림 모니터링 시작 
        notificationController.startMonitoring();
    }

    public ReservationMgmtView(String userId) {
        this(); 
        setVisible(true);
    }

    // 옵저버 인터페이스 구현 메서드
    // 모델 데이터가 변경되면 이 메서드가 자동으로 호출됨.
    @Override
    public void onReservationUpdated(List<Reservation> reservationList) {
        SwingUtilities.invokeLater(() -> {
            DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
            tableModel.setRowCount(0); // 기존 테이블 내용 삭제

            for (Reservation r : reservationList) {
                tableModel.addRow(r.toArray());
            }
            
            System.out.println("[View] 테이블이 갱신되었습니다. 데이터 수: " + reservationList.size());
        });
    }
    
    private void setTableColumns() {
        String[] columnNames = {
            "학번", "학과", "이름", "구분", "강의실", "날짜(요일)", "시간", "목적", "상태"
        };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        jTable1.setModel(tableModel);
    }

    private void setupApprovalColumnEditor() {
        String[] statusOptions = {"예약대기", "승인", "거절"};
        JComboBox<String> comboBox = new JComboBox<>(statusOptions);

        if (jTable1.getColumnCount() > 8) {
            TableColumn statusColumn = jTable1.getColumnModel().getColumn(8);
            statusColumn.setCellEditor(new DefaultCellEditor(comboBox));
        }
    }

   private void setupTableListener() {
        jTable1.getModel().addTableModelListener(e -> {
            // 1. 값이 되돌려지는 중이라면 리스너 무시 (무한루프 방지)
            if (isReverting) return;
            if (e.getFirstRow() < 0) return; 
            
            int row = e.getFirstRow();
            int column = e.getColumn();

            // 8번 컬럼(승인 여부)이 변경되었을 때
            if (column == 8 && row < jTable1.getRowCount()) {
                // 데이터 가져오기
                String studentId = (String) jTable1.getValueAt(row, 0); 
                String roomName  = (String) jTable1.getValueAt(row, 4);
                
                String dateStr = (String) jTable1.getValueAt(row, 5);
                String date = dateStr.contains("(") ? dateStr.substring(0, dateStr.indexOf("(")) : dateStr;

                String timeStr = (String) jTable1.getValueAt(row, 6);
                String startTime = timeStr.contains("~") ? timeStr.split("~")[0] : timeStr;
                
                String newStatus = (String) jTable1.getValueAt(row, 8);

                //  이미 승인/거절/취소된 상태인지 확인
                String currentStatus = model.getCurrentStatus(studentId, roomName, date, startTime);
                
                // 원래 상태가 '예약대기'가 아닌데, 값을 바꾸려고 한다면?
                if (currentStatus != null && !"예약대기".equals(currentStatus) && !currentStatus.equals(newStatus)) {
                    
                    // 경고 메시지
                    JOptionPane.showMessageDialog(this, 
                            "이미 '" + currentStatus + "' 처리된 예약은 변경할 수 없습니다.\n(취소가 필요한 경우 '강제 취소' 버튼을 이용하세요.)",
                            "변경 불가", JOptionPane.WARNING_MESSAGE);

                    // 값을 원래대로 되돌리기 
                    SwingUtilities.invokeLater(() -> {
                        isReverting = true; // 깃발 들기 (리스너 동작 멈춰!)
                        jTable1.setValueAt(currentStatus, row, 8); // 값 원복
                        isReverting = false; // 깃발 내리기
                    });
                    return; // 커맨드 실행 안 하고 종료
                }
               

                // 승인/거절 커맨드 실행 로직 
                ReservationCommand command = null;

                if ("승인".equals(newStatus)) {
                    command = new ApproveCommand(model, studentId, roomName, date, startTime);
                } else if ("거절".equals(newStatus)) {
                    command = new RejectCommand(model, studentId, roomName, date, startTime);
                } else {
                    model.updateStatus(studentId, roomName, date, startTime, newStatus);
                    return;
                }

                if (command != null) {
                    // 직접 execute() 하지 않고, Invoker에게 전달하여 실행
                    invoker.setCommand(command); // 리모컨에 명령 세팅
                    invoker.buttonPressed();     // 리모컨 버튼 누름
                }
            }
        });
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "학번", "학과", "이름", "강의실", "날짜", "시간", "승인 여부"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("사용규칙 관리");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("날짜 제한");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setText("강의실 정보");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("맑은 고딕", 0, 26)); // NOI18N
        jLabel2.setText("예약 리스트");

        jButton5.setText("로그아웃");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("제한");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("해제");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("예약/취소 현황");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jTextField1.setText("검색해 주세요");

        jButton9.setText("검색");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setText("알림");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setText("공지사항 관리");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setText("새로고침");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "전체", "이름", "학과", "학번", "구분", "강의실", "날짜", "상태" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton13.setText("예약 강제취소");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.setText("예약 통계 현황");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton3.setText("백업");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton15.setText("복구");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton16.setText("제한 사용자 목록");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(208, 208, 208)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton15))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton13)))))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton12)
                        .addComponent(jButton10)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jButton16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton13)
                    .addComponent(jButton6)
                    .addComponent(jButton7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton4)
                    .addComponent(jButton8)
                    .addComponent(jButton11)
                    .addComponent(jButton14))
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton15))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            RuleManagementController controller = new RuleManagementController("src/main/resources/rules.txt");
            controller.showView();
            this.dispose();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "규칙 화면 열기 실패", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        ReservationRepositoryModel repo = new ReservationRepositoryModel();
        ReservationServiceModel service = new ReservationServiceModel(repo);
        new CalendarController(service);  // 생성자에서 CalendarView 띄움
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        int result = JOptionPane.showConfirmDialog(this, "로그아웃 하시겠습니까?", "로그아웃", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
    try {
   
        java.net.Socket socket = ServerClient.SocketManager.getSocket();
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    } catch (java.io.IOException e) {
        e.printStackTrace();
    }
    ServerClient.CommandProcessor.resetInstance();
    login.ConnectView view = new login.ConnectView();
    view.setVisible(true);
    
    this.dispose();
}
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "제한할 사용자를 선택하세요.");
            return;
        }
        
        String studentId = (String) jTable1.getValueAt(selectedRow, 0); 
        String dept      = (String) jTable1.getValueAt(selectedRow, 1);
        String name      = (String) jTable1.getValueAt(selectedRow, 2);
        String type      = (String) jTable1.getValueAt(selectedRow, 3);
        
        // 컨트롤러에게 모든 정보 전달 -> banlist.txt에 저장됨
        oldController.banUser(studentId, name, dept, type); 
        
        JOptionPane.showMessageDialog(this, studentId + " 사용자가 제한되었습니다.");
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // 사용자 제한 해제 기능
        String studentId = JOptionPane.showInputDialog(this, "해제할 사용자의 학번을 입력하세요.");
        if (studentId != null && !studentId.isEmpty()) {
            oldController.unbanUser(studentId); 
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        String keyword = jTextField1.getText().trim();
        if(keyword.equals("검색해 주세요")) keyword = "";
        
        String selectedType = (String) jComboBox1.getSelectedItem();
        
        model.filterData(keyword, selectedType);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        ClassroomView classroomView = new ClassroomView();
        classroomView.setVisible(true);
        classroomView.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        UserStatsController userStatsController = new UserStatsController();
        userStatsController.showUserStatsUI();
        this.dispose();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        notificationController.showNotificationView();
        notificationController.refreshNotifications();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            try {
                notice.NoticeRepository repo = new notice.NoticeRepository("src/main/resources/Notice.txt");
                notice.NoticeModel model = new notice.NoticeModel(repo);
                notice.NoticeEditorView editorView = new notice.NoticeEditorView(this); // this = 현재 JFrame
                notice.NoticeListView listView = new notice.NoticeListView();
                new notice.NoticeController(model, listView, editorView);
                listView.setLocationRelativeTo(null);
                listView.setVisible(true);
                this.dispose(); 
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
         model.loadData();
        
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
       int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "취소할 예약을 선택해주세요.");
            return;
        }

        String studentId = (String) jTable1.getValueAt(selectedRow, 0); 
        String roomName  = (String) jTable1.getValueAt(selectedRow, 4);
        String dateStr = (String) jTable1.getValueAt(selectedRow, 5);
        String date = dateStr.contains("(") ? dateStr.substring(0, dateStr.indexOf("(")) : dateStr;
        String timeStr = (String) jTable1.getValueAt(selectedRow, 6);
        String startTime = timeStr.contains("~") ? timeStr.split("~")[0] : timeStr;

        String currentStatus = model.getCurrentStatus(studentId, roomName, date, startTime);
        if ("거절".equals(currentStatus) || "취소".equals(currentStatus)) {
            JOptionPane.showMessageDialog(this, "이미 처리된 예약입니다.", "경고", JOptionPane.WARNING_MESSAGE); 
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "강제 취소하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // [커맨드 패턴 적용]
            ReservationCommand command = new CancelCommand(model, studentId, roomName, date, startTime);
            
            // Invoker를 통해 실행
            invoker.setCommand(command);
            invoker.buttonPressed();
            
            JOptionPane.showMessageDialog(this, "예약이 취소되었습니다.");
        }
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        new statistics.StatsView().setVisible(true);
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
            String result = backupManager.performBackup();

            if (result.startsWith("백업 완료")) {
                JOptionPane.showMessageDialog(this, 
                        result, // "백업 완료: 파일명.txt"
                        "백업 성공", 
                        JOptionPane.INFORMATION_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(this, 
                        result, 
                        "오류", 
                        JOptionPane.ERROR_MESSAGE);
            }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // 복구 실행 
            String result = backupManager.performRestore();
            
            if (result != null) { // 사용자가 취소하지 않음
                if (result.startsWith("성공")) {
                    JOptionPane.showMessageDialog(this, 
                            result, 
                            "복구 완료", 
                            JOptionPane.INFORMATION_MESSAGE);
                    
                    model.loadData(); 
                    
                } else {
                    JOptionPane.showMessageDialog(this, 
                            result, 
                            "복구 실패", 
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            // result가 null이면 사용자가 '취소' 누른 것이므로 아무것도 안 함.
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed

            new BannedUserListView(this, oldController).setVisible(true);
    }//GEN-LAST:event_jButton16ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
