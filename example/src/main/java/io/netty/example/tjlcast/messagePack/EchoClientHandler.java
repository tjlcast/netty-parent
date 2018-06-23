package io.netty.example.tjlcast.messagePack;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by tangjialiang on 2018/6/21.
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    private final int sendNumber;

    public EchoClientHandler(int sendNumber) {
        this.sendNumber = sendNumber;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        UserInfo[] infos = UserInfo();

        for(UserInfo infoE : infos) {
            ctx.write(infoE);
        }
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Client receive the msgpack message : " + msg);
        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private UserInfo[] UserInfo() {
        UserInfo[] userInfos = new UserInfo[sendNumber];
        UserInfo userInfo = null;

        for (int i=0; i<sendNumber; i++) {
            userInfo = new UserInfo();
            userInfo.setAge(i);
            userInfo.setName("ABCDEF ---> " + i);
            userInfos[i] = userInfo;
        }

        return userInfos;
    }
}
