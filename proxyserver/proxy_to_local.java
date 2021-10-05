/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxyserver;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rao
 */
public class proxy_to_local extends Thread {

    private InputStream proxyin = null;
    private OutputStream localout = null;
    private OutputStream proxyout = null;

    public proxy_to_local(InputStream proxyin, OutputStream proxyout, OutputStream localout) {
        this.localout = localout;
        this.proxyin = proxyin;
        this.proxyout = proxyout;
    }

    public void run() {
        Socket socket = null;
        FileOutputStream outputStream = null;
        DataInputStream in = new DataInputStream(new BufferedInputStream(proxyin));
        try {
            outputStream = new FileOutputStream("a.txt", true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(local_to_proxy.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("读取远程服务器消息");
        long len = 0;
        while (true) {
            try {
                len = in.readLong();//一直堵塞，直到新的报文发送
                System.out.println(len);
                Socket localsocket = new Socket("localhost", 80);//和本地端口通讯，要确保本地端口80开了
                localout = localsocket.getOutputStream();
                new local_to_proxy(localsocket.getInputStream(), proxyout).start();
                System.out.println("发送至本地服务器完毕1");
                for (long i = 0; i < len; i++) {
                    localout.write(in.read());
                }
                System.out.println("发送至本地服务器完毕2");

            } catch (IOException ex) {
                Logger.getLogger(proxy_to_local.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
