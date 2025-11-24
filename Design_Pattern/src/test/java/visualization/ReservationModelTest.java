/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package visualization;

/**
 *
 * @author adsd3
 */
import visualization.ReservationModel;
import java.util.Set;

public class ReservationModelTest {
    public static void main(String[] args) {
        System.out.println("============== [Unit Test] ReservationModel ==============");
        
        ReservationModel model = new ReservationModel();
        
        // TEST 1: 연도 로딩 확인
        Set<Integer> years = model.getYears();
        if (!years.isEmpty()) {
            System.out.println(" [Pass] 연도 데이터 로딩 성공: " + years);
        } else {
            System.out.println(" [Fail] 데이터가 비어있습니다. (파일 경로 확인 필요)");
        }

        // TEST 2: 데이터 집계 확인
        if (!years.isEmpty()) {
            int year = years.iterator().next();
            int total = model.getYearTotal(year);
            if (total > 0) {
                System.out.println(" [Pass] " + year + "년 데이터 집계 성공 (" + total + "건)");
            } else {
                System.out.println(" [Fail] 집계된 데이터가 0건입니다.");
            }
        }
        System.out.println("==========================================================");
    }
}
