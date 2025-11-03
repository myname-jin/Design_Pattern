/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ruleagreement;

/**
 *
 * @author adsd3
 */
import java.io.*;
import java.util.*;

public class RuleAgreementModel {
    private final List<String> rules;

    public RuleAgreementModel(String absolutePath) throws IOException {
        rules = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(absolutePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                rules.add(line.trim());
            }
        }
    }

    public List<String> getRules() {
        return rules;
    }
}