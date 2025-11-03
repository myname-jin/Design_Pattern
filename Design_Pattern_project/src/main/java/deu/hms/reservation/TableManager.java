/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.reservation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author adsd3
 */
public class TableManager {
    private DefaultTableModel tableModel;



    public TableManager(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
    }

    // 테이블에 데이터 추가
   public static void addRow(DefaultTableModel model, ReservationData data) {
    model.addRow(new Object[]{
        data.getUniqueNumber(),
        data.getName(),
        data.getAddress(),
        data.getPhoneNumber(),
        data.getCheckInDate(),
        data.getCheckOutDate(),
        data.getRoomNumber(),
        data.getGuestCount(),
        data.getStayCost(),
        data.getPaymentMethod(),
        data.getStatus()
    });
}

   
   
 //txt수정버튼 기능 용도
     public static void updateRow(DefaultTableModel model, int rowIndex, ReservationData data) {
        if (rowIndex >= 0 && rowIndex < model.getRowCount()) {
            model.setValueAt(data.getUniqueNumber(), rowIndex, 0);
            model.setValueAt(data.getName(), rowIndex, 1);
            model.setValueAt(data.getAddress(), rowIndex, 2);
            model.setValueAt(data.getPhoneNumber(), rowIndex, 3);
            model.setValueAt(data.getCheckInDate(), rowIndex, 4);
            model.setValueAt(data.getCheckOutDate(), rowIndex, 5);
            model.setValueAt(data.getRoomNumber(), rowIndex, 6);
            model.setValueAt(data.getGuestCount(), rowIndex, 7);
            model.setValueAt(data.getStayCost(), rowIndex, 8);
            model.setValueAt(data.getPaymentMethod(), rowIndex, 9);
            model.setValueAt(data.getStatus(), rowIndex, 10);

        }
    }
      public List<String[]> readFile(String filePath) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // CSV 형식으로 구분된 데이터를 배열로 변환
                String[] rowData = line.split(","); // 콤마로 구분
                data.add(rowData);
            }
        } catch (IOException e) {
            System.err.println("파일 읽기 오류: " + e.getMessage());
        }
        return data;
    }
       public void loadTableData(List<String[]> fileData) {
        tableModel.setRowCount(0); // 기존 데이터 삭제
        for (String[] row : fileData) {
            tableModel.addRow(row);
        }
    }
}