/*
这是用户信息类
 */
package common;

import qqclient_view.qqchat;

public class User implements java.io.Serializable{//序列化，让一个对象可以在网络中传输
    private String userId;
    private String passwd;

    public String getPasswd() {
        return passwd;
    }

    public String getUserId() {
        return userId;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
