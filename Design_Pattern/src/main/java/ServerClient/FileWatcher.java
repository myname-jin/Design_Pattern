/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerClient;

/**
 *
 * @author adsd3
 */
import java.io.IOException;
import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import java.util.ArrayList; // 1. List 임포트 추가
import java.util.List;       // 2. ArrayList 임포트 추가

/**
 * ConcreteSubject (구체적인 주제)
 * 파일 변경을 감지하고 옵저버에게 알립니다.
 * FileSubject 인터페이스를 구현합니다.
 */
public class FileWatcher extends Thread implements FileSubject { // 3. FileSubject 구현
    private final String watchDir = "src/main/resources";
    
    // 4. 옵저버(관찰자) 리스트 추가
    private List<FileObserver> observers = new ArrayList<>();

    /**
     * 옵저버(관찰자)를 등록합니다.
     * @param o 등록할 옵저버
     */
    @Override
    public void addObserver(FileObserver o) {
        observers.add(o);
    }

    /**
     * 옵저버(관찰자)를 제거합니다.
     * @param o 제거할 옵저버
     */
    @Override
    public void removeObserver(FileObserver o) {
        observers.remove(o);
    }

    /**
     * 변경 사실을 모든 옵저버에게 알립니다.
     * @param filename 변경된 파일의 이름
     */
    @Override
    public void notifyChanged(String filename) {
        // 등록된 모든 옵저버에게 변경 사실을 알림
        for (FileObserver o : observers) {
            o.onFileChanged(filename);
        }
    }

    @Override
    public void run() {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(watchDir);
            path.register(watchService, ENTRY_MODIFY);

            while (true) {
                WatchKey key = watchService.take(); // blocking
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    if (kind == ENTRY_MODIFY) {
                        String filename = event.context().toString();
                        if (filename.endsWith(".txt")) {
                            // 5. 기존의 직접 호출 코드를 삭제
                            // FileSyncClient.syncFile(filename); // <-- 삭제
                            
                            // 6. 대신 옵저버에게 알림
                            notifyChanged(filename);
                        }
                    }
                }
                key.reset();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}