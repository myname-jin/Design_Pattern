/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calendar;

/**
 *
 * @author adsd3
 */
 //다이얼로그 순차 흐름 제어 (블록→방종류→호수→시간대)

import java.time.LocalDate;
import java.util.List;

public class DialogController {
    private final ReservationServiceModel service;
    private final CalendarView view;

    public DialogController(ReservationServiceModel service, CalendarView view) {
        this.service = service;
        this.view = view;
    }

    public void handleDate(LocalDate date) {
        BlockTypeDialogView blockDlg = new BlockTypeDialogView();
        boolean isFull = service.isFullyBlocked(date.toString());
        blockDlg.setDisablePartialBlock(isFull);

        blockDlg.setHandler(new BlockTypeDialogView.Handler() {
            @Override public void onBlockFull() {
                service.blockAll(date.toString());
                view.refresh();
                blockDlg.close();
            }

            @Override public void onBlockPartial() {
                blockDlg.close();
                showRoomType(date);
            }

            @Override public void onUnblock() {
                service.unblock(date.toString());
                view.refresh();
                blockDlg.close();
            }

            @Override public void onCancel() {
                blockDlg.close();
            }
        });

        blockDlg.show();
    }

    private void showRoomType(LocalDate date) {
        RoomTypeDialogView roomTypeDialog = new RoomTypeDialogView();
        roomTypeDialog.setHandler(new RoomTypeDialogView.Handler() {
            @Override public void onSelectLecture() {
                roomTypeDialog.close();
                showRoomNumber(date, "강의실");
            }

            @Override public void onSelectLab() {
                roomTypeDialog.close();
                showRoomNumber(date, "실습실");
            }

            @Override public void onCancel() {
                roomTypeDialog.close();
            }
        });

        roomTypeDialog.show();
    }

    private void showRoomNumber(LocalDate date, String roomType) {
        RoomNumberDialogView roomNumberDialog = new RoomNumberDialogView();
        roomNumberDialog.setHandler(new RoomNumberDialogView.Handler() {
            @Override public void onSelect(String roomNumber) {
                roomNumberDialog.close();
                showTimeSlot(date, roomType, roomNumber);
            }

            @Override public void onCancel() {
                roomNumberDialog.close();
            }
        });

        roomNumberDialog.show(roomType);
    }

    private void showTimeSlot(LocalDate date, String roomType, String roomNumber) {
        List<String> blockedSlots = service.getBlockedTimeSlots(date.toString(), roomType, roomNumber);

        TimeSlotDialogView timeSlotDialog = new TimeSlotDialogView(blockedSlots);
        timeSlotDialog.setHandler(new TimeSlotDialogView.Handler() {
            @Override public void onSelect(String timeSlot) {
                service.blockPartial(date.toString(), roomType, roomNumber, timeSlot);
                view.refresh();
                timeSlotDialog.close();
            }

            @Override public void onCancel() {
                timeSlotDialog.close();
            }
        });

        timeSlotDialog.show();
    }
}