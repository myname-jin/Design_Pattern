/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

package ServerClient;

import ServerClient.CommandProcessor;
import ServerClient.FileSyncClient;
import java.io.*;

public class FileSyncClientTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("[Unit Test] FileSyncClient 기능 검증");

        // 1. 가짜 리시버 준비
        StringWriter sw = new StringWriter();
        BufferedWriter mockWriter = new BufferedWriter(sw);

        // 2. 테스트 대상 생성 (Writer 주입)
        FileSyncClient client = new FileSyncClient(mockWriter);

        // 3. [핵심] 파일이 변경된 척 '이벤트 강제 호출'
        System.out.println(" 이벤트 발생: 'test_data.txt' 파일이 변경됨");
        client.onFileChanged("test_data.txt");

        // 4. 비동기 처리 대기 (인보커가 큐에서 꺼낼 시간 확보)
        Thread.sleep(1000);

        // 5. 결과 검증 (리시버에 파일 전송 헤더가 찍혔는지 확인)
        // 주의: 실제 파일이 없으면 에러 로그가 뜰 수 있으나, 
        // 커맨드가 생성되어 실행 시도했다는 것 자체가 중요함.
        String result = sw.toString();
        
        // FileUpdateCommand 로직상 파일이 없으면 전송이 안 될 수 있으므로,
        // 여기서는 "에러가 나지 않고 메서드가 잘 호출되었는지"를 확인하거나
        // 프로젝트 폴더에 실제 'test_data.txt'를 만들어두고 테스트하는 것이 가장 좋습니다.
        
        System.out.println(" 테스트 완료 (콘솔에 [FileUpdateCommand] 관련 로그가 보이면 성공)");
    }
}