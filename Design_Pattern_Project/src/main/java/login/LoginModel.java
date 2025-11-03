/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package login;

/**
 *
 * @author adsd3
 */
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class LoginModel {

  public boolean validateCredentials(String userId, String password, String role) {
        // 클래스패스에서 파일 읽기
        String fileName = role.equals("admin") ? 
                "/ADMIN_LOGIN.txt" : 
                "/USER_LOGIN.txt";

        try (InputStream input = getClass().getResourceAsStream(fileName)) {
            if (input == null) {
                System.err.println("❌ 파일을 찾을 수 없습니다: " + fileName);
                return false;
            }

            Scanner scanner = new Scanner(input, StandardCharsets.UTF_8);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 2 &&    // 회원가입 정보 Id, Pw, 이름, 학과 중 Id, Pw 사용
                        parts[0].trim().equals(userId) &&
                        parts[1].trim().equals(password)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
