package management;

import java.io.Serializable;

public class Reservation implements Serializable {
    private String userName;    // 이름
    private String department;  // 학과 (기존 기능 유지용)
    private String studentId;   // 학번 (기존 기능 유지용)
    private String roomName;    // 강의실
    private String date;        // 날짜
    private String time;        // 시간
    private String status;      // 승인 여부

    public Reservation(String userName, String department, String studentId, String roomName, String date, String time, String status) {
        this.userName = userName;
        this.department = department;
        this.studentId = studentId;
        this.roomName = roomName;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    // Getter
    public String getUserName() { return userName; }
    public String getDepartment() { return department; }
    public String getStudentId() { return studentId; }
    public String getRoomName() { return roomName; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getStatus() { return status; }

    // Setter
    public void setStatus(String status) { this.status = status; }

    // JTable 행 데이터로 변환 (기존 테이블 순서: 이름, 학과, 학번, 강의실, 날짜, 시간, 승인여부)
    public Object[] toArray() {
        return new Object[]{userName, department, studentId, roomName, date, time, status};
    }
}