/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package visualization;

import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import management.ReservationMgmtView;

public class ReservationController extends MouseAdapter implements ActionListener {
    private final ReservationModel model;
    private MainView view = null;

    private final Map<Integer, String> roomIndexMap = new HashMap<>();
    private final Map<Integer, Integer> xPositionMap = new HashMap<>();

    public ReservationController(ReservationModel model) {
        this.model = model;
    }

    // MainView에서 연결
    public void setView(MainView view) {
        this.view = view;
        view.getBackButton().addActionListener(this);
    }

    public void setClickArea(int index, int x, String room) {
        xPositionMap.put(index, x);
        roomIndexMap.put(index, room);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int mx = e.getX();
        for (int i : xPositionMap.keySet()) {
            int x = xPositionMap.get(i);
            if (mx >= x && mx <= x + 50) {
                String room = roomIndexMap.get(i);
                new DetailView(room, model.getRoomByDay(room));
                break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    if (view != null && e.getSource() == view.getBackButton()) {

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        // main()에서 하던 그대로 실행 → 메인처럼 작동
        SwingUtilities.invokeLater(() -> {
            ReservationMgmtView mgmtView = new ReservationMgmtView();
            mgmtView.setLocationRelativeTo(null);
            mgmtView.setVisible(true);
        });

        view.dispose(); // 현재 MainView 닫기
    }
}
}