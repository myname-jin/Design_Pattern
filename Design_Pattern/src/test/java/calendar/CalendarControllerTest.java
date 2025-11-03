/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package calendar;

import calendar.CalendarController;
import calendar.ReservationServiceModel;
import calendar.ReservationRepositoryModel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class CalendarControllerTest {

    private CalendarController calendarController;
    private ReservationServiceModel serviceMock;

    @BeforeEach
    void setUp() {
        // ReservationRepositoryModel은 Service에 필요한 의존성이므로 가짜로 만들어줍니다.
        ReservationRepositoryModel repoMock = mock(ReservationRepositoryModel.class);
        serviceMock = new ReservationServiceModel(repoMock);

        calendarController = new CalendarController(serviceMock);
    }

    @Test
    void testControllerCreation() {
        assertNotNull(calendarController);
    }
}