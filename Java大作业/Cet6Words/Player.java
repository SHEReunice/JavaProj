package Words;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class Player extends JFrame implements ActionListener,Runnable,KeyListener{
	//从这里开始，都是取词、挖空白
		static private ArrayList <String> wordsList = new ArrayList();
		Random rnd = new Random();  //用java.util里面的random类取随机数
		String word; //取的单词
		private String explain;  //单词释义
		int blank1,blank2;//空白
		private StringBuffer absentWord;  //挖空后的单词
		
		public void getWords() throws IOException{
			do {
				int line = rnd.nextInt(2088);
				String words[] = wordsList.get(line).split("#");
				word = words[0];
				explain = words[1];
			}while(word.length()<5);
			blank1 = rnd.nextInt(word.length()-1);
			do {
				blank2 = rnd.nextInt(word.length()-1);
			}while(blank1 == blank2); //要两个不相等
			absentWord = new StringBuffer(word);
			absentWord.replace(blank1, blank1+1, "_");
			absentWord.replace(blank2, blank2+1, "_");
		}
		public int fillWord(char a) { //判断填的空对不对
			if(absentWord.charAt(blank1) == '_' && word.charAt(blank1) == a)
			{
				absentWord.replace(blank1, blank1+1, String.valueOf(a));
				return blank1;
			}
			else if(absentWord.charAt(blank2) == '_' && word.charAt(blank2) == a)
			{
				absentWord.replace(blank2, blank2+1,  String.valueOf(a));
				return blank2;
			}
			else
				return -1;
		}
		public boolean compareWord() {
			if(word.compareTo(absentWord.toString()) == 0)//StringBuffer转String
				return true;
			else
				return false;
		}
	private JPanel panel;
	private JButton begin;
	private String name;
	PrintStream ps; //输出流
	Thread thread;
	boolean start = false;//开始标志
	boolean Right = true;
	JLabel wordLabel;  //掉下来的单词
	JLabel explainLabel;  //中文释义
	JLabel lifeLabel; //生命值
	int life;
	int x,y;
	int check; //判断输入是否正确
	Socket s;
	FileOutputStream fos;   //文件输出流
	File right;
	File wrong;
	File unfilled;
	Player() throws Exception{
		String str;
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("E:/word.txt"),"UTF-8"));
		while((str = br.readLine() )!= null)
			wordsList.add(str);
		life = 20;
		name = javax.swing.JOptionPane.showInputDialog("请输入你的游戏昵称:");
		right = new File("E:/" + name + "right.txt");//打开文件
		wrong = new File("E:/" + name + "wrong.txt");
		unfilled = new File("E:/" + name + "unfilled.txt");
		s = new Socket("127.0.0.1",6543);
		this.setTitle(name);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setSize(600, 800);
		panel = new JPanel();
		this.add(panel);
		panel.setSize(600, 800);
		panel.setLayout(null);
		panel.setVisible(true);
		this.setVisible(true);
		begin = new JButton("开始");
		begin.setSize(150, 50);
		begin.addActionListener(this);//给按钮加功能，actionPerformed
		panel.add(begin);
		begin.setLocation(300,400);
		ps = new PrintStream(s.getOutputStream());//获取套接字中的输出流
		this.setVisible(true);
		thread = new Thread(this); //开启了一个线程执行run

	}
	@Override
	public void actionPerformed(ActionEvent e) {
		begin.setVisible(false);
		new Thread(new startSign()).start();  //开启一个线程给服务器传游戏开始的消息
		new Thread(new communicate(s)).start(); //开启一个线程让服务器和客户端交流
	}
	
	
	
	@Override
	public void run() {//游戏进行的代码
		panel.addKeyListener(this);//键盘监听
		panel.requestFocus();
		wordLabel = new JLabel();
		wordLabel.setSize(150,50);
		panel.add(wordLabel);
		explainLabel = new JLabel();
		explainLabel.setSize(150,50);
		panel.add(explainLabel);
		explainLabel.setLocation(250,this.getHeight()-100);
		lifeLabel = new JLabel();
		lifeLabel.setSize(150, 50);
		panel.add(lifeLabel);
		lifeLabel.setText(String.valueOf(life));
		while(start&&life>0) {
			check = 0; 
			try {
				getWords();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			wordLabel.setText(absentWord.toString());
			explainLabel.setText(explain);
			
			x = 250;
			y = 0;
			wordLabel.setLocation(x,y);
			while(y < panel.getHeight()&&start&&check ==0) {
				y += 1;
				wordLabel.setLocation(x, y);
				try {
					Thread.sleep(10); //位置改变的时间间隔
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(check == 0) {
				life --;//不输入掉下来也扣分
				lifeLabel.setText(String.valueOf(life));
				try {
					fos = new FileOutputStream(unfilled,true);
					fos.write((word + "\n").getBytes());
					fos.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		wordLabel.setVisible(false);
		explainLabel.setVisible(false);
		JLabel gameover = new JLabel("游戏结束");
		gameover.setSize(250, 100);
		gameover.setFont(new Font("宋体",Font.PLAIN,16));
		panel.add(gameover);
		gameover.setLocation(200,300);
		panel.setEnabled(false);//游戏结束不能操作了
		new Thread(new endSign()).start();//开启一个线程告诉服务器游戏结束
	}

	class communicate implements Runnable{  //和服务器交流
		Socket s;
		BufferedReader br;
		communicate(Socket s){
			this.s = s;
			try {
				br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			String info = null;
			while(true) {
				try {
					info = br.readLine(); //读服务器传来的消息
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(info.compareTo("START") == 0) {//收到开始信号
					start = true;
					thread.start();//开始游戏
				} else if(info.compareTo("END") == 0) {//收到结束信号
					start = false;
				}else if(info.compareTo("UP") == 0) { //加分
						if(Right) {
							life++;
							lifeLabel.setText(String.valueOf(life));
						}
						else
							Right = true;//自己输错了给对方加分
				}
			}
		}
	}
	class startSign implements Runnable{

		@Override
		public void run() {
			ps.println("START#PLAYER");//传给服务器
		}
	}
	class wrongSign implements Runnable{

		@Override
		public void run() {
			ps.println("HEALTH#-1");
		}
	}
	class endSign implements Runnable{

		@Override
		public void run() {
			ps.println("HEALTH#0");
		}
	}
	public static void main(String arg[]) throws Exception {
		new Player();
	}
	
	class character implements Runnable{
		char c;
		JLabel charLabel;
		character(char c){
			this.c = c;
		}
		@Override
		public void run() {
			int Y = panel.getHeight();
			charLabel = new JLabel();
			charLabel.setText(String.valueOf(c));
			charLabel.setSize(50,50);
			panel.add(charLabel);
			charLabel.setForeground(Color.red);
			charLabel.setLocation(x,Y);
			while(charLabel.getY() > wordLabel.getY()) {
				Y -= 4;
				charLabel.setLocation(x,Y);
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(fillWord(c) == -1) {
				life--;
				lifeLabel.setText(String.valueOf(life));
				check = -1;
				Right = false;
				try {
					fos = new FileOutputStream(wrong,true);
					fos.write((word + "\n").getBytes());
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				new Thread(new wrongSign()).start();//告诉服务器我输错了
			}else {
				wordLabel.setText(absentWord.toString());
				if(compareWord()) {
					life++;
					lifeLabel.setText(String.valueOf(life));
					check = 1;
					try {
						fos = new FileOutputStream(right,true);
						fos.write((word + "\n").getBytes());
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			charLabel.setVisible(false);
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		char c = e.getKeyChar();
		if(c <= 'z'&&c >= 'a'||c <= 'Z'&&c >= 'A')
			new Thread(new character(c)).start(); //开启一个线程飞字母
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}