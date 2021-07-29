package B1;

import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
class Server6 extends JFrame implements Runnable{
    private ArrayList<ChatThread> al = new ArrayList<ChatThread>();
    static public ArrayList<String> clientsNames = new ArrayList<String>();
    private JTextArea jta = new JTextArea();
    public Server6() throws Exception{
        this.setTitle("服务器");
        Font f = new Font("宋体", Font.BOLD, 30);
        this.add(jta, BorderLayout.CENTER);
        jta.setBackground(Color.yellow);
        jta.setFont(f);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setSize(300,600);
        this.setVisible(true);
        new Thread(this).start();
    }
    public void run() {
        while(true){
            try{
                ServerSocket ss = new ServerSocket(9999);
                Socket s = ss.accept(); //等待函数（阻塞，除非有人连上）
                ChatThread ct = new ChatThread(s);
                ct.start();
                al.add(ct);
            } catch(Exception e){}
        }
    }


    class ChatThread extends Thread{
        BufferedReader br = null;
        PrintStream ps = null;
        String nickName;
        ChatThread(Socket s) throws Exception{
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            ps = new PrintStream(s.getOutputStream());
        }
        public void run(){
            while(true){
                try{
                    String str = br.readLine();
                    String[] strs = str.split("#");

                    //服务器收到LOGIN消息后，告知每一个客户端现在系统已经有的客户端
                    if(strs[0].equals("LOGIN")){
                        Server6.clientsNames.add(strs[1]);
                        for(ChatThread ct : al){
                            String tellTheClients = "LOGIN";
                            for(String name : Server6.clientsNames){
                                tellTheClients = tellTheClients + "#" + name;
                            }
                            tellTheClients = tellTheClients + "#";
                            ct.ps.println(tellTheClients);
                        }
                    }

                    //服务器收到MSG消息后，直接将消息转发给各个客户端
                    else if (strs[0].equals("MSG")){
                        for(ChatThread ct : al){
//                            jta.append(str + "\n");
                            ct.ps.println(str);
                        }
                    }

                }catch(Exception ex){}
            }
        }
    }

    public static void main(String[] args) throws Exception{
        new Server6();
    }
}

