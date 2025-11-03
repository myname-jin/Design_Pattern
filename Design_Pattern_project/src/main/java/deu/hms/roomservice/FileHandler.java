package deu.hms.roomservice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FileHandler {
    // 멤버 변수 선언
    private String filePath;
    private FileReader fileReader;
    private FileWriter fileWriter;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private DefaultTableModel tableModel;
    private DefaultComboBoxModel<String> comboBoxModel;
    
    // 생성자
    public FileHandler() {
        // 기본 생성자
    }
    
    public FileHandler(String filePath) {
        this.filePath = filePath;
    }
    
    // Getter/Setter 메소드
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public DefaultTableModel getTableModel() {
        return tableModel;
    }
    
    public void setTableModel(DefaultTableModel model) {
        this.tableModel = model;
    }
    
    // 객실 목록 파일 불러오기
    public void loadRoomNumbersFromFile(DefaultComboBoxModel<String> model, String filePath) {
        this.comboBoxModel = model;
        this.filePath = filePath;
        
        try {
            initializeReader();
            clearComboBoxModel();
            loadRoomData();
            closeReader();
        } catch (IOException e) {
            showError("호실 목록을 불러오는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    // 메뉴 목록 파일 불러오기
    public void loadMenuFromFile(DefaultTableModel model, String filePath) {
        this.tableModel = model;
        this.filePath = filePath;
        
        try {
            initializeReader();
            clearTableModel();
            loadMenuData();
            closeReader();
        } catch (Exception e) {
            showError("메뉴를 불러오는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    // 예약 목록 파일 불러오기
    public void loadReservationFromFile(DefaultTableModel model) {
        this.tableModel = model;
        this.filePath = "ServiceList.txt";
        
        try {
            initializeReader();
            clearTableModel();
            loadReservationData();
            closeReader();
        } catch (Exception e) {
            showError("예약 목록을 불러오는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    // 예약 목록 파일 저장
    public void saveReservationToFile(DefaultTableModel model, String filePath) {
        this.tableModel = model;
        this.filePath = filePath;
        
        try {
            initializeWriter();
            saveReservationData();
            closeWriter();
        } catch (Exception e) {
            showError("예약 목록 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    // 내부 헬퍼 메소드들
    private void initializeReader() throws IOException {
        fileReader = new FileReader(filePath);
        bufferedReader = new BufferedReader(fileReader);
    }
    
    private void initializeWriter() throws IOException {
        fileWriter = new FileWriter(filePath, true);
        bufferedWriter = new BufferedWriter(fileWriter);
    }
    
    private void clearComboBoxModel() {
        comboBoxModel.removeAllElements();
    }
    
    private void clearTableModel() {
        tableModel.setRowCount(0);
    }
    
    private void loadRoomData() throws IOException {
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] data = line.split(",");
            if (isValidRoomData(data)) {
                comboBoxModel.addElement(data[5].trim());
            }
        }
    }
    
    private void loadMenuData() throws IOException {
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] data = line.split(",");
            if (data.length > 2) {
                tableModel.addRow(new Object[]{data[1], data[2]});
            }
        }
    }
    
    private void loadReservationData() throws IOException {
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] data = line.split(",");
            tableModel.addRow(data);
        }
    }
    
   private void saveReservationData() throws IOException {
      int lastNumber = 0;
    if (fileExists()) {
        initializeReader();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] data = line.split(",");
            try {
                lastNumber = Integer.parseInt(data[0]);
            } catch (NumberFormatException e) {
                // 숫자 변환 실패 시 무시
            }
        }
        closeReader();
    }
    
    // 새로운 예약의 순번을 마지막 번호 + 1로 설정
    for (int i = 0; i < tableModel.getRowCount(); i++) {
        lastNumber++; // 각 행마다 순번 증가
        bufferedWriter.write(String.valueOf(lastNumber));
        bufferedWriter.write(",");
        
        // 나머지 데이터 저장
        for (int j = 1; j < tableModel.getColumnCount(); j++) {
            bufferedWriter.write(tableModel.getValueAt(i, j).toString());
            if (j < tableModel.getColumnCount() - 1) {
                bufferedWriter.write(",");
            }
        }
        bufferedWriter.newLine();
    }
}
    
    private boolean isValidRoomData(String[] data) {
        return data.length >= 7 && !data[6].trim().isEmpty();
    }
    
    private void closeReader() throws IOException {
        if (bufferedReader != null) bufferedReader.close();
        if (fileReader != null) fileReader.close();
    }
    
    private void closeWriter() throws IOException {
        if (bufferedWriter != null) bufferedWriter.close();
        if (fileWriter != null) fileWriter.close();
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(null, 
            message,
            "오류", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    // 파일 존재 여부 확인 메소드
    public boolean fileExists() {
        try {
            FileReader testReader = new FileReader(filePath);
            testReader.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    // 파일 초기화 메소드
    public void clearFile() {
        try {
            new FileWriter(filePath, false).close();
        } catch (IOException e) {
            showError("파일 초기화 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
