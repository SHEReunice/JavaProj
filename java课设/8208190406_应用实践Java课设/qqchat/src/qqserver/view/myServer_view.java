/*
这是服务器的控制界面，可以完成启动服务器
 */
package qqserver.view;

import common.Message;
import common.MessageType;
import qqclient_view.FriendList;
import qqserver.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class myServer_view extends JFrame implements ActionListener {
    JPanel jp1;
    JButton jb1,jb2;
    JPanel jp2,jp3,jp4;
    JButton jp2_jb1,jp2_jb2;
    JButton jp3_jb;
    JTextField bh,xtxx;
    JTextArea jta;
    JTextArea jtalist;
    JPanel jpc;
    String id;
    ArrayList<String> zxList = new ArrayList<String>();
    public static void main(String[] args){

        myServer_view myserver = new myServer_view("1");
        ManageView.addView("1",myserver);
        new myQqServer();
    }
    public myServer_view(String id){
        this.id = id;
       jp1 = new JPanel();
       jb1 = new JButton("启动服务器");
       jb1.addActionListener(this);
       jb2 = new JButton("关闭服务器");
       jb2.addActionListener(this);

       jp2 = new JPanel(new BorderLayout());
       jp3 = new JPanel(new BorderLayout());
       jp4 = new JPanel(new GridLayout(2,1,10,10));

       jp2_jb1 = new JButton("用户编号：");
       bh = new JTextField();
       jp2_jb2 = new JButton("强制下线");
       jp2_jb2.addActionListener(this);
       jp3_jb = new JButton("系统公告");
       jp3_jb.addActionListener(this);
       xtxx = new JTextField();

       jp2.add(jp2_jb1,BorderLayout.WEST);
       jp2.add(bh,BorderLayout.CENTER);
       jp2.add(jp2_jb2,BorderLayout.EAST);

       jp3.add(xtxx,BorderLayout.CENTER);
       jp3.add(jp3_jb,BorderLayout.EAST);

       jp4.add(jp3);
       jp4.add(jp2);

       jp1.add(jb1);
       jp1.add(jb2);
       jta = new JTextArea();
       jtalist = new JTextArea();
       jpc = new JPanel(new GridLayout(1,2,10,10));
       jpc.add(jta);
       jpc.add(jtalist);


       this.add(jp1,"North");
       this.add(jpc,"Center");
       this.add(jp4,"South");
       this.setSize(500,400);
       this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jb1){
            System.out.println("启动成功");
        }else if(e.getSource() == jp3_jb){
            try {
                SendNotice();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }else if(e.getSource() == jb2){
            System.exit(0);
        }else if(e.getSource() == jp2_jb2){
            String outId = bh.getText();
            Message m = new Message();
            m.setCon(outId);
            m.setMesType(MessageType.message_selfout);
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(ManageClientThread.getClientThread(outId).getS().getOutputStream());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            m.setGetter(outId);
            try {
                oos.writeObject(m);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            ManageClientThread.removeClientThread(outId);
            HashMap hm = ManageClientThread.hm;
            Iterator it = hm.keySet().iterator();
            Message m4 = new Message();
            m4.setCon(outId);
            m4.setMesType(MessageType.message_out);
            while(it.hasNext()){
                //取出在线的人的id
                String onLineUserId = it.next().toString();
                ObjectOutputStream ooos = null;
                try {
                    ooos = new ObjectOutputStream(ManageClientThread.getClientThread(onLineUserId).getS().getOutputStream());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                m4.setGetter(onLineUserId);
                try {
                    ooos.writeObject(m4);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            jta.append(outId + "下线啦!" + "\n");
            zxList.remove(outId);
            jtalist.setText("");
            Collections.sort(zxList);
            jtalist.append("在线用户： \n");
            for (Iterator iter = zxList.iterator(); iter.hasNext();) {
                jtalist.append(iter.next().toString()+"\n");

            }
            bh.setText("");
        }

    }

    //发送系统公告
    public void SendNotice() throws IOException {
        //得到在线的人的线程
        HashMap hm = ManageClientThread.hm;
        Iterator it = hm.keySet().iterator();

        while(it.hasNext()){
            Message m = new Message();
            m.setCon(xtxx.getText());
            m.setMesType(MessageType.message_xtxx);
            //取出在线的人的id
            String onLineUserId = it.next().toString();
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientThread.getClientThread(onLineUserId).getS().getOutputStream());
            m.setGetter(onLineUserId);
            oos.writeObject(m);
        }
        jta.append("系统公告： " + xtxx.getText() + "\n");
        xtxx.setText("");
    }

    public void showXt(String mes){
        String info = mes + "上线啦！" + "\n";
        jta.append(info);
        zxList.add(mes);
        jtalist.setText("");
        Collections.sort(zxList);
        jtalist.append("在线用户： \n");
        for (Iterator iter = zxList.iterator(); iter.hasNext();) {
            jtalist.append(iter.next().toString()+"\n");

        }

    }
    public void showXx(String mes){
        String info = mes + "下线啦！" + "\n";
        zxList.remove(mes);
        jta.append(info);
        jtalist.setText("");
        Collections.sort(zxList);
        jtalist.append("在线用户： \n");
        for (Iterator iter = zxList.iterator(); iter.hasNext();) {
            jtalist.append(iter.next().toString()+"\n");

        }
    }
}
