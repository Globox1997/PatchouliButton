package net.patchoulibutton.screen;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.patchoulibutton.PatchouliButtonMain;
import net.patchoulibutton.util.ScreenCompat;
import vazkii.patchouli.api.PatchouliAPI;

@Environment(EnvType.CLIENT)
public class PatchouliButtonScreen extends Screen {

    private List<Object> list = new ArrayList<>();
    private int page = 0;

    public PatchouliButtonScreen(List<Object> list) {
        super(Text.translatable("screen.patchoulibutton.title"));
        this.list.clear();
        this.list.addAll(list);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, PatchouliButtonMain.PATCHOULI_BUTTON);

        this.drawTexture(matrices, this.width / 2 - 73, this.height / 2 - 90, 0, 0, 146, 180);
        this.textRenderer.draw(matrices, this.title, this.width / 2 - this.textRenderer.getWidth(this.title) / 2, this.height / 2 - 75, 0x000000);

        int u = this.page * 6 * 3;
        int count = 0;
        for (; u < this.list.size(); u += 3) {
            if (this.isPointWithinBounds(-62, -60 + ((u / 3) - this.page * 6) * 20, 110, 18, (double) mouseX, (double) mouseY)) {
                RenderSystem.setShaderTexture(0, PatchouliButtonMain.PATCHOULI_BUTTON);
                RenderSystem.enableBlend();
                this.drawTexture(matrices, this.width / 2 - 62, this.height / 2 - 61 + ((u / 3) - this.page * 6) * 20, 0, 200, 120, 18);
                RenderSystem.disableBlend();
            }

            this.itemRenderer.renderInGui(new ItemStack(Registry.ITEM.get((Identifier) this.list.get(u + 1))), this.width / 2 - 60, this.height / 2 - 60 + ((u / 3) - this.page * 6) * 20);

            String text = Text.translatable((String) this.list.get(u + 2)).getString();
            boolean isTooLong = this.textRenderer.getWidth(text) > 90;
            if (isTooLong)
                text = text.substring(0, 14) + "..";

            this.textRenderer.draw(matrices, text, this.width / 2 - 36, this.height / 2 - 60 + ((u / 3) - this.page * 6) * 20 + 4, 0x000000);
            if (isTooLong && this.isPointWithinBounds(-62, -60 + ((u / 3) - this.page * 6) * 20, 110, 18, (double) mouseX, (double) mouseY))
                this.renderTooltip(matrices, Text.translatable((String) this.list.get(u + 2)), mouseX, mouseY);

            count++;
            if (count >= 6 || count >= (this.list.size() - this.page * 6 * 3) / 3)
                break;
        }

        if (this.list.size() / 18 > 0) {
            RenderSystem.setShaderTexture(0, PatchouliButtonMain.PATCHOULI_BUTTON);
            if (this.list.size() / 18 > this.page) {
                // right
                if (this.isPointWithinBounds(30, 65, 18, 10, (double) mouseX, (double) mouseY))
                    this.drawTexture(matrices, this.width / 2 + 30, this.height / 2 + 65, 18, 180, 18, 10);
                else
                    this.drawTexture(matrices, this.width / 2 + 30, this.height / 2 + 65, 0, 180, 18, 10);
            }
            if (this.page > 0) {
                // left
                if (this.isPointWithinBounds(-55, 65, 18, 10, (double) mouseX, (double) mouseY))
                    this.drawTexture(matrices, this.width / 2 - 55, this.height / 2 + 65, 18, 190, 18, 10);
                else
                    this.drawTexture(matrices, this.width / 2 - 55, this.height / 2 + 65, 0, 190, 18, 10);
            }
        }
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.list.size() / 18 > 0) {
            if (this.list.size() / 18 > this.page && this.isPointWithinBounds(30, 65, 18, 10, (double) mouseX, (double) mouseY)) {
                this.page++;
                return true;
            }
            if (this.page > 0 && this.isPointWithinBounds(-55, 65, 18, 10, (double) mouseX, (double) mouseY)) {
                this.page--;
                return true;
            }
        }

        int u = this.page * 6 * 3;
        int count = 0;
        for (; u < this.list.size(); u += 3) {
            if (this.isPointWithinBounds(-62, -60 + ((u / 3) - this.page * 6) * 20, 110, 18, (double) mouseX, (double) mouseY)) {
                if (PatchouliButtonMain.isBYGLoaded && ((Identifier) this.list.get(u)).equals(new Identifier("byg", "biomepedia"))) {
                    ScreenCompat.setBiomepediaScreen(this.client);
                } else {
                    PatchouliAPI.get().openBookGUI((Identifier) this.list.get(u));
                }
                return true;
            }

            count++;
            if (count >= 6 || count >= (this.list.size() - this.page * 6 * 3) / 3)
                break;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean isPointWithinBounds(int x, int y, int width, int height, double pointX, double pointY) {
        int i = this.width / 2;
        int j = this.height / 2;
        return (pointX -= (double) i) >= (double) (x - 1) && pointX < (double) (x + width + 1) && (pointY -= (double) j) >= (double) (y - 1) && pointY < (double) (y + height + 1);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.client != null && this.client.options.inventoryKey.matchesKey(keyCode, scanCode))
            this.client.setScreen(new InventoryScreen(this.client.player));
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

}
