/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * ConcreteCommand (파일 동기화)
 * @author adsd3
 */
public class FileUpdateCommand implements NetworkCommand {

    private static final String CLIENT_RESOURCE_PATH = "src/main/resources/";
    private final String filename;

    public FileUpdateCommand(String filename) {
        this.filename = filename;
    }
    
    @Override
    public void execute(BufferedWriter out) throws IOException {
        File file = new File(CLIENT_RESOURCE_PATH + filename);
        if (!file.exists()) {
            System.err.println("[FileUpdateCommand] 파일이 존재하지 않음: " + filename);
            return;
        }
        
        out.write("FILE_UPDATE:" + filename + "\n");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                out.write(line + "\n");
            }
        }
        out.write("<<EOF>>\n");
        out.flush();
    }
}
