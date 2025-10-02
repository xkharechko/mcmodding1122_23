package com.enclave.enclavemod.packets;

import com.enclave.enclavemod.inventory.ContainerEnchantedEnchantment;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSyncSliderPos implements IMessage {
    private int sliderPos;

    public MessageSyncSliderPos() {}
    public MessageSyncSliderPos(int sliderPos) {
        this.sliderPos = sliderPos;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(sliderPos);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        sliderPos = buf.readInt();
    }

    public static class Handler implements IMessageHandler<MessageSyncSliderPos, IMessage> {
        @Override
        public IMessage onMessage(MessageSyncSliderPos message, MessageContext ctx) {
            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
                Container container = ctx.getServerHandler().player.openContainer;
                if (container instanceof ContainerEnchantedEnchantment) {
                    ((ContainerEnchantedEnchantment) container).sliderPos = message.sliderPos;
                }
            });
            return null;
        }
    }
}

