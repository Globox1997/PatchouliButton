package net.patchoulibutton.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.patchoulibutton.PatchouliButtonMain;
import net.patchoulibutton.network.PatchouliButtonClientPacket;
import vazkii.patchouli.api.PatchouliAPI;

@Environment(EnvType.CLIENT)
@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> {

    public InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void mouseClickedMixin(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> info) {
        if (this.client != null && this.focusedSlot == null && this.isPointWithinBounds(PatchouliButtonMain.CONFIG.posX, PatchouliButtonMain.CONFIG.posY, 20, 18, (double) mouseX, (double) mouseY))
            if (PatchouliButtonMain.CONFIG.openAllBooksScreen)
                PatchouliButtonClientPacket.writeC2SModpackScreenPacket(client);
            else
                PatchouliAPI.get().openBookGUI(new Identifier(PatchouliButtonMain.CONFIG.bookIdentifier));

    }

    @Inject(method = "drawBackground", at = @At("TAIL"))
    protected void drawBackgroundMixin(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo info) {
        RenderSystem.setShaderTexture(0, PatchouliButtonMain.PATCHOULI_BUTTON);
        if (this.isPointWithinBounds(PatchouliButtonMain.CONFIG.posX, PatchouliButtonMain.CONFIG.posY, 20, 18, (double) mouseX, (double) mouseY)) {
            this.drawTexture(matrices, this.x + PatchouliButtonMain.CONFIG.posX, this.y + PatchouliButtonMain.CONFIG.posY, 166, 0, 20, 18);
            this.renderTooltip(matrices, Text.translatable("screen.patchoulibutton"), mouseX, mouseY);
        } else
            this.drawTexture(matrices, this.x + PatchouliButtonMain.CONFIG.posX, this.y + PatchouliButtonMain.CONFIG.posY, 146, 0, 20, 18);

    }
}
