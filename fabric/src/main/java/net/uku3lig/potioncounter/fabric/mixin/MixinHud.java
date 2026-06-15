package net.uku3lig.potioncounter.fabric.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.uku3lig.potioncounter.PotionCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Hud.class, priority = 999)
public class MixinHud {
    @Inject(method = "extractEffects", at = @At("HEAD"))
    private void afterRenderOverlay(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        PotionCounter.extractCurrentPlayerPotions(graphics);
    }
}
