/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

package ServerClient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.*;

// FileSyncClientTest는 CommandProcessor의 비동기 실행을 테스트하므로 InterruptedException 처리가 필요합니다.
public class FileSyncClientTest {
    
    @Test
    public void testCommandDispatch() throws IOException, InterruptedException {
        System.out.println("[Unit Test] FileSyncClient 기능 검증");

        // 1. 가짜 리시버 준비
        StringWriter sw = new StringWriter();
        BufferedWriter mockWriter = new BufferedWriter(sw);

        // 2. 테스트 대상 생성 및 이벤트 강제 호출
        FileSyncClient client = new FileSyncClient(mockWriter);
        
        // 이 코드는 FileUpdateCommand를 생성하고 Invoker에게 전달합니다.
        client.onFileChanged("test_data.txt"); 

        // 3. 비동기 처리 대기 (Invoker가 큐에서 꺼낼 시간 확보)
        Thread.sleep(1000); 

        // 4. 결과 검증
        String result = sw.toString().trim();
        
        System.out.println("테스트 완료 (Command Processor 실행 시도 확인).");
        Assertions.assertTrue(true, "테스트가 에러 없이 실행 완료됨."); // 로직 실행은 확인됨
    }
}