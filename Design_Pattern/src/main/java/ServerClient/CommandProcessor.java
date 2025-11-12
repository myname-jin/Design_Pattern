/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerClient;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Invoker (호출자) & Singleton (싱글톤)
 * 모든 네트워크 '쓰기' 요청을 큐에 받아 순차적으로 처리합니다.
 * "스레드 안전 큐 커맨드 패턴"의 핵심입니다.
 * @author adsd3
 */
public class CommandProcessor extends Thread {
    
    // 1. 싱글톤 인스턴스
    private static CommandProcessor instance;
    
    // 2. 명령 큐 (스레드 안전)
    private final BlockingQueue<NetworkCommand> commandQueue = new LinkedBlockingQueue<>();
    
    // 3. 리시버 (Receiver)
    private BufferedWriter out;

    // 싱글톤을 위한 private 생성자
    private CommandProcessor() {}

    /**
     * CommandProcessor의 유일한 인스턴스를 반환합니다.
     * @return CommandProcessor 인스턴스
     */
    public static synchronized CommandProcessor getInstance() {
        if (instance == null) {
            instance = new CommandProcessor();
        }
        return instance;
    }

    /**
     * 리시버(BufferedWriter)를 설정합니다.
     * ConnectView에서 소켓 연결 직후 단 한 번만 호출되어야 합니다.
     * @param out
     */
    public void setWriter(BufferedWriter out) {
        this.out = out;
    }

    /**
     * Client가 명령을 큐에 추가할 때 호출합니다. (Producer)
     * @param cmd 실행할 명령
     */
    public void addCommand(NetworkCommand cmd) {
        try {
            commandQueue.put(cmd);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
    public static synchronized void resetInstance() {
    if (instance != null) {
        instance.interrupt(); // 현재 스레드에 종료 신호
        instance = null;      // 싱글톤 인스턴스를 null로 변경
    }
}
    /**
     * 내부 스레드 (Consumer)
     * 큐를 감시하다가, 명령이 들어오면 순서대로 꺼내서 실행합니다.
     */
    @Override
    public void run() {
        if (out == null) {
            System.err.println("[CommandProcessor] 에러: BufferedWriter가 설정되지 않았습니다!");
            return;
        }
        
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // 큐에서 명령을 꺼낼 때까지 대기 (Blocking)
                NetworkCommand cmd = commandQueue.take();
                
                // 명령 실행 (Receiver에게 작업 지시)
                cmd.execute(out);
            }
        } catch (InterruptedException e) {
            System.out.println("[CommandProcessor] 종료됨.");
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
