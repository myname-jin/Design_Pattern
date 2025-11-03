

package Reservation;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class ExcelLoader {
    private static final String FILE_PATH = "src/main/resources/available_rooms.xlsx";

    public static Map<String, List<String>> loadRoomAvailability() {
        Map<String, List<String>> roomAvailability = new HashMap<>();

        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = WorkbookFactory.create(fis)) {

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                String roomName = sheet.getSheetName();  // 시트 이름 = 강의실 번호

                List<String> availableTimes = new ArrayList<>();

                // A열 = 시간대 (0), B~F = 월~금 (1~5)
                for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    if (row == null) continue;

                    Cell timeCell = row.getCell(0); // A열 = 시간대
                    if (timeCell == null) continue;

                    String timeSlot = timeCell.getStringCellValue();

                    for (int day = 1; day <= 5; day++) {
                        Cell cell = row.getCell(day);
                        if (cell != null && "비어있음".equals(cell.getStringCellValue())) {
                            String dayStr = getDayOfWeek(day); // 월~금
                            availableTimes.add(dayStr + " " + timeSlot);
                        }
                    }
                }

                roomAvailability.put(roomName, availableTimes);
            }

        } catch (Exception e) {
            System.out.println("엑셀 강의실 정보 읽기 실패: " + e.getMessage());
        }

        return roomAvailability;
    }

    private static String getDayOfWeek(int columnIndex) {
        return switch (columnIndex) {
            case 1 -> "월";
            case 2 -> "화";
            case 3 -> "수";
            case 4 -> "목";
            case 5 -> "금";
            default -> "";
        };
    }
}

