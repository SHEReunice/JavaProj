package spider;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;
import java.io.*;

public class ShowFileContent {

    public static void main(String[] args) {
        new MyFrame("打开文件");
    }
}

class MyFrame extends JFrame implements ActionListener {

    private static final long serialVersionUID = -8193131179158828964L;
    private Style keywordStyle;
    private Style normalStyle;

    //public SensitivewordFilter filter = new SensitivewordFilter();

    private JButton btnOpen = new JButton("  打开  ");
    private JTextArea txtInfo = new JTextArea(50, 50);
    //JTextArea不能改变文字部分属性，用JTextPane作为文本输入区
    //JTextPane txtInfo=new JTextPane();
    private JScrollPane sp = new JScrollPane(txtInfo);
    private JPanel btnPane = new JPanel();
    private final String [] fileExts = { "txt", "c", "cpp", "java" };

    private BufferedReader reader = null;
    private JFileChooser fileDlg = null;

    static String line,lineNo;
    // HighlightKeywordsDemo hl = new HighlightKeywordsDemoz();

    MyFrame(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        btnPane.add(btnOpen);
        btnOpen.setActionCommand("open");
        btnOpen.addActionListener(this);

        txtInfo.setFont( new Font( "宋体", Font.PLAIN, 16 ) );
        txtInfo.setBackground( Color.WHITE);
        txtInfo.setForeground( Color.black);

        Container cp = getContentPane();
        cp.add(btnPane, BorderLayout.NORTH);
        cp.add(sp, BorderLayout.CENTER);

        setPreferredSize(new Dimension(500, 400));
        setLocation(400,200);
        setVisible(true);
        pack();

        //styleInit();

    }

    private void styleInit(){

        keywordStyle = ((StyledDocument) txtInfo.getDocument()).addStyle("Keyword_Style", null);
        normalStyle = ((StyledDocument) txtInfo.getDocument()).addStyle("Keyword_Style", null);
        StyleConstants.setForeground(keywordStyle, Color.RED);
        StyleConstants.setForeground(normalStyle, Color.BLACK);

    }

    private void showFileContent(String fileName) {
        try {
            reader = new BufferedReader(new FileReader(fileName));
            int idxLine = 0;
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    String lineNo = null;
                    ++idxLine;
                    if (idxLine < 10) lineNo = "00" + idxLine;
                    else if (idxLine < 100) lineNo = "0" + idxLine;
                    else lineNo = "" + idxLine;
                    txtInfo.append(lineNo + ": " + line + "\r\n");
                    // webPageResource wpr=new webPageResource();
                    //txtInfo.getDocument().addDocumentListener(new SyntaxHighlighter(txtInfo));
                   /* if (line.equals(wpr.set)) {//有问题
                        txtInfo.getStyledDocument().insertString(txtInfo.getStyledDocument().getLength(), lineNo + ": " + line + "\r\n", keywordStyle);
                    }
                   else txtInfo.getStyledDocument().insertString(txtInfo.getStyledDocument().getLength(), lineNo + ": " + line + "\r\n", normalStyle);*/
                    //txtInfo.getStyledDocument().insertString(txtInfo.getStyledDocument().getLength(),filter.set,keywordStyle);
                   /*txtInfo.setText( lineNo + ": " + line + "\r\n" );
                    Highlighter highLighter = txtInfo.getHighlighter();
                    String text = txtInfo.getText();
                    File file=new File("D:\\Web Spider\\sensitive words.txt");
                    FileReader fr=new FileReader(file);
                    BufferedReader br=new BufferedReader(fr);
                    DefaultHighlighter.DefaultHighlightPainter p = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
                    String keyWord="";
                    while((keyWord=br.readLine())!=null){
                        int pos = 0;
                        while ((pos = text.indexOf(keyWord, pos)) >= 0)
                        {
                            try
                            {
                                highLighter.addHighlight(pos, pos + keyWord.length(), p);
                                pos += keyWord.length();
                            }
                            catch (BadLocationException ex)
                            {
                                ex.printStackTrace();
                            }
                        }

                    }
                    fr.close();
                    br.close();
                }*/
                    //txtInfo.getDocument().addDocumentListener(new spider.SyntaxHighlighter(txtInfo));
                }
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "open") {
            fileDlg = new JFileChooser("D:\\Web spider");

            for( int i = 0; i < fileExts.length; ++i )
                fileDlg.addChoosableFileFilter(new MyFileFilter( fileExts[i] ));

            switch (fileDlg.showOpenDialog(this)) {
                case JFileChooser.APPROVE_OPTION:
                    txtInfo.setText( null );
                    String filePath = fileDlg.getSelectedFile().getAbsolutePath();
                    setTitle( fileDlg.getSelectedFile().getName() );
                    showFileContent( filePath );
                    Highlighter highLighter = txtInfo.getHighlighter();
                    String text = txtInfo.getText();
                    File file=new File("D:\\Web Spider\\敏感词记录.txt");
                    FileReader fr= null;
                    try {
                        fr = new FileReader(file);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                    BufferedReader br=new BufferedReader(fr);
                    DefaultHighlighter.DefaultHighlightPainter p = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
                    String keyWord="";
                    try {
                        while((keyWord=br.readLine())!=null){
                            int pos = 0;
                            while ((pos = text.indexOf(keyWord, pos)) >= 0)
                            {
                                try
                                {
                                    highLighter.addHighlight(pos, pos + keyWord.length(), p);
                                    pos += keyWord.length();
                                }
                                catch (BadLocationException ex)
                                {
                                    ex.printStackTrace();
                                }
                            }

                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    try {
                        fr.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    try {
                        br.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
            }
            //txtInfo.getDocument().addDocumentListener(new spider.SyntaxHighlighter(txtInfo));
        }
    }
}

class MyFileFilter extends FileFilter {

    private String fileExt;

    MyFileFilter(String ext) {
        fileExt = ext;
    }

    private String getExtName( String absPath ) {
        int idxDot = absPath.lastIndexOf( '.' );
        return absPath.substring( idxDot + 1 );
    }

    @Override
    public boolean accept(File f) {
        return f.isDirectory() || getExtName( f.getName() ).equals( fileExt );
    }

    @Override
    public String getDescription() {
        String descr = null;
        if( fileExt.equals( "txt" ) )
            descr = "Text File (*.txt)";
        else if( fileExt.equals( "cpp" ) )
            descr = "C++ Source File (*.cpp)";
        else if( fileExt.equals( "c" ) )
            descr = "C Source File (*.c)";
        else if( fileExt.equals( "java" ) )
            descr = "Java Source File (*.java)";
        return descr;
    }
}








