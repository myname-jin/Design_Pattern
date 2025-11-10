/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerClient;

/**
 *
 * @author adsd3
 * 
 * '관찰자' 인터페이스 (옵저버 패턴)
 * FileSyncClient가 이 인터페이스를 구현합니다.
 */
public interface FileObserver {
    void onFileChanged(String filename);
}
