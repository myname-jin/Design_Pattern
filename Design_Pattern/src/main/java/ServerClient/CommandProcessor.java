/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerClient;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Invoker (호출자) & Singleton
 * 리시버를 몰라도 되는 "순수한 실행기"가 되었습니다.
 */
public class CommandProcessor extends Thread {
    
    private static CommandProcessor instance;
    private final BlockingQueue<NetworkCommand> commandQueue = new LinkedBlockingQueue<>();

    private CommandProcessor() {}

    public static synchronized CommandProcessor getInstance() {
        if (instance == null) {
            instance = new CommandProcessor();
            instance.start(); // 스레드 시작
        }
        return instance;
    }

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
            instance.interrupt();
            instance = null;
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // 큐에서 명령을 꺼냄
                NetworkCommand cmd = commandQueue.take();
                
                // [수정됨] 인자 없이 실행! (커맨드가 알아서 전송함)
                cmd.execute(); 
            }
        } catch (InterruptedException e) {
            System.out.println("[CommandProcessor] 종료됨.");
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}