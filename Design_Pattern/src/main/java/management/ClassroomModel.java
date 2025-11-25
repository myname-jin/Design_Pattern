package management;

public class ClassroomModel {

    private String room; // 강의실 번호 (Key)
    private String info; // 강의실 정보 (비고)

    public ClassroomModel(String room, String info) {
        this.room = room;
        this.info = info;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    // 파일 저장 포맷 (강의실,정보)
    public String toFileString() {
        return room + "," + info;
    }
    
    // 테이블 표시용
    public Object[] toArray() {
        return new Object[]{room, info};
    }
}