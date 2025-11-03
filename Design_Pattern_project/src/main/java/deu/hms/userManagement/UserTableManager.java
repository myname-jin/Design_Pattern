/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.userManagement;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;



/**
 *
 * @author yunhe
 */
public class UserTableManager {
   // 텍스트 파일에서 사용자 정보를 읽어와 JTable에 데이터를 설정
    public static void loadUsersToTable(JTable userTable, String filePath) {
        DefaultTableModel model = (DefaultTableModel) userTable.getModel();
        model.setRowCount(0); // 기존 데이터 초기화

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userFields = line.split(", ");
                if (userFields.length == 4) {
                    model.addRow(new Object[]{userFields[0], userFields[1], userFields[2], userFields[3]});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
