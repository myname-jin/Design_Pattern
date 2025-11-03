/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ServerClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

public class SocketManagerTest {

    private ServerSocket serverSocket;
    private Socket testSocket;

    @AfterEach
    public void cleanup() {
        SocketManager.close();
        if (testSocket != null && !testSocket.isClosed()) {
            try {
                testSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testSetAndGetSocket() throws IOException {
        serverSocket = new ServerSocket(0); // 임시 포트
        testSocket = new Socket("localhost", serverSocket.getLocalPort());
        Socket serverSideSocket = serverSocket.accept();

        SocketManager.setSocket(serverSideSocket);

        assertNotNull(SocketManager.getSocket(), "Socket이 null이면 안 됩니다.");
        assertEquals(serverSideSocket, SocketManager.getSocket(), "set과 get된 소켓이 동일해야 합니다.");
    }

    @Test
    public void testIsAlive() throws IOException {
        serverSocket = new ServerSocket(0);
        testSocket = new Socket("localhost", serverSocket.getLocalPort());
        Socket serverSideSocket = serverSocket.accept();

        SocketManager.setSocket(serverSideSocket);

        assertTrue(SocketManager.isAlive(), "소켓이 열려 있어야 합니다.");

        SocketManager.close();
        assertFalse(SocketManager.isAlive(), "소켓이 닫혔으면 안 살아있어야 합니다.");
    }

    @Test
    public void testCloseSocket() throws IOException {
        serverSocket = new ServerSocket(0);
        testSocket = new Socket("localhost", serverSocket.getLocalPort());
        Socket serverSideSocket = serverSocket.accept();

        SocketManager.setSocket(serverSideSocket);
        SocketManager.close();

        assertTrue(SocketManager.getSocket().isClosed(), "소켓은 close() 이후 닫혀 있어야 합니다.");
    }
}