package com.github.franckyi.wsc.util;

import com.github.franckyi.wsc.ModReference;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public class ChatUtil {

	public static void sendError(EntityPlayer player, String message) {
		player.sendMessage(new TextComponentString(
				ChatFormatting.RED + "[" + ModReference.MODID.toUpperCase() + "] " + message + ChatFormatting.RESET));
	}

	public static void sendSuccess(EntityPlayer player, String message) {
		player.sendMessage(new TextComponentString(
				ChatFormatting.GREEN + "[" + ModReference.MODID.toUpperCase() + "] " + message + ChatFormatting.RESET));
	}

	public static void sendInfo(EntityPlayer player, String message) {
		player.sendMessage(new TextComponentString(
				ChatFormatting.BLUE + "[" + ModReference.MODID.toUpperCase() + "] " + message + ChatFormatting.RESET));
	}

}
