package sangmyung.chatprompt.Util;
import java.io.*;

public class TxtAppender {
    private BufferedWriter writer;

    public TxtAppender(BufferedWriter writer) {
        this.writer = writer;
    }

    public void write(String content) throws IOException {
        writer.write(content);
    }

    public void close() throws IOException {
        writer.close();
    }
}
