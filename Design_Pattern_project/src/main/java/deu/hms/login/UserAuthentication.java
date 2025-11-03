/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.login;

import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author yunhe
 */
public class UserAuthentication {
    private Users users;
    private boolean loggedIn;  // 로그인 상태 관리
    private String currentUserId;  // 현재 로그인한 사용자 ID

    public UserAuthentication() {
        this.users = new Users();  // Users 객체 초기화
        this.users.loadUsersFromFile();  // 파일에서 사용자 목록 불러오기
        this.loggedIn = false;  // 기본적으로 로그아웃 상태
        this.currentUserId = null;  // 현재 로그인한 사용자 ID는 없음
    }

    // 사용자 인증 메서드
    public boolean authenticate(String userId, String password) {
        // 사용자 정보를 찾기
        User user = users.findUserById(userId);
        if (user != null) {
            // 사용자 ID가 존재하면 비밀번호 확인
            if (user.getPassword().equals(password)) {
                loggedIn = true;  // 로그인 상태로 변경
                currentUserId = userId;  // 로그인한 사용자 ID 저장
                return true;
            }
        }
        return false;  // 사용자 ID가 없으면 인증 실패
    }

    // 인증된 사용자의 역할을 반환
    public String getUserRole(String userId) {
        User user = users.findUserById(userId);
        return (user != null) ? user.getRole() : null;
    }

    // 인증된 사용자의 이름을 반환
    public String getUserName(String userId) {
        User user = users.findUserById(userId);
        return (user != null) ? user.getName() : null;
    }

  // 로그아웃 메서드
    public void logout() {
        this.loggedIn = false;  // 로그인 상태를 false로 설정
        this.currentUserId = null;  // 현재 로그인한 사용자 ID를 초기화
    }

    // 로그인 상태 반환
    public boolean isLoggedIn() {
        return loggedIn;
    }

    // 현재 로그인한 사용자 ID 반환
    public String getCurrentUserId() {
        return currentUserId;
    }
}