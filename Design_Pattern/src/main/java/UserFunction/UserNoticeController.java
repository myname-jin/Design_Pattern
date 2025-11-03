/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserFunction;

/**
 *
 * @author jms5310
 */
import ServerClient.LogoutUtil;
import notice.Notice;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.List;

public class UserNoticeController {
    private UserNoticeModel model;
    private UserNoticeView view;

    public UserNoticeController(String userId, java.net.Socket socket, 
                               java.io.BufferedReader in, java.io.BufferedWriter out) {
        // 모델 초기화
        this.model = new UserNoticeModel(userId, socket, in, out);
        
        // 뷰 초기화
        this.view = new UserNoticeView();
        
        // 로그아웃 처리 연결
        if (socket != null && out != null) {
            LogoutUtil.attach(view, userId, socket, out);
        }
        
        // 이벤트 리스너 등록
        initListeners();
        
        // 초기 데이터 로드
        refreshNoticeList();
        
        // 화면 표시
        view.setVisible(true);
    }

    private void initListeners() {
        // 검색 기능 - 실시간 검색
        view.addSearchListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { refreshNoticeList(); }
            
            @Override
            public void removeUpdate(DocumentEvent e) { refreshNoticeList(); }
            
            @Override
            public void changedUpdate(DocumentEvent e) { refreshNoticeList(); }
        });
        
        // 카테고리 필터 변경
        view.addFilterListener(e -> refreshNoticeList());
        
        // 공지사항 선택 시 읽음 처리
        view.addNoticeSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                handleNoticeSelection();
            }
        });
        
        // 뒤로가기 버튼
        view.addBackButtonListener(e -> handleBackButton());
        
        // 새로고침 버튼
        view.addRefreshButtonListener(e -> handleRefreshButton());
    }

    private void refreshNoticeList() {
        String searchKeyword = view.getSearchKeyword();
        String selectedCategory = view.getSelectedCategory();
        
        // 모델에서 필터링된 공지사항 조회
        List<Notice> filteredNotices = model.getFilteredNotices(searchKeyword, selectedCategory);
        List<Notice> allNotices = model.getAllNotices();
        
        // 뷰 업데이트
        view.updateNoticeList(filteredNotices);
        view.updateNoticeCount(filteredNotices.size(), allNotices.size());
        
        // 안읽은 공지사항 개수도 표시
        int unreadCount = model.getUnreadNoticeCount();
        view.updateUnreadCount(unreadCount);
    }

    private void handleNoticeSelection() {
        int selectedIndex = view.getSelectedNoticeIndex();
        if (selectedIndex >= 0) {
            Notice selectedNotice = model.getNoticeAt(selectedIndex);
            if (selectedNotice != null) {
                // 읽음 처리 (안읽은 상태인 경우에만)
                if (!selectedNotice.isRead()) {
                    boolean marked = model.markNoticeAsRead(selectedIndex);
                    if (marked) {
                        // 읽음 처리 후 목록 갱신
                        refreshNoticeList();
                    }
                }
                
                // 상세 내용 표시
                view.showNoticeDetail(selectedNotice);
            }
        }
    }

    private void handleBackButton() {
        view.dispose();
        new UserMainController(
            model.getUserId(), 
            model.getUserType(), 
            model.getSocket(), 
            model.getIn(), 
            model.getOut()
        );
    }

    private void handleRefreshButton() {
        // 모델 재초기화 (새로운 공지사항 로드)
        model = new UserNoticeModel(
            model.getUserId(), 
            model.getSocket(), 
            model.getIn(), 
            model.getOut()
        );
        
        // 목록 갱신
        refreshNoticeList();
        
        // 사용자에게 알림
        JOptionPane.showMessageDialog(
            view, 
            "공지사항을 새로고침했습니다.", 
            "알림", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}