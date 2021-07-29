/*
功能：服务器和某个客户端的通讯线程
 */
package qqserver.model;
import common.Message;
import common.MessageType;
import qqClient_model.ManageClientConServerThread;
import qqclient_view.ManageQqFriendList;
import qqserver.view.ManageView;
import qqserver.view.myServer_view;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;

public class SerConClientThread extends Thread{
    myServer_view mview = null;
    Socket s;
    public SerConClientThread(Socket s)
    {
        //把服务器与该客户端的连接赋给s
        this.s = s;
    }

    //让该线程去通知其它用户
    public void notifyOther(String iam) throws IOException {
        //得到在线的人的线程
        HashMap hm = ManageClientThread.hm;
        Iterator it = hm.keySet().iterator();

        while(it.hasNext()){
            Message m = new Message();
            m.setSender(iam);
            m.setCon(iam);
            m.setMesType(MessageType.message_ret_onLineFriend);
            //取出在线的人的id
            String onLineUserId = it.next().toString();
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientThread.getClientThread(onLineUserId).s.getOutputStream());
            m.setGetter(onLineUserId);
            oos.writeObject(m);
        }

    }

    public Socket getS() {
        return s;
    }

    public void run()
    {
        while(true)
        {
            //这里该线程就可以接收客户端的信息
            try {

                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                Message m = (Message) ois.readObject();

                //取出message中信息,确认服务器拿到了信息
                System.out.println(m.getSender() + " 给 " + m.getGetter() + " 说 " + m.getCon());

                //对从客户端取得的消息进行类型判断，然后做相应的处理
                if(m.getMesType().equals(MessageType.message_comm_mes)){
                    //一会完成转发任务
                    //取得接收人的通讯线程
                    SerConClientThread sc =  ManageClientThread.getClientThread(m.getGetter());
                    ObjectOutputStream oos = new ObjectOutputStream(sc.s.getOutputStream());
                    oos.writeObject(m);
                }
                else if(m.getMesType().equals(MessageType.message_get_onLineFriend)){
                    System.out.println(m.getSender() + "要他的好友");
                    //把在服务器的好友给该客户端返回
                    mview = ManageView.getView("1");
                    mview.showXt(m.getSender());
                    String res = ManageClientThread.getAllOnLineUserid();
                    Message mnew = new Message();
                    mnew.setCon(res);
                    mnew.setSender(m.getSender());
                    mnew.setMesType(MessageType.message_ret_onLineFriend);
                    mnew.setGetter(m.getSender());

                    ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                    oos.writeObject(mnew);

                }else if(m.getMesType().equals(MessageType.message_qxx)){
                    System.out.println(m.getSender() + "发送群消息");
                   //得到在线的人的线程
                    HashMap hm = ManageClientThread.hm;
                    Iterator it = hm.keySet().iterator();

                    while(it.hasNext()){
                        //取出在线的人的id
                        String onLineUserId = it.next().toString();
                        ObjectOutputStream oos = new ObjectOutputStream(ManageClientThread.getClientThread(onLineUserId).getS().getOutputStream());
                        m.setGetter(onLineUserId);
                        oos.writeObject(m);
                    }
                }else if(m.getMesType().equals(MessageType.message_out)){
                    //得到在线的人的线程
                    ManageClientThread.removeClientThread(m.getCon());
                    HashMap hm = ManageClientThread.hm;
                    Iterator it = hm.keySet().iterator();

                    while(it.hasNext()){
                        //取出在线的人的id
                        String onLineUserId = it.next().toString();
                        ObjectOutputStream oos = new ObjectOutputStream(ManageClientThread.getClientThread(onLineUserId).getS().getOutputStream());
                        m.setGetter(onLineUserId);
                        oos.writeObject(m);
                    }
                    mview = ManageView.getView("1");
                    mview.showXx(m.getSender());
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

}
