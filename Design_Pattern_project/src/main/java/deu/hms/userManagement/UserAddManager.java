/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.userManagement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.List;

/**
 *
 * @author yunhe
 */
public class UserAddManager {
     /**
     * 사용자를 텍스트 파일과 JTable에 추가
     * 
     * @param newUser 추가할 사용자 객체
     * @param table   JTable 객체
     * @param filePath 텍스트 파일 경로
     */
    
    // 사용자 추가 메서드
    public static void addUser(User user, userManagementFrame parent) {
        BufferedWriter writer = null;
        try {
            // 1. 텍스트 파일에 사용자 추가
            writer = new BufferedWriter(new FileWriter("users.txt", true));
            writer.write(user.getId() + ", " + user.getPassword() + ", " + user.getName() + ", " + user.getRole());
            writer.newLine();

            // 2. JTable 갱신
            DefaultTableModel model = (DefaultTableModel) parent.getUserInfoTable().getModel();
            model.addRow(new Object[]{user.getId(), user.getPassword(), user.getName(), user.getRole()});

        } catch (IOException e) {
            // 예외 처리: 오류 메시지 출력
            System.err.println("사용자를 저장하는 중 오류가 발생했습니다: " + e.getMessage());
        } finally {
            // BufferedWriter 닫기
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.err.println("파일 닫기 중 오류가 발생했습니다: " + e.getMessage());
                }
            }
        }
    }
}
