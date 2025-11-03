package deu.hms.roomservice;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class TableManager {
    // 멤버 변수 선언
    private MenuManager menuManager;
    private DefaultTableModel sourceModel;
    private DefaultTableModel targetModel;
    private int rowCount;
    private int columnCount;
    
    // 생성자
    public TableManager() {
        this.menuManager = new MenuManager();
    }
    
    // Getter/Setter 메소드
    public DefaultTableModel getSourceModel() {
        return sourceModel;
    }
    
    public void setSourceModel(DefaultTableModel model) {
        this.sourceModel = model;
    }
    
    public DefaultTableModel getTargetModel() {
        return targetModel;
    }
    
    public void setTargetModel(DefaultTableModel model) {
        this.targetModel = model;
    }
    
    public int getRowCount() {
        return rowCount;
    }
    
    public void setRowCount(int count) {
        this.rowCount = count;
    }
    
    public int getColumnCount() {
        return columnCount;
    }
    
    public void setColumnCount(int count) {
        this.columnCount = count;
    }
    
    // 테이블 데이터 복사 메소드
    public void copyTableData(DefaultTableModel sourceModel, DefaultTableModel targetModel) {
        if (isValidModels(sourceModel, targetModel)) {
            try {
                this.sourceModel = sourceModel;
                this.targetModel = targetModel;
                copyData();
                clearSourceTable();
            } catch (Exception e) {
                showError("테이블 데이터 복사 중 오류가 발생했습니다: " + e.getMessage());
            }
        }
    }
    
    // 테이블 초기화 메소드
    public void reset(DefaultTableModel model, JLabel total) {
        if (model != null) {
            try {
                clearTable(model);
                updateTotal(model, total);
            } catch (Exception e) {
                showError("테이블 초기화 중 오류가 발생했습니다: " + e.getMessage());
            }
        } else {
            showError("테이블 모델이 없습니다.");
        }
    }
    
    // 내부 헬퍼 메소드들
    private boolean isValidModels(DefaultTableModel source, DefaultTableModel target) {
        if (source == null || target == null) {
            showError("유효하지 않은 테이블 모델입니다.");
            return false;
        }
        return true;
    }
    
    private void copyData() {
        rowCount = sourceModel.getRowCount();
        columnCount = sourceModel.getColumnCount();
        
        for (int i = 0; i < rowCount; i++) {
            Object[] rowData = new Object[columnCount];
            for (int j = 0; j < columnCount; j++) {
                rowData[j] = sourceModel.getValueAt(i, j);
            }
            targetModel.addRow(rowData);
        }
    }
    
    private void clearSourceTable() {
        sourceModel.setRowCount(0);
    }
    
    private void clearTable(DefaultTableModel model) {
        model.setRowCount(0);
    }
    
    private void updateTotal(DefaultTableModel model, JLabel total) {
        if (model != null && total != null) {
            menuManager.updateTotal(model, total);
        } else {
            showError("테이블 모델 또는 라벨이 없습니다.");
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(null, 
            message, 
            "오류", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    // 테이블 행 삭제 메소드
    public void deleteRow(DefaultTableModel model, int row) {
        if (isValidRow(model, row)) {
            try {
                model.removeRow(row);
            } catch (Exception e) {
                showError("행 삭제 중 오류가 발생했습니다: " + e.getMessage());
            }
        }
    }
    
    private boolean isValidRow(DefaultTableModel model, int row) {
        if (model == null) {
            showError("테이블 모델이 없습니다.");
            return false;
        }
        if (row < 0 || row >= model.getRowCount()) {
            showError("유효하지 않은 행 번호입니다.");
            return false;
        }
        return true;
    }
    
    // 테이블 데이터 검증 메소드
    public boolean validateTableData(DefaultTableModel model) {
        if (model == null) {
            showError("테이블 모델이 없습니다.");
            return false;
        }
        
        if (model.getRowCount() == 0) {
            showError("테이블에 데이터가 없습니다.");
            return false;
        }
        
        return true;
    }
}