/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerClient;

import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileWatcherTest {

    private static final String WATCH_DIR = "src/test/resources";
    private static volatile boolean fileModified = false;

    @Test
    public void testFileWatcherDetectsModification() throws Exception {
        // 파일 이름
        String filename = "FileWatcherTest.txt";
        Path dirPath = Paths.get(WATCH_DIR);
        Path filePath = dirPath.resolve(filename);

        // 디렉터리 및 파일 생성 (없으면)
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }

        // 파일 변경 감지 스레드 시작
        Thread watcherThread = new Thread(() -> {
            try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
                dirPath.register(watchService, ENTRY_MODIFY);

                while (!Thread.currentThread().isInterrupted()) {
                    WatchKey key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        if (event.kind() == ENTRY_MODIFY && event.context().toString().equals(filename)) {
                            fileModified = true;
                            return;
                        }
                    }
                    key.reset();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        watcherThread.setDaemon(true);
        watcherThread.start();

        // 감시 준비 대기
        Thread.sleep(1000);

        // 파일 수정
        try (FileWriter writer = new FileWriter(filePath.toFile(), true)) {
            writer.write("Modified at " + System.currentTimeMillis() + "\n");
        }

        // 감지 대기
        Thread.sleep(1500);

        // 테스트 종료
        assertTrue(fileModified, "수정된 파일이 감지되어야 합니다.");
    }
}