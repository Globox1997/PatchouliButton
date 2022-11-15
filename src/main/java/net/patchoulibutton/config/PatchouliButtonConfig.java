package net.patchoulibutton.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "patchoulibutton")
@Config.Gui.Background("minecraft:textures/block/stone.png")
public class PatchouliButtonConfig implements ConfigData {

    @Comment("Patchouli book identifier")
    public String bookIdentifier = "";
    public int posX = 127;
    public int posY = 61;
    @Comment("If false, open directly modpack book")
    public boolean openAllBooksScreen = true;
}
