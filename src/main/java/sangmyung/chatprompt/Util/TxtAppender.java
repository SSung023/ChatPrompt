package sangmyung.chatprompt.Util;
import org.springframework.stereotype.Service;
import sangmyung.chatprompt.xml.DTO.PromptDTO;

import java.io.*;
import java.util.List;

@Service
public class TxtAppender {
//    private File file;

    public File appendTds(File file, List<PromptDTO> infoList, int idx) throws IOException {
//        this.file = file;
        FileWriter fileWriter = new FileWriter(file, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        writeLowerText(bufferedWriter,
               infoList.get(0).getInputStr(),
               infoList.get(1).getInputStr(),
               infoList.get(0).getOutputStr(),
               infoList.get(1).getOutputStr(),
               idx);

        bufferedWriter.close();

        return file;
    }

    public void writeLowerText(BufferedWriter writer,
                               String i1, String i2, String o1, String o2, int idx) throws IOException {
        writer.write("\t\t<tr>\n" +
                        "\t\t\t<td bgcolor='green' align='center'>\n" +
                        "\t\t\t\t<br> 입력" + idx + " <br><br>\n" +
                        "\t\t\t</td>\n" +
                        "\t\t\t<td bgcolor='green'>\n" +
                        "\t\t\t\t<br> " + i1 + " <br><br>\n" +
                        "\t\t\t\t<br> " + i2 + " <br><br>\n" +
                        "\t\t\t</td>\n" +
                        "\t\t</tr>\n" +

                        "\t\t<tr>\n" +
                        "\t\t\t<td align='center'>\n" +
                        "\t\t\t\t<br> 출력" + idx + " <br><br>\n" +
                        "\t\t\t</td>\n" +
                        "\t\t\t<td bgcolor='green'>\n" +
                        "\t\t\t\t<br> " + o1 + " <br><br>\n" +
                        "\t\t\t\t<br> " + o2 + " <br><br>\n" +
                        "\t\t\t</td>\n" +
                        "\t\t</tr>\n"
                );
    }
}
