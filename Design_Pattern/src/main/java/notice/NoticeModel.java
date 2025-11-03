/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package notice;

/**
 *
 * @author adsd3
 */
import java.util.*;

public class NoticeModel {
    private final List<Notice> notices;
    private final NoticeRepository repo;

    public NoticeModel(NoticeRepository repo) {
        this.repo = repo;
        this.notices = repo.loadNotices();
    }

    public List<Notice> getNotices() {
        return new ArrayList<>(notices);
    }

    public void addNotice(String content, String category) {
        notices.add(new Notice(content, category, false));
        repo.saveNotices(notices);
    }

    public void updateNotice(int index, String content, String category) {
        if (index >= 0 && index < notices.size()) {
            Notice n = notices.get(index);
            n.setContent(content);
            n.setCategory(category);
            repo.saveNotices(notices);
        }
    }

    public void deleteNotice(int index) {
        if (index >= 0 && index < notices.size()) {
            notices.remove(index);
            repo.saveNotices(notices);
        }
    }

    public void toggleRead(int index) {
        if (index >= 0 && index < notices.size()) {
            Notice n = notices.get(index);
            n.setRead(!n.isRead());
            repo.saveNotices(notices);
        }
    }
}
