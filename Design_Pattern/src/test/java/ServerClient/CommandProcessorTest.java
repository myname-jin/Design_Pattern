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

public class CommandProcessorTest {
    @Test
    public void testSingletonInstanceUniqueness() {
        System.out.println("=== CommandProcessor (Singleton) 유일성 검증 ===");

        // 1. 인스턴스를 두 번 가져옴
        CommandProcessor cp1 = CommandProcessor.getInstance();
        CommandProcessor cp2 = CommandProcessor.getInstance();

        // 2. [핵심] 두 객체의 내부 HashCode를 명시적으로 출력
        int hash1 = System.identityHashCode(cp1);
        int hash2 = System.identityHashCode(cp2);

        System.out.println("  [객체 1] HashCode: " + hash1);
        System.out.println("  [객체 2] HashCode: " + hash2);
        
        // 3. 검증: 두 객체가 메모리상 동일한지 확인
        Assertions.assertSame(cp1, cp2, 
                "두 객체는 동일해야 합니다. 싱글톤 패턴 실패.");
    }
}