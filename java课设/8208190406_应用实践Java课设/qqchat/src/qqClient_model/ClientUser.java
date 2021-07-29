/*
验证该用户是否合格
 */
package qqClient_model;

import common.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientUser {

    public static boolean checkUser(User u){
        return new ClientConServer().SendLoginInfoToServer(u);
    }


}
