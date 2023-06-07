package net.uku3lig.potioncounter.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.item.ItemStack;
import net.uku3lig.potioncounter.PotionCounter;
import net.uku3lig.potioncounter.config.PotionCounterConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = InGameHud.class, priority = 999)
public class MixinInGameHud {
    @Shadow @Final private MinecraftClient client;
    @Shadow private int scaledHeight;
    @Shadow private int scaledWidth;

    @Inject(method = "renderStatusEffectOverlay", at = @At("HEAD"))
    private void afterRenderOverlay(DrawContext context, CallbackInfo ci) {
        if (!PotionCounter.getManager().getConfig().isEnabled()) return;
        if (client.player == null) return;
        PotionCounterConfig config = PotionCounter.getManager().getConfig();
        TextRenderer textRenderer = client.textRenderer;
        List<ItemStack> items = PotionCounter.getPotions(client.player.getInventory());

        PotionCounter.renderPotions(context, items, config.getX(), config.getY(), scaledWidth, scaledHeight, textRenderer);
    }
}
