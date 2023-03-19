package sangmyung.chatprompt.Util;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sangmyung.chatprompt.xml.DTO.PromptDTO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
@NoArgsConstructor
public class TxtWriter {
    private String txtPath = "prompt/reference/";
    private String refSuffix = "_reference.txt";


    public void checkFileExist(PromptDTO promptDTO) throws IOException {
        File file = new File(txtPath + "002" + refSuffix);

        if (!file.exists()){
            file.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        bufferedWriter.write("테스트 텍스트입니다");

        bufferedWriter.close();

    }
}
