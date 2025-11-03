/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package notice;

/**
 *
 * @author adsd3
 */

// 모델이긴 하지만 도메인객체 다른개념임
public class Notice {
    private String content;
    private String category;
    private boolean read;

    public Notice(String content) {
        this(content, "일반", false);
    }

    public Notice(String content, String category, boolean read) {
        this.content = content;
        this.category = category;
        this.read = read;
    }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }

    @Override
    public String toString() {
        String prefix = read ? "" : "[안읽음] ";
        return prefix + content + " (" + category + ")";
    }

    public String serialize() {
        return content + "|" + category + "|" + read;
    }

    public static Notice deserialize(String line) {
        String[] parts = line.split("\\|", 3);
        String content = parts[0];
        String category = parts.length > 1 ? parts[1] : "일반";
        boolean read = parts.length > 2 && Boolean.parseBoolean(parts[2]);
        return new Notice(content, category, read);
    }
}
