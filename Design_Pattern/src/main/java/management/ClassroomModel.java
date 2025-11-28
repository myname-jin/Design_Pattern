package management;

public class ClassroomModel {

    private String room; 
    private String info; 

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

    public String toFileString() {
        return room + "," + info;
    }
    
    public Object[] toArray() {
        return new Object[]{room, info};
    }
}