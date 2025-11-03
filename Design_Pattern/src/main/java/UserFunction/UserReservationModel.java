/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserFunction;

/**
 *
 * @author jms5310
 */
public class UserReservationModel {
    private String studentId;   // 학번
    private String department;  // 학과
    private String name;        // 이름
    private String room;        // 강의실
    private String time;        // 시간 (예: "10:00~12:00")
    private String status;      // 승인상태 (승인, 대기, 거절)

    public UserReservationModel(String studentId, String department, String name,
                                String room, String time, String status) {
        this.studentId = studentId;
        this.department = department;
        this.name = name;
        this.room = room;
        this.time = time;
        this.status = status;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getDepartment() {
        return department;
    }

    public String getName() {
        return name;
    }

    public String getRoom() {
        return room;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return studentId + "," + department + "," + name + "," + room + "," + time + "," + status;
    }
}
