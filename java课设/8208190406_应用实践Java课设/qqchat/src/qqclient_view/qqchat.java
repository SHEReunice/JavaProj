/*
这是与好友聊天的界面,
因为客户端要处于读取的状态，因此我们把它做成一个线程
 */
package qqclient_view;

import common.Message;
import common.MessageType;
import qqClient_model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class qqchat extends JFrame implements ActionListener {
    JTextArea jta;
    JTextField jtf;
    JButton jb;
    JPanel jp;
    String ownerId;
    String friendId;
    public static void main(String[] args){
        //qqchat qq = new qqchat("1");
    }
    public qqchat(String ownerId,String friend){
        this.ownerId = ownerId;
        this.friendId = friend;
        jta = new JTextArea();
        jtf = new JTextField(15);
        jb = new JButton("发送");
        jb.addActionListener(this);
        jp = new JPanel();
        jp.add(jtf);
        jp.add(jb);

        this.add(jta,"Center");
        this.add(jp,"South");
        this.setTitle(ownerId + " 正在和 " + friend + " 聊天");
        this.setIconImage((new ImageIcon("src//image//qe.jpg")).getImage());
        this.setSize(300,200);
        this.setVisible(true);


    }


    //写一个方法显示消息
    public void showMessage(Message m){
        String info = m.getSender() + " 对 " + m.getGetter() + " 说 " + m.getCon() + "\r\n";
        this.jta.append(info);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jb)
        {
           //如果用户点击发送按钮
            Message m = new Message();
            m.setMesType(MessageType.message_comm_mes);
            m.setSender(this.ownerId);
            m.setGetter(this.friendId);
            m.setCon(jtf.getText());
            m.setSendTime(new Date().toString());
            //发送给服务器
            try {

                //通过管理这个线程的类得到这个线程，然后通过getSocket得到对应的socket，然后得到输出流
                ObjectOutputStream oos = new ObjectOutputStream(ManageClientConServerThread.getClientConServerThread(ownerId).getS().getOutputStream());
                oos.writeObject(m);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            //显示
            String info = m.getSender() + " 对 " + m.getGetter() + " 说 " + m.getCon() + "\r\n";
            this.jta.append(info);
            jtf.setText("");

        }
    }

}
