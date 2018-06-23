package io.netty.example.tjlcast.messagePack;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by tangjialiang on 2018/6/21.
 */
public class EchoServerV2 {

    public void run() throws Exception {
        // Configure the server
        EventLoopGroup acceptorGroup = new NioEventLoopGroup();
        EventLoopGroup IOGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(acceptorGroup, IOGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("msgpack decoder", new LengthFieldBasedFrameDecoder(655535, 0, 2, 0, 2));
                            ch.pipeline().addLast("msgpack decoder", new MsgpackDecoder());
                            ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
                            ch.pipeline().addLast("msgpack encoder", new MsgpackEncoder());
                            ch.pipeline().addLast(null);
                        }
                    });
        } finally {
            acceptorGroup.shutdownGracefully();
            IOGroup.shutdownGracefully();
        }
    }

}
