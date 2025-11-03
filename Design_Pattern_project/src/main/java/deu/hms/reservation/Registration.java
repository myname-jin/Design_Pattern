/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package deu.hms.reservation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.concurrent.*;
import java.time.Duration;
import java.time.format.DateTimeParseException;
import deu.hms.reservation.ReservationUtils;
import java.io.IOException; 
import java.util.UUID;
import deu.hms.utility.CardRegistFrame; //카드등록 불러옴
import java.awt.Frame;
import javax.swing.ButtonGroup;
import deu.hms.utility.HotelRoomReservationUI;


/**
 *
 * @author adsd3
 */
public class Registration extends JFrame {
    
    private reservationFrame reservationFrame;
    private JTable mainTable; // Reservation 테이블과 연결
    private DefaultTableModel tableModel;
    private static int uniqueNumber = 1;
    private int editingRow = -1; // 수정 중인 행의 인덱스 (-1은 수정 아님)
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1); //타이머 
    private CardManager cardManager = new CardManager();
    private ReservationStatusScheduler statusScheduler = new ReservationStatusScheduler();
private reservationFrame parentFrame;
//버튼
    private ButtonGroup paymentGroup = new ButtonGroup(); // 클래스 필드로 선언
private javax.swing.JLabel reservationStatusLabel;




    // 수정 중인 행 인덱스 설정 메서드 수정버튼을 눌렸을때 작동하는 메소드
     // 수정 중인 행 인덱스 설정 메서드 수정버튼을 눌렸을때 작동하는 메소드
    public void setEditingRow(int rowIndex) {
    this.editingRow = rowIndex;
    }
      public void setRegistrationData(String name, String address, String phoneNumber, String checkInDate,
                                String checkOutDate, String roomNumber, String guestCount,
                                String paymentMethod, String status, String stayCost) {
         textName.setText(name);
    textAddress.setText(address);
    textPhoneNumber.setText(phoneNumber);
    textCheckInDate.setText(checkInDate);
    textCheckOutDate.setText(checkOutDate);
    textRoomNumber.setText(roomNumber);
    textGuestCount.setText(guestCount);
    Money.setText(stayCost);

        if (paymentMethod.equals("현장결제")) {
        onSitePaymentButton.setSelected(true);
    } else if (paymentMethod.equals("카드결제")) {
        cardRegistButton.setSelected(true);
    }
        
    }
    public Registration(reservationFrame parentFrame) {
     this.parentFrame = parentFrame;
    if (parentFrame == null) {
        System.err.println("parentFrame이 null입니다. reservationFrame 객체가 제대로 전달되지 않았습니다.");
    }
    initComponents();
}

    public Registration(JTable table) {
        this.mainTable = table;
        initComponents();
    }

 

   
       // LocalDateTime targetTime = LocalDateTime.of(checkInDay, LocalTime.of(18, 0));

 
    
public boolean isCardRegistered() {
        // 카드 등록 여부를 확인하는 로직 구현 (예: cardRegistButton.isSelected() 등)
        return cardRegistButton.isSelected();
    }
  


public ReservationData populateReservationData() { 
        String uniqueNumber;
    if (editingRow != -1) {
        uniqueNumber = parentFrame.getMainTable().getValueAt(editingRow, 0).toString(); // 기존 고유번호 유지
    } else {
        uniqueNumber = UUID.randomUUID().toString(); // 새로운 고유번호 생성
    }
String Status = labelCardStatus.isVisible() ? "카드등록" : "카드미등록";
    
return new ReservationData(
        uniqueNumber,
        textName.getText(),
        textAddress.getText(),
        textPhoneNumber.getText(),
        textCheckInDate.getText(),
        textCheckOutDate.getText(),
        textRoomNumber.getText(),
        textGuestCount.getText(),
        Money.getText(),
        onSitePaymentButton.isSelected() ? "현장결제" : "카드결제",
        Status // 카드 상태 포함

    );
}

