package cn.loryu.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

public class Server {
    BufferedReader reader;
    ServerSocket server;
    Socket socket;
    File file = new File("C:\\Program Files\\HWIDServer");
    File hwid = new File("C:\\Program Files\\HWIDServer\\HWID.txt");
    File log = new File("C:\\Program Files\\HWIDServer\\Log.txt");
    FileWriter fw;
    BufferedWriter writer;
    String client;
    String s;
    String[] s1;

    public void getServer() {
        try {
            server = new ServerSocket(5233);
            checkFilePath();
            System.out.println(getTime() + "\nServer Init Success\nHWID File Path: " + hwid + "\nLog File Path: " + log + "\n");
            writeLog(getTime() + "Server Init Success");
            writeLog("");
            while (true) {
                System.out.println(getTime() + "Wait for Client Connect");
                socket = server.accept();
                if (null == socket.getInputStream()) {
                    System.out.println(getTime() + (client = socket.getInetAddress().getHostName()) + " Connected");
                    writeLog(getTime() + (client = socket.getInetAddress().getHostName()) + " Connected");
                    OutputStream os = socket.getOutputStream();
                    System.out.println(getTime() + "LOGIN FAILED\n");
                    writeLog(getTime() + "LOGIN FAILED");
                    writeLog("");
                    os.write("FALSE".getBytes());
                } else {
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    System.out.println(getTime() + (client = socket.getInetAddress().getHostName()) + " Connected");
                    writeLog(getTime() + (client = socket.getInetAddress().getHostName()) + " Connected");
                    OutputStream os = socket.getOutputStream();
                    if (getClientMessage()) {
                        System.out.println(getTime() + "LOGIN SUCCESS\n");
                        writeLog(getTime() + "LOGIN SUCCESS");
                        writeLog("");
                        os.write("TRUE".getBytes());
                    } else {
                        System.out.println(getTime() + "LOGIN FAILED\n");
                        writeLog(getTime() + "LOGIN FAILED");
                        writeLog("");
                        os.write("FALSE".getBytes());
                    }
                }
                Thread.sleep(10);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean getClientMessage() {
        try {
            while (true) {
                s = reader.readLine();
                System.out.println(getTime() + s);
                writeLog(getTime() + s);
                List<String> s1 = ReadFileString();
                for (int i = 0; i < s1.size(); i++) {
                    if (null == s) return false;
                    if (s.equals(ReadFileString().get(i))) {
                        return true;
                    }
                }
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (reader != null) {
                reader.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String> ReadFileString() {
        try {
            checkFilePath();
            List<String> list = new ArrayList<String>();
            FileInputStream fileInputStream = new FileInputStream(hwid);
            InputStreamReader isr = new InputStreamReader(fileInputStream, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line = "";
            while ((line = br.readLine()) != null) {
                if (line.lastIndexOf("---") < 0) {
                    list.add(line);
                }
            }
            br.close();
            isr.close();
            fileInputStream.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeLog(String s) {
        try {
            checkFilePath();
            fw = new FileWriter(log, true);
            writer = new BufferedWriter(fw);
            writer.write(s);
            writer.newLine();
            writer.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTime() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return "[" + sdf.format(d) + "] ";
    }

    public void checkFilePath() {
        try {
            if (!file.exists()) {
                file.mkdir();
            }
            if (!hwid.exists()) {
                hwid.createNewFile();
            }
            if (!log.exists()) {
                log.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
