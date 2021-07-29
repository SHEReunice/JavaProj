package javaMail;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Properties;

public class CheckPage extends JFrame implements ActionListener{
    private final String folderName = "inbox";
    private final String savePosition = "E:\\mailFile\\";
    private String Account;
    private String Password;
    JButton jbdelete,jbreset,jbupdate,jbdownload,jbsender,jbtopic,jbbody;
    JTextArea jtabody;
    JLabel jtanote;
    JLabel jlbmail,jlbtopic;
    JPanel jpnorth,jp11,jp12,jp1,jp2,jpsouth,jpCenter,jpWest,jpEast,jpall;
    JComboBox MailList;
    private Session session;
    private Store store;
    private String Pop3Server = "pop.qq.com";
    private int number;

    public CheckPage(String account,String password) throws Exception {
        this.Account = account;
        this.Password = password;
       Properties pro = new Properties();
        pro.put("mail.store.protocol", "pop3");
        pro.put("mail.imap.class", "com.sun.mail.imap.IMAPStore");
        pro.put("mail.pop3.class", "com.sun.mail.pop3.POP3Store");

        session = Session.getInstance(pro,null);
        try
        {
            store = session.getStore("pop3");
            store.connect(Pop3Server, Account, Password);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


       jtanote = new JLabel("",SwingConstants.RIGHT);
        ReceiveMes();

        jpnorth = new JPanel();
        jpnorth.add(jtanote);

        jbsender = new JButton("from:");
        jlbmail = new JLabel();
        jp11 = new JPanel(new BorderLayout());
        jp11.add(jbsender,BorderLayout.WEST);
        jp11.add(jlbmail,BorderLayout.CENTER);
        jbtopic = new JButton("topic:");
        jlbtopic = new JLabel();
        jp12 = new JPanel(new BorderLayout());
        jp12.add(jbtopic,BorderLayout.WEST);
        jp12.add(jlbtopic,BorderLayout.CENTER);
        jp1 = new JPanel(new GridLayout(2,1,10,10));
        jp1.add(jp11);
        jp1.add(jp12);

        jbbody = new JButton("body:");
        jtabody = new JTextArea();
        jp2 = new JPanel(new BorderLayout());
        jp2.add(jbbody,BorderLayout.WEST);
        jp2.add(jtabody,BorderLayout.CENTER);
        jpsouth = new JPanel(new BorderLayout());
        jpsouth.add(jp1,BorderLayout.NORTH);
        jpsouth.add(jp2,BorderLayout.CENTER);

        jpCenter = new JPanel(new BorderLayout());
        jpCenter.add(jpnorth,BorderLayout.NORTH);
        jpCenter.add(jpsouth,BorderLayout.CENTER);

        MailList = new JComboBox();
        MailList.setMaximumRowCount(5);
        for(int i = 0; i < number; i++){
            MailList.addItem("Mail-No." + (i+1));
        }
       MailList.setSelectedIndex(0);
        jbdelete = new JButton("delete");
        jbdelete.addActionListener(this);
        jpEast = new JPanel(new GridLayout(2,1,10,10));
        jpEast.add(MailList);
        jpEast.add(jbdelete);

        jbreset = new JButton("reset");
        jbreset.addActionListener(this);
        jbupdate = new JButton("refresh");
        jbupdate.addActionListener(this);
        jbdownload = new JButton("download");
        jbdownload.addActionListener(this);
        jpWest = new JPanel(new GridLayout(5,1,10,10));
        jpWest.add(jbreset);
        jpWest.add(jbupdate);
        jpWest.add(jbdownload);

        jpall = new JPanel(new BorderLayout());
        jpall.add(jpWest,BorderLayout.WEST);
        jpall.add(jpCenter,BorderLayout.CENTER);
        jpall.add(jpEast,BorderLayout.EAST);

        this.setTitle("JavaMail");
        this.setSize(800,600);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton j = (JButton) e.getSource();
        if(j == jbreset){
            try {
                ReceiveMes();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            jlbmail.setText("the sender.");
            jlbtopic.setText("the Topic.");
            jtabody.setText("the body");
        }else if(j == jbupdate){
            try {
                viewCon();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }else if(j == jbdownload){
            try {
                DownloadAttach(MailList.getSelectedIndex());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }else if(j == jbdelete){
            try {
                DeleteMail(MailList.getSelectedIndex());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private void ReceiveMes() throws Exception {
        Folder folder = store.getFolder(folderName);
        if(folder==null)
        {
            throw new Exception(folderName+" does not exist.");
        }
        folder.open(Folder.READ_WRITE);
        number = folder.getMessageCount();
        jtanote.setText("Totally, mail number is "+number+".");
    }

    private void viewCon() throws Exception {
        Folder folder = store.getFolder(folderName);
        if(folder==null)
        {
            throw new Exception(folderName+" does not exist.");
        }
        folder.open(Folder.READ_WRITE);
        int Mail_Index = MailList.getSelectedIndex();
        MimeMessage ThisMessage = (MimeMessage)((folder.getMessages())[Mail_Index]);

        String sender = String.valueOf((ThisMessage.getFrom())[0]);
        jlbmail.setText(sender);
        jlbtopic.setText(ThisMessage.getSubject());

        if(hasAttachment(ThisMessage))
        {
            StringBuffer textbody = new StringBuffer();
            GetTextBody(ThisMessage,textbody);
            jtabody.setText(textbody.toString()+"\n\nNOTE:此邮件带有附件.");
        }
        else
        {
            String textBody = String.valueOf(ThisMessage.getContent());
            jtabody.setText(textBody);
        }

        ThisMessage.setFlag(Flags.Flag.SEEN, true);
        folder.close(true);
    }

    private boolean hasAttachment(Part part) throws MessagingException, IOException {
        boolean has = false;
        if(part.isMimeType("multipart/*"))
        {
            MimeMultipart multipart = (MimeMultipart)part.getContent();
            int partCount = multipart.getCount();
            for(int i=0;i<partCount;++i)
            {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disp = bodyPart.getDisposition();
                if(disp!=null&&(disp.equalsIgnoreCase(Part.ATTACHMENT)||disp.equalsIgnoreCase(Part.INLINE)))
                {
                    has = true;
                }
                else if(bodyPart.isMimeType("multipart/*"))
                {
                    has = hasAttachment(bodyPart);
                }
                else
                {
                    String contentType = bodyPart.getContentType();
                    if(contentType.indexOf("application")!=-1)
                    {
                        has = true;
                    }
                    if(contentType.indexOf("name")!=-1)
                    {
                        has = true;
                    }
                }
                if(has)
                {
                    break;
                }
            }
        }
        else if(part.isMimeType("message/rfc822"))
        {
            has = hasAttachment((Part)part.getContent());
        }
        return has;
    }

    private StringBuffer GetTextBody(Part part, StringBuffer textbody) throws MessagingException, IOException {
        boolean hasTextAttach = part.getContentType().indexOf("name")>0;
        if(part.isMimeType("text/*")&&!hasTextAttach)
        {
            textbody.append(part.getContent().toString());
        }
        else if(part.isMimeType("message/rfc822"))
        {
            GetTextBody((Part)part.getContent(),textbody);
        }
        else if(part.isMimeType("multipart/*"))
        {
            Multipart multipart = (Multipart)part.getContent();
            int partCount = multipart.getCount();
            for(int i=0;i<partCount;++i)
            {
                BodyPart bodypart = multipart.getBodyPart(i);
                GetTextBody(bodypart, textbody);
            }
        }

        return textbody;
    }

    private void DownloadAttach(int index) throws Exception {
        Folder folder = store.getFolder(folderName);
        if(folder==null)
        {
            throw new Exception(folderName+" does not exist.");
        }
        folder.open(Folder.READ_WRITE);
        int Mail_Index = MailList.getSelectedIndex();
        MimeMessage ThisMessage = (MimeMessage)((folder.getMessages())[Mail_Index]);

        if(hasAttachment(ThisMessage))
        {
            SaveAttach(ThisMessage,savePosition+ThisMessage.getSubject()+"_");
            JOptionPane.showMessageDialog(CheckPage.this, "附件下载成功!");
        }
        else
        {
            JOptionPane.showMessageDialog(CheckPage.this,"This mail has no attachment.","ERROR",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void SaveAttach(Part part, String s) throws Exception {
        if(part.isMimeType("multipart/*"))
        {
            Multipart multipart = (Multipart)part.getContent();
            int partCount = multipart.getCount();
            for (int i=0;i<partCount;++i)
            {
                BodyPart bodypart = multipart.getBodyPart(i);
                String disp = bodypart.getDisposition();
                if(disp!=null&&(disp.equalsIgnoreCase(Part.ATTACHMENT)||disp.equalsIgnoreCase(Part.INLINE)))
                {
                    InputStream is = bodypart.getInputStream();
                    SaveFile(is,savePosition,Decode(bodypart.getFileName()));
                }
                else if(bodypart.isMimeType("multipart/*"))
                {
                    SaveAttach(bodypart, savePosition);
                }
                else
                {
                    String contentType = bodypart.getContentType();
                    if(contentType.indexOf("name")!=-1||contentType.indexOf("application")!=-1)
                    {
                        SaveFile(bodypart.getInputStream(), savePosition, Decode(bodypart.getFileName()));
                    }
                }
            }
        }
    }

    private void SaveFile(InputStream is,String savePosition,String fileName) throws Exception
    {
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(savePosition+fileName)));
        int index=-1;
        while((index=bis.read())!=-1)
        {
            bos.write(index);
            bos.flush();
        }
        bos.close();
        bis.close();
    }

    private String Decode(String encode) throws Exception
    {
        if(encode==null||encode.equals(""))
        {
            return "";
        }
        else
        {
            return MimeUtility.decodeText(encode);
        }
    }

    private void DeleteMail(int index) throws Exception
    {
        Folder folder = store.getFolder(folderName);
        if(folder==null)
        {
            throw new Exception(folderName+" does not exist.");
        }
        folder.open(Folder.READ_WRITE);
        int inquire = JOptionPane.showConfirmDialog(CheckPage.this,
                "确定删除Mail-No."+(index+1)+" ?","Delete",
                JOptionPane.YES_NO_OPTION);
        if(inquire==JOptionPane.YES_OPTION)
        {
            Message DeleteMessage = (folder.getMessages())[index];
            DeleteMessage.setFlag(Flags.Flag.DELETED, true);
            JOptionPane.showMessageDialog(CheckPage.this, "Mail-No."+(index+1)+" 已删除");
        }
        else
        {
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }
        folder.close(true);
    }

    private void exit()
    {
        int inquire = JOptionPane.showConfirmDialog(CheckPage.this,
                "确定退出吗?","Leave",
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

    public static void main(String[] args) throws Exception {
        new CheckPage("1","1");
    }


}
