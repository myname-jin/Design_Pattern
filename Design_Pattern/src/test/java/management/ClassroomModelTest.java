package management;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ClassroomModel 클래스 단위 테스트
 * (강의실 데이터 객체의 생성, 수정, 변환 로직 검증)
 */
class ClassroomModelTest {

    private ClassroomModel instance; 
    
    @BeforeEach
    public void setUp() throws Exception {
        instance = new ClassroomModel("915", "프로젝트실(PC 30대)");
    }

    @AfterEach
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetRoom() {
        System.out.println("getRoom");
        // setUp에서 설정한 "915"가 나오는지 확인
        assertEquals("915", instance.getRoom());
    }

    @Test
    public void testSetRoom() {
        System.out.println("setRoom");
        // 값을 "920"으로 변경하고 잘 바뀌었는지 확인
        String newRoom = "920";
        instance.setRoom(newRoom);
        assertEquals(newRoom, instance.getRoom());
    }

    @Test
    public void testGetInfo() {
        System.out.println("getInfo");
        // setUp에서 설정한 정보가 나오는지 확인
        assertEquals("프로젝트실(PC 30대)", instance.getInfo());
    }

    @Test
    public void testSetInfo() {
        System.out.println("setInfo");
        // 정보를 변경하고 잘 바뀌었는지 확인
        String newInfo = "수리중";
        instance.setInfo(newInfo);
        assertEquals(newInfo, instance.getInfo());
    }

    /**
     * toFileString 메서드 테스트
     * (파일에 저장될 때 "강의실,정보" 형식인지 확인)
     */
    @Test
    void testToFileString() {
        System.out.println("toFileString");
        String expected = "915,프로젝트실(PC 30대)";
        assertEquals(expected, instance.toFileString());
    }

    /**
     * toArray 메서드 테스트
     * (테이블에 표시될 때 Object 배열로 잘 변환되는지 확인)
     */
    @Test
    public void testToArray() {
        System.out.println("toArray");
        Object[] expected = new Object[] {"915", "프로젝트실(PC 30대)"};
        assertArrayEquals(expected, instance.toArray());
    }
}