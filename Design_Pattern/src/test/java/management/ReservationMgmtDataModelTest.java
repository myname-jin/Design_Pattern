package management;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ReservationMgmtDataModel 단위 테스트
 * (데이터 추가, 전체 조회, 검색 필터링 로직 검증)
 */
public class ReservationMgmtDataModelTest {
    
    private ReservationMgmtDataModel dataModel;

    @BeforeEach
    public void setUp() {
        dataModel = new ReservationMgmtDataModel();
        
        dataModel.addReservation(new ReservationMgmtModel("김철수", "20201111", "컴공", "911", "2025-10-01", "10:00", "승인"));
        dataModel.addReservation(new ReservationMgmtModel("이영희", "20202222", "전기", "912", "2025-10-02", "12:00", "대기"));
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * 데이터 추가 및 전체 조회 테스트
     */
    @Test
    public void testAddAndGetAllReservations() {
        System.out.println("add & getAll");
        
        List<ReservationMgmtModel> list = dataModel.getAllReservations();
        
        assertEquals(2, list.size()); // 2개가 잘 들어갔는지 확인
        assertEquals("김철수", list.get(0).getName());
        assertEquals("이영희", list.get(1).getName());
    }

    /**
     * 검색 기능 테스트 (이름, 학번, 강의실 복합 검색)
     */
    @Test
    public void testSearchReservations() {
        System.out.println("searchReservations");

        // 1. 이름으로 검색 ("철수")
        List<ReservationMgmtModel> resultByName = dataModel.searchReservations("철수", "", "");
        assertEquals(1, resultByName.size());
        assertEquals("김철수", resultByName.get(0).getName());

        // 2. 학번으로 검색 ("2222")
        List<ReservationMgmtModel> resultById = dataModel.searchReservations("", "2222", "");
        assertEquals(1, resultById.size());
        assertEquals("이영희", resultById.get(0).getName());

        // 3. 강의실로 검색 ("911")
        List<ReservationMgmtModel> resultByRoom = dataModel.searchReservations("", "", "911");
        assertEquals(1, resultByRoom.size());
        assertEquals("911", resultByRoom.get(0).getRoom());
        
        // 4. 없는 조건 검색
        List<ReservationMgmtModel> resultEmpty = dataModel.searchReservations("없는사람", "", "");
        assertEquals(0, resultEmpty.size());
    }
}