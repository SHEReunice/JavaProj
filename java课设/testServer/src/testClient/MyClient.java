package testClient;
import common.User;

import java.net.*;
import java.io.*;
public class MyClient {
    public static void main(String[] args){
        MyClient mc = new MyClient();
    }
    public MyClient(){
        try{
            Socket s = new Socket("127.0.0.1",3456);
            //通过ObjectOutputStream给服务器传送对象
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            User u = new User();
            u.setName("hello");
            u.setPass("123456");
            oos.writeObject(u); //没有序列化，会报错NotSerializableException
            System.out.println("ok");

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
