package net.uku3lig.potioncounter.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import net.uku3lig.potioncounter.PotionCounter;
import net.uku3lig.potioncounter.config.PotionCounterConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = Gui.class, priority = 999)
public class MixinGui {
    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "extractEffects", at = @At("HEAD"))
    private void afterRenderOverlay(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (!PotionCounter.getManager().getConfig().isEnabled()) return;
        if (minecraft.player == null) return;
        PotionCounterConfig config = PotionCounter.getManager().getConfig();
        Font textRenderer = minecraft.font;
        List<ItemStack> items = PotionCounter.getPotions(minecraft.player.getInventory());

        PotionCounter.extractPotions(graphics, items, config.getX(), config.getY(), graphics.guiWidth(), graphics.guiHeight(), textRenderer);
    }
}
