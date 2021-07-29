import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.List;

class Client5 extends JFrame implements ActionListener,Runnable{
    private JTextField jtf = new JTextField();
    private JTextArea jta = new JTextArea();
    private List users = new List();
    private PrintStream ps = null;
    private BufferedReader br = null;
    private String nickName;
    public Client5() throws Exception{
        this.add(jtf, BorderLayout.SOUTH);
        jtf.addActionListener(this);
        Font f = new Font("宋体", Font.BOLD, 30);
        this.add(jta, BorderLayout.CENTER);
        jta.setBackground(Color.orange);
        this.add(users,BorderLayout.EAST);
        jta.setFont(f);
        jtf.setFont(f);
        users.setFont(f);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setSize(600,400);
        this.setVisible(true);
        nickName = JOptionPane.showInputDialog("请您输入昵称");
        this.setTitle(nickName);
        Socket s = new Socket(InetAddress.getLocalHost(), 9999);
        ps = new PrintStream(s.getOutputStream());
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        ps.println("LOGIN#" + nickName);
        new Thread(this).start();
    }
    public void run() {
        while(true){
            try{
                String s = br.readLine();
                String[] strs = s.split("#");
                if(strs[0].equals("LOGIN")){
                    //	users.clear();
                    //	for(int i=1;i<strs.size;i++){
                    //		users.add(strs[i]);
                    //	}
                }
                else{
                    jta.append(strs[1] + "\n");
                }
            } catch(Exception e){}
        }
    }
    public void actionPerformed(ActionEvent e){
        String str = "MSG#" + nickName + "说:" + jtf.getText();
        ps.println(str);
    }
    public static void main(String[] args) throws Exception{
        new Client5();
    }
}

