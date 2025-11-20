package management;

import java.io.Serializable;

public class Reservation implements Serializable {
    // 필드 정의 (12개) - 순서는 파일 저장 순서와 동일하게 유지
    private String studentId;   // 1. 사용자ID (학번)
    private String userType;    // 2. 사용자구분
    private String userName;    // 3. 이름
    private String department;  // 4. 학과
    private String roomType;    // 5. 강의실타입
    private String roomName;    // 6. 호실
    private String date;        // 7. 날짜
    private String dayOfWeek;   // 8. 요일
    private String startTime;   // 9. 시작시간
    private String endTime;     // 10. 종료시간
    private String purpose;     // 11. 목적
    private String status;      // 12. 상태

    public Reservation(String studentId, String userType, String userName, String department, 
                       String roomType, String roomName, String date, String dayOfWeek, 
                       String startTime, String endTime, String purpose, String status) {
        this.studentId = studentId;
        this.userType = userType;
        this.userName = userName;
        this.department = department;
        this.roomType = roomType;
        this.roomName = roomName;
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.purpose = purpose;
        this.status = status;
    }

    // Getter
    public String getStudentId() { return studentId; }
    public String getUserType() { return userType; }
    public String getUserName() { return userName; }
    public String getDepartment() { return department; }
    public String getRoomType() { return roomType; }
    public String getRoomName() { return roomName; }
    public String getDate() { return date; }
    public String getDayOfWeek() { return dayOfWeek; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getPurpose() { return purpose; }
    public String getStatus() { return status; }

    // Setter
    public void setStatus(String status) { this.status = status; }

    // [수정됨] JTable 화면 컬럼 순서 변경
    // 변경 전: 이름, 학과, 학번...
    // 변경 후: [0:학번, 1:학과, 2:이름, 3:구분, 4:강의실, 5:날짜, 6:시간, 7:목적, 8:상태]
    public Object[] toArray() {
        return new Object[]{
            studentId,  // 0번: 학번 (맨 앞으로 이동)
            department, // 1번: 학과
            userName,   // 2번: 이름 (뒤로 이동)
            userType,   // 3번
            roomName,   // 4번
            date + "(" + dayOfWeek + ")", // 5번
            startTime + "~" + endTime,    // 6번
            purpose,    // 7번
            status      // 8번
        };
    }
}