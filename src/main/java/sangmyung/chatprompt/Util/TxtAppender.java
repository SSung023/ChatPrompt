package sangmyung.chatprompt.Util;
import org.springframework.stereotype.Service;
import sangmyung.chatprompt.xml.DTO.PromptDTO;

import java.io.*;
import java.util.List;

@Service
public class TxtAppender {
    private File file;
    private BufferedWriter writer;
    private BufferedReader reader;

    private Boolean isTableExist = false;

//    public TxtAppender(File file) {
//        this.file = file;
//    }

    public File appendTds(File file) throws IOException {
        this.reader = new BufferedReader(new FileReader(file));

        String line = "";
        String content = "";

        while((line = reader.readLine()) != null){
            if (line.contains("테스트 텍스트입니다")) {
                isTableExist = true;
                continue;
            }
            if (line.contains("테스트 끝입니다") && isTableExist) {
                content += "추가할 문자열\n";
                isTableExist = false;
            }
            reader.close();

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        }
        return file;
    }

//    public File appendTds(List<PromptDTO> promptDTO) throws IOException {
//        this.writer = new BufferedWriter(new FileWriter(file, true));
//
//        for (int i = 0; i < promptDTO.size(); i++){
//
//        }
//
//        return file;
//    }
}
