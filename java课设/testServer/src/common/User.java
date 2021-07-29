package common;

public class User implements java.io.Serializable{ //这个接口用来实现序列化
    private String name;
    private  String pass;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
