package testServer;
import common.User;

import java.net.*;
import java.io.*;
public class MyServer {

    public static void main(String[] args){
        MyServer ms = new MyServer();
    }
    public MyServer(){
        try{
            System.out.println("在3456端口监听..");
            ServerSocket ss = new ServerSocket(3456);
            Socket s = ss.accept();
            //以对象流的方式读取(假设客户端发送的是User的一个对象)
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            User u = (User)ois.readObject();

            //输出
            System.out.println("从客户端接收到" + u.getName() + u.getPass());

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
