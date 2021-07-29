/*
这是一个管理客户端和服务器保持通讯的线程类
 */
package qqClient_model;
import java.util.*;


public class ManageClientConServerThread {
    private static HashMap hm = new HashMap<String,ClientConServerThread>();
    //把创建好的通讯线程ClientConServerThread放入到hm中
    public static void addClientConServerThread(String qqId,ClientConServerThread ccst)
    {
        hm.put(qqId,ccst);
    }

    //可与通过qqId取得该线程
    public static ClientConServerThread getClientConServerThread(String qqId)
    {
        return (ClientConServerThread)hm.get(qqId);
    }
    public static void removeClientConServerThread(String qqId)
    {
        hm.remove(qqId);
    }
}
