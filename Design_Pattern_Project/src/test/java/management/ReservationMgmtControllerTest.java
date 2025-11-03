/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package management;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author suk22
 */
public class ReservationMgmtControllerTest {

    private static Path tempReservationFile;
    private static Path tempBanListFile;
    private ReservationMgmtController controller;

    @BeforeAll
    static void setupFiles() throws IOException {
        tempReservationFile = Files.createTempFile("reservation", ".txt");
        tempBanListFile = Files.createTempFile("banlist", ".txt");

        // 샘플 예약 데이터 입력
        Files.write(tempReservationFile, List.of(
                "홍길동,01012345678,20231234,컴퓨터소프트웨어학과,913,A,2025-05-21,B,09:00,10:00,X,승인",
                "김철수,01087654321,20231235,컴퓨터소프트웨어학과,912,A,2025-05-22,B,11:00,12:00,X,승인"
        ));

        // 초기 차단 사용자 없음
        Files.write(tempBanListFile, List.of());
    }

    @BeforeEach
    void setUp() throws Exception {
        // 테스트용 컨트롤러 생성자를 통해 경로 주입 (생성자 수정이 필요할 수 있음)
        controller = new ReservationMgmtController() {
            @Override
            public List<ReservationMgmtModel> getAllReservations() {
                return readReservations(tempReservationFile.toString());
            }

            @Override
            public void updateApprovalStatus(String studentId, String newStatus) {
                updateApprovalStatus(studentId, newStatus, tempReservationFile.toString());
            }

            @Override
            public List<String> getBannedUsers() {
                return readBannedUsers(tempBanListFile.toString());
            }

            @Override
            public void banUser(String studentId) {
                List<String> list = readBannedUsers(tempBanListFile.toString());
                if (!list.contains(studentId)) {
                    list.add(studentId);
                    writeBannedUsers(list, tempBanListFile.toString());
                }
            }

            @Override
            public void unbanUser(String studentId) {
                List<String> list = readBannedUsers(tempBanListFile.toString());
                list.remove(studentId);
                writeBannedUsers(list, tempBanListFile.toString());
            }

            @Override
            public boolean isUserBanned(String studentId) {
                return readBannedUsers(tempBanListFile.toString()).contains(studentId);
            }

            @Override
            public List<ReservationMgmtModel> searchReservations(String name, String studentId, String room) {
                return super.searchReservations(name, studentId, room);
            }

            private List<ReservationMgmtModel> readReservations(String filePath) {
                try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                    List<ReservationMgmtModel> list = new java.util.ArrayList<>();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] data = line.split(",");
                        if (data.length >= 12) {
                            list.add(new ReservationMgmtModel(
                                    data[0], data[2], data[3], data[4],
                                    data[6], data[8] + "~" + data[9], data[11]
                            ));
                        }
                    }
                    return list;
                } catch (IOException e) {
                    return List.of();
                }
            }

            private void updateApprovalStatus(String studentId, String newStatus, String filePath) {
                try {
                    List<String> lines = Files.readAllLines(Path.of(filePath));
                    List<String> updated = new java.util.ArrayList<>();
                    for (String line : lines) {
                        String[] data = line.split(",");
                        if (data.length >= 12 && data[2].equals(studentId)) {
                            data[11] = newStatus;
                            updated.add(String.join(",", data));
                        } else {
                            updated.add(line);
                        }
                    }
                    Files.write(Path.of(filePath), updated);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private List<String> readBannedUsers(String filePath) {
                try {
                    return Files.readAllLines(Path.of(filePath));
                } catch (IOException e) {
                    return List.of();
                }
            }

            private void writeBannedUsers(List<String> list, String filePath) {
                try {
                    Files.write(Path.of(filePath), list);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Test
    void testGetAllReservations() {
        List<ReservationMgmtModel> list = controller.getAllReservations();
        assertEquals(2, list.size());
        assertEquals("홍길동", list.get(0).getName());
        assertEquals("912", list.get(1).getRoom());
    }

    @Test
    void testUpdateApprovalStatus() {
        controller.updateApprovalStatus("20231234", "승인");
        List<ReservationMgmtModel> updated = controller.getAllReservations();
        assertEquals("승인", updated.get(0).getApproved());
    }

    @Test
    void testBanAndUnbanUser() {
        String studentId = "20231234";
        assertFalse(controller.isUserBanned(studentId));

        controller.banUser(studentId);
        assertTrue(controller.isUserBanned(studentId));

        controller.unbanUser(studentId);
        assertFalse(controller.isUserBanned(studentId));
    }

    @Test
    void testSearchReservations() {
        List<ReservationMgmtModel> result = controller.searchReservations("김철수", null, null);
        assertEquals(1, result.size());
        assertEquals("김철수", result.get(0).getName());

        List<ReservationMgmtModel> none = controller.searchReservations(null, "999999", null);
        assertTrue(none.isEmpty());
    }

    @AfterAll
    static void cleanup() throws IOException {
        Files.deleteIfExists(tempReservationFile);
        Files.deleteIfExists(tempBanListFile);
    }
}
