package test;


import javax.ejb.Local;

@Local
public interface SessionBeanLocal {
    String multipleString(String str, int amount);
}
