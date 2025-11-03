/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package management;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.*;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author suk22
 */
public class ReservationMgmtController {

    private static final String FILE_PATH = "src/main/resources/reservation.txt";
    private static final String BAN_LIST_FILE = "src/main/resources/banlist.txt";

    public List<ReservationMgmtModel> getAllReservations() {
        List<ReservationMgmtModel> reservations = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 12) {
                    reservations.add(new ReservationMgmtModel(
                            data[0], // name
                            data[2], // studentId
                            data[3], // department
                            data[4], // room
                            data[6], // date
                            data[8] + "~" + data[9], // time
                            data[11] // approved
                    ));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    public void updateApprovalStatus(String studentId, String newStatus) {
        List<String> updatedLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 12 && data[2].equals(studentId)) {
                    data[11] = newStatus;
                    updatedLines.add(String.join(",", data));
                } else {
                    updatedLines.add(line);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(null,
                "학번 " + studentId + "의 승인 여부가 '" + newStatus + "'(으)로 변경되었습니다.",
                "승인 결과",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public List<String> getBannedUsers() {
        List<String> bannedUsers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(BAN_LIST_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                bannedUsers.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bannedUsers;
    }

    public void banUser(String studentId) {
        List<String> bannedUsers = getBannedUsers();
        if (!bannedUsers.contains(studentId)) {
            bannedUsers.add(studentId);
            saveBannedUsers(bannedUsers);
        }
    }

    public void unbanUser(String studentId) {
        List<String> bannedUsers = getBannedUsers();

        if (!bannedUsers.contains(studentId)) {
            JOptionPane.showMessageDialog(null,
                    "제한된 사용자 목록에 없는 학번입니다: " + studentId,
                    "알림",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        bannedUsers.remove(studentId);
        saveBannedUsers(bannedUsers);

        JOptionPane.showMessageDialog(null,
                "학번 " + studentId + "의 예약 제한이 해제되었습니다.",
                "알림",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void saveBannedUsers(List<String> bannedUsers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BAN_LIST_FILE))) {
            for (String id : bannedUsers) {
                writer.write(id);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isUserBanned(String studentId) {
        List<String> bannedUsers = getBannedUsers();
        return bannedUsers.contains(studentId);
    }

    public List<ReservationMgmtModel> searchReservations(String name, String studentId, String room) {
        List<ReservationMgmtModel> allReservations = getAllReservations();
        List<ReservationMgmtModel> filtered = new ArrayList<>();

        for (ReservationMgmtModel r : allReservations) {
            boolean match = true;

            if (name != null && !name.isEmpty() && !r.getName().contains(name)) {
                match = false;
            }
            if (studentId != null && !studentId.isEmpty() && !r.getStudentId().contains(studentId)) {
                match = false;
            }
            if (room != null && !room.isEmpty() && !r.getRoom().contains(room)) {
                match = false;
            }

            if (match) {
                filtered.add(r);
            }
        }

        return filtered;
    }

}
