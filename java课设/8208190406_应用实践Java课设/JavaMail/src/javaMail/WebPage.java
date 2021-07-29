package javaMail;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WebPage extends JFrame{

    private String Account;
    private String Password;
    JPanel jp1,jp2,jp3,jp4,jp5;
    JButton outbox,inbox;
    JLabel send,check;
    JLabel jbl1,jbl2;

    public WebPage(String account,String password) {
        this.Account = account;
        this.Password = password;

        jbl1 = new JLabel("Welcome！");
        jbl1.setPreferredSize(new Dimension(90,30));
        jbl2 = new JLabel("");
        jbl2.setPreferredSize(new Dimension(170,50));
        jp5 = new JPanel(new BorderLayout());
        jp5.add(jbl2,BorderLayout.WEST);
        jp5.add(jbl1,BorderLayout.CENTER);
        jp2 = new JPanel(new GridLayout(2,1,40,40));
        send = new JLabel("send a mail please click:");
        send.setPreferredSize(new Dimension(70,40));
        outbox = new JButton("sendMail");
        outbox.setPreferredSize(new Dimension(70,40));
        jp2.add(send);
        jp2.add(outbox);

        jp3 = new JPanel(new GridLayout(2,1,40,40));
        check = new JLabel("check a mail please click:");

        check.setPreferredSize(new Dimension(70,40));
        inbox = new JButton("checkMail");
        inbox.setPreferredSize(new Dimension(70,40));
        jp3.add(check);
        jp3.add(inbox);

        jp1 = new JPanel(new GridLayout(1,2,70,70));
        jp1.add(jp2);
        jp1.add(jp3);

        jp4 = new JPanel(new GridLayout(2,1,20,20));
        jp4.add(jp5);
        jp4.add(jp1);

        this.add(jp4);
        this.setTitle("JavaMail");
        this.setSize(400,250);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);

        outbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SendPage(Account,Password);
            }
        });

        inbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new CheckPage(Account,Password);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
    }

    private void exit(){
        int inquire = JOptionPane.showConfirmDialog(WebPage.this,"确定退出吗？","Leave",JOptionPane.YES_NO_OPTION);
        if(inquire == JOptionPane.YES_OPTION){
            System.exit(0);
        }else{
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }
    }

    public static void main(String[] args){
        new WebPage("1","1");
    }
}
