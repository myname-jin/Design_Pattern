/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package notice;

/**
 *
 * @author adsd3
 */
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class NoticeRepository {
    private final Path filePath;

    public NoticeRepository(String path) {
        this.filePath = Paths.get(path);
    }

    public List<Notice> loadNotices() {
        List<Notice> list = new ArrayList<>();
        if (Files.exists(filePath)) {
            try (BufferedReader br = Files.newBufferedReader(filePath)) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        list.add(Notice.deserialize(line));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public void saveNotices(List<Notice> notices) {
        try (BufferedWriter bw = Files.newBufferedWriter(filePath)) {
            for (Notice notice : notices) {
                bw.write(notice.serialize());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
