package qqserver.model;

import java.util.*;

public class ManageClientThread {
    public static HashMap hm = new HashMap<String,SerConClientThread>();

    //向hm中添加一个客户端通讯线程
    public static void addClientThread(String uid,SerConClientThread ct)
    {
        hm.put(uid,ct);
    }

    public static SerConClientThread getClientThread(String uid)
    {
        return (SerConClientThread)hm.get(uid);
    }
    public static void removeClientThread(String uid)
    {
        hm.remove(uid);
    }

    //返回当前在线的人的情况
    public static String getAllOnLineUserid(){
        //用迭代器返回HashMap的k值
        Iterator it = hm.keySet().iterator();
        String res = "";
        while(it.hasNext())
        {
            res += it.next().toString()+" ";
        }
        return  res;
    }
}
