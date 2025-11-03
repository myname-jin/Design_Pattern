package deu.hms.roomservice;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class MenuManager {
    // 멤버 변수 선언
    private DefaultTableModel menuModel;
    private DefaultTableModel orderModel;
    private String menuName;
    private int price;
    private int quantity;
    private int selectedRow;
    private int totalAmount;
    
    // 생성자
    public MenuManager() {
        this.totalAmount = 0;
    }
    
    // Getter/Setter 메소드
    public DefaultTableModel getMenuModel() {
        return menuModel;
    }
    
    public void setMenuModel(DefaultTableModel model) {
        this.menuModel = model;
    }
    
    public DefaultTableModel getOrderModel() {
        return orderModel;
    }
    
    public void setOrderModel(DefaultTableModel model) {
        this.orderModel = model;
    }
    
    public int getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(int amount) {
        this.totalAmount = amount;
    }
    
    public String getMenuName() {
        return menuName;
    }
    
    public void setMenuName(String name) {
        this.menuName = name;
    }
    
    public int getPrice() {
        return price;
    }
    
    public void setPrice(int price) {
        this.price = price;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    // 메뉴를 주문 목록에 추가하는 메소드
    public void addMenuToOrder(JTable menuTable, JTable orderTable, JLabel total) {
        initializeModels(menuTable, orderTable);
        selectedRow = menuTable.getSelectedRow();
        
        if (isValidSelection()) {
            getMenuDetails();
            processOrder();
            updateTotal(orderModel, total);
        } else {
            showSelectionError();
        }
    }
    
    // 총액 업데이트 메소드
    public void updateTotal(DefaultTableModel model, JLabel total) {
        if (model != null) {
            calculateTotal(model);
            displayTotal(total);
        } else {
            showError("테이블 모델이 없습니다.");
        }
    }
    
    // 내부 헬퍼 메소드들
    private void initializeModels(JTable menuTable, JTable orderTable) {
        this.menuModel = (DefaultTableModel) menuTable.getModel();
        this.orderModel = (DefaultTableModel) orderTable.getModel();
    }
    
    private boolean isValidSelection() {
        return selectedRow != -1;
    }
    
    private void getMenuDetails() {
        menuName = menuModel.getValueAt(selectedRow, 0).toString();
        price = Integer.parseInt(menuModel.getValueAt(selectedRow, 1).toString());
    }
    
    private void processOrder() {
        if (isMenuExistsInOrder()) {
            updateExistingOrder();
        } else {
            addNewOrder();
        }
    }
    
    private boolean isMenuExistsInOrder() {
        for (int i = 0; i < orderModel.getRowCount(); i++) {
            if (orderModel.getValueAt(i, 0).equals(menuName)) {
                quantity = Integer.parseInt(orderModel.getValueAt(i, 1).toString());
                return true;
            }
        }
        return false;
    }
    
    private void updateExistingOrder() {
        for (int i = 0; i < orderModel.getRowCount(); i++) {
            if (orderModel.getValueAt(i, 0).equals(menuName)) {
                quantity++;
                int newPrice = price * quantity;
                orderModel.setValueAt(quantity, i, 1);
                orderModel.setValueAt(newPrice, i, 2);
                break;
            }
        }
    }
    
    private void addNewOrder() {
        orderModel.addRow(new Object[]{menuName, 1, price});
    }
    
    private void calculateTotal(DefaultTableModel model) {
        totalAmount = 0;
        try {
            for (int i = 0; i < model.getRowCount(); i++) {
                Object value = model.getValueAt(i, 2);
                if (value != null) {
                    totalAmount += Integer.parseInt(value.toString());
                }
            }
        } catch (NumberFormatException e) {
            showError("가격 계산 중 오류가 발생했습니다.");
        }
    }
    
    private void displayTotal(JLabel total) {
        if (total != null) {
            total.setText(String.valueOf(totalAmount) + "원");
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(null, 
            message, 
            "오류", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSelectionError() {
        JOptionPane.showMessageDialog(null, 
            "메뉴를 선택해주세요.", 
            "선택 오류", 
            JOptionPane.WARNING_MESSAGE);
    }
    
    // 주문 초기화 메소드
    public void resetOrder(DefaultTableModel model, JLabel total) {
        if (model != null) {
            model.setRowCount(0);
            totalAmount = 0;
            updateTotal(model, total);
        } else {
            showError("테이블 모델이 없습니다.");
        }
    }
    
    // 메뉴 삭제 메소드
    public void deleteMenuItem(JTable orderTable, JLabel total) {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) orderTable.getModel();
            model.removeRow(selectedRow);
            updateTotal(model, total);
        } else {
            showError("삭제할 메뉴를 선택해주세요.");
        }
    }
}