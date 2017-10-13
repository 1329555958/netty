package io.netty.example.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;

import java.util.Date;

/**
 * Created by weichunhe on 2017/10/11.
 */
@ChannelHandler.Sharable
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(final ChannelHandlerContext ctx) { // (1)

        sendTime(ctx);


    }

    private void sendTime(ChannelHandlerContext ctx) {
        final ByteBuf time = ctx.alloc().buffer(4); // (2)

        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));

        final ChannelFuture f = ctx.writeAndFlush(time); // (3)
    }

    private double random = Math.random();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf m = (ByteBuf) msg; // (1)

        long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
        System.out.println(new Date(currentTimeMillis) + "========="+random);
        sendTime(ctx);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
}