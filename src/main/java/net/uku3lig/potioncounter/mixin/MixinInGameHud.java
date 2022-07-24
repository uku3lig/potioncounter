package net.uku3lig.potioncounter.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.uku3lig.potioncounter.PotionCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mixin(InGameHud.class)
public class MixinInGameHud {
    private static final ItemStack SPLASH_POT = new ItemStack(Items.SPLASH_POTION);

    @Shadow @Final private MinecraftClient client;

    @Shadow @Final private ItemRenderer itemRenderer;

    @Inject(method = "renderStatusEffectOverlay", at = @At("RETURN"))
    private void afterRenderOverlay(MatrixStack matrices, CallbackInfo ci) {
        if (!PotionCounter.getConfig().isEnabled()) return;
        if (client.player == null) return;
        TextRenderer textRenderer = client.textRenderer;

        Stream<ItemStack> stream = client.player.getInventory().main.stream().filter(i -> i.isItemEqual(SPLASH_POT));
        List<ItemStack> items = new ArrayList<>();
        if (PotionCounter.getConfig().isShowUpgrades()) {
            stream.collect(Collectors.groupingBy(PotionUtil::getPotion, Collectors.counting()))
                    .entrySet().stream()
                    .map(e -> PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION, e.getValue().intValue()), e.getKey()))
                    .forEach(items::add);
        } else {
            stream.filter(i -> !PotionUtil.getPotionEffects(i).isEmpty())
                    .map(PotionUtil::getPotionEffects)
                    .map(l -> l.get(0))
                    .map(StatusEffectInstance::getEffectType)
                    .collect(Collectors.groupingBy(e -> e, Collectors.counting()))
                    .entrySet().stream()
                    .map(e -> PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION, e.getValue().intValue()), new Potion(new StatusEffectInstance(e.getKey()))))
                    .forEach(items::add);
        }

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
