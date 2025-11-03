/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.utility;

import javax.swing.JOptionPane;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Jimin
 */
public class CardRegistService {

    // 카드 정보를 파일에 저장하는 메서드
    public void saveCardInformation(CardDetails cardDetails) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("card_data.txt", true))) {
            writer.write("카드 번호: " + cardDetails.getCardNumber() + ", 유효기간: " + cardDetails.getExpirationDate()
                    + ", 비밀번호: " + cardDetails.getPassword() + ", CVC: " + cardDetails.getCVC());
            writer.newLine();
            JOptionPane.showMessageDialog(null, "카드 정보가 성공적으로 저장되었습니다!", "성공", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "저장 중 오류가 발생했습니다!", "오류", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
