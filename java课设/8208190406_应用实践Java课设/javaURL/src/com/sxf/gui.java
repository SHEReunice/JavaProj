package com.sxf;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.util.ArrayList;

public class gui extends JFrame implements ActionListener {
    private JPanel jpanel=new JPanel();
    private JPanel jptop = new JPanel();
    private JPanel jptopright = new JPanel();
    private JPanel jphtml = new JPanel();
    private JPanel jptext = new JPanel();
    private JPanel jpsen = new JPanel();
    private JPanel jpword1 = new JPanel();
    private JPanel jpword2 = new JPanel();
    private JPanel jpdown = new JPanel();
    private JTabbedPane alterPane=new JTabbedPane();
    private JPanel jpweb = new JPanel();
    private JLabel lburl = new JLabel("输入网址");
    private JTextField tfurl = new JTextField(10);
    private JScrollPane spurl=new JScrollPane(tfurl);
    private JButton goSpider=new JButton("开始爬取");
    private JButton urls = new JButton("打开网址库");
    private JTextArea htmlArea=new JTextArea(15, 25);
    private JScrollPane htmlSPane=new JScrollPane(htmlArea);
    private JTextArea textArea=new JTextArea(15,25);
    private JScrollPane textSPane=new JScrollPane(textArea);
    private JTextArea senArea=new JTextArea(15,25);
    private JScrollPane senSPane=new JScrollPane(senArea);
    private JButton words = new JButton("选择敏感词库");
    private JButton match = new JButton("提取敏感词");
    private JTextArea senword = new JTextArea();
    private JScrollPane wordspane = new JScrollPane(senword);

    Set<String> set = null;
    HashMap senWordMap;
    ArrayList<String> sensList = new ArrayList<String>();
    ArrayList<String> senWordList = new ArrayList<String>();
    ArrayList<Integer> wordNum = new ArrayList<Integer>();


