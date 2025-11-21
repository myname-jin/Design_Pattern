/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerClient;
/**
 * ConcreteCommand (파일 동기화)
 * @author adsd3
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileUpdateCommand implements NetworkCommand {

    private static final String CLIENT_RESOURCE_PATH = "src/main/resources/";
    private final BufferedWriter writer;
    private final String filename;

    public FileUpdateCommand(BufferedWriter writer, String filename) {
        this.writer = writer;
        this.filename = filename;
    }
    
    @Override
    public void execute() throws IOException {
        File file = new File(CLIENT_RESOURCE_PATH + filename);
        if (!file.exists()) {
            System.err.println("[FileUpdateCommand] 파일이 존재하지 않음: " + filename);
            return;
        }
        
        writer.write("FILE_UPDATE:" + filename + "\n");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line + "\n");
            }
        }
        writer.write("<<EOF>>\n");
        writer.flush();
    }
}