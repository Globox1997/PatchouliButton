package net.patchoulibutton.mixin.compat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import potionstudios.byg.client.BiomepediaInventoryConfig;

@Mixin(BiomepediaInventoryConfig.class)
public class BiomepediaInventoryConfigMixin {

    @Shadow
    @Mutable
    private static BiomepediaInventoryConfig INSTANCE;

    @Inject(method = "Lpotionstudios/byg/client/BiomepediaInventoryConfig;getConfig(ZZ)Lpotionstudios/byg/client/BiomepediaInventoryConfig;", at = @At("RETURN"), cancellable = true, remap = false)
    private static void getConfigMixin(boolean serialize, boolean recreate, CallbackInfoReturnable<BiomepediaInventoryConfig> info) {
        if (info.getReturnValue() != null && INSTANCE != null)
            info.setReturnValue(new BiomepediaInventoryConfig(false, INSTANCE.settings()));
    }

}
