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

public class FileSyncClient {
    private static final String CLIENT_RESOURCE_PATH = "src/main/resources/";

    public static void syncFile(String filename) {
        try {
            Socket socket = SocketManager.getSocket();  // ✅ 항상 SocketManager에서 받아옴
            if (socket == null || socket.isClosed()) {
                System.err.println("[FileSyncClient] 소켓이 닫혀있습니다.");
                return;
            }

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
}