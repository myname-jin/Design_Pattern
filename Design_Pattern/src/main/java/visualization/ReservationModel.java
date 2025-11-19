    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package visualization;

import java.io.*;
import java.util.*;

public class ReservationModel {
    private final Map<String, Integer> roomTotals = new TreeMap<>();
    private final Map<String, Map<String, Integer>> roomByDay = new HashMap<>();
    private final String[] rooms = {"911", "912", "913", "914", "915", "916", "917", "918"};
    private final String[] days = {"월", "화", "수", "목", "금"};
    // private final String filePath = "C:\\SWG\\JAVAPROJECKT\\src\\main\\resources/visualization.txt"; // 이 절대 경로 부분을 수정합니다.
    private final String resourceName = "/visualization.txt"; // 클래스패스 루트에서 파일을 찾습니다.

    public ReservationModel() {
        loadData();
    }

    public Map<String, Integer> getRoomTotals() {
        return roomTotals;
    }

    public Map<String, Integer> getRoomByDay(String room) {
        return roomByDay.getOrDefault(room, new HashMap<>());
    }

    private void loadData() {
        for (String room : rooms) {
            roomTotals.put(room, 0);
            roomByDay.put(room, new HashMap<>());
            for (String day : days) {
                roomByDay.get(room).put(day, 0);
            }
        }

        // 1. 클래스 로더를 사용하여 리소스를 스트림으로 읽어옵니다.
        // 이 방법은 JAR 파일로 패키징해도 작동하며, 현재 프로젝트의 리소스 폴더(src/main/resources 등)에
        // visualization.txt 파일이 있을 때 적합합니다.
        try (InputStream is = getClass().getResourceAsStream(resourceName);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            if (is == null) {
                // 리소스 파일을 찾지 못했을 경우, 테스트 데이터를 생성합니다.
                System.err.println("경로: " + resourceName + "에서 리소스 파일을 찾을 수 없습니다. 테스트 데이터를 생성합니다.");
                
                // 파일이 없으면 기본 데이터로 초기화 (원본 파일의 파일 생성 로직 대체)
                initializeWithDefaultData();
                return;
            }

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String day = parts[0].trim();
                    String room = parts[1].trim();
                    if (roomTotals.containsKey(room)) {
                        roomTotals.put(room, roomTotals.get(room) + 1);
                        Map<String, Integer> dayMap = roomByDay.get(room);
                        dayMap.put(day, dayMap.getOrDefault(day, 0) + 1);
                    }
                }
            }
        } catch (IOException | NullPointerException e) {
            // NullPointerException은 is == null 일 때 br.readLine()에서 발생할 수 있습니다.
            System.err.println("데이터 로딩 중 오류 발생: " + e.getMessage());
            initializeWithDefaultData(); // 오류 발생 시 기본 데이터로 초기화
        }
    }

    // 파일이 없을 경우 원본 코드에서 생성하던 테스트 데이터를 로드하는 메서드
    private void initializeWithDefaultData() {
        String defaultData = "월,911\n월,912\n화,911\n수,911\n수,913\n목,915\n금,911\n금,912\n화,914\n화,911\n목,911";
        
        try (BufferedReader br = new BufferedReader(new StringReader(defaultData))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String day = parts[0].trim();
                    String room = parts[1].trim();
                    if (roomTotals.containsKey(room)) {
                        roomTotals.put(room, roomTotals.get(room) + 1);
                        Map<String, Integer> dayMap = roomByDay.get(room);
                        dayMap.put(day, dayMap.getOrDefault(day, 0) + 1);
                    }
                }
            }
        } catch (IOException ignored) {
            // StringReader를 사용하므로 IOException은 발생하지 않습니다.
        }
    }
}