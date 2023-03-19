package sangmyung.chatprompt.Util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import sangmyung.chatprompt.xml.DTO.PromptDTO;
import sangmyung.chatprompt.xml.DTO.PromptListDTO;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class XmlParserTest {
    private String xmlPath = "prompt/preparation/sample.xml";

    @Test
    @DisplayName("unmarshal 테스트")
    public void jaxbTest() throws JAXBException, IOException {

        // Given
        FileInputStream fileInputStream = new FileInputStream(xmlPath);
        JAXBContext jaxbContext = JAXBContext.newInstance(PromptListDTO.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        // When
        PromptListDTO xmlListTag = (PromptListDTO) unmarshaller.unmarshal(fileInputStream);
        fileInputStream.close();

        // Then
        assertNotNull(xmlListTag);
        assertNotNull(xmlListTag.getInfoList());
        assertEquals(4, xmlListTag.getInfoList().length);
//        assertEquals("iPhone", xmlListTag.getInfoList()[0].getModel());
    }

}