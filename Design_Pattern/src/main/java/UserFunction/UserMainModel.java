/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserFunction;

/**
 *
 * @author jms5310
 */

import java.io.*;
import java.net.Socket;

public class UserMainModel {
    private String userId;
    private String userName;
    private String userDept;
    private String userType;
    
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

   public UserMainModel(String userId, String userType, Socket socket, BufferedReader in, BufferedWriter out) {
    this.userId = userId;
    this.userType = userType;
    this.socket = socket;
    this.in = in;
    this.out = out;
        
        loadUserInfoFromFile(); //  파일에서 userName, userDept, userType 한 번만 읽어서 저장
    }
    
     private void loadUserInfoFromFile() {
        File file = new File("src/main/resources/user_signup.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5 && parts[0].equals(userId)) {
                        this.userName = parts[2]; // 이름
                        this.userDept = parts[3]; // 학과
                        this.userType = parts[4]; // 유저타입
                        return;
                    }
                }
            } catch (IOException e) {
                System.out.println("사용자 정보 파일 읽기 실패: " + e.getMessage());
            }
        }
        
        this.userName = "알수없음";
        this.userDept = "미지정";
        this.userType = "학생";
     }
     
    public String getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getUserDept() { return userDept; }
    public String getUserType() { return userType; }
    public Socket getSocket() { return socket; }
    public BufferedReader getIn() { return in; }
    public BufferedWriter getOut() { return out; }
}
