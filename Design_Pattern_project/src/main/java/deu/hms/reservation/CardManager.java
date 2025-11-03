/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.reservation;

/**
 *
 * @author adsd3
 */
import javax.swing.JOptionPane;

public class CardManager {
    public boolean isCardRegistered(boolean cardRegistButtonSelected) {
        return cardRegistButtonSelected;
    }

    public boolean validateCard(String cardNum1, String cardNum2, String cardNum3, String cardNum4, 
                                String month, String year, String pw, String cvc) {
        // 유효성 검사 로직
        if (cardNum1.isEmpty() || cardNum2.isEmpty() || cardNum3.isEmpty() || cardNum4.isEmpty()
                || month.isEmpty() || year.isEmpty() || pw.isEmpty() || cvc.isEmpty()) {
            return false;
        }
        return cardNum1.matches("\\d{4}") && cardNum2.matches("\\d{4}")
                && cardNum3.matches("\\d{4}") && cardNum4.matches("\\d{4}")
                && month.matches("\\d{2}") && year.matches("\\d{2}")
                && Integer.parseInt(month) >= 1 && Integer.parseInt(month) <= 12
                && pw.matches("\\d{2}") && cvc.matches("\\d{3}");
    }

    public void saveCardData(String fullCardNumber, String expirationDate, String pw, String cvc) throws Exception {
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter("card_data.txt", true))) {
            writer.write("카드 번호: " + fullCardNumber + ", 유효기간: " + expirationDate
                    + ", 비밀번호: " + pw + ", CVC: " + cvc);
            writer.newLine();
        }
    }
}