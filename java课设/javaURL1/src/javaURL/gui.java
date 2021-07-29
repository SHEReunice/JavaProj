package javaURL;
import java.awt.FlowLayout;
import javax.swing.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class gui extends JFrame{
    private JLabel lburl = new JLabel("输入网址");
    private JTextField tfurl = new JTextField(10);
    private JPanel jpl = new JPanel();
    public gui(){
        jpl.add(lburl);
        jpl.add(tfurl);
        this.add(jpl);
        this.setSize(150,220);
        this.setVisible(true);
    }
    public static void main(String[] args){
        new gui();
    }
}
