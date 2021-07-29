package javaMail;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.sound.midi.Receiver;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;


public class SendPage extends JFrame implements ActionListener{
    private String Account;
    private String Password;
    private Session session;
    private Transport transport;
    JPanel jpnorth,jpcenter,jp1,jp2,jpsouth,jp3,jp4,jp5,jpall,jp6,jp7;
    JButton jb1,jb2,jb3,jb4,jb5,jb6;
    JTextField jt1,jt2;
    JTextArea jta;
    JLabel jbl2,jbl3;
    JButton jbl1;
    private boolean hasAttach = false;
    private ArrayList<String> fileName = new ArrayList<String>();

    public SendPage(String account,String password){
        this.Account = account;
        this.Password = password;

        jbl1 = new JButton("Send a email");

        jbl2 = new JLabel("");

        jpnorth = new JPanel(new BorderLayout());
        jpnorth.add(jbl2,BorderLayout.WEST);
        jpnorth.add(jbl1,BorderLayout.CENTER);

        jb1 = new JButton("收件人:");

        jt1 = new JTextField();
        jb2 = new JButton("主题:");
        jt2 = new JTextField();
        jp6 = new JPanel(new GridLayout(2,1,10,10));

        jb3 = new JButton("正文:");

        jb4 = new JButton("添加附件");
        jb4.addActionListener((ActionListener) this);

        jb5 = new JButton("发送");
        jb5.addActionListener((ActionListener) this);

        jp1 = new JPanel(new BorderLayout());
        jp2 = new JPanel(new BorderLayout());
        jp1.add(jb1,BorderLayout.WEST);
        jp1.add(jt1,BorderLayout.CENTER);
        jp2.add(jb2,BorderLayout.WEST);
        jp2.add(jt2,BorderLayout.CENTER);
        jp6.add(jp1);
        jp6.add(jp2);
        jp3 = new JPanel(new GridLayout(8,1,10,10));
        jp3.add(jb3);
        jp3.add(jb4);
        jp3.add(jb5);
        jta = new JTextArea();
        jta.setPreferredSize(new Dimension(660,500));
        jp4 = new JPanel();
        jp4.add(jta);
        jp7 = new JPanel(new BorderLayout());
        jp7.add(jp3,BorderLayout.WEST);
        jp7.add(jp4,BorderLayout.CENTER);
        jpcenter = new JPanel(new BorderLayout());
        jpcenter.add(jp6,BorderLayout.NORTH);
        jpcenter.add(jp7,BorderLayout.CENTER);




        jb6 = new JButton("退出");
        jb6.addActionListener((ActionListener) this);

        jbl3 = new JLabel("");

        jp5 = new JPanel(new BorderLayout());
        jp5.add(jb6,BorderLayout.CENTER);
        jp5.add(jbl3,BorderLayout.WEST);

        jpsouth = new JPanel();
        jpsouth.add(jp5);

        jpall = new JPanel(new BorderLayout());


        this.setTitle("JavaMail");
        this.setSize(800,600);
        jpall.add(jpnorth,BorderLayout.NORTH);
        jpall.add(jpcenter,BorderLayout.CENTER);
        jpall.add(jpsouth,BorderLayout.SOUTH);
        this.add(jpall);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);

        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                exit();
            }
        });
    }
    public static void main(String[] args){
        new SendPage("1","1");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton j = (JButton) e.getSource();
        if (j == jb6) {
            exit();
        }else if(j == jb5){

            if(hasAttach){
                try {
                    SendMailPro();
                } catch (MessagingException messagingException) {
                    messagingException.printStackTrace();
                }
            }else{
                try {
                    SendMail();
                } catch (MessagingException messagingException) {
                    messagingException.printStackTrace();
                }
            }
        }else if(j == jb4){
            try {
                AppendAttach();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }
    }

    public void exit(){
            int inquire = JOptionPane.showConfirmDialog(SendPage.this,
                    "确定退出？","Leave",
                    JOptionPane.YES_NO_OPTION);
            if(inquire==JOptionPane.YES_OPTION)
            {
                this.dispose();
            }
            else
            {
                this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            }
        }

        private void SendMail() throws MessagingException {

            Properties pro = new Properties();
            pro.put("mail.transport.protocol","smtp");
            pro.put("mail.smtp.class","com.sun.mail.smtp.SMTPTransport");
            pro.put("mail.smtp.host","smtp.qq.com");
            /**SMTP port.*/
            pro.put("mail.smtp.port","25");

            /**Verify account.*/
            pro.put("mail.smtp.auth","true");
            session = Session.getInstance(pro, new Authenticator()
            {
                public PasswordAuthentication getPasswordAuthentication()
                {
                    return new PasswordAuthentication(Account, Password);
                }
            });

            transport = session.getTransport();

            Session NewSession = Session.getDefaultInstance(new Properties());
            MimeMessage message = new MimeMessage(NewSession);
            message.setFrom(new InternetAddress(Account));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(jt1.getText()));
            message.setSentDate(new Date());
            message.setSubject(jt2.getText());
            message.setText(jta.getText());
            message.saveChanges();

            transport.connect();
            transport.sendMessage(message, message.getAllRecipients());

            JOptionPane.showMessageDialog(SendPage.this, "邮件发送成功！");
            jt1.setText("");
            jt2.setText("");
            jta.setText("");

            transport.close();
    }

    public void AppendAttach() throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        if(fileChooser.showOpenDialog(SendPage.this) == JFileChooser.APPROVE_OPTION){
            String fileAddr = fileChooser.getSelectedFile().getCanonicalPath();
            if(fileAddr != null && fileAddr.length()!=0){
                hasAttach = true;
                fileName.add(fileAddr);
            }
        }
    }

    private void SendMailPro() throws MessagingException {
        Properties pro = new Properties();
        pro.put("mail.transport.protocol","smtp");
        pro.put("mail.smtp.class","com.sun.mail.smtp.SMTPTransport");
        pro.put("mail.smtp.host","smtp.qq.com");
        /**SMTP port.*/
        pro.put("mail.smtp.port","25");

        /**Verify account.*/
        pro.put("mail.smtp.auth","true");
        session = Session.getInstance(pro, new Authenticator()
        {
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(Account, Password);
            }
        });

        transport = session.getTransport();
        Session NewSession = Session.getDefaultInstance(new Properties());
        MimeMessage message = new MimeMessage(NewSession);
        message.setFrom(new InternetAddress(Account));
        message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(jt1.getText()));
        message.setSentDate(new Date());
        message.setSubject(jt2.getText());

        MimeBodyPart ContentPart = CreateCon(jta.getText());
        ArrayList<MimeBodyPart> FileList = new ArrayList<MimeBodyPart>();
        for(int i=0;i<fileName.size();++i)
        {
            FileList.add(CreateAttach(fileName.get(i)));
        }

        MimeMultipart AllMultiPart  =new MimeMultipart("mixed");
        AllMultiPart.addBodyPart(ContentPart);
        for(int i=0;i<FileList.size();++i)
        {
            AllMultiPart.addBodyPart(FileList.get(i));
        }

        message.setContent(AllMultiPart);
        message.saveChanges();

        transport.connect();
        transport.sendMessage(message, message.getAllRecipients());

        JOptionPane.showMessageDialog(SendPage.this, "邮件发送成功!");

        transport.close();
    }

    public static MimeBodyPart CreateCon(String body) throws MessagingException {
        MimeBodyPart HTMLBodyPart = new MimeBodyPart();
        HTMLBodyPart.setContent(body, "text/html;charset=UTF-8");
        return HTMLBodyPart;
    }

    public static MimeBodyPart CreateAttach(String fileName) throws MessagingException {
        MimeBodyPart Attachment = new MimeBodyPart();
        FileDataSource file = new FileDataSource(fileName);
        Attachment.setDataHandler(new DataHandler(file));
        Attachment.setFileName(file.getName());
        return Attachment;
    }

}
