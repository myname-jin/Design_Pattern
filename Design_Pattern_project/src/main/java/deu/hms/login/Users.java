/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.login;
    
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author yunhe
 */
public class Users {
   private List<User> userList;

    public Users() {
        userList = new ArrayList<>();
    }

    // users.txt 파일에서 사용자 데이터를 읽어와 userList에 저장
    public void loadUsersFromFile() {
         try {
            // 상대경로로 수정: "users.txt" 파일이 프로젝트 폴더에 있어야 합니다.
            File file = new File("users.txt");
            System.out.println("현재 경로: " + file.getAbsolutePath());  // 디버깅을 위해 절대경로 출력

            if (!file.exists()) {
                System.out.println("파일이 존재하지 않습니다. 경로를 다시 확인하세요.");
                return;
            }

            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] userInfo = line.split(",\\s*"); // 쉼표와 공백을 기준으로 분리
                if (userInfo.length == 4) {
                    String userId = userInfo[0].trim();
                    String password = userInfo[1].trim();
                    String name = userInfo[2].trim();
                    String role = userInfo[3].trim();
                    User user = new User(userId, password, name, role);
                    userList.add(user);
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("사용자 데이터를 불러올 수 없습니다: " + e.getMessage());
        }
    }

    // 사용자 ID를 기반으로 특정 사용자 검색
    public User findUserById(String userId) {
        for (User user : userList) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
}
  
