/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rulemanagement;

/**
 *
 * @author adsd3
 */
import java.io.*;
import java.util.*;

/**
 * 관리자가 추가·삭제하는 규칙을 파일에서 로드/저장하는 모델.
 */
public class RuleManagementModel {
    private final String filePath;
    private final List<String> rules = new ArrayList<>();

    public RuleManagementModel(String filePath) throws IOException {
        this.filePath = filePath;
        loadRules();
    }

    private void loadRules() throws IOException {
        rules.clear();
        File file = new File(filePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    rules.add(line.trim());
                }
            }
        }
    }

    private void saveRules() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String rule : rules) {
                writer.write(rule);
                writer.newLine();
            }
        }
    }

    public List<String> getRules() {
        return new ArrayList<>(rules);
    }

    public void addRule(String rule) throws IOException {
        rules.add(rule);
        saveRules();
    }

    public void deleteRules(List<String> toDelete) throws IOException {
        rules.removeAll(toDelete);
        saveRules();
    }
}