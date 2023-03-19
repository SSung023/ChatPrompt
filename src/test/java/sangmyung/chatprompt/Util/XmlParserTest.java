package sangmyung.chatprompt.Util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import sangmyung.chatprompt.xml.DTO.PromptDTO;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class XmlParserTest {

    @Test
    @DisplayName("unmarshal 테스트")
    public void jaxbTest() throws JAXBException, IOException {

        // Given
        FileInputStream fileInputStream = new FileInputStream("prompt/preparation/reference.xml");
        JAXBContext jaxbContext = JAXBContext.newInstance(PromptDTO.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        // When
        PromptDTO xmlListTag = (PromptDTO) unmarshaller.unmarshal(fileInputStream);
        fileInputStream.close();

        // Then
        assertNotNull(xmlListTag);
//        assertNotNull(xmlListTag.getSmartPhoneTags());
//        assertEquals(4, xmlListTag.getSmartPhoneTags().length);
//        assertEquals("iPhone", xmlListTag.getSmartPhoneTags()[0].getModel());
    }

}