//Registration에서 저장버튼 
public void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {
     DefaultTableModel model = (DefaultTableModel) parentFrame.getMainTable().getModel();
    ReservationData updatedData = populateReservationData();
    
     

    try {
        // 선택된 행이 있을 경우 삭제 
        if (editingRow != -1) { 
            model.removeRow(editingRow); // 테이블에서 선택된 행 삭제
            FileManager.deleteFromFile(updatedData.getUniqueNumber(), "Reservation.txt"); // 파일에서도 삭제
        }

        // 새 데이터 추가
        FileManager.saveToFile(updatedData.toCSV());
        TableManager.addRow(model, updatedData);
        
        

        JOptionPane.showMessageDialog(this, "데이터가 성공적으로 저장되었습니다!", "성공", JOptionPane.INFORMATION_MESSAGE);
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "파일 처리 중 오류가 발생했습니다: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
    }

    this.dispose(); // 창 닫기
    editingRow = -1; // 수정 상태 초기화
}

public void transferRegistrationToReservation() {
    DefaultTableModel model = (DefaultTableModel) reservationFrame.getMainTable().getModel();

    // ReservationData 객체 생성
    ReservationData reservationData = populateReservationData();

    // ReservationUtils의 addOrUpdateRow 호출
    ReservationUtils.addOrUpdateRow(model, reservationData);
}



    private void clearFields() {
        textName.setText("");
        textAddress.setText("");
        textPhoneNumber.setText("");
        textCheckInDate.setText("");
        textCheckOutDate.setText("");
        textRoomNumber.setText("");
        textGuestCount.setText("");
    }

    /**
     * Creates new form Registration
     */
    

    public Registration() {
        initComponents();
        

    if (textCheckInDate == null || textCheckOutDate == null) {
        System.err.println("textCheckInDate 또는 textCheckOutDate가 초기화되지 않았습니다.");
    }
      
        paymentTypeRegistButton.setEnabled(false);
        paymentType.setEnabled(false);
 
    }
 

 //카드상태를 보여주는거
