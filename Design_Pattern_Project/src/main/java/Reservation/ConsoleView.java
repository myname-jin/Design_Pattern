/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reservation;

//터미널 창 구현 파일 ReservationView.java 파일로 볼 것

import java.util.Scanner;

/**
 *
 * @author scq37
 */
public class ConsoleView { //입출력 클래스
     private Scanner scanner = new Scanner(System.in);
     
     public String inputName() {
        System.out.print("예약자 이름: ");
        return scanner.nextLine().trim();
    }

    public String inputUserType() {
        System.out.print("사용자 유형 (학생 / 교수): ");
        return scanner.nextLine().trim();
    }

    public String inputUserId() {
        System.out.print("학번 또는 교수번호: ");
        return scanner.nextLine().trim();
    }

    public String inputDepartment() {
        System.out.print("학과 이름: ");
        return scanner.nextLine().trim();
    }



    public String selectRoomType() {
    System.out.println("[강의실 예약 시스템]");
    System.out.println("1. 강의실");
    System.out.println("2. 실습실");
    System.out.print("> ");
    int input = scanner.nextInt();  //숫자 선택
    scanner.nextLine(); 
    return input == 1 ? "강의실" : "실습실";  // 반환
}

//필터링된 강의실 목록 출력
    public void showRooms(RoomModel[] rooms) {
        System.out.println("\n[강의실 목록]");
        for (RoomModel room : rooms) {
            System.out.println("- " + room.getName());
        }
        System.out.print("\n원하는 강의실을 입력하세요: ");
    }

    //예약 가능 시간대 출력 
    public void showAvailableTimes(String[] times) {
        System.out.println("\n예약 가능한 시간대:");
        for (String time : times) {
            System.out.println("- " + time);
        }
    }
    
    public String inputDate() {
        System.out.print("\n예약 날짜 : ");
        return scanner.nextLine().trim();
    }

    public String inputStartTime() {
        System.out.print("\n시작 시간 : ");
        return scanner.nextLine().trim();
    }

    public String inputEndTime() {
        System.out.print("종료 시간 : ");
        return scanner.nextLine().trim();
    }

    public String inputPurpose() {
        System.out.print("\n예약 목적: ");
        return scanner.nextLine().trim();
    }



    public String getInput() {
        return scanner.nextLine().trim();
    }
    
}


