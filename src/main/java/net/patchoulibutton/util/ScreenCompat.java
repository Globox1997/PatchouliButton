package net.patchoulibutton.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import potionstudios.byg.client.gui.biomepedia.screen.BiomepediaHomeScreen;

@Environment(EnvType.CLIENT)
public class ScreenCompat {

    public static void setBiomepediaScreen(MinecraftClient client) {
        client.setScreen(new BiomepediaHomeScreen(Text.literal("")));
    }

}
