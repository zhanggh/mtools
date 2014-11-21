///**
// * @author 张广海
// * @date 2014-7-19
// */
//package com.mtools.core.plugin.netty.server;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.Channel;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import io.netty.handler.logging.LogLevel;
//import io.netty.handler.logging.LoggingHandler;
//import io.netty.handler.ssl.SslContext;
//import io.netty.handler.ssl.util.SelfSignedCertificate;
//
//import com.mtools.core.plugin.helper.SpringUtil;
//import com.mtools.core.plugin.properties.CoreParams;
//
///**
// *  功能：netty服务器
// */
//public class HttpNettyServer {
//
//    static boolean SSL = true;//System.getProperty("ssl") != null;
//    static final int PORT = Integer.parseInt(System.getProperty("port", SSL? "8443" : "8080"));
//
//    public static  Log log= LogFactory.getLog(HttpNettyServer.class);
//    
//    public static void main(String[] args) throws Exception {
//    	log.info("服务开始启动");
//    	//初始化配置
//    	SpringUtil.initSpringCfg("core-config.xml");
//    	CoreParams coreParams = (CoreParams) SpringUtil.getAnoBean("coreParams");
//    	HttpServerInitializer httpServerInitializer =(HttpServerInitializer) SpringUtil.getAnoBean("httpServerInitializer");
//    	SSL=Boolean.parseBoolean(coreParams.getSsl());
//        // Configure SSL.
//        final SslContext sslCtx;
//        if (SSL) {
//            SelfSignedCertificate ssc = new SelfSignedCertificate();
//            sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
//            httpServerInitializer.setSslCtx(sslCtx);
//        } else {
//            sslCtx = null;
//        }
//
//        // Configure the server.
//        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//        try {
//            ServerBootstrap b = new ServerBootstrap();
//            b.group(bossGroup, workerGroup)
//             .channel(NioServerSocketChannel.class)
//             .handler(new LoggingHandler(LogLevel.INFO))
//             .childHandler(httpServerInitializer);
//
//            Channel ch = b.bind(PORT).sync().channel();
//
//            log.info("Open your web browser and navigate to " +
//                    (SSL? "https" : "http") + "://127.0.0.1:" + PORT + '/');
//
//            ch.closeFuture().sync();
//        } finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//            log.info("服务已关闭");
//        }
//    }
//}
