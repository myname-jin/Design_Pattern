/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package notice;

/**
 *
 * @author adsd3
 */
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class NoticeController {
    private final NoticeModel model;
    private final NoticeListView listView;
    private final NoticeEditorView editorView;
    private List<Notice> filteredNotices;

    public NoticeController(NoticeModel model, NoticeListView listView, NoticeEditorView editorView) {
        this.model = model;
        this.listView = listView;
        this.editorView = editorView;

        // 검색 필드
        listView.addSearchListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { refreshList(); }
            public void removeUpdate(DocumentEvent e) { refreshList(); }
            public void changedUpdate(DocumentEvent e) { refreshList(); }
        });

        // 필터
        listView.addFilterListener(e -> refreshList());

        // 버튼들
        listView.addToggleReadListener(e -> onToggleRead());
        listView.addAddListener(e -> onAdd());
        listView.addEditListener(e -> onEdit());
        listView.addDeleteListener(e -> onDelete());

        //  뒤로가기 버튼 동작 추가
        listView.getBackButton().addActionListener(e -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception ignored) {}

            SwingUtilities.invokeLater(() -> {
                try {
                    management.ReservationMgmtView mgmtView = new management.ReservationMgmtView();
                    mgmtView.setLocationRelativeTo(null);
                    mgmtView.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace(); // 혹시 오류 발생 시 확인
                }
            });

            listView.dispose(); // 현재 창 닫기
        });

        refreshList(); // 최초 목록 표시
    }

    public void refreshList() {
        String search = listView.getSearchText().toLowerCase();
        String filterCat = listView.getFilterCategory();
        List<Notice> all = model.getNotices();
        filteredNotices = new ArrayList<>();

        for (Notice n : all) {
            boolean matchCat = filterCat.equals("전체")
                || n.getCategory().equals(filterCat)
                || (filterCat.equals("읽음") && n.isRead())
                || (filterCat.equals("안읽음") && !n.isRead());

            boolean matchSearch = n.getContent().toLowerCase().contains(search);

            if (matchCat && matchSearch) {
                filteredNotices.add(n);
            }
        }

        listView.setNotices(filteredNotices);
    }

    private void onAdd() {
        editorView.showEditor("", "일반");
        if (editorView.isSaved()) {
            String text = editorView.getText();
            String cat = editorView.getSelectedCategory();
            if (!text.isEmpty()) {
                model.addNotice(text, cat);
                refreshList();
            }
        }
    }

    private void onEdit() {
        int viewIdx = listView.getSelectedIndex();
        if (viewIdx >= 0) {
            Notice sel = filteredNotices.get(viewIdx);
            int modelIdx = model.getNotices().indexOf(sel);

            editorView.showEditor(sel.getContent(), sel.getCategory());
            if (editorView.isSaved()) {
                String text = editorView.getText();
                String cat = editorView.getSelectedCategory();
                if (!text.isEmpty()) {
                    model.updateNotice(modelIdx, text, cat);
                    refreshList();
                }
            }
        } else {
            JOptionPane.showMessageDialog(listView, "수정할 항목을 선택하세요.", "알림", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onDelete() {
        int viewIdx = listView.getSelectedIndex();
        if (viewIdx >= 0) {
            Notice sel = filteredNotices.get(viewIdx);
            int modelIdx = model.getNotices().indexOf(sel);

            int confirm = JOptionPane.showConfirmDialog(listView, "정말 삭제하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                model.deleteNotice(modelIdx);
                refreshList();
            }
        } else {
            JOptionPane.showMessageDialog(listView, "삭제할 항목을 선택하세요.", "알림", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onToggleRead() {
        int viewIdx = listView.getSelectedIndex();
        if (viewIdx >= 0) {
            Notice sel = filteredNotices.get(viewIdx);
            int modelIdx = model.getNotices().indexOf(sel);
            model.toggleRead(modelIdx);
            refreshList();
        } else {
            JOptionPane.showMessageDialog(listView, "읽음/안읽음 토글할 항목을 선택하세요.", "알림", JOptionPane.WARNING_MESSAGE);
        }
    }
}