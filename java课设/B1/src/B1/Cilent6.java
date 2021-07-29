package B1;

import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.List;

class Client6 extends JFrame implements ActionListener,Runnable{
    private JTextField jtf = new JTextField();
    private JTextArea jta = new JTextArea();
    private JButton sendBtn = new JButton("发送");
    private List users = new List(10,true);
    private PrintStream ps = null;
    private BufferedReader br = null;
    private String nickName;
    public Client6() throws Exception{
        this.setTitle(nickName);

        Font f = new Font("宋体", Font.BOLD, 25);

        this.setLayout(null);
        this.add(jtf);
        this.add(jta);
        this.add(users);
        this.add(sendBtn);

        users.add("hello");
//        users.add("你好");
        users.setVisible(true);

        jta.setBackground(Color.orange);
        jtf.setBackground(Color.blue);
//        users.setBackground(Color.CYAN);

        jta.setFont(f);
        jtf.setFont(f);
        users.setFont(f);
        sendBtn.setFont(f);

        jta.setBounds(0,0,400,300);
        users.setBounds(400,0,200,300);
        jtf.setBounds(0,300,400,100);
        sendBtn.setBounds(470,300,120,65);



        jtf.addActionListener(this);
        users.addActionListener(this);
        sendBtn.addActionListener(this);

        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setSize(600,400);
        this.setVisible(true);
        nickName = JOptionPane.showInputDialog("请您输入昵称");
//        users.add(nickName);


        Socket s = new Socket(InetAddress.getLocalHost(), 9999);
        ps = new PrintStream(s.getOutputStream());
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        new Thread(this).start();
    }
    public void run() {

        while(true){
            try{
                String s = br.readLine();
                jta.append(s+ "\n");
            } catch(Exception e){}
        }
    }
    public void actionPerformed(ActionEvent e){
        String sendNames = nickName;


        if(e.getSource() == sendBtn){
            for(String sendTo : users.getSelectedItems())
            {
                sendNames = sendNames + "#" + sendTo + "#" ;
            }

            sendNames = sendNames + jtf.getText();
            System.out.println(sendNames);

        }

        String receiveMsg = nickName + ":" + jtf.getText();
        ps.println(receiveMsg);
        jtf.setText("");
    }
    public static void main(String[] args) throws Exception {
        new Client6();
    }
    }
     