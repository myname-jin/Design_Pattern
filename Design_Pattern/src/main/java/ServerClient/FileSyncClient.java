/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerClient;

/**
 *
 * @author adsd3
 */

import java.io.*;
import java.net.Socket;

/**
 * ConcreteObserver (구체적인 관찰자)
 * FileWatcher로부터 알림을 받아 파일 동기화를 수행합니다.
 * FileObserver 인터페이스를 구현합니다.
 */
public class FileSyncClient implements FileObserver { // 1. FileObserver 구현
    private static final String CLIENT_RESOURCE_PATH = "src/main/resources/";

    /**
     * 2. FileWatcher로부터 알림을 받으면 이 메서드가 호출됩니다. 
     * -> FileWatcher 로부터 filename 데이터를 직접 push 받는다.
     * (기존의 static syncFile 메서드 로직이 이곳으로 이동)
     */
    @Override
    public void onFileChanged(String filename) {
        try {
            Socket socket = SocketManager.getSocket();  // ✅ 항상 SocketManager에서 받아옴
            if (socket == null || socket.isClosed()) {
                System.err.println("[FileSyncClient] 소켓이 닫혀있습니다.");
                return;
            }

            // 3. FileWatcher와 다른 스레드에서 소켓을 사용할 수 있으므로
            //    스트림은 매번 새로 생성하는 것이 안전합니다.
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            File file = new File(CLIENT_RESOURCE_PATH + filename);
            if (!file.exists()) {
                System.err.println("[FileSyncClient] 파일이 존재하지 않음: " + filename);
                return;
            }

            out.write("FILE_UPDATE:" + filename + "\n");
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    out.write(line + "\n");
                }
            }
            out.write("<<EOF>>\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // 4. 기존의 static syncFile(String filename) 메서드는
    //    이제 onFileChanged가 대체하므로 삭제합니다.
}