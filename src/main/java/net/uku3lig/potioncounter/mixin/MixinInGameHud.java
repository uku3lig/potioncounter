package net.uku3lig.potioncounter.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(InGameHud.class)
public class MixinInGameHud {
    private static final ItemStack HEAL_POT = PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.HEALING);
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "renderStatusEffectOverlay", at = @At("RETURN"))
    private void afterRenderOverlay(MatrixStack matrices, CallbackInfo ci) {
        if (client.player == null) return;

        TextRenderer textRenderer = client.textRenderer;
        ItemRenderer itemRenderer = client.getItemRenderer();

        long count = client.player.getInventory().main.stream()
                .filter(i -> i.isItemEqual(HEAL_POT))
                .count();

        ItemStack item = PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION, (int) count), Potions.HEALING);

        itemRenderer.renderGuiItemIcon(item, 5, 5);
        textRenderer.draw(matrices, String.valueOf(item.getCount()), 5 + 16 + 2f, 5 + textRenderer.fontHeight / 2f, Color.WHITE.getRGB());
    }
}
