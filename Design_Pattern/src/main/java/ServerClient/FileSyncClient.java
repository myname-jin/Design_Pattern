/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerClient;

/**
 *
 * @author adsd3
 */

import java.io.BufferedWriter;

/**
 * ConcreteObserver (구체적인 관찰자)
 * "Push" 모델을 사용하며, "커맨드 패턴"을 호출합니다.
 */
public class FileSyncClient implements FileObserver {

    // [추가] 리시버 (커맨드에게 넘겨주기 위해 필요)
    private final BufferedWriter writer;

    // [수정] 생성자에서 writer를 받아옴
    public FileSyncClient(BufferedWriter writer) {
        this.writer = writer;
    }

    /**
     * FileWatcher로부터 'filename' 데이터를 직접 "Push" 받습니다.
     * 이제 이 메서드는 'Command'를 생성하는 'Client' 역할만 합니다.
     */
    @Override
    public void onFileChanged(String filename) {
        // 2. [커맨드 패턴 적용]
        //    FileUpdateCommand 생성 시 writer(리시버)를 주입합니다.
        CommandProcessor.getInstance().addCommand(
            new FileUpdateCommand(writer, filename) // [수정] writer 전달
        );
    }
}