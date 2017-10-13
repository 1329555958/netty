package io.netty.example.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

/**
 * Created by weichunhe on 2017/10/11.
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf m = (ByteBuf) msg; // (1)

        long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
        System.out.println(new Date(currentTimeMillis) + "=========");

        sendTime(ctx);
    }

    private void sendTime(ChannelHandlerContext ctx) {
        final ByteBuf time = ctx.alloc().buffer(4); // (2)

        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));

        final ChannelFuture f = ctx.writeAndFlush(time); // (3)
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
}