package test;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="task")
public class TaskClass {
    private String word;
    private int amount;
    private String result;

    public String getWord() {
        return word;
    }
    @XmlElement(name="argument1")
    public void setWord(String word) {
        this.word = word;
    }

    public int getAmount() {
        return amount;
    }
    @XmlElement(name="argument2")
    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getResult() {
        return result;
    }
    @XmlElement(name="result")
    public void setResult(String result) {
        this.result = result;
    }

    public TaskClass(String word, int amount) {
        this.word = word;
        this.amount = amount;
    }

    public TaskClass() {

    }
}
