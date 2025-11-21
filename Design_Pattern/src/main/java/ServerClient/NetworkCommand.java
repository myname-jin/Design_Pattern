/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerClient;
import java.io.IOException;

/**
 * Command 인터페이스 (커맨드 패턴)
 * 모든 네트워크 요청 명령이 구현해야 할 설계도입니다.
 * @author adsd3
 */
public interface NetworkCommand {
    void execute() throws IOException;
}