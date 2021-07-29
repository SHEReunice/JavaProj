/*
这是一个用于管理用户聊天界面的类
 */
package qqclient_view;

import java.util.*;

public class ManageQqChat {

    private static HashMap hm = new HashMap<String,qqchat>();
    //加入
    public static  void addQqChat(String loginIdAnFriendId,qqchat qq)
    {
        hm.put(loginIdAnFriendId,qq);
    }

    //取出
    public static qqchat getQqChat(String loginIdAnFriendId)
    {
        return (qqchat)hm.get(loginIdAnFriendId);
    }

}
