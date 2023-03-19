package sangmyung.chatprompt.Util;
import org.springframework.stereotype.Service;
import sangmyung.chatprompt.xml.DTO.PromptDTO;

import java.io.*;
import java.util.List;

@Service
public class TxtAppender {
    private String txtPath = "prompt/reference/";
    private String refSuffix = "_reference.txt";

    public void appendTds(String taskNum, List<PromptDTO> infoList, String idx) throws IOException {
//        this.file = file;

        File file = new File(txtPath + taskNum + refSuffix);
        FileWriter fileWriter = new FileWriter(file, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        writeLowerText(bufferedWriter, infoList, idx);

        bufferedWriter.close();
    }

    public void writeLowerText(BufferedWriter writer,
                               List<PromptDTO> infoList, String idx) throws IOException {
        writer.write("<tr>\n" +
                        "\t\t\t<td bgcolor='green' align='center'>\n" +
                        "\t\t\t\t<br> 입력 "+ idx +" <br><br>\n" +
                        "\t\t\t</td>\n" +
                        "\t\t\t<td bgcolor='green'>\n" +
                        "\t\t\t\t<br> " + infoList.get(0).getInputStr().toString() + " <br><br>\n" +
                        "\t\t\t\t<br> " + infoList.get(1).getInputStr().toString() + " <br><br>\n" +
                        "\t\t\t</td>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td align='center'>\n" +
                        "\t\t\t\t<br> 출력 " + idx + " <br><br>\n" +
                        "\t\t\t</td>\n" +
                        "\t\t\t<td>\n" +
                        "\t\t\t\t<br> " + infoList.get(0).getOutputStr().toString() + " <br><br>\n" +
                        "\t\t\t\t<br> " + infoList.get(1).getOutputStr().toString() + " <br><br>\n" +
                        "\t\t\t</td>\n" +
                        "\t\t</tr>"
                );
    }
}