public void showCardRegistrationStatus() {
    labelCardStatus.setText("등록완료");
    labelCardStatus.setVisible(true);
}
 //체크인날짜.아웃날짜 불러오기
    public void updateDates(String checkInDate, String checkOutDate) {
    if (textCheckInDate != null && textCheckOutDate != null) {
        textCheckInDate.setText(checkInDate); // 텍스트 필드에 값 설정
        textCheckOutDate.setText(checkOutDate); // 텍스트 필드에 값 설정
    } 
}
    public reservationFrame getParentFrame() { //저장버튼에 null뜨는현상
        return parentFrame;
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        paymentButtonGroup = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        name = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        onSitePaymentButton = new javax.swing.JRadioButton();
        cardRegistButton = new javax.swing.JRadioButton();
        paymentTypeRegistButton = new javax.swing.JButton();
        textName = new java.awt.TextField();
        textCheckOutDate = new java.awt.TextField();
        textAddress = new java.awt.TextField();
        textPhoneNumber = new java.awt.TextField();
        textCheckInDate = new java.awt.TextField();
        textRoomNumber = new java.awt.TextField();
        textGuestCount = new java.awt.TextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        Money = new javax.swing.JTextPane();
        jLabel11 = new javax.swing.JLabel();
        reservationsubmit = new javax.swing.JButton();
        back = new javax.swing.JButton();
        labelReservationStatus = new javax.swing.JLabel();
        labelCardStatus = new javax.swing.JLabel();
        paymentType = new javax.swing.JComboBox<>();
        guestPlusButton = new javax.swing.JButton();

        jScrollPane1.setViewportView(jTextPane1);

        name.setText("이름");

        jLabel2.setText("전화번호");

        jLabel3.setText("주소");

        jLabel4.setText("예상 체크인 날짜");

        jLabel5.setText("예상 체크아웃 날짜");

        jLabel6.setText("방번호");

        jLabel7.setText("인원수");

        jLabel9.setText("금액");

        jLabel10.setText("결제수단");

        paymentButtonGroup.add(onSitePaymentButton);
        onSitePaymentButton.setText("현장결제");
        onSitePaymentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onSitePaymentButtonActionPerformed(evt);
            }
        });

        paymentButtonGroup.add(cardRegistButton);
        cardRegistButton.setText("카드결제");
        cardRegistButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cardRegistButtonActionPerformed(evt);
            }
        });

        paymentTypeRegistButton.setText("등록");
        paymentTypeRegistButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymentTypeRegistButtonActionPerformed(evt);
            }
        });

        textName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textNameActionPerformed(evt);
            }
        });

        textAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textAddressActionPerformed(evt);
            }
        });

        textCheckInDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textCheckInDateActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(Money);

        jLabel11.setText("원");

        reservationsubmit.setText("저장");
        reservationsubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reservationsubmitActionPerformed(evt);
            }
        });

        back.setText("뒤로");
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });

        labelReservationStatus.setText("예약완료! 체크인은 당일 6시입니다 !!");
        labelReservationStatus.setVisible(false);

        labelCardStatus.setText("등록완료");
        labelCardStatus.setVisible(false);

        paymentType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "현금", "카드" }));
        paymentType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymentTypeActionPerformed(evt);
            }
        });

        guestPlusButton.setText("인원 추가");
        guestPlusButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guestPlusButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel5)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGap(54, 54, 54)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel3)
                                        .addComponent(name)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel9)))
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textPhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(textName, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(textCheckOutDate, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(textCheckInDate, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                            .addGap(2, 2, 2)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jLabel11))
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                    .addComponent(textGuestCount, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(guestPlusButton))
                                                .addComponent(textRoomNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(textAddress, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(onSitePaymentButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(paymentType, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cardRegistButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(labelCardStatus))
                                    .addComponent(paymentTypeRegistButton, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(138, 138, 138)
                        .addComponent(back, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(reservationsubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(118, 118, 118)
                .addComponent(labelReservationStatus)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(16, 16, 16)
                        .addComponent(jLabel5)
                        .addGap(17, 17, 17)
                        .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addGap(30, 30, 30)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel9)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(textCheckInDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textCheckOutDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(textName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(textAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textPhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(textRoomNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textGuestCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(guestPlusButton))))
                .addGap(8, 8, 8)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(onSitePaymentButton)
                    .addComponent(cardRegistButton)
                    .addComponent(paymentTypeRegistButton)
                    .addComponent(paymentType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(back)
                            .addComponent(reservationsubmit)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(labelCardStatus)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelReservationStatus)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void onSitePaymentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onSitePaymentButtonActionPerformed
    labelCardStatus.setVisible(false);
    }//GEN-LAST:event_onSitePaymentButtonActionPerformed

    private void cardRegistButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cardRegistButtonActionPerformed
   
    }//GEN-LAST:event_cardRegistButtonActionPerformed

    private void paymentTypeRegistButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentTypeRegistButtonActionPerformed
    CardRegistFrame cardRegistWindow = new CardRegistFrame(this);
    cardRegistWindow.setVisible(true);
        cardRegistWindow.setLocationRelativeTo(this);  // 부모 컴포넌트를 기준으로 중앙에 배치  

    // 창이 닫힌 후 카드 정보가 등록되었는지 확인
    cardRegistWindow.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
       public void windowClosed(java.awt.event.WindowEvent e) {
            // 파일에서 카드 정보 읽기
            String cardInfo = readCardInfoFromFile();
            if (cardInfo != null) {
                labelCardStatus.setText("등록완료"); // 등록완료로 변경
                labelCardStatus.setVisible(true);
            } else {
                labelCardStatus.setText("미등록"); // 미등록 상태로 설정
                labelCardStatus.setVisible(false);
            }
        }
    });
}

    // 파일에서 카드 정보를 읽는 메서드
    private String readCardInfoFromFile() {
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader("card_data.txt"))) {
            return reader.readLine(); // 첫 번째 줄 읽기
        } catch (java.io.IOException ex) {
            return null; // 읽기 실패 시 null 반환
        }

    
// 카드 등록 완료 후 라벨 업데이트
    }//GEN-LAST:event_paymentTypeRegistButtonActionPerformed

    private void textNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textNameActionPerformed

    private void textAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textAddressActionPerformed

    private void reservationsubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reservationsubmitActionPerformed
     try {
        if (parentFrame == null) {
            throw new NullPointerException("parentFrame이 null입니다. ReservationFrame 객체가 전달되지 않았습니다.");
        }

        DefaultTableModel model = (DefaultTableModel) parentFrame.getMainTable().getModel();
        ReservationData updatedData = populateReservationData();

        if (editingRow != -1) {
            TableManager.updateRow(model, editingRow, updatedData);
            FileManager.updateInFile(updatedData, "Reservation.txt");
        } else {
            TableManager.addRow(model, updatedData);
            FileManager.saveToFile(updatedData.toCSV());
        }

        JOptionPane.showMessageDialog(this, "저장 완료!", "성공", JOptionPane.INFORMATION_MESSAGE);

        ReservationStatusScheduler statusScheduler = new ReservationStatusScheduler();
        statusScheduler.scheduleStatusUpdate(updatedData.getCheckInDate(), model.getRowCount() - 1, model);

        labelReservationStatus.setText("예약완료! 체크인은 당일 6시입니다!!");
        labelReservationStatus.setVisible(true);

    } catch (NullPointerException e) {
        JOptionPane.showMessageDialog(this, "오류 발생: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "저장 실패: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }

    editingRow = -1; 
    }//GEN-LAST:event_reservationsubmitActionPerformed

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
      editingRow = -1; // 수정 상태 초기화
    this.setVisible(false); // 현재 창(Registration)을 닫기

    // 숨겨진 reservationFrame 다시 보이기
    if (parentFrame != null) { // 부모 프레임이 null인지 확인
        parentFrame.setVisible(true); // reservationFrame 다시 표시
        parentFrame.toFront(); // 최상단으로 가져오기
        parentFrame.repaint();// 새로고침
        parentFrame.setSize(850, 250);

    } else {
        // reservationFrame이 없으면 새로 생성해서 표시
            reservationFrame newReservationFrame = new reservationFrame();

        newReservationFrame.setVisible(true); 
        newReservationFrame.setSize(850, 250);
        newReservationFrame.setLocationRelativeTo(null); // 화면 중앙에 표시
    }
    }//GEN-LAST:event_backActionPerformed

    private void paymentTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_paymentTypeActionPerformed

    private void guestPlusButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guestPlusButtonActionPerformed
        try {
            int currentCost = Integer.parseInt(Money.getText().trim());
            currentCost += 20000;
            Money.setText(String.valueOf(currentCost));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "객실 금액이 올바르지 않습니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_guestPlusButtonActionPerformed

    private void textCheckInDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textCheckInDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textCheckInDateActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane Money;
    private javax.swing.JButton back;
    private javax.swing.JRadioButton cardRegistButton;
    private javax.swing.JButton guestPlusButton;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JLabel labelCardStatus;
    private javax.swing.JLabel labelReservationStatus;
    private javax.swing.JLabel name;
    private javax.swing.JRadioButton onSitePaymentButton;
    private javax.swing.ButtonGroup paymentButtonGroup;
    private javax.swing.JComboBox<String> paymentType;
    private javax.swing.JButton paymentTypeRegistButton;
    private javax.swing.JButton reservationsubmit;
    private java.awt.TextField textAddress;
    private java.awt.TextField textCheckInDate;
    private java.awt.TextField textCheckOutDate;
    private java.awt.TextField textGuestCount;
    private java.awt.TextField textName;
    private java.awt.TextField textPhoneNumber;
    private java.awt.TextField textRoomNumber;
    // End of variables declaration//GEN-END:variables
}
