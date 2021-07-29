package javaMail;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Login_view extends JFrame {
    JTextField account;
    JPasswordField password;
    JLabel jbl1;
    JButton Account,Password;
    JPanel login,jp1,jp2,jp3;
    JButton enter;

    public static void main(String[] args){
        Login_view login = new Login_view();
    }

    public Login_view(){
        jbl1 = new JLabel(new ImageIcon("src//image//tou.png"));
        login = new JPanel(new GridLayout(3,1,20,10));
        Account = new JButton("账号: ");
        Password = new JButton("密码: ");
        enter = new JButton("登录");
        account = new JTextField();
        account.requestFocusInWindow();
        jp1 = new JPanel(new BorderLayout());
        jp1.add(Account,BorderLayout.WEST);
        jp1.add(account,BorderLayout.CENTER);
        password = new JPasswordField();
        jp2 = new JPanel(new BorderLayout());
        jp2.add(Password,BorderLayout.WEST);
        jp2.add(password,BorderLayout.CENTER);
        jp3 = new JPanel();
        jp3.add(enter);

        login.add(jp1);
        login.add(jp2);
        login.add(jp3);

        this.add(jbl1,BorderLayout.NORTH);
        this.add(login,BorderLayout.CENTER);
        this.setTitle("JavaMail");
        this.setSize(400,600);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });

    }

    public void login(){
        if(account.getText().length() == 0 || password.getPassword().length == 0){
            JOptionPane.showMessageDialog(Login_view.this,"账号或密码不能为空！","ERROR",JOptionPane.ERROR_MESSAGE);
            if(account.getText().length() != 0){
                password.requestFocusInWindow();
            }else{
                account.requestFocusInWindow();
            }
            return;
        }
        String format = "^(\\w*|\\d*)@\\w*.(com|cn)$";
        if(!(account.getText().matches(format))){
            JOptionPane.showMessageDialog(Login_view.this,"账号格式错误","ERROR",JOptionPane.ERROR_MESSAGE);
            account.requestFocusInWindow();
            return;
        }
        Login_view.this.dispose();
        new WebPage(account.getText(),new String(password.getPassword()));
    }

    private void exit(){
        int inquire = JOptionPane.showConfirmDialog(Login_view.this,"确定退出吗？","Leave",JOptionPane.YES_NO_OPTION);
        if(inquire == JOptionPane.YES_OPTION){
            System.exit(0);
        }else{
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }
    }
}
