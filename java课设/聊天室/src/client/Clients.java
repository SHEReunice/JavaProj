package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Clients extends JFrame implements Runnable,ActionListener {
    //north
    private JMenuBar bar = new JMenuBar();
    private JMenu menu = new JMenu("����");
    private JMenuItem about = new JMenuItem("���ڱ����");
    private JMenuItem exit = new JMenuItem("�˳�");
    JPanel north = new JPanel();
    //west
    JPanel west = new JPanel();
    DefaultListModel<String> dl = new DefaultListModel<String>();//�����޸�JList
    private JList<String> userList = new JList<String>(dl);//����չʾ��ѡ��
    JScrollPane listPane = new JScrollPane(userList);
    //center
    JPanel center = new JPanel();
    JTextArea jta = new JTextArea(10,20);
    JScrollPane js = new JScrollPane(jta);
    JPanel operPane = new JPanel();//������Ϣ�Ĳ������
    JLabel input = new JLabel("������:");
    JTextField jtf = new JTextField(24);

    JButton jButton = new JButton("����Ϣ");

    private JButton jbt = new JButton("������Ϣ");
    private JButton jbt1 = new JButton("˽����Ϣ");
    private BufferedReader br = null;
    private PrintStream ps = null;
    private String nickName = null;

    //˽�����
    JTextArea jTextArea = new JTextArea(11,45);
    JScrollPane js1 = new JScrollPane(jTextArea);
    JTextField jTextField = new JTextField(25);
    String suser = new String();
    
    double MAIN_FRAME_LOC_X;//������x����
    double MAIN_FRAME_LOC_Y;//������y����
    
    boolean FirstSecret = true;//�Ƿ��һ��˽��
    String sender=null;//˽�ķ����ߵ�����
    String receiver=null;//˽�Ľ����ߵ�����

    public Clients() throws Exception{
        //north �˵���
        bar.add(menu);
        menu.add(about);
        menu.add(exit);
        about.addActionListener(this);
        exit.addActionListener(this);
        BorderLayout bl = new BorderLayout();
        north.setLayout(bl);
        north.add(bar,BorderLayout.NORTH);
        add(north,BorderLayout.NORTH);
        
        //east �����б�
        Dimension dim = new Dimension(100,150);
        west.setPreferredSize(dim);//��ʹ���˲��ֹ���������setPreferredSize�����ô��ڴ�С
        //Dimension dim2 = new Dimension(100,150);
        //listPane.setPreferredSize(dim2);
        BorderLayout bl2 = new BorderLayout();
        west.setLayout(bl2);
        west.add(listPane,BorderLayout.CENTER);//��ʾ�����б�
        add(west,BorderLayout.EAST);
        userList.setFont(new Font("����",Font.BOLD,18));
        
        //center ������Ϣ��  ������Ϣ�������
        jta.setEditable(false);//��Ϣ��ʾ���ǲ��ܱ༭��
        jTextArea.setEditable(false);

        BorderLayout bl3 = new BorderLayout();
        center.setLayout(bl3);
        FlowLayout fl = new FlowLayout(FlowLayout.LEFT);
        operPane.setLayout(fl);
        operPane.add(input);
        operPane.add(jtf);
        operPane.add(jbt);
        operPane.add(jbt1);
        center.add(js,BorderLayout.CENTER);//js����Ϣչʾ��JScrollPane
        center.add(operPane,BorderLayout.SOUTH);
        add(center,BorderLayout.CENTER);

        js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);//��Ҫʱ����ʾ������

        //����¼������
        jbt.addActionListener(this);
        jbt1.addActionListener(this);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //this.setAlwaysOnTop(true);

        nickName = JOptionPane.showInputDialog("�û�����");
        this.setTitle(nickName + "��������");
        this.setSize(700,400);
        this.setVisible(true);

        Socket s = new Socket("127.0.0.1", 9999);
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        ps = new PrintStream(s.getOutputStream());
        new Thread(this).start();//run()
        ps.println("LOGIN#" + nickName);//���͵�¼��Ϣ����Ϣ��ʽ��LOGIN#nickName
        
        jtf.setFocusable(true);//���ý���
        
        //�����¼���ʵ�ֵ�����Ҫ���͵����ݺ�ֱ�Ӱ��س�����ʵ�ַ���
        //����������Ӧ�Ŀؼ������ǻ�ý��㣨focus��������²���������
       jtf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    ps.println("MSG#" + nickName + "#" +  jtf.getText());//������Ϣ�ĸ�ʽ��MSG#nickName#message
                    //��������������������Ϊ��
                    jtf.setText("");
                }
            }
        });
       
       //˽����Ϣ�򰴻س�������Ϣ
        jTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleSS();
                }
            }
        });
        
        //����ϵͳ�ر��¼����˳�ʱ���������˷���ָ����Ϣ
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ps.println("OFFLINE#" + nickName);//����������Ϣ����Ϣ��ʽ��OFFLINE#nickName
            }
        });
        
        this.addComponentListener(new ComponentAdapter() {//���������ڴ�С�ĸı�     
            public void componentMoved(ComponentEvent e) {
                Component comp = e.getComponent();							
                MAIN_FRAME_LOC_X = comp.getX();
                MAIN_FRAME_LOC_Y = comp.getY();			
            }
        }); 
    }
    
    public void run(){//�ͻ�����������˷���Ϣ���߳�
        while (true){
            try{
                String msg = br.readLine();//��ȡ�������Ƿ�������Ϣ���ÿͻ���
                String[] strs = msg.split("#");
                //�ж��Ƿ�Ϊ�����������ĵ�½��Ϣ
                if(strs[0].equals("LOGIN")){
                    if(!strs[1].equals(nickName)){//���Ǳ��˵�������Ϣ����ʾ�����˵Ĳ���ʾ
                        jta.append(strs[1] + "��������\n");
                        dl.addElement(strs[1]);//DefaultListModel������JList������
                        userList.repaint();
                    }
                }else if(strs[0].equals("MSG")){//�ӵ�������������Ϣ����Ϣ
                    if(!strs[1].equals(nickName)){//����˵��
                        jta.append(strs[1] + "˵��" + strs[2] + "\n");
                    }else{
                        jta.append("��˵��" + strs[2] + "\n");
                    }
                }else if(strs[0].equals("USERS")){//USER��Ϣ��Ϊ�½����Ŀͻ��˸��º����б�
                    dl.addElement(strs[1]);
                    userList.repaint();
                } else if(strs[0].equals("ALL")){
                    jta.append("ϵͳ��Ϣ��" + strs[1] + "\n");
                }else if(strs[0].equals("OFFLINE")){
                	if(strs[1].equals(nickName)) {//������Լ����ߵ���Ϣ��˵�������������߳������ң�ǿ������
                        javax.swing.JOptionPane.showMessageDialog(this, "���ѱ�ϵͳ��������ң�");
                        System.exit(0);
                	}
                    jta.append(strs[1] + "��������\n");
                    dl.removeElement(strs[1]);
                    userList.repaint();
                }else if((strs[2].equals(nickName) || strs[1].equals(nickName)) && strs[0].equals("SMSG")){
                	if(!strs[1].equals(nickName)){
                    	jTextArea.append(strs[1] + "˵��" + strs[3] + "\n");
                        jta.append("ϵͳ��ʾ��" + strs[1] + "˽������" + "\n");
                    }else{
                        jTextArea.append("��˵��" + strs[3] + "\n");
                    }
                }else if((strs[2].equals(nickName) || strs[1].equals(nickName)) && strs[0].equals("FSMSG"))
                {
                	sender = strs[1];
                	receiver = strs[2];
                	//���շ���һ���յ�˽����Ϣ���Զ�����˽�Ĵ���
                	if(!strs[1].equals(nickName)) {
                		FirstSecret = false;
                		jTextArea.append(strs[1] + "˵��" + strs[3] + "\n");
                		jta.append("ϵͳ��ʾ��" + strs[1] + "˽������" + "\n");
                		handleSec(strs[1]);
                	}
                	else {
                		jTextArea.append("��˵��" + strs[3] + "\n");
                	}
                }
            }catch (Exception ex){//����������˳������⣬��ͻ���ǿ������
            	javax.swing.JOptionPane.showMessageDialog(this, "���ѱ�ϵͳ��������ң�");
                System.exit(0);
            }
        }
    }

    
    @Override
    public void actionPerformed(ActionEvent e) {//������¼�
        String label = e.getActionCommand();
        if(label.equals("������Ϣ")){//Ⱥ��
            handleSend();
        }else if(label.equals("˽����Ϣ") && !userList.isSelectionEmpty()){//δ����û���ִ��
            suser = userList.getSelectedValuesList().get(0);//��ñ�ѡ����û�
            handleSec(suser);//����˽�Ĵ���
            sender = nickName;
            receiver = suser;
        }else if(label.equals("����Ϣ")){
            handleSS();//˽����Ϣ
        }else if(label.equals("���ڱ����")){
            JOptionPane.showMessageDialog(this, "1.������������н���Ⱥ��\n\n2.���Ե��ѡ���û�����˽��");
        }else if(label.equals("�˳�")){
            JOptionPane.showMessageDialog(this, "���ѳɹ��˳���");
            ps.println("OFFLINE#" + nickName);
            System.exit(0);
        } else{
            System.out.println("��ʶ����¼�");
        }
    }

    public void handleSS(){//��˽�Ĵ����з���Ϣ
    	String name=sender;
    	if(sender.equals(nickName)) {
    		name = receiver;
    	}
    	if(FirstSecret) {
    		ps.println("FSMSG#" + nickName + "#" + name + "#" + jTextField.getText());
        	jTextField.setText(""); 
        	FirstSecret = false;
    	}
    	else {
    		ps.println("SMSG#" + nickName + "#" + name + "#" + jTextField.getText());
    		jTextField.setText("");
    	} 	     
    }

    public void handleSend(){//Ⱥ����Ϣ
        //������Ϣʱ��ʶһ����Դ
        ps.println("MSG#" + nickName + "#" +  jtf.getText());
        //��������������������Ϊ��
        jtf.setText("");
    }

    public void handleSec(String name){ //����˽�Ĵ���
        JFrame jFrame = new JFrame();//�½���һ������      
        JPanel JPL = new JPanel();
        JPanel JPL2 = new JPanel();
        FlowLayout f2 = new FlowLayout(FlowLayout.LEFT);
        JPL.setLayout(f2);
        JPL.add(jTextField);
        JPL.add(jButton);
        JPL2.add(js1,BorderLayout.CENTER);
        JPL2.add(JPL,BorderLayout.SOUTH);
        jFrame.add(JPL2);

        jButton.addActionListener(this);
        jTextArea.setFont(new Font("����", Font.PLAIN,15));
        jFrame.setSize(400,310);
        jFrame.setLocation((int)MAIN_FRAME_LOC_X+20,(int)MAIN_FRAME_LOC_Y+20);//��˽�Ĵ������������ڸ����ڵ��м䵯��
        jFrame.setTitle("��" + name + "˽����");
        jFrame.setVisible(true);

        jTextField.setFocusable(true);//���ý���
        
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	jTextArea.setText("");
            	FirstSecret = true;
            }
        });
    }//˽�Ĵ���

    public static void main(String[] args)throws Exception{
        new Clients();
    }
}

