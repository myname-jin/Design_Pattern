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

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class UserNoticeView extends JFrame {
    
    // UI 컴포넌트
    private JTextField searchField;
    private JComboBox<String> categoryComboBox;
    private DefaultListModel<Notice> listModel;
    private JList<Notice> noticeList;
    private JTextArea detailTextArea;
    private JLabel countLabel;
    private JLabel unreadCountLabel;
    private JButton refreshButton;
    private JButton backButton;
    
    public UserNoticeView() {
        initComponents();
        setupLayout();
        setupListRenderer();
    }
    
    private void initComponents() {
        setTitle("공지사항 조회");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // 검색 필드
        searchField = new JTextField(20);
        searchField.setToolTipText("공지사항 내용을 검색하세요");
        
        // 카테고리 필터
        categoryComboBox = new JComboBox<>(new String[]{"전체", "일반", "긴급", "시스템", "읽음", "안읽음"});
        
        // 공지사항 목록
        listModel = new DefaultListModel<>();
        noticeList = new JList<>(listModel);
        noticeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        noticeList.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        
        // 상세 내용 표시 영역
        detailTextArea = new JTextArea();
        detailTextArea.setEditable(false);
        detailTextArea.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        detailTextArea.setLineWrap(true);
        detailTextArea.setWrapStyleWord(true);
        detailTextArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("공지사항 내용"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // 카운트 라벨
        countLabel = new JLabel("공지사항: 0개");
        countLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        
        // 안읽은 공지사항 카운트 라벨
        unreadCountLabel = new JLabel("안읽음: 0개");
        unreadCountLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        unreadCountLabel.setForeground(new Color(220, 20, 60)); // 진한 빨강색
        
        // 버튼들
        refreshButton = new JButton("새로고침");
        refreshButton.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        
        backButton = new JButton("뒤로가기");
        backButton.setFont(new Font("맑은 고딕", Font.BOLD, 12));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // 상단 패널 (검색 및 필터)
        JPanel topPanel = new JPanel(new BorderLayout());
        
        // 검색 패널
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("검색:"));
        searchPanel.add(searchField);
        searchPanel.add(Box.createHorizontalStrut(20));
        searchPanel.add(new JLabel("카테고리:"));
        searchPanel.add(categoryComboBox);
        
        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);
        
        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);
        
        // 중앙 패널 (공지사항 목록과 상세 내용)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(350);
        splitPane.setResizeWeight(0.4);
        
        // 왼쪽: 공지사항 목록
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder("공지사항 목록"));
        listPanel.add(new JScrollPane(noticeList), BorderLayout.CENTER);
        
        // 카운트 정보 패널
        JPanel countPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        countPanel.add(countLabel);
        countPanel.add(Box.createHorizontalStrut(20));
        countPanel.add(unreadCountLabel);
        listPanel.add(countPanel, BorderLayout.SOUTH);
        
        // 오른쪽: 상세 내용
        JScrollPane detailScrollPane = new JScrollPane(detailTextArea);
        
        splitPane.setLeftComponent(listPanel);
        splitPane.setRightComponent(detailScrollPane);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private void setupListRenderer() {
        noticeList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                if (value instanceof Notice) {
                    Notice notice = (Notice) value;
                    
                    // 읽지 않은 공지사항은 굵게 표시
                    if (!notice.isRead()) {
                        setFont(getFont().deriveFont(Font.BOLD));
                        setText("[안읽음] " + notice.getContent());
                    } else {
                        setFont(getFont().deriveFont(Font.PLAIN));
                        setText(notice.getContent());
                    }
                    
                    // 카테고리별 색상 구분
                    if (!isSelected) {
                        switch (notice.getCategory()) {
                            case "긴급":
                                setBackground(new Color(255, 230, 230)); // 연한 빨강
                                break;
                            case "시스템":
                                setBackground(new Color(230, 230, 255)); // 연한 파랑
                                break;
                            case "일반":
                            default:
                                setBackground(Color.WHITE);
                                break;
                        }
                    }
                }
                
                return this;
            }
        });
    }
    
    // 공지사항 목록 업데이트
    public void updateNoticeList(List<Notice> notices) {
        listModel.clear();
        for (Notice notice : notices) {
            listModel.addElement(notice);
        }
        
        // 첫 번째 항목 선택 (있는 경우)
        if (!notices.isEmpty()) {
            noticeList.setSelectedIndex(0);
        } else {
            detailTextArea.setText("표시할 공지사항이 없습니다.");
        }
    }
    
    // 공지사항 개수 업데이트
    public void updateNoticeCount(int filteredCount, int totalCount) {
        countLabel.setText(String.format("공지사항: %d개 (전체 %d개)", filteredCount, totalCount));
    }
    
    // 안읽은 공지사항 개수 업데이트
    public void updateUnreadCount(int unreadCount) {
        unreadCountLabel.setText("안읽음: " + unreadCount + "개");
        if (unreadCount > 0) {
            unreadCountLabel.setForeground(new Color(220, 20, 60)); // 진한 빨강
        } else {
            unreadCountLabel.setForeground(new Color(34, 139, 34)); // 초록색
        }
    }
    
    // 선택된 공지사항 상세 내용 표시
    public void showNoticeDetail(Notice notice) {
        if (notice != null) {
            StringBuilder detail = new StringBuilder();
            detail.append("카테고리: ").append(notice.getCategory()).append("\n");
            detail.append("상태: ").append(notice.isRead() ? "읽음" : "안읽음").append("\n");
            detail.append("────────────────────────────────\n\n");
            detail.append(notice.getContent());
            
            detailTextArea.setText(detail.toString());
            detailTextArea.setCaretPosition(0); // 스크롤을 맨 위로
        }
    }
    
    // Getter 메서드들
    public String getSearchKeyword() {
        return searchField.getText().trim();
    }
    
    public String getSelectedCategory() {
        return (String) categoryComboBox.getSelectedItem();
    }
    
    public int getSelectedNoticeIndex() {
        return noticeList.getSelectedIndex();
    }
    
    // 이벤트 리스너 등록 메서드들
    public void addSearchListener(DocumentListener listener) {
        searchField.getDocument().addDocumentListener(listener);
    }
    
    public void addFilterListener(ActionListener listener) {
        categoryComboBox.addActionListener(listener);
    }
    
    public void addNoticeSelectionListener(ListSelectionListener listener) {
        noticeList.addListSelectionListener(listener);
        
        // 선택 변경 시 자동으로 상세 내용 표시
        noticeList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Notice selectedNotice = noticeList.getSelectedValue();
                if (selectedNotice != null) {
                    showNoticeDetail(selectedNotice);
                }
            }
        });
    }
    
    public void addRefreshButtonListener(ActionListener listener) {
        refreshButton.addActionListener(listener);
    }
    
    public void addBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }
}