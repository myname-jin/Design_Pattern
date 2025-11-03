    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package deu.hms.checkin;

import deu.hms.checkin.CheckInData;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Jimin
 */

/**
 * CheckInManager 클래스는 체크인 관련 데이터를 처리하는 기능을 제공합니다.
 * - 예약 데이터를 불러와 필요한 데이터를 필터링하고 반환
 * - 체크인 데이터를 파일에 저장
 */
public class CheckInManager {

    // 예약 데이터가 저장된 파일의 경로
    private String reservationFilePath = "Reservation.txt";

    // 체크인 데이터가 저장될 파일의 경로
    private String checkInFilePath = "CheckInData.txt";

    /**
     * 예약 데이터를 모두 가져오는 메서드
     * Reservation.txt 파일에서 데이터를 읽어와 CheckInData 객체 리스트로 반환합니다.
     * 필요한 데이터만 필터링해서 반환합니다.
     * 
     * @return List<CheckInData> 필터링된 체크인 데이터 리스트
     */
    public List<CheckInData> getCheckInDataList() {
        List<CheckInData> checkInDataList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(reservationFilePath))) {
            String line;
            // 파일의 각 줄을 읽어서 처리
            while ((line = reader.readLine()) != null) {
                // 예약 데이터를 파싱하여 CheckInData 객체로 변환
                CheckInData checkInData = parseCheckInData(line);
                // 변환된 데이터가 유효하면 리스트에 추가
                if (checkInData != null) {
                    checkInDataList.add(checkInData);
                }
            }
        } catch (IOException e) {
            // 파일 읽기 중 오류 발생 시 콘솔에 출력
            System.err.println("파일 읽기 오류: " + e.getMessage());
        }
        return checkInDataList; // 읽어온 데이터 리스트 반환
    }

    /**
     * Reservation.txt 파일의 한 줄을 읽어 CheckInData 객체로 변환하는 메서드
     * 각 필드를 ','로 구분하여 파싱한 후 필요한 데이터만 객체로 생성합니다.
     * 
     * @param csvLine CSV 형식의 예약 데이터 한 줄
     * @return CheckInData 변환된 체크인 데이터 객체
     */
    private CheckInData parseCheckInData(String csvLine) {
        // ','로 데이터를 분리
        String[] fields = csvLine.split(",");
        // 데이터 필드의 개수가 정확해야 처리 가능
        if (fields.length == 11) { // 예상 필드: 11개
            String uniqueNumber = fields[0].trim(); // 고유번호
            String name = fields[1].trim(); // 이름
            String phoneNumber = fields[3].trim(); // 전화번호
            String checkInDate = fields[4].trim(); // 체크인 날짜
            String checkOutDate = fields[5].trim(); // 체크아웃 날짜
            String roomNumber = fields[6].trim(); // 방 번호
            String guestCount = fields[7].trim(); // 인원 수
            String stayCost = fields[8].trim(); // 숙박 금액
            String paymentMethod = fields[9].trim(); // 결제 방법
            String status = fields[10].trim(); // 예약 상태

            // 요청 사항 필드: 필요 시 사용
            String requestDetails = ""; // 기본값으로 빈 문자열 설정

            // 파싱된 데이터를 기반으로 CheckInData 객체 생성
            return new CheckInData(uniqueNumber, name, phoneNumber, checkInDate, checkOutDate, roomNumber, guestCount, stayCost, paymentMethod, status, requestDetails);
        }
        // 잘못된 데이터는 null 반환
        return null;
    }

    /**
     * 체크인 데이터를 요청 사항과 함께 파일에 저장하는 메서드
     * CheckInData 객체와 요청 사항을 포함하여 CheckInData.txt 파일에 추가로 저장합니다.
     * 
     * @param checkInData CheckInData 객체
     * @param requestDetails 고객이 요청한 추가 사항
     */
    public void saveCheckInDataWithRequest(CheckInData checkInData, String requestDetails) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(checkInFilePath, true))) {
            // CheckInData 객체를 CSV 형식으로 변환
            String fullCsvData = checkInData.toCSV();
            // 요청 사항 포함: 필요 시 fullCsvData에 추가 작업 가능
            writer.write(fullCsvData);
            writer.newLine(); // 줄바꿈 추가
        } catch (IOException e) {
            // 파일 쓰기 오류 발생 시 콘솔에 출력
            System.err.println("체크인 데이터와 요청 사항 쓰기 오류: " + e.getMessage());
        }
    }
}
