/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reservation;

import java.util.List;

public class ReservationInfo {
    private final String userId;
    private final String userName;
    private final String userDept;
    private final String roomName;
    private final List<String> times;
    private final String date;
    private final String purpose;

    private ReservationInfo(Builder builder) {
        this.userId = builder.userId;
        this.userName = builder.userName;
        this.userDept = builder.userDept;
        this.roomName = builder.roomName;
        this.times = builder.times;
        this.date = builder.date;
        this.purpose = builder.purpose;
    }

    // Getter
    public String getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getUserDept() { return userDept; }
    public String getRoomName() { return roomName; }
    public List<String> getTimes() { return times; }
    public String getDate() { return date; }
    public String getPurpose() { return purpose; }

    // Builder
    public static class Builder {
        private String userId;
        private String userName;
        private String userDept;
        private String roomName;
        private List<String> times;
        private String date;
        private String purpose;

        public Builder setUserInfo(String id, String name, String dept) {
            this.userId = id;
            this.userName = name;
            this.userDept = dept;
            return this;
        }

        public Builder setRoomInfo(String roomName) {
            this.roomName = roomName;
            return this;
        }

        public Builder setDateAndTimes(String date, List<String> times) {
            this.date = date;
            this.times = times;
            return this;
        }

        public Builder setPurpose(String purpose) {
            this.purpose = purpose;
            return this;
        }

        public ReservationInfo build() {
            if (userId == null || userName == null || userDept == null ||
                roomName == null || date == null || times == null || times.isEmpty() ||
                purpose == null) {
                throw new IllegalStateException("모든 예약 정보를 올바르게 입력해야 합니다.");
            }
            // 하루 전, 2시간 이하 제한 체크
            if (!validateDateAndDuration(date, times)) {
                throw new IllegalStateException("예약은 최소 하루 전, 2시간 이하만 가능합니다.");
            }
            return new ReservationInfo(this);
        }

        private boolean validateDateAndDuration(String dateStr, List<String> times) {
            try {
                /*java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                java.util.Date reservationDate = sdf.parse(dateStr);
                java.util.Date now = new java.util.Date();
                long diff = reservationDate.getTime() - now.getTime();
                if (diff < 24 * 60 * 60 * 1000L) return false; // 최소 하루 전 체크 */
                
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);

                java.util.Date reservationDate = sdf.parse(dateStr);

                // 오늘 날짜의 00:00으로 맞추기
                java.util.Calendar calToday = java.util.Calendar.getInstance();
                calToday.set(java.util.Calendar.HOUR_OF_DAY, 0);
                calToday.set(java.util.Calendar.MINUTE, 0);
                calToday.set(java.util.Calendar.SECOND, 0);
                calToday.set(java.util.Calendar.MILLISECOND, 0);

                // 예약 날짜도 00:00 기준으로 세팅
                java.util.Calendar calReserve = java.util.Calendar.getInstance();
                calReserve.setTime(reservationDate);
                calReserve.set(java.util.Calendar.HOUR_OF_DAY, 0);
                calReserve.set(java.util.Calendar.MINUTE, 0);
                calReserve.set(java.util.Calendar.SECOND, 0);
                calReserve.set(java.util.Calendar.MILLISECOND, 0);

                // 오늘과 같으면(=당일) false
                if (calReserve.getTimeInMillis() == calToday.getTimeInMillis()) {
                    return false;
                }

                // 총 예약 시간 계산
                int totalMinutes = 0;
                java.text.SimpleDateFormat timeFormat = new java.text.SimpleDateFormat("HH:mm");
                for (String time : times) {
                    String[] split = time.split("~");
                    if (split.length == 3) {
                        java.util.Date start = timeFormat.parse(split[0].trim());
                        java.util.Date end = timeFormat.parse(split[1].trim());
                        totalMinutes += (end.getTime() - start.getTime()) / (1000 * 60);
                    }
                }
                return totalMinutes <= 120;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
