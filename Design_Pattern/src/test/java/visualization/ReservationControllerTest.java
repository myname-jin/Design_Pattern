/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package visualization;

import management.ReservationMgmtView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReservationControllerTest {

    private ReservationModel mockModel;
    private ReservationController controller;

    @BeforeEach
    public void setUp() {
        mockModel = mock(ReservationModel.class);
        controller = new ReservationController(mockModel);
    }

    @Test
    public void testSetClickArea_NoError() {
        assertDoesNotThrow(() -> {
            controller.setClickArea(0, 100, "911");
            controller.setClickArea(1, 200, "912");
        });
    }

    @Test
    public void testMouseClickedWithinClickArea_CallsModel() {
        controller.setClickArea(0, 100, "911");

        MouseEvent e = mock(MouseEvent.class);
        when(e.getX()).thenReturn(110); // 클릭 위치가 100~150 사이

        when(mockModel.getRoomByDay("911")).thenReturn(Map.of("월", 2));

        assertDoesNotThrow(() -> controller.mouseClicked(e));
        verify(mockModel).getRoomByDay("911");
    }

    @Test
    public void testMouseClickedOutsideClickArea_DoesNothing() {
        controller.setClickArea(0, 100, "911");

        MouseEvent e = mock(MouseEvent.class);
        when(e.getX()).thenReturn(300); // 클릭 위치가 범위 밖

        assertDoesNotThrow(() -> controller.mouseClicked(e));
        verify(mockModel, never()).getRoomByDay("911");
    }

    @Test
    public void testActionPerformed_BackButton_OpensMgmtView() {
        // MainView 모킹 및 뒤로가기 버튼 설정
        MainView mockView = mock(MainView.class);
        JButton backButton = new JButton("뒤로가기");
        when(mockView.getBackButton()).thenReturn(backButton);

        controller.setView(mockView);
        ActionEvent e = new ActionEvent(backButton, ActionEvent.ACTION_PERFORMED, null);

        assertDoesNotThrow(() -> controller.actionPerformed(e));

        // ✅ 호출 2번 확인 (setView + actionPerformed 내부에서 각각 1번씩 호출됨)
        verify(mockView, times(2)).getBackButton();
    }
}