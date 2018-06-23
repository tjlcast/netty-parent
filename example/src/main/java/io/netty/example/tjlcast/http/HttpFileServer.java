package io.netty.example.tjlcast.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Created by tangjialiang on 2018/6/21.
 */
public class HttpFileServer {

    // 默认url路径
    private static final String DEFAULT_URL = "/src/com/phei/netty";

    public void run(final int port, final String url) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // http 相关编码逻辑
                            ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());    // HTTP请求消息解码器。在每个 HTTP 消息中会生成多个消息对象。
                            ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));  // 将多个消息转化为单一 FullHttpRequest 或者 FullHttpResponse

                            // http 相关解码逻辑
                            ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                            ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());   // 主要用于支持异步发送大码流

                            // http 相关处理逻辑
                            ch.pipeline().addLast("fileServerHandler", new HttpFileServerHandler(url));
                        }
                    });

            ChannelFuture f = b.bind("192.168.1.102", port).sync();

            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;

        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        String url = DEFAULT_URL;
        if (args.length > 1) {
            url = args[1];
            new HttpFileServer().run(port, url);
        }
    }
}
