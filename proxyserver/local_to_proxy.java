/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxyserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.input.KeyCode;

/**
 *
 * @author rao
 */
public class local_to_proxy extends Thread {//从本地80端口读取数据到代理服务器，-1为和本地端口连接,localin->proxyout

    private OutputStream proxyout = null;
    private InputStream localin = null;

    public local_to_proxy(InputStream inputStream, OutputStream proxyout) {
        this.localin = inputStream;
        this.proxyout = proxyout;
    }

    public void run() {
        String a = "";

        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(proxyout));
        DataInputStream in = new DataInputStream(new BufferedInputStream(localin));
        StringBuilder headStr = new StringBuilder();
        String s = "";
        try {
            /*
            // BufferedReader  in =new BufferedReader(new InputStreamReader(localin));
            BufferedReader bufferin = new BufferedReader(new InputStreamReader(localin));
            while(null!=(s=bufferin.readLine())){
                System.out.println(s);
                headStr.append(s+"\r\n");
            }
            try {
                System.out.println("接下来发送给远程服务器");
                out.writeLong(headStr.toString().length());
                System.out.println("返回的长度为"+headStr.toString().length());
                out.write(headStr.toString().getBytes());
                out.flush();
                System.out.println("发送到远程服务器完毕");
             */
            int z = 0;
            Vector sint = new Vector();
            while (z != -1) {
                z = localin.read();
                sint.add(z);
            }
            out.writeLong(sint.size());
            for (int i = 0; i < sint.size(); i++) {
                out.write((int) sint.get(i));
            }
            out.flush();

            System.out.println("接下来发送给远程服务器");
            System.out.println("返回的长度为" + sint.size());
            System.out.println("发送到远程服务器完毕");

        } catch (IOException ex1) {
            Logger.getLogger(local_to_proxy.class.getName()).log(Level.SEVERE, null, ex1);
        }

        System.out.println("读取本地80端口的数据读取完毕");
    }

}
