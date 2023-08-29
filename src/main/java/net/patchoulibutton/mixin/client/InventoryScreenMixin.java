package net.patchoulibutton.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
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
            if (PatchouliButtonMain.CONFIG.openAllBooksScreen) {
                PatchouliButtonClientPacket.writeC2SModpackScreenPacket(client);
            } else {
                PatchouliAPI.get().openBookGUI(new Identifier(PatchouliButtonMain.CONFIG.bookIdentifier));
            }
    }

    @Inject(method = "drawBackground", at = @At("TAIL"))
    protected void drawBackgroundMixin(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo info) {
        if (this.isPointWithinBounds(PatchouliButtonMain.CONFIG.posX, PatchouliButtonMain.CONFIG.posY, 20, 18, (double) mouseX, (double) mouseY)) {
            context.drawTexture(PatchouliButtonMain.PATCHOULI_BUTTON, this.x + PatchouliButtonMain.CONFIG.posX, this.y + PatchouliButtonMain.CONFIG.posY, 166, 0, 20, 18);
            context.drawTooltip(this.textRenderer, Text.translatable("screen.patchoulibutton"), mouseX, mouseY);
        } else {
            context.drawTexture(PatchouliButtonMain.PATCHOULI_BUTTON, this.x + PatchouliButtonMain.CONFIG.posX, this.y + PatchouliButtonMain.CONFIG.posY, 146, 0, 20, 18);
        }
    }
}
