package net.uku3lig.potioncounter.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Mixin(InGameHud.class)
public class MixinInGameHud {
    private static final ItemStack SPLASH_POT = new ItemStack(Items.SPLASH_POTION);
    @Shadow @Final private MinecraftClient client;

    @Shadow @Final private ItemRenderer itemRenderer;

    @Inject(method = "renderStatusEffectOverlay", at = @At("RETURN"))
    private void afterRenderOverlay(MatrixStack matrices, CallbackInfo ci) {
        if (client.player == null) return;
        TextRenderer textRenderer = client.textRenderer;

        Map<Potion, Long> counts = client.player.getInventory().main.stream()
                .filter(i -> i.isItemEqual(SPLASH_POT))
                .collect(Collectors.groupingBy(PotionUtil::getPotion, Collectors.counting()));
        List<ItemStack> items = counts.keySet().stream().map(p -> PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION, counts.get(p).intValue()), p)).toList();

        for (int i = 0; i < items.size(); i++) {
            ItemStack item = items.get(i);
            String baseName = Optional.ofNullable(item.getNbt()).map(nbt -> nbt.getString(PotionUtil.POTION_KEY)).orElse(null);

            int textOffset = 0;
            if (baseName != null) {
                if (baseName.contains("long")) {
                    textOffset += 16;
                    itemRenderer.renderGuiItemIcon(new ItemStack(Items.REDSTONE), 5 + textOffset, 5 + 16*i);
                }
                if (baseName.contains("strong")) {
                    textOffset += 16;
                    itemRenderer.renderGuiItemIcon(new ItemStack(Items.GLOWSTONE_DUST), 5 + textOffset, 5 + 16*i);
                }
            }

            itemRenderer.renderGuiItemIcon(item, 5, 5 + 16*i);
            textRenderer.draw(matrices, String.valueOf(item.getCount()), 5 + 16 + 2f + textOffset, 5 + 16*i + textRenderer.fontHeight / 2f, Color.WHITE.getRGB());
        }
    }
}
