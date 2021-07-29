/*
好友列表，陌生人，黑名单
 */

package qqclient_view;

import common.Message;
import common.MessageType;
import qqClient_model.ManageClientConServerThread;
import qqserver.model.ManageClientThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;

public class FriendList extends JFrame implements ActionListener,MouseListener{
    //处理第一张卡片
    JPanel jphy1,jphy2,jphy3,jphy4,jphy5,jphy6;
    JButton jphy_jb1,jphy_jb2,jphy_jb3;
    JScrollPane jsp1,jsp3;

    JTextArea qxx;
    JTextField qxxfs;
    JButton jphy4_jbt;


    String owner;
    //处理第二张卡片陌生人
    JPanel jpmsr1,jpmsr2,jpmsr3;
    JButton jpmsr_jb1,jpmsr_jb2,jpmsr_jb3;
    JScrollPane jsp2;
    JLabel[] jbls;

    //把整个JFrame设置成CardLayout布局
    CardLayout cl;

    public static void main(String[] args){

        //FriendList friendList = new FriendList();
    }



    public FriendList(String ownerId){
        this.owner = ownerId;
        //处理第一张卡片，显示好友列表
        jphy1 = new JPanel(new BorderLayout());
        //假定有50个好友
        jphy2 = new JPanel(new GridLayout(50,1,4,4));;
        //初始化50个好友
        jbls = new JLabel[50];

        for(int i=0; i < jbls.length; i++){
            jbls[i] = new JLabel(i+1+"",new ImageIcon("src//image//mm.jpg"),JLabel.LEFT);
            jbls[i].setEnabled(false);
            if(jbls[i].getText().equals(ownerId)){
                jbls[i].setEnabled(true);
            }
            jbls[i].addMouseListener(this);
            jphy2.add(jbls[i]);


        }

        jphy_jb1 = new JButton("我的好友");
        jphy_jb2 = new JButton("陌生人");
        jphy_jb2.addActionListener(this);
        jphy_jb3 = new JButton("黑名单");

        jphy3 = new JPanel(new GridLayout(2,1));
        //把两个按钮加到jphy3
        jphy3.add(jphy_jb2);
        jphy3.add(jphy_jb3);

        jphy4 = new JPanel(new BorderLayout());
        jphy5 = new JPanel();
        jphy5.setLayout(new BorderLayout());
        jphy6 = new JPanel();
        jphy6.setLayout(new GridLayout(2,1,10,10));

        qxx = new JTextArea();
        jsp3 = new JScrollPane(qxx);
        qxxfs = new JTextField();
        jphy4_jbt = new JButton("发送群消息");
        jphy4_jbt.addActionListener(this);
        jphy5.add(qxxfs,BorderLayout.CENTER);
        jphy5.add(jphy4_jbt,BorderLayout.EAST);
        jphy4.add(jsp3,"Center");
        jphy4.add(jphy5,"South");




        jsp1 = new JScrollPane(jphy2);
        jphy6.add(jsp1);
        jphy6.add(jphy4);

        //对jphy1初始化
        jphy1.add(jphy_jb1,"North");
        jphy1.add(jphy6,"Center");
        jphy1.add(jphy3,"South");


        //对第二张卡片处理
        jpmsr1 = new JPanel(new BorderLayout());
        //假定有50个好友
        jpmsr2 = new JPanel(new GridLayout(20,1,4,4));;
        //初始20个陌生人
        JLabel[] jbls2 = new JLabel[20];

        for(int i=0; i < jbls2.length; i++){
            jbls2[i] = new JLabel(i+1+"",new ImageIcon("src//image//gg.png"),JLabel.LEFT);
            jpmsr2.add(jbls2[i]);

        }

        jpmsr_jb1 = new JButton("好友");
        jpmsr_jb1.addActionListener(this);
        jpmsr_jb2 = new JButton("陌生人");
        jpmsr_jb3 = new JButton("黑名单");

        jpmsr3 = new JPanel(new GridLayout(2,1));
        //把两个按钮加到jphy3
        jpmsr3.add(jpmsr_jb1);
        jpmsr3.add(jpmsr_jb2);

        jsp2 = new JScrollPane(jpmsr2);

        //对jphy1初始化
        jpmsr1.add(jpmsr3,"North");
        jpmsr1.add(jsp2,"Center");
        jpmsr1.add(jpmsr_jb3,"South");




        cl = new CardLayout();
        this.setLayout(cl);
        this.add(jphy1,"1");
        this.add(jpmsr1,"2");
        //在窗口显示自己的编号
        this.setTitle(ownerId);
        this.setSize(300,500);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //监听系统关闭事件，退出时给服务器端发出指定消息
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //发送一个下线消息


                ObjectOutputStream oos = null;
                try {
                    oos = new ObjectOutputStream(ManageClientConServerThread.getClientConServerThread(ownerId).getS().getOutputStream());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                //做一个message包
                Message m3 = new Message();
                m3.setMesType(MessageType.message_out);
                //指明是谁发的
                m3.setSender(ownerId);
                m3.setCon(ownerId);
                try {
                    oos.writeObject(m3);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                ManageQqFriendList.removeFriendList(ownerId);
                ManageClientConServerThread.removeClientConServerThread(ownerId);
                System.exit(0);
            }
        });

    }

    public void out(){
        System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //如果点击陌生人，显示第二张卡片
        if(e.getSource()==jphy_jb2){
            cl.show(this.getContentPane(),"2");
        }else if(e.getSource()==jpmsr_jb1){
            cl.show(this.getContentPane(),"1");
        }else if(e.getSource() == jphy4_jbt){
            //发送一个群消息
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(ManageClientConServerThread.getClientConServerThread(owner).getS().getOutputStream());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            //做一个message包
            Message m = new Message();
            m.setMesType(MessageType.message_qxx);
            //指明是谁发的
            m.setSender(this.owner);
            m.setCon(qxxfs.getText());
            try {
                oos.writeObject(m);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            qxxfs.setText("");
        }
    }



    @Override
    public void mouseClicked(MouseEvent e) {
        //响应用户双击的事件并得到好友的编号
        if(e.getClickCount()==2){
            //得到该好友的编号
            String friendNo = ((JLabel)e.getSource()).getText();
            //System.out.println("你希望和" + friendNo + "聊天");
           qqchat qqc = new qqchat(this.owner,friendNo);

           //把聊天界面加入到管理类
            ManageQqChat.addQqChat(this.owner+" "+friendNo,qqc);


        }

    }

    //更新在线的好友情况
    public void updateFriendList(Message m){
        String onLineFriend [] = m.getCon().split(" ");

        for(int i = 0; i < onLineFriend.length ; i++){
            jbls[Integer.parseInt(onLineFriend[i]) - 1].setEnabled(true);
        }
    }

    //更新下线的好友
    public void outFriend(Message m){
        String outfriend = m.getCon();
        jbls[Integer.parseInt(outfriend)-1].setEnabled(false);
    }

    //修改群公告
    public void showQgg(Message m){
        String info = "系统公告：  " + m.getCon() + "\r\n";
        this.qxx.append(info);
    }

    public void showQxx(Message m){
        String info = m.getSender() + " 说 " +  m.getCon() + "\r\n";
        this.qxx.append(info);
    }

    public void showSx(String mes){
        String info = mes + "上线啦！" + "\r\n";
        this.qxx.append(info);
    }

    public void showXx(Message m){
        String info = m.getCon() + "下线啦！" + "\r\n";
        this.qxx.append(info);
    }


    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        JLabel jl = (JLabel)e.getSource();
        jl.setForeground(Color.ORANGE);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        JLabel jl = (JLabel)e.getSource();
        jl.setForeground(Color.BLACK);
    }


}
