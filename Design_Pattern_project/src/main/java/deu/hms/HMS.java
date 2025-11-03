package deu.hms;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author yunhe
 */
public class HMS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // UserAuthentication 객체 생성 
        deu.hms.login.UserAuthentication auth = new deu.hms.login.UserAuthentication();
        // 로그인 창 호출
        java.awt.EventQueue.invokeLater(() -> {
            try {
                deu.hms.login.loginFrame loginframe = new deu.hms.login.loginFrame(auth);
                loginframe.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });//dddddddddddddddddddddd
    }
    }
