/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserFunction;

/**
 *
 * @author jms5310
 */
import notice.Notice;
import notice.NoticeModel;
import notice.NoticeRepository;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

public class UserNoticeModel {
    
    // 사용자 정보
    private String userId;
    private String userName;
    private String userDept;
    private String userType;
    
    // 네트워크 연결 정보
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    
    // 공지사항 관련
    private NoticeModel noticeModel;
    private List<Notice> filteredNotices;
    
    public UserNoticeModel(String userId, Socket socket, BufferedReader in, BufferedWriter out) {
        this.userId = userId;
        this.socket = socket;
        this.in = in;
        this.out = out;
        
        // 사용자 정보 로드
        loadUserInfo();
        
        // 공지사항 모델 초기화
        initializeNoticeModel();
    }
    
    private void loadUserInfo() {
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
        
        // 기본값 설정
        this.userName = "사용자";
        this.userDept = "미지정";
        this.userType = "학생";
    }
    
    private void initializeNoticeModel() {
        String filePath = System.getProperty("user.dir") + "/src/main/resources/Notice.txt";
        NoticeRepository repo = new NoticeRepository(filePath);
        this.noticeModel = new NoticeModel(repo);
    }
    
    public List<Notice> getAllNotices() {
        return noticeModel.getNotices();
    }
    
    public List<Notice> getFilteredNotices(String searchKeyword, String category) {
        List<Notice> allNotices = noticeModel.getNotices();
        
        this.filteredNotices = allNotices.stream()
                .filter(notice -> {
                    // 검색어 필터링
                    boolean matchesSearch = searchKeyword.isEmpty() || 
                            notice.getContent().toLowerCase().contains(searchKeyword.toLowerCase());
                    
                    // 카테고리 필터링
                    boolean matchesCategory = category.equals("전체") ||
                            notice.getCategory().equals(category) ||
                            (category.equals("읽음") && notice.isRead()) ||
                            (category.equals("안읽음") && !notice.isRead());
                    
                    return matchesSearch && matchesCategory;
                })
                .collect(Collectors.toList());
        
        return this.filteredNotices;
    }
    
    public boolean markNoticeAsRead(int viewIndex) {
        if (filteredNotices == null || viewIndex < 0 || viewIndex >= filteredNotices.size()) {
            return false;
        }
        
        Notice selectedNotice = filteredNotices.get(viewIndex);
        List<Notice> allNotices = noticeModel.getNotices();
        int modelIndex = allNotices.indexOf(selectedNotice);
        
        if (modelIndex >= 0 && !selectedNotice.isRead()) {
            noticeModel.toggleRead(modelIndex);
            return true;
        }
        
        return false;
    }
    
    public Notice getNoticeAt(int viewIndex) {
        if (filteredNotices == null || viewIndex < 0 || viewIndex >= filteredNotices.size()) {
            return null;
        }
        return filteredNotices.get(viewIndex);
    }
    
    public int getUnreadNoticeCount() {
        return (int) noticeModel.getNotices().stream()
                .filter(notice -> !notice.isRead())
                .count();
    }
    
    // Getter 메서드들
    public String getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getUserDept() { return userDept; }
    public String getUserType() { return userType; }
    public Socket getSocket() { return socket; }
    public BufferedReader getIn() { return in; }
    public BufferedWriter getOut() { return out; }
}