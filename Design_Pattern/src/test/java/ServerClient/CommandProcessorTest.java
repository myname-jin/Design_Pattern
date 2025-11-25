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

public class CommandProcessorTest {
    public static void main(String[] args) {
        System.out.println("=== CommandProcessor (Singleton) 유일성 검증 ===");

        // 1. 인스턴스를 두 번 가져옴
        CommandProcessor cp1 = CommandProcessor.getInstance();
        CommandProcessor cp2 = CommandProcessor.getInstance();

        // 2. [핵심] 두 객체의 내부 HashCode를 명시적으로 출력
        int hash1 = System.identityHashCode(cp1);
        int hash2 = System.identityHashCode(cp2);

        System.out.println("  [객체 1] HashCode: " + hash1);
        System.out.println("  [객체 2] HashCode: " + hash2);
        
        // 3. 검증
        if (cp1 == cp2) {
            System.out.println(" Pass: 인스턴스 주소 일치 (두 객체는 동일합니다.)");
        } else {
            System.err.println(" Fail: 객체가 다르게 생성되었습니다.");
        }
    }
}