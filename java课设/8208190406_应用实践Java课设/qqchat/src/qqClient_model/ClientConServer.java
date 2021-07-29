/*
这是客户端连接服务器的后台
 */
package qqClient_model;
import common.Message;
import common.User;
import qqclient_view.qqchat;

import java.net.*;
import java.io.*;
import java.util.*;
public class ClientConServer {
    public Socket s;
    //发送第一次请求
    public boolean SendLoginInfoToServer(Object o){
        boolean b = false;
        try{
            System.out.println("kk");
            s = new Socket("127.0.0.1",9999);
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeObject(o);
            //发出一个信息后将收到一个信息
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

            Message ms = (Message)ois.readObject(); //转换成Message

            //这里就是验证用户登录的地方
            if(ms.getMesType().equals("1")){
                //创建一个该qq号和服务器端保持通讯连接的线程
                ClientConServerThread ccst = new ClientConServerThread(s);
                //启动该通讯线程
                ccst.start();
                //把object对象先转为User型，再拿到它的qq号
                ManageClientConServerThread.addClientConServerThread(((User)o).getUserId(),ccst);

                b = true;
            }else{
                //关闭socket
                s.close();
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
        return b;
    }

    public void SendInfoToServer(Object o){
        try{
            Socket s = new Socket("127.0.0.1",9999);
        }catch(Exception e){
            e.printStackTrace();
        }finally{

        }
    }
}
