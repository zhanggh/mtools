/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.mtools.core.plugin.netty.server;

import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.AsciiString;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.mtools.core.plugin.helper.SpringUtil;
import com.mtools.core.plugin.notify.AsyncNotify;
import com.mtools.core.plugin.properties.CoreParams;

@Component("httpServerHandler")
public class HttpServerHandler extends SimpleChannelInboundHandler<Object> {

	public  Log log= LogFactory.getLog(this.getClass());
    private HttpRequest request;
    /** Buffer that stores the response content */
    private final StringBuilder buf = new StringBuilder();

    private static final AsciiString CONTENT_TYPE = new AsciiString("Content-Type");
    private static final AsciiString CONTENT_LENGTH = new AsciiString("Content-Length");
    private static final AsciiString CONNECTION = new AsciiString("Connection");
    private static final AsciiString KEEP_ALIVE = new AsciiString("keep-alive");
	private AsyncNotify notify;
	private TaskExecutor taskExecutor;
	private HttpProcessAdpter processAdpter;
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
    	log.info("读取请求完成!");
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
    	log.info("开始读取请求信息！");
    	String responeMsg="";
        if (msg instanceof HttpRequest) {
            HttpRequest request = this.request = (HttpRequest) msg;
            log.info("请求URI:"+request.uri()); 
            CoreParams coreParams = (CoreParams) SpringUtil.getAnoBean("coreParams");
            if(!coreParams.getTranxUri().equals(request.uri().split("?")[0])){
            	writeResponseExt(OK,ctx,"无效请求");
            	return;
            }
            if (HttpHeaders.is100ContinueExpected(request)) {
                send100Continue(ctx);
            }
            buf.setLength(0);
        }

        if (msg instanceof HttpContent) {
        	log.info("开始读取请求httpContent信息！");
            HttpContent httpContent = (HttpContent) msg;

            ByteBuf content = httpContent.content();
            if (content.isReadable()) {
            	log.info("CONTENT: ");
                log.info(content.toString(CharsetUtil.UTF_8));
                appendDecoderResult(buf, request);
                if(processAdpter==null)
                	processAdpter = (HttpProcessAdpter) SpringUtil.getAnoBean("processAdpter");
                responeMsg = processAdpter.reqAdapter(content.toString(CharsetUtil.UTF_8));
            }

            if (msg instanceof LastHttpContent) {
                log.info("开始读取请求LastHttpContent信息！");
                LastHttpContent trailer = (LastHttpContent) msg;
                if (!trailer.trailingHeaders().isEmpty()) {
                    buf.append("\r\n");
                    for (String name: trailer.trailingHeaders().names()) {
                        for (String value: trailer.trailingHeaders().getAll(name)) {
                        	 log.info("TRAILING HEADER: ");
                        	 log.info(name+" = "+value+"\r\n");
                        }
                    }
                }

                writeResponseExt(OK,ctx,responeMsg);
            }
        }
    }

    private  void appendDecoderResult(StringBuilder buf, HttpObject o) {
        DecoderResult result = o.decoderResult();
        if (result.isSuccess()) {
        	log.debug("解码成功!");
            return;
        }
        log.debug("解码失败!");
        buf.append(".. WITH DECODER FAILURE: ");
        buf.append(result.cause());
        buf.append("\r\n");
    }

    private boolean writeResponseExt(HttpResponseStatus status,ChannelHandlerContext ctx,String content){
    	
    	log.info("返回客户端信息");
        // Decide whether to close the connection or not.
        boolean keepAlive = HttpHeaders.isKeepAlive(request);
        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, status,
                Unpooled.copiedBuffer(content,Charset.forName("GBK")));
         
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

        if (keepAlive) {
            // Add 'Content-Length' header only for a keep-alive connection.
            response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
            // Add keep alive header as per:
            // - http://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }

        if (!keepAlive) {
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            response.headers().set(CONNECTION, KEEP_ALIVE);
            ctx.write(response);
        }
        return keepAlive;
    }
    
    private  void send100Continue(ChannelHandlerContext ctx) {
    	log.debug("****************send100Continue*****************");
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, CONTINUE);
        ctx.write(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	log.debug("****************发生了异常*****************");
		try {
			cause.printStackTrace();
	        if(notify==null)
				notify= (AsyncNotify) SpringUtil.getAnoBean("sysRunningNotify");
			notify.initData(null, cause,"");
			if(taskExecutor==null)
				taskExecutor=(TaskExecutor) SpringUtil.getAnoBean("taskExecutor");
			taskExecutor.execute(notify);
	        ctx.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
