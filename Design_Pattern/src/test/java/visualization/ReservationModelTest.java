/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package visualization;

/**
 *
 * @author adsd3
 */

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import visualization.ReservationModel;
import java.util.Set;

/**
 */
public class ReservationModelTest {
    
    @Test
    public void testDataLoadingAndAggregation() {
        System.out.println("============== [Unit Test] ReservationModel ==============");
        
        ReservationModel model = new ReservationModel();
        
        // TEST 1: 연도 로딩 확인
        Set<Integer> years = model.getYears();
        System.out.println(" [Info] 로딩된 연도: " + years);
        
        // 데이터 파일이 있어야 통과됨
        Assertions.assertFalse(years.isEmpty(), "데이터 파일 로딩에 성공하여 연도 데이터가 비어있지 않아야 합니다.");

        // TEST 2: 데이터 집계 확인 (로딩된 첫 해를 대상으로 집계 시도)
        int year = years.iterator().next();
        int total = model.getYearTotal(year);
        
        System.out.println(" [Info] " + year + "년 총 집계 건수: " + total + "건");
        
        Assertions.assertTrue(total > 0, "데이터 파일이 있다면 총 집계 건수가 0보다 커야 합니다.");

        System.out.println("==========================================================");
    }
}