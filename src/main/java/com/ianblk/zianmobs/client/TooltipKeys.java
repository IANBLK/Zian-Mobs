package com.ianblk.zianmobs.client;
import net.minecraft.client.gui.screens.Screen;
/** Client-only key state; reach it through {@code Tooltips.shift()} so dedicated servers never load it. */
public final class TooltipKeys {
 public static boolean shift(){return Screen.hasShiftDown();}
 private TooltipKeys(){}
}
