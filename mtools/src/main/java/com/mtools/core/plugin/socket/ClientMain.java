package com.mtools.core.plugin.socket;

import java.io.*; 
import java.net.InetAddress; 
import java.net.Socket; 
import java.util.Scanner; 
 
public class ClientMain { 
 
    private   int ServerPort = 9527; 
    private   String ServerAddress = "192.168.1.154"; 
    private   String GetOrPut = "get";    
    private   String local_filename = "";  
    private   String remote_filename  = "";  
    private   byte[] buf; 
    private   int len; 
    class SocketThread extends Thread{ 
         
        @Override 
        public void run() { 
             try { 
                 
                File file = new File("C:\\",local_filename); //假设文件放在C盘 
                if(!file.exists()&&GetOrPut.equals("put")){  
                    System.out.println("本地没有这个文件，无法上传！");  
                    return; 
                }  
                 
                InetAddress loalhost = InetAddress.getLocalHost(); 
                Socket socket = new Socket(ServerAddress,ServerPort,loalhost,44); 
                                            //服务器IP地址  端口号   本机IP 本机端口号 
                DataInputStream dis = new DataInputStream(socket.getInputStream()); 
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream()); 
                  
                //dos.writeUTF(GetOrPut+" "+remote_filename);//服务器端如果是io的socket，writeUTF和writeUTF对接 
                dos.write((GetOrPut+" "+remote_filename).getBytes()); 
                dos.flush();  
               
                //String tempString = dis.writeUTF();  
                buf = new byte[1024]; 
                len = dis.read(buf); 
                String tempString = new String(buf,0,len);//服务器反馈的信息 
                 
                //System.out.println(tempString);  
                if(tempString.equals("notexists")){ 
                    System.out.println("服务器没有这个文件，无法下载！");  
                    dos.close(); 
                    dis.close(); 
                    socket.close(); 
                    return; 
                } 
                 
                if(tempString.startsWith("准备下载")){   
                    DataOutputStream fileOut =  
                        new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file))); 
  
                    while ((len = dis.read(buf))!=-1) {    
                        fileOut.write(buf, 0, len); 
                    } 
                    System.out.println("下载完毕！"); 
                    fileOut.close(); 
                    dos.close(); 
                    dis.close(); 
                    socket.close(); 
                } 
                else if(tempString.equals("UploadReady")){   
                    System.out.println("正在上传文件......."); 
                    DataInputStream fis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));  
                       
                    while ((len = fis.read(buf))!= -1) {   
                        dos.write(buf, 0, len); 
                    } 
                    dos.flush(); 
                    System.out.println("上传完毕！"); 
                    fis.close(); 
                    dis.close(); 
                    dos.close(); 
                    socket.close(); 
                } 
                 
            } catch (Exception e) { 
                e.printStackTrace(); 
            } 
        } 
         
    } 
     
    public boolean checkCommand(String command) 
    {  
        if(!command.startsWith("put")&&!command.startsWith("get")){ 
            System.out.println("输入命令错误"); 
            return false; 
        } 
         
        int index = -1; 
        String temp = ""; 
        String[] tempStrings = null; 
         
        if((index=command.indexOf("-h"))>0){ 
            temp = command.substring(index+3); 
            temp = temp.substring(0, temp.indexOf(' ')); 
            ServerAddress = temp; 
        } 
        if((index=command.indexOf("-p"))>0){ 
            temp = command.substring(index+3); 
            temp = temp.substring(0, temp.indexOf(' ')); 
            ServerPort = Integer.valueOf(temp); 
        } 
         
        tempStrings = command.split(" "); 
        if(command.startsWith("put")){ 
            GetOrPut = "put"; 
            local_filename = tempStrings[tempStrings.length-2]; 
            remote_filename = tempStrings[tempStrings.length-1]; 
        } 
        else if(command.startsWith("get")){ 
            GetOrPut = "get"; 
            local_filename = tempStrings[tempStrings.length-1]; 
            remote_filename = tempStrings[tempStrings.length-2]; 
        } 
         
        return true; 
    } 
     
    public static void main(String[] args) { 
        ClientMain thisC= new ClientMain();  
        Scanner sc = new Scanner(System.in); 
        String commandString = ""; 
        do { 
            System.out.println("请输入命令：");  
            commandString = sc.nextLine(); 
        } while (!thisC.checkCommand(commandString));  
         
        ClientMain.SocketThread a = thisC.new SocketThread(); 
        a.start(); 
    } 
} 