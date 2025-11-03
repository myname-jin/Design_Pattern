package deu.hms.restaurantManagement;


import deu.hms.login.MainScreenEmployees;
import deu.hms.login.MainScreenManager;
import deu.hms.login.UserAuthentication;
import javax.swing.table.DefaultTableModel;


public class restaurantManagementFrame extends javax.swing.JDialog {
    public restaurantManagementFrame(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        loadMenuFromFile((DefaultTableModel) ListTable.getModel());
    }

    
    private void addMenuToTable(DefaultTableModel model) {
    try {
        String type = (String) Type.getSelectedItem(); // JComboBox에서 선택된 항목 가져오기
        String menuName = MenuName.getText();
        int price = Integer.parseInt(Price.getText());
        
        model.addRow(new Object[]{type, menuName, price}); // 선택된 항목 사용
        
        // 입력 필드 초기화 
        Type.setSelectedIndex(0); // JComboBox 초기화
        MenuName.setText("");
        Price.setText("");
        
        // addmenu 창 닫기
        addmenu.dispose();
        
    } catch (NumberFormatException e) {
        javax.swing.JOptionPane.showMessageDialog(this, 
            "가격은 숫자로 입력해주세요",
            "입력 오류",
            javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}
    
    private void deleteSelectedMenu(DefaultTableModel model) {
    
    int selectedRow = ListTable.getSelectedRow();
    
    if (selectedRow != -1) {
        model.removeRow(selectedRow);
    } else {
        javax.swing.JOptionPane.showMessageDialog(this,
            "삭제할 메뉴를 선택해주세요",
            "선택 오류",
            javax.swing.JOptionPane.WARNING_MESSAGE);
    }
}
    
    

    private void saveMenuToFile(DefaultTableModel model) {
    try {
        java.io.FileWriter fw = new java.io.FileWriter("메뉴목록.txt", false);
        java.io.BufferedWriter bw = new java.io.BufferedWriter(fw);
        
        java.io.FileWriter roomServiceFw = new java.io.FileWriter("룸서비스메뉴.txt", false);
        java.io.BufferedWriter roomServiceBw = new java.io.BufferedWriter(roomServiceFw);
        
        java.io.FileWriter restaurantFw = new java.io.FileWriter("레스토랑메뉴.txt", false);
        java.io.BufferedWriter restaurantBw = new java.io.BufferedWriter(restaurantFw);
        
        for (int i = 0; i < model.getRowCount(); i++) {
            StringBuilder line = new StringBuilder();
            for (int j = 0; j < model.getColumnCount(); j++) {
                line.append(model.getValueAt(i, j).toString());
                if (j < model.getColumnCount() - 1) {
                    line.append(",");
                }
            }
            bw.write(line.toString());
            bw.newLine();
            
            // "룸서비스"인 경우 별도로 저장
            if ("룸서비스".equals(model.getValueAt(i, 0).toString())) {
                roomServiceBw.write(line.toString());
                roomServiceBw.newLine();
            }
            
            // "레스토랑"인 경우 별도로 저장
            if ("레스토랑".equals(model.getValueAt(i, 0).toString())) {
                restaurantBw.write(line.toString());
                restaurantBw.newLine();
            }
        }
        
        bw.close();
        fw.close();
        roomServiceBw.close();
        roomServiceFw.close();
        restaurantBw.close();
        restaurantFw.close();
        
    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(null, "파일 저장 중 오류가 발생했습니다: " + e.getMessage(),
            "저장 오류", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}
    private void loadMenuFromFile(DefaultTableModel model) {
    try {
        
        java.io.FileReader fr = new java.io.FileReader("메뉴목록.txt");
        java.io.BufferedReader br = new java.io.BufferedReader(fr);
        
        model.setRowCount(0); // 기존 데이터 초기화
        
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            model.addRow(data);
        }
        
        br.close();
        fr.close();
            
    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(null, 
            "파일을 불러오는 중 오류가 발생했습니다: " + e.getMessage(),
            "불러오기 오류", 
            javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}
    private void changeMenuPrice(DefaultTableModel model) {
    try {
        int selectedRow = ListTable.getSelectedRow();
        int newPrice = Integer.parseInt(Price1.getText());
        String newType = (String) Type1.getSelectedItem(); // JComboBox에서 선택된 항목 가져오기
        
        model.setValueAt(newType, selectedRow, 0); // 서비스 유형 수정
        model.setValueAt(newPrice, selectedRow, 2); // 가격 수정
        
        // 입력 필드 초기화
        Price1.setText("");
        Type1.setSelectedIndex(0); // JComboBox 초기화
        
        // Changemenu 창 닫기
        Changemenu.dispose();
        
        saveMenuToFile(model);
        
    } catch (NumberFormatException e) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "가격은 숫자로 입력해주세요",
            "입력 오류", 
            javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addmenu = new javax.swing.JFrame();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        MenuName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        Price = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        label = new javax.swing.JLabel();
        Type = new javax.swing.JComboBox<>();
        Changemenu = new javax.swing.JFrame();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        MenuName1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        Price1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        Type1 = new javax.swing.JComboBox<>();
        restaurantManagement = new javax.swing.JLabel();
        jScrollPane = new javax.swing.JScrollPane();
        ListTable = new javax.swing.JTable();
        deleteButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();
        ChangeButton = new javax.swing.JButton();

        jLabel3.setFont(new java.awt.Font("맑은 고딕", 1, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("메뉴 추가");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
        );

        jLabel1.setFont(new java.awt.Font("맑은 고딕", 1, 12)); // NOI18N
        jLabel1.setText("메뉴:");

        MenuName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuNameActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("맑은 고딕", 1, 12)); // NOI18N
        jLabel2.setText("가격:");

        Price.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PriceActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("맑은 고딕", 1, 12)); // NOI18N
        jButton1.setText("추가");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        label.setFont(new java.awt.Font("맑은 고딕", 1, 12)); // NOI18N
        label.setText("서비스 유형:");

        Type.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "룸서비스", "레스토랑" }));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(jButton1))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(MenuName)
                            .addComponent(Price)
                            .addComponent(Type, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label)
                    .addComponent(Type, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(MenuName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(Price, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout addmenuLayout = new javax.swing.GroupLayout(addmenu.getContentPane());
        addmenu.getContentPane().setLayout(addmenuLayout);
        addmenuLayout.setHorizontalGroup(
            addmenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        addmenuLayout.setVerticalGroup(
            addmenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jLabel4.setFont(new java.awt.Font("맑은 고딕", 1, 24)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("메뉴 수정");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
        );

        jLabel5.setFont(new java.awt.Font("맑은 고딕", 1, 12)); // NOI18N
        jLabel5.setText("메뉴:");

        MenuName1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuName1ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("맑은 고딕", 1, 12)); // NOI18N
        jLabel6.setText("가격:");

        Price1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Price1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("맑은 고딕", 1, 12)); // NOI18N
        jButton2.setText("수정");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("맑은 고딕", 1, 12)); // NOI18N
        jLabel7.setText("서비스 유형:");
        jLabel7.setToolTipText("");

        Type1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "룸서비스", "레스토랑" }));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(MenuName1)
                            .addComponent(Price1)
                            .addComponent(Type1, 0, 91, Short.MAX_VALUE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(jButton2)))
                .addGap(28, 28, 28))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(Type1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(MenuName1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(Price1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout ChangemenuLayout = new javax.swing.GroupLayout(Changemenu.getContentPane());
        Changemenu.getContentPane().setLayout(ChangemenuLayout);
        ChangemenuLayout.setHorizontalGroup(
            ChangemenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        ChangemenuLayout.setVerticalGroup(
            ChangemenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(false);

        restaurantManagement.setFont(new java.awt.Font("맑은 고딕", 1, 24)); // NOI18N
        restaurantManagement.setText("식당 관리");

        ListTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "서비스 유형", "음식 이름", "음식 가격"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ListTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ListTableMouseClicked(evt);
            }
        });
        jScrollPane.setViewportView(ListTable);

        deleteButton.setText("삭제");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        addButton.setText("추가");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        backButton.setText("<");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        ChangeButton.setText("수정");
        ChangeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChangeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(backButton)
                        .addGap(142, 142, 142)
                        .addComponent(restaurantManagement))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(addButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ChangeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(146, 146, 146)
                                .addComponent(deleteButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(39, 39, 39))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(backButton)
                        .addGap(304, 304, 304))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(restaurantManagement)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(ChangeButton)
                            .addComponent(addButton)
                            .addComponent(deleteButton))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void ListTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ListTableMouseClicked

    }//GEN-LAST:event_ListTableMouseClicked

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
    //메뉴 삭제 버튼
    deleteSelectedMenu((DefaultTableModel) ListTable.getModel());
    
    saveMenuToFile((DefaultTableModel) ListTable.getModel());
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
    addmenu.setSize(230, 300);
        addmenu.setLocationRelativeTo(null);
        addmenu.setVisible(true);
        
        
    }//GEN-LAST:event_addButtonActionPerformed

    private void MenuNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MenuNameActionPerformed

    private void PriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PriceActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // 테이블에 메뉴 추가
       addMenuToTable((DefaultTableModel) ListTable.getModel());
       saveMenuToFile((DefaultTableModel) ListTable.getModel());
     
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void MenuName1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuName1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MenuName1ActionPerformed

    private void Price1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Price1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Price1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // 수정 버튼
        changeMenuPrice((DefaultTableModel) ListTable.getModel());      
    }//GEN-LAST:event_jButton2ActionPerformed

    private void ChangeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChangeButtonActionPerformed
        // 수정
        int selectedRow = ListTable.getSelectedRow();
    
    if (selectedRow != -1) {
        String menuName = (String) ListTable.getValueAt(selectedRow, 1);
        String price = String.valueOf(ListTable.getValueAt(selectedRow, 2));
        
        MenuName1.setText(menuName);
        Price1.setText(price);
        
        Changemenu.setSize(230, 300);
        Changemenu.setLocationRelativeTo(null); 
        Changemenu.setVisible(true);
    } else {
        javax.swing.JOptionPane.showMessageDialog(this,
            "수정할 메뉴를 선택해주세요",
            "선택 오류", 
            javax.swing.JOptionPane.WARNING_MESSAGE);
    }
    }//GEN-LAST:event_ChangeButtonActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
      // 현재 창을 닫고 MainScreenManager로 이동
        this.dispose();  // userManagementFrame 닫기

        // UserAuthentication 객체 생성 (예시: 로그인 정보를 사용)
        UserAuthentication userAuth = new UserAuthentication();

        // MainScreenManager로 이동, UserAuthentication 객체 전달
        MainScreenManager mainScreenManager = new MainScreenManager(userAuth);
        mainScreenManager.setLocationRelativeTo(null);  // 화면 가운데 배치
        mainScreenManager.setVisible(true);  // MainScreenManager 창을 표시
    }//GEN-LAST:event_backButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ChangeButton;
    private javax.swing.JFrame Changemenu;
    private javax.swing.JTable ListTable;
    private javax.swing.JTextField MenuName;
    private javax.swing.JTextField MenuName1;
    private javax.swing.JTextField Price;
    private javax.swing.JTextField Price1;
    private javax.swing.JComboBox<String> Type;
    private javax.swing.JComboBox<String> Type1;
    private javax.swing.JButton addButton;
    private javax.swing.JFrame addmenu;
    private javax.swing.JButton backButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JLabel label;
    private javax.swing.JLabel restaurantManagement;
    // End of variables declaration//GEN-END:variables
}
