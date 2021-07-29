/*
客户端和服务器端保持通讯的线程
 */
package qqClient_model;

import common.Message;
import common.MessageType;
import qqclient_view.FriendList;
import qqclient_view.ManageQqChat;
import qqclient_view.ManageQqFriendList;
import qqclient_view.qqchat;

import java.net.Socket;
import java.io.*;

public class ClientConServerThread extends Thread {
    private Socket s;

    public Socket getS() {
        return s;
    }

    //构造函数
    public ClientConServerThread(Socket s)
    {
        this.s = s;
    }
    public void run()
    {
        while (true)
        {
            //不停的读取从服务器端发来的消息
            try{
                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                Message m = (Message)ois.readObject();
                //System.out.println("读取到从服务器发来的消息" + m.getSender() + " 给 " + m.getGetter() + " 内容 " + m.getCon());

                if(m.getMesType().equals(MessageType.message_comm_mes)){
                    //把从服务器获得的消息显示到该显示的聊天界面
                    qqchat qqc = ManageQqChat.getQqChat(m.getGetter() + " " +m.getSender());

                    //显示
                    qqc.showMessage(m);
                }else if(m.getMesType().equals(MessageType.message_ret_onLineFriend))
                {
                    System.out.println("客户端接收到" + m.getCon());
                    String con = m.getCon();
                    String friends[] = con.split(" ");
                    String getter = m.getGetter();
                    System.out.println("getter = " + getter);
                    //修改相应的好友列表
                    FriendList qqFriendList = ManageQqFriendList.getQqFriendList(getter);
                    if(qqFriendList != null){
                        //更新在线好友
                        qqFriendList.updateFriendList(m);
                    }
                    qqFriendList.showSx(m.getSender());

                }else if(m.getMesType().equals(MessageType.message_xtxx))
                {
                    System.out.println("客户端接收到系统公告" + m.getCon());
                    String con = m.getCon();
                    String geter = m.getGetter();
                    System.out.println("geter = " + geter);
                    FriendList frilist = ManageQqFriendList.getQqFriendList(geter);
                    frilist.showQgg(m);
                }else if(m.getMesType().equals(MessageType.message_qxx))
                {
                    System.out.println("客户端接收到群消息" + m.getCon());
                    String con = m.getCon();
                    String geter = m.getGetter();
                    System.out.println("geter = " + geter);
                    FriendList frilist = ManageQqFriendList.getQqFriendList(geter);
                    frilist.showQxx(m);
                }else if(m.getMesType().equals(MessageType.message_out)){
                    String outer = m.getCon();
                    System.out.println("outer = " + outer);
                    String gettter = m.getGetter();
                    //修改相应的好友列表
                    FriendList qqFriendList = ManageQqFriendList.getQqFriendList(gettter);
                    if(qqFriendList != null){
                        //更新在线好友
                        qqFriendList.outFriend(m);
                    }
                    qqFriendList.showXx(m);
                }else if(m.getMesType().equals(MessageType.message_selfout)){
                    System.out.println(m.getCon() + "被强制下线");
                    String com = m.getCon();
                    String geter = m.getGetter();
                    System.out.println("geter = " + geter);
                    FriendList frilist = ManageQqFriendList.getQqFriendList(geter);
                    frilist.out();
                }


            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }


}
