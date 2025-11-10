/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerClient;
/**
 * '주제' 인터페이스 (옵저버 패턴)
 * FileWatcher가 이 인터페이스를 구현합니다.
 * @author adsd3
 */
public interface FileSubject {
    /**
     * 옵저버(관찰자)를 등록합니다.
     * @param o 등록할 옵저버
     */
    void addObserver(FileObserver o);

    /**
     * 옵저버(관찰자)를 제거합니다.
     * @param o 제거할 옵저버
     */
    void removeObserver(FileObserver o);

    /**
     * 변경 사실을 모든 옵저버에게 알립니다.
     * @param filename 변경된 파일의 이름
     */
    void notifyChanged(String filename);
}