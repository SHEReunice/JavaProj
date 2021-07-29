/*
管理好友、黑名单、陌生人、界面类
 */
package qqclient_view;
import  java.util.*;

public class ManageQqFriendList {
    private static HashMap hm = new HashMap<String, FriendList>();
    public  static void addFriendList(String qqId,FriendList qqFriendList){
        hm.put(qqId,qqFriendList);
    }
    public static FriendList getQqFriendList(String qqId){
        return (FriendList)hm.get(qqId);
    }
    public  static void removeFriendList(String qqId){
        hm.remove(qqId);
    }
}
