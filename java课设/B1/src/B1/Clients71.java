package B1;

import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.List;
import java.util.ArrayList;

class Client71 extends JFrame implements ActionListener,Runnable{
    public ArrayList<String> clientsNames = new ArrayList<String>();

    private JTextField jtf = new JTextField();
    private JTextArea jta = new JTextArea();
    private JButton sendBtn = new JButton("发送");
    private List users = new List(10,true);
    private PrintStream ps = null;
    private BufferedReader br = null;
    private String nickName;
    public Client71() throws Exception{


        Font f = new Font("宋体", Font.BOLD, 25);

        this.setLayout(null);
        this.add(jtf);
        this.add(jta);
        this.add(users);
        this.add(sendBtn);

//        users.add("hello");
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
        this.setResizable(false);
        this.setVisible(true);
        nickName = "Client71";
        clientsNames.add(nickName);
        this.setTitle(nickName);
        Socket s = new Socket(InetAddress.getLocalHost(), 9999);
        ps = new PrintStream(s.getOutputStream());
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        new Thread(this).start();
    }
    public void run() {
        ps.println("LOGIN#"+nickName);

        while(true){
            try{
                String s = br.readLine();
                String[] strs = s.split("#");
                if(strs[0].equals("LOGIN"))
                {
                    users.removeAll();
                    for (int i = 1; i < strs.length; i++) {
                        users.add(strs[i]);
                    }
                }

                else{
                    for (int i = 1; i < strs.length - 1; i++) {
                        if(nickName.equals(strs[i]) || strs[1].equals("ALL") ){
                            jta.append(strs[strs.length-1] + "\n" );
                            break;
                        }
                    }
                }

//                jta.append(s+ "\n");
            } catch(Exception e){}
        }
    }
    public void actionPerformed(ActionEvent e){
        String sendMSG = "MSG#";


        if(e.getSource() == sendBtn){

            if(users.getSelectedItems().length == 0){
                sendMSG = sendMSG + "ALL#";
            }
            else
            {
                sendMSG = sendMSG + nickName + "#";
                for (String sendTo : users.getSelectedItems()) {
                    sendMSG = sendMSG  + sendTo + "#";
                }
            }

            sendMSG = sendMSG +nickName + ":"+ jtf.getText();
            ps.println(sendMSG);
            jtf.setText("");
        }

//        String receiveMsg = nickName + ":" + jtf.getText();receiveMsg

    }
    public static void main(String[] args) throws Exception {
        new Client71();
    }
}
