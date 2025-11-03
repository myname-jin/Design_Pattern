/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package management;

import management.UserStatsModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author suk22
 */
public class UserStatsModelTest {

    @Test
    void testUserStatsModelCreationAndGetters() {
        String expectedName = "홍길동";
        String expectedUserId = "user01";
        int expectedReservationCount = 3;
        int expectedCancelCount = 2;
        String expectedCancelReason = "개인 사정; 중복 예약";

        UserStatsModel model = new UserStatsModel(
                expectedName,
                expectedUserId,
                expectedReservationCount,
                expectedCancelCount,
                expectedCancelReason
        );

        assertEquals(expectedName, model.getName());
        assertEquals(expectedUserId, model.getUserId());
        assertEquals(expectedReservationCount, model.getReservationCount());
        assertEquals(expectedCancelCount, model.getCancelCount());
        assertEquals(expectedCancelReason, model.getCancelReason());
    }
}
