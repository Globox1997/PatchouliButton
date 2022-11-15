package net.patchoulibutton.network;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.patchoulibutton.screen.PatchouliButtonScreen;

@Environment(EnvType.CLIENT)
public class PatchouliButtonClientPacket {

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(PatchouliButtonServerPacket.OPEN_MODPACK_SCREEN_PACKET, (client, handler, buf, sender) -> {
            List<Object> list = new ArrayList<>();
            int count = 1;
            while (buf.isReadable()) {
                if (count != 1 && count % 3 == 0)
                    list.add(buf.readString());
                else
                    list.add(buf.readIdentifier());
                count++;
            }
            client.execute(() -> {
                client.setScreen(new PatchouliButtonScreen(list));
            });
        });
    }

    public static void writeC2SModpackScreenPacket(MinecraftClient client) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        CustomPayloadC2SPacket packet = new CustomPayloadC2SPacket(PatchouliButtonServerPacket.SEND_PATCHOULI_SCREEN_PACKET, buf);
        client.getNetworkHandler().sendPacket(packet);
    }
}
