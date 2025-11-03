package deu.hms.roomservice;

import javax.swing.JRadioButton;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Calendar;

public class PaymentManager {
    // 클래스 멤버 변수들을 private으로 선언
    private final FileHandler fileHandler;
    private final String paymentFilePath;
    private DefaultTableModel currentPaymentModel;
    private String currentPaymentMethod;
    private int currentOrderNumber;
    private boolean isPaymentProcessing;
    
    // 생성자에서 초기화
    public PaymentManager() {
        this.fileHandler = new FileHandler();
        this.paymentFilePath = "Payment.txt";
        this.currentPaymentModel = null;
        this.currentPaymentMethod = "";
        this.currentOrderNumber = 0;
        this.isPaymentProcessing = false;
    }
    
    // Getter 메소드들
    public FileHandler getFileHandler() {
        return fileHandler;
    }
    
    public String getPaymentFilePath() {
        return paymentFilePath;
    }
    
    public DefaultTableModel getCurrentPaymentModel() {
        return currentPaymentModel;
    }
    
    public String getCurrentPaymentMethod() {
        return currentPaymentMethod;
    }
    
    public int getCurrentOrderNumber() {
        return currentOrderNumber;
    }
    
    public boolean isPaymentProcessing() {
        return isPaymentProcessing;
    }
    
    // Setter 메소드들 (final이 아닌 필드들에 대해서만)
    private void setCurrentPaymentModel(DefaultTableModel model) {
        this.currentPaymentModel = model;
    }
    
    private void setCurrentPaymentMethod(String method) {
        this.currentPaymentMethod = method;
    }
    
    private void setCurrentOrderNumber(int number) {
        this.currentOrderNumber = number;
    }
    
    private void setPaymentProcessing(boolean processing) {
        this.isPaymentProcessing = processing;
    }
    
    // 결제 처리 메인 메소드
    public void processPayment(JRadioButton cashButton, JRadioButton cardButton, 
                             DefaultTableModel originalModel, String serviceType) {
        setPaymentProcessing(true);
        try {
            if (!validateAndSetPaymentMethod(cashButton, cardButton)) {
                return;
            }
            
            setCurrentOrderNumber(generateOrderNumber());
            DefaultTableModel paymentModel = createPaymentModel(originalModel, serviceType);
            savePayment(paymentModel);
            showSuccessMessage();
            
        } catch (Exception e) {
            showErrorMessage("결제 처리 중 오류가 발생했습니다: " + e.getMessage());
        } finally {
            setPaymentProcessing(false);
        }
    }
    
    // 결제 방식 검증 및 설정
    private boolean validateAndSetPaymentMethod(JRadioButton cashButton, JRadioButton cardButton) {
        if (cashButton.isSelected()) {
            setCurrentPaymentMethod("현금결제");
            return true;
        } else if (cardButton.isSelected()) {
            setCurrentPaymentMethod("카드결제");
            return true;
        } else {
            showErrorMessage("결제방식을 선택해주세요.");
            return false;
        }
    }
    
    // 주문 번호 생성
    private int generateOrderNumber() {
        int orderNum = 1;
        try {
            File file = new File(paymentFilePath);
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String lastLine = null;
                    String line;
                    while ((line = reader.readLine()) != null) {
                        lastLine = line;
                    }
                    if (lastLine != null) {
                        String[] data = lastLine.split(",");
                        orderNum = Integer.parseInt(data[0]) + 1;
                    }
                }
            }
        } catch (Exception e) {
            orderNum = 1;
        }
        return orderNum;
    }
    
    // 결제 모델 생성
    private DefaultTableModel createPaymentModel(DefaultTableModel originalModel, String serviceType) {
        DefaultTableModel newModel = new DefaultTableModel(
            new Object[]{"순번", "시간", "메뉴", "수량", "가격", "결제방식"}, 0
        );
        
        Calendar now = Calendar.getInstance();
        String currentTime = String.format("%tF %tT", now, now);
        
        for (int i = 0; i < originalModel.getRowCount(); i++) {
            addPaymentRow(newModel, originalModel, i, currentTime, serviceType);
        }
        
        setCurrentPaymentModel(newModel);
        return newModel;
    }
    
    // 결제 행 추가
    private void addPaymentRow(DefaultTableModel newModel, DefaultTableModel originalModel, 
                             int rowIndex, String currentTime, String serviceType) {
        Object[] row = new Object[7];
        row[0] = getCurrentOrderNumber();
        row[1] = serviceType;
        row[2] = currentTime;
        row[3] = originalModel.getValueAt(rowIndex, 0); // 메뉴
        row[4] = originalModel.getValueAt(rowIndex, 1); // 수량
        row[5] = originalModel.getValueAt(rowIndex, 2); // 가격
        row[6] = getCurrentPaymentMethod();
        newModel.addRow(row);
    }
    
    // 결제 정보 저장
    private void savePayment(DefaultTableModel model) {
        fileHandler.saveReservationToFile(model, paymentFilePath);
    }
    
    // 성공 메시지 표시
    private void showSuccessMessage() {
        JOptionPane.showMessageDialog(null, 
            "결제가 완료되었습니다.",
            "결제 완료", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    // 에러 메시지 표시
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, 
            message,
            "오류", 
            JOptionPane.WARNING_MESSAGE);
    }
}