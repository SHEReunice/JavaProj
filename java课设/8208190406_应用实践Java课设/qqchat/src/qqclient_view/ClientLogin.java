/*
qq客户端登录界面
 */
package qqclient_view;


import common.Message;
import common.MessageType;
import common.User;
import qqClient_model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ClientLogin extends JFrame implements ActionListener{
    //定义北部需要的图片
    JLabel jbl1;

    //定义中部
    //中部有三个JPanel，有一个选项卡窗口管理
    JTabbedPane jtp;
    JPanel jp2,jp3,jp4;
    JLabel jp2_jbl1,jp2_jbl2,jp2_jbl3,jp2_jbl4;
    JButton jp2_jb1;
    JTextField jp2_jtf;
    JPasswordField jp2_jpf;
    JCheckBox jp2_jcb1,jp2_jcb2;


    //定义南部
    JPanel jp1;
    JButton jp1_jb1,jp1_jb2,jp1_jb3;

    public static void main(String[] args){
        ClientLogin qqclientLogin = new ClientLogin();
    }

    public ClientLogin(){
        //处理北边
        jbl1 = new JLabel(new ImageIcon("src//image//tou.gif"));
        //处理中部
        jp2 = new JPanel(new GridLayout(3,3));
        jp2_jbl1 = new JLabel("QQ号码",JLabel.CENTER);
        jp2_jbl2 = new JLabel("QQ密码",JLabel.CENTER);
        jp2_jbl3 = new JLabel("忘记密码",JLabel.CENTER);
        jp2_jbl3.setForeground(Color.BLUE);
        jp2_jbl4 = new JLabel("申请密码保护",JLabel.CENTER);
        jp2_jb1 = new JButton("清除号码");
        jp2_jtf = new JTextField();
        jp2_jpf = new JPasswordField();
        jp2_jcb1 = new JCheckBox("隐身登录");
        jp2_jcb2 = new JCheckBox("记住密码");
        //把控件按照顺序加入到jp2
        jp2.add(jp2_jbl1);
        jp2.add(jp2_jtf);
        jp2.add(jp2_jb1);
        jp2.add(jp2_jbl2);
        jp2.add(jp2_jpf);
        jp2.add(jp2_jbl3);
        jp2.add(jp2_jcb1);
        jp2.add(jp2_jcb2);
        jp2.add(jp2_jbl4);

        jtp = new JTabbedPane();
        jtp.add("QQ号码",jp2);
        jp3 = new JPanel();
        jp4 = new JPanel();
        jtp.add("手机号码",jp3);
        jtp.add("电子邮件",jp4);


        //处理南部
        jp1 = new JPanel(); //默认居中
        jp1_jb1 = new JButton("登录");
        //响应用户点击登录
        jp1_jb1.addActionListener(this);
        jp1_jb2 = new JButton("取消");
        jp1_jb3 = new JButton("注册向导");
        //三个按钮放入JPanel
        jp1.add(jp1_jb1);
        jp1.add(jp1_jb2);
        jp1.add(jp1_jb3);

        this.add(jbl1,"North");
        this.add(jp1,"South");
        this.add(jtp,"Center");
        jbl1.setSize(550,300);
        this.setSize(550,500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //当用户点击登录按钮
        if(e.getSource() == jp1_jb1){
            ClientUser qqClientUser = new ClientUser();
            User u = new User();
            u.setUserId(jp2_jtf.getText().trim()); //从输入框得到号码
            u.setPasswd(new String(jp2_jpf.getPassword()));

            if(ClientUser.checkUser(u)){

                try {
                    FriendList qqFriendList =  new FriendList(u.getUserId());
                    ManageQqFriendList.addFriendList(u.getUserId(),qqFriendList);

                    //发送一个要求返回在线好友的请求包
                    ObjectOutputStream oos = new ObjectOutputStream(ManageClientConServerThread.getClientConServerThread(u.getUserId()).getS().getOutputStream());

                    //做一个message包
                    Message m = new Message();
                    m.setMesType(MessageType.message_get_onLineFriend);
                    //指明我要的是这个qq号的好友情况
                    m.setSender(u.getUserId());
                    oos.writeObject(m);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                //同时关闭这个登录界面
                this.dispose();
            }else{
                //提示未成功
                JOptionPane.showMessageDialog(this,"用户名密码错误");
            }
        }
    }
}
