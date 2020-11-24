package cn.loryu.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import static cn.loryu.client.HWID.getHWID;

public class Client {
    PrintWriter writer;
    Socket socket;

    public void connect() {
        try {
            socket = new Socket("xxx.xxx.xxx.xxx", 5233);
            writer = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Connect Success");
            sendHWID();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendHWID() {
        String message = getHWID();
        writer.println(message);
        System.out.println("Send: " + message);
        try {
            char[] charArray = new char[10];
            InputStream is = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            int readLength = inputStreamReader.read(charArray);
            while (readLength != -1) {
                String newString = new String(charArray, 0, readLength);
                System.out.println("Return: " + newString);
                readLength = inputStreamReader.read(charArray);
            }
            is.close();
            inputStreamReader.close();
            socket.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
