package sangmyung.chatprompt.xml.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sangmyung.chatprompt.xml.DTO.PromptDTO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TxtWriter {
    private final TxtAppender appender;
    private String txtPath = "prompt/reference/";
    private String refSuffix = "_reference.txt";



    public void checkAndWriteFile(List<PromptDTO> infoList, String taskNum, String idx) throws IOException {
        File file = new File(txtPath + taskNum + refSuffix);


        if (!file.exists()){
            file.createNewFile();

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            String machineTrans = infoList.get(1).getDefinition();
            writeUpperText(bufferedWriter, machineTrans);

            bufferedWriter.flush();
            bufferedWriter.close();
        }

        appender.appendTds(taskNum, infoList, idx);
    }

    public void addTableSuffix(String taskNum) throws IOException {
        File file = new File(txtPath + taskNum + refSuffix);

        FileWriter fileWriter = new FileWriter(file, true);
        BufferedWriter writer = new BufferedWriter(fileWriter);

        writer.write("\n\t</table>");
        writer.close();

    }

    private void writeUpperText(BufferedWriter writer, String machineTrans) throws IOException {
        writer.write("\t<table border='1' width='1024px' style='table-layout:fixed; word-break:break-all;'>\n" +
                "\t\t<tr>\n" +
                "\t\t\t<td width='100px' align='center'>\n" +
                "\t\t\t\t<br> 지시문 <br><br>\n" +
                "\t\t\t</td>\n" +
                "\t\t\t<td>\n" +
                "\t\t\t\t<br> ( 지시문 ) <br><br>\n" +
                "\t\t\t</td>\n" +
                "\t\t</tr>\n" +
                "\t\t<tr>\n" +
                "\t\t\t<td align='center'>\n" +
                "\t\t\t\t<br> 기계번역문1 <br><br>\n" +
                "\t\t\t</td>\n" +
                "\t\t\t<td>\n" +
                "\t\t\t\t<br> " + machineTrans + " <br><br>\n" +
                "\t\t\t</td>\n" +
                "\t\t</tr>\n" +
                "\t\t<tr>\n" +
                "\t\t\t<td align='center'>\n" +
                "\t\t\t\t<br> 기계번역문2 <br><br>\n" +
                "\t\t\t</td>\n" +
                "\t\t\t<td>\n" +
                "\t\t\t\t<br> ( 기계번역문2 ) <br><br>\n" +
                "\t\t\t</td>\n" +
                "\t\t</tr> \n" +
                "\t</table><br>\n" +
                "\n" +
                " ( split this page up and down ) \n" +
                "\n" +
                "\t<table border='1' table width='1024px' style='table-layout:fixed; word-break:break-all;'>");
    }

}
