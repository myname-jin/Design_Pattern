/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package calendar;

import calendar.DialogController;
import calendar.ReservationServiceModel;
import calendar.CalendarView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class DialogControllerTest {

    private DialogController dialogController;
    private ReservationServiceModel serviceMock;
    private CalendarView viewMock;

    @BeforeEach
    void setUp() {
        serviceMock = mock(ReservationServiceModel.class);
        viewMock = mock(CalendarView.class);

        dialogController = new DialogController(serviceMock, viewMock);
    }

    @Test
    void testControllerCreation() {
        assertNotNull(dialogController);
    }
}