    public gui(){
        this.setTitle("Spider");
        this.setLocation(400, 200);
        this.setSize(600, 500);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        jpanel.setLayout(new BorderLayout());

        //顶部,输入网址，开始爬取
        jptop.setLayout(new BorderLayout());
        lburl.setPreferredSize(new Dimension(70,30));
        spurl.setPreferredSize(new Dimension(300,30));
        goSpider.setPreferredSize(new Dimension(90,30));
        urls.setPreferredSize(new Dimension(90,30));
        jptopright.setLayout(new GridLayout(1,2,10,10));
        jptopright.add(goSpider);
        jptopright.add(urls);
        jptop.add(lburl,BorderLayout.WEST);
        jptop.add(spurl,BorderLayout.CENTER);
        jptop.add(jptopright,BorderLayout.EAST);

        //html源代码及抽取的文本
        htmlArea.setEditable(false);
        htmlArea.setLineWrap(true); //文本区域的换行策略
        htmlArea.setFont(new Font("楷体",Font.PLAIN,14));
        jphtml.setLayout(new BorderLayout());
        jphtml.add(htmlSPane,BorderLayout.CENTER);

        textArea.setFont(new Font("楷体",Font.PLAIN,14));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        jptext.setLayout(new BorderLayout());
        jptext.add(textSPane,BorderLayout.CENTER);

        senArea.setFont(new Font("楷体",Font.PLAIN,14));
        senArea.setEditable(false);
        senArea.setLineWrap(true);
        jpsen.setLayout(new BorderLayout());
        jpsen.add(senSPane,BorderLayout.CENTER);

        alterPane.add("HTML源代码",jphtml);
        alterPane.add("文本",jptext);
        alterPane.add("敏感词汇",jpsen);
        jpweb.setLayout(new BorderLayout());
        jpweb.add(alterPane,BorderLayout.CENTER);

        //敏感词库和提取敏感词汇
        senword.setLineWrap(true);
        senword.setEditable(false);
        wordspane.setPreferredSize(new Dimension(6,400));
        jpword1.setLayout(new GridLayout(2,1,10,10));
        jpword1.add(words);
        jpword1.add(match);
        jpword2.setLayout(new BorderLayout());
        jpword2.add(jpword1,BorderLayout.NORTH);
        jpword2.add(wordspane,BorderLayout.CENTER);

        jpdown.setLayout(new BorderLayout());
        jpdown.add(jpweb,BorderLayout.CENTER);
        jpdown.add(jpword2,BorderLayout.EAST);

        jpanel.add(jptop,BorderLayout.NORTH);
        jpanel.add(jpdown,BorderLayout.CENTER);
        this.add(jpanel);
        this.setVisible(true);

        //事件
        goSpider.addActionListener((ActionListener) this);
        urls.addActionListener((ActionListener) this);
        words.addActionListener((ActionListener) this);
        match.addActionListener((ActionListener) this);

    }
    public static void main(String[] args){
        new gui();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton j = (JButton)e.getSource();
        if(j == goSpider){
            String weburll = tfurl.getText();
            new urlpage(weburll).start();
        }else if(j == words){
            try {
                readSenWordFile();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }else if (j == match){
            hashinit(set);
            String weburl = tfurl.getText();
            try {
                getSenWord((gettext(weburl)),2);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            try {
                showSenWord();
            } catch (BadLocationException badLocationException) {
                badLocationException.printStackTrace();
            }
            String br = senWordList.toString();
            senArea.append(br);
        }else if(j == urls){
            spiderurls();
        }
    }


    public String getHtml(String weburl) throws IOException {
        System.out.println("开始爬取");
        Document doc = Jsoup.connect(weburl).get();
        String html = doc.html();
        System.out.println("爬取结束");
        return html;
    }
    public String gettext(String weburl) throws IOException {
        Document doc = Jsoup.connect(weburl).get();
        String title = doc.title();
        String text = title + doc.text();
        return text;
    }

    private void hashinit(Set<String> keyWordSet){
        senWordMap = new HashMap(keyWordSet.size());
        String key = null;
        Map nowMap = null;
        Map<String,String> newWordMap = null;
        Iterator<String> iterator = keyWordSet.iterator();
        while(iterator.hasNext()){
            key = iterator.next();
            nowMap = senWordMap;
            for(int i = 0; i < key.length(); i++){
                char keyChar = key.charAt(i);
                Object wordMap = nowMap.get(keyChar);
                if(wordMap != null){
                    nowMap = (Map) wordMap;//存在该key，直接赋值
                }else{
                    newWordMap = new HashMap<String,String>();
                    newWordMap.put("isEnd", "0");
                    nowMap.put(keyChar,newWordMap);
                    nowMap = newWordMap;
                }
                if(i == key.length()-1){
                    nowMap.put("isEnd","1");
                }
            }
        }
    }
    private void readSenWordFile() throws Exception {
        String ENCODING = "UTF-8";
        JFileChooser fchooser = new JFileChooser(); //文件选择
        int ok = fchooser.showOpenDialog(this);
        if(ok != JFileChooser.APPROVE_OPTION) return ; //判断是否正常选择
        File file = fchooser.getSelectedFile();//获取选择的文件
        InputStreamReader read = new InputStreamReader(new FileInputStream(file),ENCODING);
        try {
            if (file.isFile() && file.exists()) {
                set = new HashSet<String>();
                BufferedReader bufferedReader = new BufferedReader(read);
                String txt = null;
                while ((txt = bufferedReader.readLine()) != null) {
                    set.add(txt);
                    sensList.add(txt);
                    wordNum.add(0);
                    senword.append(txt + "\n");
                }
            } else {
                throw new Exception("敏感词库文件不存在");
            }
            }catch(Exception e){
                throw e;
            }finally{
                read.close();
            }

    }

    public void  getSenWord(String txt, int matchType){
        for(int i = 0; i < txt.length(); i++){
            int length = CheckSenWord(txt, i, matchType);
            if(length > 0){
                senWordList.add(txt.substring(i,i+length));
                i = i + length - 1;
            }
        }
    }

    public int CheckSenWord(String txt, int beginIndex, int matchType){
        boolean flag = false;
        int matchFlag = 0;
        char word = 0;
        int minMatchTYpe = 1;      //最小匹配规则
        int maxMatchType = 2;
        Map nowMap = senWordMap;
        for(int i = beginIndex; i < txt.length(); i++){
            word = txt.charAt(i);
            nowMap = (Map)nowMap.get(word); //获取key
            if(nowMap != null){
                matchFlag++;
                if("1".equals(nowMap.get("isEnd"))){
                    flag = true;
                    if(minMatchTYpe == matchType){
                        break;
                    }
                }
            }else{
                break;
            }
        }
        if(matchFlag < 2 || !flag){
            matchFlag = 0;
        }
        return matchFlag;
    }

    public void showSenWord() throws BadLocationException {
        Highlighter hg = textArea.getHighlighter();
        hg.removeAllHighlights();
        String text = textArea.getText();
        DefaultHighlighter.DefaultHighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
        for(String str:sensList){
            int index = 0;
            while((index = text.indexOf(str,index)) >= 0){
                hg.addHighlight(index,index+str.length(),painter);
                index += str.length();
            }
        }
    }

    public void spiderurls(){
        if(wordNum.size() <= 0){
            JOptionPane.showMessageDialog(null,"请先选择敏感词库");
            return;
        }
        JFileChooser fchooser = new JFileChooser();
        int ok = fchooser.showOpenDialog(this);
        if(ok != JFileChooser.APPROVE_OPTION) return;
        File file = fchooser.getSelectedFile();
        new spiderurls(this, file).start();
    }
    class spiderurls extends Thread{
        private File file = null;
        public spiderurls(gui gui, File f){
            file = f;
        }
        public void run(){
            try{
                BufferedReader brr = new BufferedReader(new FileReader(file));
                PrintStream ps = new PrintStream(new File("data.txt"));
                ps.println("敏感词记录如下：");
                int size = sensList.size();
                int j = 10;
                while(j>0){
                    j--;
                    String web = brr.readLine();
                    if(web == null) break;
                    ps.println(web + "数据如下：");
                    String html = getHtml(web);
                    String text = gettext(web);
                    for(int i=0;i<size;i++) {		//在网页文本中进行匹配
                        String word=sensList.get(i);
                        int index=0,account=0,len=word.length();
                        while((index=text.indexOf(word,index))>=0) {
                            account++;
                            int temp=wordNum.get(i);	//更新数据
                            wordNum.set(i,++temp);
                            index+=len;		//更新匹配条件
                        }
                        ps.println(word+"  出现  "+account+"次");	//写入当前数据
                    }
                    ps.println();
                }
                brr.close();
                System.out.println("爬取完毕");
                ps.close();
                JOptionPane.showMessageDialog(null,"爬取完毕！请打开文件查看!");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        }

    class urlpage extends Thread{
        private String weburl = null;
        public urlpage(String s){
            weburl = s;
        }
        public void run(){
            if(weburl.length() <= 0){
                JOptionPane.showMessageDialog(null,"网址不能为空");
                return;
            }
            htmlArea.setText("");//清除
            textArea.setText("");
            String html = null;
            try {
                html = getHtml(weburl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(html.length() > 0){
                JOptionPane.showMessageDialog(null,"爬取完毕");
                htmlArea.append(html);
                String text = null;
                try {
                    text = gettext(weburl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                textArea.append(text);
               /* getSenWord(text,2);
                String br = senWordList.toString();
                senArea.append(br);
                */

            }
        }
    }



}

























