package io.netty.example.tjlcast.messagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * Created by tangjialiang on 2018/6/21.
 */
public class MsgpackEncoder extends MessageToByteEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        MessagePack messagePack = new MessagePack();

        // Serialize
        byte[] raw = messagePack.write(msg);
        out.writeBytes(raw);
    }
}
