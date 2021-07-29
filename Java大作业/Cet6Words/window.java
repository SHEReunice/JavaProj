package Words;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.*;

public class window extends JFrame {
	private ServerSocket ss;
	Socket []s = new Socket [2];
	private int Num = 0;
	int startNum = 0;
	window(){
		this.setTitle("服务器");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(500, 500);
		this.setLayout(null);
		this.setVisible(true);
		try {
			ss = new ServerSocket(6543);//端口
		} catch (IOException e) {
			e.printStackTrace();
		}
		while(Num < 2) {
			try {
				s[Num] = ss.accept();
				new Thread(new communicate(s[Num])).start();//跟客户端交流
			} catch (IOException e) {
				e.printStackTrace();
			}
			Num++;
		}
	}
	class communicate implements Runnable{//接收客户端信息
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
			String info;
			String str[] = null;
			while(true) {
				try {
					info = br.readLine();
					str = info.split("#");
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(str[0].compareTo("START") == 0) {//收到开始信号
					startNum ++;
					if(startNum == 2)
						new Thread(new startSign()).start();
				} else if(str[0].compareTo("END") == 0) {//收到结束信号
					
				}else if(str[0].compareTo("HEALTH") == 0) {//收到生命信息
					if(str[1].compareTo("-1") == 0) {
						new Thread(new upSign()).start();
					}else if(str[1].compareTo("0") == 0){
						new Thread(new endSign()).start();
					}
				}
			}
		}
	}
	class startSign implements Runnable{

		@Override
		public void run() {
			try {
				for(int time = 0;time<2;time++)
					new PrintStream(s[time].getOutputStream()).println("START");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	class endSign implements Runnable{

		@Override
		public void run() {
			try {
				for(int time = 0;time<2;time++)
					new PrintStream(s[time].getOutputStream()).println("END");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	class upSign implements Runnable{

		@Override
		public void run() {
			try {
				for(int time = 0;time<2;time++)
					new PrintStream(s[time].getOutputStream()).println("UP");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String arg[]) {
		new window();
	}
}