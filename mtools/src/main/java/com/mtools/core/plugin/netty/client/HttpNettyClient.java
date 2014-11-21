///**
// * @author 张广海
// * @date 2014-7-19
// */
//package com.mtools.core.plugin.netty.client;
//
//import io.netty.bootstrap.Bootstrap;
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.Unpooled;
//import io.netty.channel.Channel;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import io.netty.handler.codec.http.ClientCookieEncoder;
//import io.netty.handler.codec.http.DefaultCookie;
//import io.netty.handler.codec.http.DefaultFullHttpRequest;
//import io.netty.handler.codec.http.HttpHeaders;
//import io.netty.handler.codec.http.HttpMethod;
//import io.netty.handler.codec.http.HttpVersion;
//import io.netty.handler.ssl.SslContext;
//import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
//
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.nio.charset.Charset;
//
//import javax.net.ssl.SSLException;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.stereotype.Component;
//
//import com.mtools.core.plugin.helper.SpringUtil;
//import com.mtools.core.plugin.properties.CoreParams;
//
///**
// *  功能：netty客户端
// */
//@Component("httpNettyClient")
//public class HttpNettyClient {
//
//	public   Log log= LogFactory.getLog(HttpNettyClient.class);
//    static final String URL = System.getProperty("url", "https://127.0.0.1:8282/aipg/ProcessServlet");
//    private Channel ch = null;
//    private  EventLoopGroup group = null;
//    private String host = "127.0.0.1" ;
//    private URI uri =null;
//    
//    public void initChannel() throws SSLException, URISyntaxException{
//    	log.info("启动netty客户端连接");
////    	CoreParams coreParams = (CoreParams) SpringUtil.getAnoBean("coreParams");
//        uri = new URI(URL);
////        URI uri = new URI(coreParams.getTranxUrl());
//        String scheme = uri.getScheme() == null? "http" : uri.getScheme();
//        host = uri.getHost() == null? "127.0.0.1" : uri.getHost();
//        int port = uri.getPort();
//        if (port == -1) {
//            if ("http".equalsIgnoreCase(scheme)) {
//                port = 80;
//            } else if ("https".equalsIgnoreCase(scheme)) {
//                port = 443;
//            }
//        }
//
//        if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
//            System.err.println("Only HTTP(S) is supported.");
//            return;
//        }
//
//        // Configure SSL context if necessary.
//        final boolean ssl = "https".equalsIgnoreCase(scheme);
//        final SslContext sslCtx;
//        if (ssl) {
//            sslCtx = SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);
//        } else {
//            sslCtx = null;
//        }
//
//        // Configure the client.
//        group = new NioEventLoopGroup();
//        try {
//            Bootstrap b = new Bootstrap();
//            b.group(group)
//             .channel(NioSocketChannel.class)
//             .handler(new HttpNettyClientInitializer(sslCtx));
//
//            // Make the connection attempt.
//            ch = b.connect(host, port).sync().channel();
//            log.info("启动netty客户端连接完成");
//        }catch(Exception ex){
//        	log.error("启动异常:"+ex.getMessage());
//        }finally {
//            // Shut down executor threads to exit.
//            group.shutdownGracefully();
//        }
//    }
//    
//    
//    public void sendMsg(String msg){
//    	log.info("netty客户端发送请求");
//        // Prepare the HTTP request.
//        DefaultFullHttpRequest request = new DefaultFullHttpRequest(
//                HttpVersion.HTTP_1_1, HttpMethod.GET, uri.getRawPath(),Unpooled.copiedBuffer(msg,Charset.forName("GBK")));
//        request.headers().set(HttpHeaders.Names.HOST, host);
//        request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
//        request.headers().set(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP);
//        
//        // Set some example cookies.
//        request.headers().set(
//                HttpHeaders.Names.COOKIE,
//                ClientCookieEncoder.encode(
//                        new DefaultCookie("my-cookie", "foo"),
//                        new DefaultCookie("another-cookie", "bar")));
//
//        // Send the HTTP request.
//        ch.writeAndFlush(request);
//        log.info("netty客户端发送请求完毕");
//    }
//    
//    public void closeClient() throws InterruptedException{
//    	log.info("关闭netty客户端连接");
//    	  // Wait for the server to close the connection.
//        ch.closeFuture().sync();
//        // Shut down executor threads to exit.
//        group.shutdownGracefully();
//    }
//    
//    public static void main(String[] args) throws SSLException, URISyntaxException, InterruptedException{
//    	HttpNettyClient client = new HttpNettyClient();
//    	client.initChannel();
//    	client.sendMsg("hellozhang");
//    	client.closeClient();
//    }
//}
