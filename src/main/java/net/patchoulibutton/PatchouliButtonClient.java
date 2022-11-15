package net.patchoulibutton;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.patchoulibutton.network.PatchouliButtonClientPacket;

@Environment(EnvType.CLIENT)
public class PatchouliButtonClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        PatchouliButtonClientPacket.init();
    }

}
