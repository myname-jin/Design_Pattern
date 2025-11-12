/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerClient;

/**
 *
 * @author adsd3
 */

import java.io.*;
import java.net.Socket; // (Socket은 이제 불필요)

/**
 * ConcreteObserver (구체적인 관찰자)
 * "Push" 모델을 사용하며, "커맨드 패턴"을 호출합니다.
 */
public class FileSyncClient implements FileObserver {

    /**
     * FileWatcher로부터 'filename' 데이터를 직접 "Push" 받습니다.
     * 이제 이 메서드는 'Command'를 생성하는 'Client' 역할만 합니다.
     */
    @Override
    public void onFileChanged(String filename) {
        // 2. [커맨드 패턴 적용]
        //    모든 소켓 I/O 로직을 FileUpdateCommand로 이동시켰습니다.
        //    여기서는 FileUpdateCommand를 생성하여 큐에 넣기만 합니다.
        CommandProcessor.getInstance().addCommand(
            new FileUpdateCommand(filename)
        );
    }
}