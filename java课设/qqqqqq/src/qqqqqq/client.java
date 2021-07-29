package qqqqqq;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class client extends JFrame implements Runnable,ActionListener {

    //north
    private JMenuBar bar = new JMenuBar();
    private JMenu menu = new JMenu("关于");
    private JMenuItem about = new JMenuItem("关于本软件");
    private JMenuItem exit = new JMenuItem("退出");
    JPanel north = new JPanel();
    //west
    JPanel west = new JPanel();
    DefaultListModel<String> dl = new DefaultListModel<String>();
    private JList<String> userList = new JList<String>(dl);
    JScrollPane listPane = new JScrollPane(userList);
    //center
    JPanel center = new JPanel();
    JTextArea jta = new JTextArea(10,20);
    JScrollPane js = new JScrollPane(jta);
    JPanel operPane = new JPanel();
    JLabel input = new JLabel("请输入:");
    JTextField jtf = new JTextField(24);

    JButton jButton = new JButton("发消息");


    private JButton jbt = new JButton("发送消息");
    private JButton jbt1 = new JButton("私发消息");
    private BufferedReader br = null;
    private PrintStream ps = null;
    private String nickName = null;

    JTextArea jTextArea = new JTextArea();
    JTextField jTextField = new JTextField();
    String suser = new String();
    boolean flag = false;

    public client() throws Exception{
        //north
        bar.add(menu);
        menu.add(about);
        menu.add(exit);
        about.addActionListener(this);
        exit.addActionListener(this);
        BorderLayout bl = new BorderLayout();
        north.setLayout(bl);
        north.add(bar,BorderLayout.NORTH);
        add(north,BorderLayout.NORTH);
        //west
        Dimension dim = new Dimension(100,150);
        west.setPreferredSize(dim);
        Dimension dim2 = new Dimension(100,300);
        listPane.setPreferredSize(dim2);
        BorderLayout bl2 = new BorderLayout();
        west.setLayout(bl2);
        west.add(listPane,BorderLayout.CENTER);//显示好友列表哈哈
        add(west,BorderLayout.EAST);
        userList.setFont(new Font("隶书",Font.BOLD,18));
        //center
        jta.setEditable(false);
        jtf.setBackground(Color.pink);

        BorderLayout bl3 = new BorderLayout();
        center.setLayout(bl3);
        FlowLayout fl = new FlowLayout(FlowLayout.LEFT);
        operPane.setLayout(fl);
        operPane.add(input);
        operPane.add(jtf);
        operPane.add(jbt, BorderLayout.WEST);
        operPane.add(jbt1, BorderLayout.EAST);
        center.add(js,BorderLayout.CENTER);
        center.add(operPane,BorderLayout.SOUTH);
        add(center,BorderLayout.CENTER);

        js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        //鼠标事件，点击
        jbt.addActionListener(this);
        jbt1.addActionListener(this);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //this.setAlwaysOnTop(true);

        nickName = JOptionPane.showInputDialog("用户名：");
        this.setTitle(nickName + "的聊天室");
        this.setSize(600,400);
        this.setVisible(true);

        Socket s = new Socket("127.0.0.1", 9999);
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        ps = new PrintStream(s.getOutputStream());
        new Thread(this).start();
        ps.println("LOGIN#" + nickName);

        //键盘事件，实现当输完要发送的内容后，直接按回车键，实现发送
        //监听键盘相应的控件必须是获得焦点（focus）的情况下才能起作用
        jtf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    ps.println("MSG#" + nickName + "#" +  jtf.getText());
                    //发送完后，是输入框中内容为空
                    jtf.setText("");
                }
            }
        });

        jtf.setFocusable(true);

        //监听系统关闭事件，退出时给服务器端发出指定消息
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ps.println("OFFLINE#" + nickName);
            }
        });

    }
    public void run(){
        while (true){
            try{
                String msg = br.readLine();
                String[] strs = msg.split("#");
                //判断是否为服务器发来的登陆信息
                if(strs[0].equals("LOGIN")){
                    if(!strs[1].equals(nickName)){
                        jta.append(strs[1] + "上线啦！\n");
                        dl.addElement(strs[1]);
                        userList.repaint();
                    }
                }else if(strs[0].equals("MSG")){
                    if(!strs[1].equals(nickName)){
                        jta.append(strs[1] + "说：" + strs[2] + "\n");
                    }else{
                        jta.append("我说：" + strs[2] + "\n");
                    }
                }else if(strs[0].equals("USERS")){
                    dl.addElement(strs[1]);
                    userList.repaint();
                } else if(strs[0].equals("ALL")){
                    jta.append("系统消息：" + strs[1] + "\n");
                }else if(strs[0].equals("OFFLINE")){
                    jta.append(strs[1] + "下线啦！\n");
                    dl.removeElement(strs[1]);
                    userList.repaint();
                }else if((strs[2].equals(nickName) || strs[1].equals(nickName)) && strs[0].equals("SMSG")){
                    if(!strs[1].equals(nickName)){
                        jTextArea.append(strs[1] + "说：" + strs[3] + "\n");
                    }else{
                        jTextArea.append("我说：" + strs[3] + "\n");
                    }
                }else if((strs[2].equals(nickName) || strs[1].equals(nickName))&& strs[0].equals("FSMSG")){//只有发信人和私信人能看（第一次）
                    if(strs[2].equals(nickName)){//如果被私信人是自己则显示系统消息
                        jTextArea.append(strs[1] + "说：" + strs[3] + "\n");
                        jta.append("系统提示：" + strs[1] + "私信了你" + "\n");
                    }else{//若自己为发信人
                        jTextArea.append( "我说：" + strs[3] + "\n");
                    }
                }
            }catch (Exception ex){
                ex.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(this, "您已被系统请出聊天室！");
                ps.println("OFFLINE#" + nickName);
                System.exit(0);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String label = e.getActionCommand();
        if(label.equals("发送消息")){//群发
            handleSend();
        }else if(label.equals("私发消息") && !userList.isSelectionEmpty()){//未点击用户不执行
            suser = userList.getSelectedValuesList().get(0);
            handleSec();
        }else if(label.equals("发消息")){
            handleSS();
        }else if(label.equals("关于本软件")){
            JOptionPane.showMessageDialog(this, "世界上最可爱的人制作");
        }else if(label.equals("退出")){
            JOptionPane.showMessageDialog(this, "您已成功退出！");
            ps.println("OFFLINE#" + nickName);
            System.exit(0);
        } else{
            System.out.println("不识别的事件");
        }
    }


    public void handleSS(){
        if(flag){
            ps.println("SMSG#" + nickName + "#" + suser + "#" + jTextField.getText());
            jTextField.setText("");
        }else{//首次私信格式为"FSMSG#  发信人  # 收信人 # 内容
            ps.println("FSMSG#" + nickName + "#" + suser + "#" + jTextField.getText());
            jTextField.setText("");
            flag = true;
        }

    }//私聊中

    public void handleSend(){
        //发送信息时标识一下来源
        ps.println("MSG#" + nickName + "#" +  jtf.getText());
        //发送完后，是输入框中内容为空
        jtf.setText("");
    }//群聊

    public void handleSec(){
        JFrame jFrame = new JFrame();

        jFrame.add(jButton, BorderLayout.SOUTH);
        jFrame.add(jTextField, BorderLayout.NORTH);
        jFrame.add(jTextArea,BorderLayout.CENTER);

        jButton.addActionListener(this);
        jTextArea.setFont(new Font("宋体", Font.PLAIN,15));
        jTextField.setBackground(Color.pink);
        jFrame.setSize(400,300);
        jFrame.setLocation(400,150);
        jFrame.setTitle("与" + userList.getSelectedValuesList().get(0) + "私聊中");
        jFrame.setVisible(true);
        jFrame.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {

            }

            public void windowClosing(WindowEvent e) {
                flag = false;
                //JOptionPane.showMessageDialog(this, "您已成功退出！");
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }//私聊窗口

    public static void main(String[] args)throws Exception{
        new client();
    }
}




