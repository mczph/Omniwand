package com.invadermonky.omniwand.network;

import com.invadermonky.omniwand.handlers.TransformHandler;
import com.invadermonky.omniwand.init.RegistryOW;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageGuiTransform implements IMessage {
    public String modId;

    public MessageGuiTransform() {}

    public MessageGuiTransform(String modId) {
        this.modId = modId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        modId = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, modId);
    }

    public static class MsgHandler implements IMessageHandler<MessageGuiTransform,IMessage> {
        @Override
        public IMessage onMessage(MessageGuiTransform message, MessageContext ctx) {
            EntityPlayer player = ctx.getServerHandler().player;
            ItemStack stack = player.getHeldItemMainhand();
            EnumHand hand = EnumHand.MAIN_HAND;

            boolean hasWand = stack != null && stack.getItem() == RegistryOW.OMNIWAND;

            if(!hasWand) {
                stack = player.getHeldItemOffhand();
                hasWand = stack != null && stack.getItem() == RegistryOW.OMNIWAND;
                hand = EnumHand.OFF_HAND;
            }

            if (hasWand) {
                ItemStack newStack = TransformHandler.getTransformStackForMod(stack, message.modId);
                player.setHeldItem(hand, newStack);
            }
            return null;
        }
    }
}
