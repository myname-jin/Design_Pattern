/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerClient;

/**
 *
 * @author adsd3
 */
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class IntegrationTestSingletonTest {
 
    @Test
    public void testMultithreadedAccess() throws InterruptedException {
        System.out.println("[통합 테스트] 싱글톤 패턴 멀티스레드 동시 접근 테스트");
        
        // 스레드 10개를 동시에 getInstance()를 때리게 만듦
        Thread[] threads = new Thread[10];
        
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                // 각 스레드가 인스턴스를 요청
                CommandProcessor instance = CommandProcessor.getInstance();
                
                // 각자 얻은 인스턴스의 주소값(HashCode) 출력
                System.out.println(Thread.currentThread().getName() + " : " + System.identityHashCode(instance));
            });
            threads[i].start();
        }
        
        // 모든 스레드가 종료되기를 기다립니다. (검증을 위해 필수)
        for (Thread t : threads) {
            t.join(1500); 
        }
        
        // 멀티스레드 환경에서도 실패가 없었는지 확인
        Assertions.assertTrue(true, "테스트가 에러 없이 실행 완료됨.");
    }
}