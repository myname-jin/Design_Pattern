/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package login;

/**
 *
 * @author msj02
 */
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import javax.swing.JOptionPane;
//import java.io.*;

public class SignupModel {
    /**
     * @exception 5개의 매개변수를 받는 회원가입 메서드
     * @param userId
     * @param password
     * @param role
     * @param userName
     * @param userDept
     * @return 
     */    
    public boolean registerUser(String userId, String password, String role, String userName, String userDept) {
        // 입력값 유효성 검사: 빈 값 입력 제한
        if (userId.isEmpty() || password.isEmpty() || role.isEmpty() || userName.isEmpty() || userDept.isEmpty()) {
            return false;
        }
        // role 값을 "admin" 또는 "학생" 또는 "교사"로 제한
        if (!role.equals("admin") && !role.equals("학생")&& !role.equals("교수")) {
            return false;
        }
        // role에 따라 저장할 파일 이름 선택
        String fileName = role.equals("admin") ? "admin_signup.txt" : "user_signup.txt";
        try {
            File file = new File("src/main/resources/" + fileName);

            // role에 따라 해당되는 파일에 사용자 정보 추가
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(userId + "," + password + "," + userName + "," + userDept + "," + role);
                writer.newLine();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 관리자 회원가입 정보 txt에서 Id와 Pw를 관리자 로그인 txt로 전달하는 메서드
     */
    public void adminTransfer() {
        String[] sourceFiles = {
            "src/main/resources/admin_signup.txt",
        };
        String outputFile = "src/main/resources/ADMIN_LOGIN.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, true))) {
            for (String filePath : sourceFiles) {
                try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] tokens = line.split(",");
                        if (tokens.length >= 2) {
                            String id = tokens[0];
                            String pw = tokens[1];
                            writer.write("\n"+id + "," + pw);
                            writer.newLine();
                        }
                    }
                }
            }
            System.out.println("관리자 Id, Pw 정보를 " + outputFile + "로 전달했습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 사용자 회원가입 정보 txt에서 Id와 Pw를 사용자 로그인 txt로 전달하는 메서드
    public void userTransfer() {
        String[] sourceFiles = {
            "src/main/resources/user_signup.txt"
        };
        String outFile = "src/main/resources/USER_LOGIN.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile, true))) {
            for (String filePath : sourceFiles) {
                try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] tokens = line.split(",");
                        if (tokens.length >= 2) {
                            String id = tokens[0];
                            String pw = tokens[1];
                            writer.write("\n"+id + "," + pw);
                            writer.newLine();
                        }
                    }
                }
            }
            System.out.println("사용자 Id, Pw 정보를 " + outFile + "로 전달했습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 관리자와 사용자 정보 파일에서 Id, Pw, 학과를 예약정보 파일로 전달하는 메서드
    public void bothinfoTransfer() {        
        String[] sourceFiles = {
            "src/main/resources/admin_signup.txt",
            "src/main/resources/user_signup.txt"
        };
        String outputFile = "src/main/resources/FOR_RESERVATION.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (String filePath : sourceFiles) {
                try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] tokens = line.split(",");
                        if (tokens.length >= 2) {
                            String id = tokens[0];
                            String pw = tokens[1];
                            String deft = tokens[3];
                            writer.write(id + "," + pw + "," + deft);
                            writer.newLine();
                        }
                    }
                }
            }
            System.out.println("Id, Pw, 학과 정보를 " + outputFile + "로 전달했습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

