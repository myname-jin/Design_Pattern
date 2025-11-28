package management;

import java.io.Serializable;

public class Reservation implements Serializable {
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

    public void setStatus(String status) { this.status = status; }

    public Object[] toArray() {
        
        // 예약 리스트 화면 표시용 목적 다듬기
        // "팀 회의 (동반: 2022...)"  ->  "팀 회의" 만 남기기
        String displayPurpose = purpose;
        if (purpose != null && purpose.contains(" (동반:")) {
            // " (동반:" 이라는 글자가 시작되는 위치 앞까지만 자름
            displayPurpose = purpose.substring(0, purpose.indexOf(" (동반:"));
        }
        
        return new Object[]{
            studentId,  
            department, 
            userName,   
            userType,   
            roomName,   
            date + "(" + dayOfWeek + ")", 
            startTime + "~" + endTime,   
            displayPurpose,    
            status      
        };
    }
}