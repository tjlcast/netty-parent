package io.netty.example.tjlcast.messagePack;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by tangjialiang on 2018/6/21.
 */
public class EchoClient {

    private final String host;
    private final int port;
    private final int sendNumber;

    public EchoClient(String host, int port, int sendNumber) {
        this.host = host;
        this.port = port;
        this.sendNumber = sendNumber;
    }

    public void run() throws Exception {
        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("msgpack decoder", new MsgpackDecoder());
                            ch.pipeline().addLast("msgpac encoder", new MsgpackEncoder());
                            ch.pipeline().addLast(new EchoClientHandler(sendNumber));
                        }
                    });

            ChannelFuture future = b.connect(host, port).sync();

            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
