import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
class Server4 extends JFrame implements Runnable{
    private ArrayList<ChatThread> al = new ArrayList<ChatThread>();
    private JTextArea jta = new JTextArea();
    public Server4() throws Exception{
        this.setTitle("服务器");
        Font f = new Font("宋体", Font.BOLD, 30);
        this.add(jta, BorderLayout.CENTER);		jta.setBackground(Color.yellow);
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
                ct.start();				al.add(ct);
            } catch(Exception e){}
        }
    }
    class ChatThread extends Thread{
        BufferedReader br = null;
        PrintStream ps = null;
        ChatThread(Socket s) throws Exception{
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            ps = new PrintStream(s.getOutputStream());
        }
        public void run(){
            while(true){
                try{
                    String str = br.readLine();
                    for(ChatThread ct : al){
                        ct.ps.println(str);
                    }
                }catch(Exception ex){}
            }
        }
    }

    public static void main(String[] args) throws Exception{
        new Server4();
    }
}

