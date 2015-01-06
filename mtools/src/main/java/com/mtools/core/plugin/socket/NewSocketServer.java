package com.mtools.core.plugin.socket;

import java.io.BufferedInputStream; 
import java.io.BufferedOutputStream; 
import java.io.DataInputStream; 
import java.io.DataOutputStream; 
import java.io.File; 
import java.io.FileInputStream; 
import java.io.FileOutputStream; 
import java.io.IOException; 
import java.net.InetSocketAddress; 
import java.nio.ByteBuffer;  
import java.nio.CharBuffer; 
import java.nio.channels.SelectionKey; 
import java.nio.channels.Selector; 
import java.nio.channels.ServerSocketChannel; 
import java.nio.channels.SocketChannel; 
import java.nio.charset.Charset; 
import java.nio.charset.CharsetDecoder; 
import java.nio.charset.CharsetEncoder; 
import java.util.Iterator; 
 
public class NewSocketServer { 
  
    private static final int port = 9527; 
    private Selector selector; 
    private ByteBuffer clientBuffer = ByteBuffer.allocate(1024);   
    private CharsetDecoder decoder = Charset.forName("GB2312").newDecoder(); 
    private CharsetEncoder encoder = Charset.forName("GB2312").newEncoder();  
    //编码解码格式设置成GBK也行.UTF-8不行，中文乱码  （前提都是客户端没有设置任何编码解码格式） 
     
    public void setListener() throws Exception{ 
         
        selector = Selector.open(); //打开选择器    
         
        ServerSocketChannel server = ServerSocketChannel.open();  //定义一个 ServerSocketChannel通道 
        server.socket().bind(new InetSocketAddress(port));  //ServerSocketChannel绑定端口   
        server.configureBlocking(false);   //配置通道使用非阻塞模式 
        server.register(selector, SelectionKey.OP_ACCEPT); //该通道在selector上注册  接受连接的动作 
         
        while(true) 
        {     
            selector.select();   //select() 会阻塞，直到在该selector上注册的channel有对应的消息读入 
            Iterator iter = selector.selectedKeys().iterator();    
            while (iter.hasNext()) {     
                SelectionKey key = (SelectionKey) iter.next();    
                iter.remove();  // 删除此消息  
                process(key);   // 当前线程内处理。（为了高效，一般会在另一个线程中处理此消息） 
            }    
        }    
    } 
     
     private void process(SelectionKey key) throws IOException {    
            if (key.isAcceptable()) { // 接收请求    
                ServerSocketChannel server = (ServerSocketChannel) key.channel();    
                SocketChannel channel = server.accept();//类似于io的socket，ServerSocketChannel的accept函数返回 SocketChannel 
                channel.configureBlocking(false);   //设置非阻塞模式    
                SelectionKey sKey = channel.register(selector, SelectionKey.OP_READ);  
                sKey.attach("read_command"); //这儿接收到连接请求之后可以为每个连接设置一个ID 
            }  
            else if (key.isReadable()) { // 读信息     
                SocketChannel channel = (SocketChannel) key.channel();    
                String name = (String) key.attachment();  
                if(name.equals("read_command")){ 
                    int count = channel.read(clientBuffer);  
                    if (count > 0) {    
                        clientBuffer.flip();    
                        CharBuffer charBuffer = decoder.decode(clientBuffer);    
                        String command = charBuffer.toString();    
                         
                        //command形如：get abc.png 或者  put aaa.png 
                        System.out.println("command===="+command);  //得到客户端传来的命令  
                         
                        String[] temp =command.split(" "); 
                        command = temp[0];  //命令  是put还是get 
                        String filename = temp[1];  //文件名 
                         
                        SelectionKey sKey = channel.register(selector,SelectionKey.OP_WRITE);    
                        if(command.equals("put"))sKey.attach("UploadReady#"+filename);  //要保护该通道的文件名 
                        else if(command.equals("get")){  
                            if(!new File("C:\\",filename).exists()){ //假设文件都是在C盘根目录 
                                System.out.println("没有这个文件，无法提供下载！"); 
                                sKey.attach("notexists");  
                            } 
                            else sKey.attach("DownloadReady#"+filename); //要保护该通道的文件名 
                        } 
                    } else {    
                        channel.close();    
                    }    
                } 
                else if(name.startsWith("read_file")){//这儿可以新开一个线程     文件操作也可以用NIO  
                    DataOutputStream fileOut =  
                        new DataOutputStream( 
                                new BufferedOutputStream( 
                                        new FileOutputStream( 
                                                new File("C:\\",name.split("#")[1])))); 
  
                    int passlen = channel.read(clientBuffer);   
                    while (passlen>=0) {    
                        clientBuffer.flip();   
                        fileOut.write(clientBuffer.array(), 0, passlen);  
                        passlen = channel.read(clientBuffer); 
                    } 
                    System.out.println("上传完毕！"); 
                    fileOut.close();  
                    channel.close(); 
                } 
                clientBuffer.clear();    
            }  
            else if (key.isWritable()) { // 写事件    
                SocketChannel channel = (SocketChannel) key.channel();    
                String flag = (String) key.attachment();     
                if(flag.startsWith("downloading")){//这儿可以新开一个线程   文件操作也可以用NIO 
                    DataInputStream fis = new DataInputStream( 
                            new BufferedInputStream( 
                                    new FileInputStream( 
                                            new File("C:\\",flag.split("#")[1]))));  
                      
                    byte[] buf = new byte[1024]; 
                    int len =0;  
                    while ((len = fis.read(buf))!= -1) {  
                        channel.write(ByteBuffer.wrap(buf, 0, len));   
                    }   
                    fis.close();      
                    System.out.println("文件传输完成"); 
                    channel.close(); 
                } 
                else if(flag.equals("notexists")){  
                    //channel.write(encoder.encode(CharBuffer.wrap(flag)));    
                    channel.write(ByteBuffer.wrap(flag.getBytes())); //不用编码也行    客户端直接接收    中文也不是乱码 
                    channel.close(); 
                } 
                else if(flag.startsWith("UploadReady")){  
                    channel.write(encoder.encode(CharBuffer.wrap("UploadReady")));  
                     
                    //这儿如果不重新注册该通道的读操作    selector选择到该通道的将继续永远是写操作，也就无法跳转到上面的接受上传的处理 
                    SelectionKey sKey =channel.register(selector, SelectionKey.OP_READ);//register是覆盖的????!!! 
                    sKey.attach("read_file#"+flag.split("#")[1]); 
                    //key.attach("read_file#"+flag.split("#")[1]); //select不到读操作 
                } 
                else if(flag.startsWith("DownloadReady")){  
                    channel.write(ByteBuffer.wrap("准备下载".getBytes()));  
                    //channel.write(encoder.encode(CharBuffer.wrap("准备下载")));    
                    key.attach("downloading#"+flag.split("#")[1]); 
                }  
            }   
        }    
     
    public static void main(String[] args) { 
         
        try { 
             System.out.println("等待来至" + port + "端口的客户端连接.....");  
            new NewSocketServer().setListener(); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
 
    } 
} 