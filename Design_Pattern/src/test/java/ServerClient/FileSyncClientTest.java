/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerClient;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

public class FileSyncClientTest {

    @Test
    void testSyncFileSuccessfully() throws Exception {
        String filename = "Notice.txt";

        // 1) src/test/resources/Notice.txt 생성 (없으면)
        File source = new File("src/test/resources/" + filename);
        if (!source.exists()) {
            source.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(source)) {
                writer.write("📄 테스트용 동기화 내용입니다.");
            }
        }

        // 2) target/test-classes/Notice.txt 로 복사
        File target = new File("target/test-classes/" + filename);
        copyFile(source, target);
        assertTrue(target.exists(), "테스트 파일이 target/test-classes 에 존재해야 합니다.");

        // 3) 포트 5000에 모의 서버 띄우기
        final int port = 5000;
        try (ServerSocket mockServer = new ServerSocket(port)) {
            Thread serverThread = new Thread(() -> {
                try (Socket sock = mockServer.accept();
                     InputStream in = sock.getInputStream();
                     BufferedOutputStream devNull = new BufferedOutputStream(OutputStream.nullOutputStream())) {
                    // 클라이언트가 보낸 데이터를 모두 읽고 버린다
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) != -1) {
                        devNull.write(buf, 0, len);
                    }
                } catch (IOException ignored) {
                }
            });
            serverThread.setDaemon(true);
            serverThread.start();

            // 4) SocketManager에 연결 소켓 설정
            try (Socket clientSock = new Socket("127.0.0.1", port)) {
                SocketManager.setSocket(clientSock);
                // 5) 실제 동기화 메서드 호출
                FileSyncClient.syncFile(filename);
                // 예외 없이 끝나면 성공
            }

            // 모의 서버를 닫아 스레드 종료 유도
        }

        // 만약 syncFile 내부에 로그나 상태 플래그가 있다면 추가 검증 가능
    }

    private void copyFile(File source, File destination) throws IOException {
        destination.getParentFile().mkdirs();
        try (InputStream in = new FileInputStream(source);
             OutputStream out = new FileOutputStream(destination)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
    }
}