package com.github.franckyi.wrc.util;

import com.github.franckyi.wrc.WirelessRedstoneControl;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public class ChatUtil {

    public static void sendSuccess(EntityPlayer player, String message) {
        send(player, message, ChatFormatting.GREEN);
    }

    public static void sendInfo(EntityPlayer player, String message) {
        send(player, message, ChatFormatting.BLUE);
    }

    public static void sendError(EntityPlayer player, String message) {
        send(player, message, ChatFormatting.RED);
    }

    public static void send(EntityPlayer player, String message, ChatFormatting color) {
        player.sendMessage(new TextComponentString(color + "[" + WirelessRedstoneControl.NAME + "] " + message + ChatFormatting.RESET));
    }

}
