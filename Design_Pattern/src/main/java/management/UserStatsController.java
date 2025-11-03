/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package management;

import management.UserStatsModel;
import management.UserStatsView;

import java.io.*;
import java.util.*;

/**
 *
 * @author suk22
 */
public class UserStatsController {

    private String reservationFile;
    private String cancelLogFile;

    public UserStatsController() {
        this("src/main/resources/reservation.txt", "src/main/resources/cancel.txt");
    }

    // 파일 경로를 외부에서 주입받는 생성자 (테스트용)
    public UserStatsController(String reservationFile, String cancelLogFile) {
        this.reservationFile = reservationFile;
        this.cancelLogFile = cancelLogFile;
    }

    public void showUserStatsUI() {
        List<UserStatsModel> stats = loadUserStats();
        new UserStatsView(stats).setVisible(true);
    }

    public  List<UserStatsModel> loadUserStats() {
        Map<String, Integer> reservations = new HashMap<>();
        Map<String, Integer> cancels = new HashMap<>();
        Map<String, String> userNames = new HashMap<>();
        Map<String, List<String>> cancelReasonsMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(reservationFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 2) {
                    String name = parts[0];
                    String userId = parts[2];
                    reservations.put(userId, reservations.getOrDefault(userId, 0) + 1);
                    userNames.put(userId, name);
                }
            }
        } catch (IOException e) {
            System.err.println("reservation.txt 읽기 실패: " + e.getMessage());
        }

        // 취소 로그 읽기
        try (BufferedReader br = new BufferedReader(new FileReader(cancelLogFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2);  // userId, reason 두 부분으로 분리
                if (parts.length == 2) {
                    String userId = parts[0].trim();
                    String reason = parts[1].trim();

                    cancels.put(userId, cancels.getOrDefault(userId, 0) + 1);

                    List<String> reasons = cancelReasonsMap.getOrDefault(userId, new ArrayList<>());
                    reasons.add(reason);
                    cancelReasonsMap.put(userId, reasons);
                }
            }
        } catch (IOException e) {
            System.err.println("cancel_log.txt 읽기 실패: " + e.getMessage());
        }

        // 사용자 목록 통합
        Set<String> allUsers = new HashSet<>();
        allUsers.addAll(reservations.keySet());
        allUsers.addAll(cancels.keySet());

        List<UserStatsModel> result = new ArrayList<>();
        for (String userId : allUsers) {
            String name = userNames.getOrDefault(userId, "알 수 없음");
            int resCount = reservations.getOrDefault(userId, 0);
            int cancelCount = cancels.getOrDefault(userId, 0);
            List<String> reasons = cancelReasonsMap.getOrDefault(userId, Collections.emptyList());
            String reasonText = String.join("; ", reasons);
            result.add(new UserStatsModel(name, userId, resCount, cancelCount, reasonText));
        }

        return result;
    }
}
