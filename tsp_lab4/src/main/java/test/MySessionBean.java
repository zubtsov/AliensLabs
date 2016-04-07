package test;

import javax.ejb.Stateless;

@Stateless
public class MySessionBean implements SessionBeanLocal {
    @Override
    public String multipleString(String str, int amount) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < amount; i++) {
            sb.append(str);
            sb.append(" ");
        }
        return sb.toString();
    }
}
