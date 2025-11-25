/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerClient;

/**
 *
 * @author adsd3
 */
import ServerClient.CommandProcessor;

public class IntegrationTestSingletonTest {
    public static void main(String[] args) {
        System.out.println("[통합 테스트] 싱글톤 패턴 멀티스레드 동시 접근 테스트");
        
        // 스레드 10개가 동시에 getInstance()를 때리게 만듦
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                // 각 스레드가 인스턴스를 요청
                CommandProcessor instance = CommandProcessor.getInstance();
                
                // 각자 얻은 인스턴스의 주소값(HashCode) 출력
                System.out.println(Thread.currentThread().getName() + " : " + System.identityHashCode(instance));
            }).start();
        }
    }
}
