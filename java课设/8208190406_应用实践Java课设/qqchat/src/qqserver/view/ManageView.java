package qqserver.view;


import java.util.HashMap;

public class ManageView {
    private static HashMap hm = new HashMap<String, myServer_view>();
    //加入
    public static  void addView(String id,myServer_view view)
    {
        hm.put(id,view);
    }

    //取出
    public static myServer_view getView(String id)
    {
        return (myServer_view)hm.get(id);
    }
}
