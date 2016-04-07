package test;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@ManagedBean(name="myTaskBean")
@SessionScoped
@XmlRootElement(name="task")
public class MyTaskBean {
    private int amount;
    private String word;
    private String response;

    @EJB
    private SessionBeanLocal mb;

    @XmlElement(name="amount")
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    @XmlElement(name="word")
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
    @XmlElement(name="result")
    public String getResponse() {
        response = mb.multipleString(word, amount);
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
