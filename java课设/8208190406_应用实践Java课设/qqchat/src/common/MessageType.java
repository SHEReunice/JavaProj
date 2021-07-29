/*
定义包的种类
 */
package common;

public interface MessageType {
    String message_succeed = "1"; //登陆成功
    String message_login_fail = "2";//登陆失败
    String message_comm_mes = "3"; //普通信息包
    String message_get_onLineFriend = "4";//要求在线好友的包
    String message_ret_onLineFriend = "5";//返回在线好友的包
    String message_xtxx = "6"; //系统消息
    String message_qxx = "7"; //群消息
    String message_out = "8"; //下线
    String message_selfout = "9";//被强制下线

}
