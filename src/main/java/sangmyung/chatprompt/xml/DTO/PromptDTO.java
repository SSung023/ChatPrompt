package sangmyung.chatprompt.xml.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/*
<type>example</type>
		<definition>In medical studies, treatments are tested within a group of study participants. You will be given a sentence of a study report in which your task is to list the phrases that give information about the participants of the study. You should list the phrases in the same order that they appear in the text, separated by commas. If no information about the participants is mentioned, just output "not found". Relevant information about participants include: gender, medical conditions, location, number of people participating. Do not contain participant mentions without relevant information.</definition>
		<input>A PPARA Polymorphism Influences the Cardiovascular Benefit of Fenofibrate in Type 2 Diabetes: Findings From ACCORD Lipid.</input>
		<output>Type 2 Diabetes</output>
		<num_input_tokens>17</num_input_tokens>
*/
//@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "text")
//@Getter @Setter
@ToString
public class PromptDTO {

    @XmlElement(name = "index_SMU")
    private int id;

    @XmlElement(name = "line_num")
    private String num;
    @XmlElement(name = "category")
    private String category;
    @XmlElement(name = "task")
    private String task;
    @XmlElement(name = "idx")
    private String index;
    @XmlElement(name = "type")
    private String type;
    @XmlElement(name = "definition")
    private String definition;
    @XmlElement(name = "input")
    private String inputStr;
    @XmlElement(name = "output")
    private String outputStr;
    @XmlElement(name = "num_input_tokens")
    private int inputToken;


}
