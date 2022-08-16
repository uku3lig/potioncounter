package net.uku3lig.potioncounter.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.util.registry.Registry;
import net.uku3lig.potioncounter.PotionCounter;
import net.uku3lig.potioncounter.config.Config;
import net.uku3lig.potioncounter.config.Position;
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

@Mixin(value = InGameHud.class, priority = 999)
public class MixinInGameHud {
    private static final ItemStack SPLASH_POT = new ItemStack(Items.SPLASH_POTION);

    @Shadow @Final private MinecraftClient client;
    @Shadow @Final private ItemRenderer itemRenderer;
    private final Config config = PotionCounter.getConfig();

    @Inject(method = "renderStatusEffectOverlay", at = @At("HEAD"))
    private void afterRenderOverlay(MatrixStack matrices, CallbackInfo ci) {
        if (!PotionCounter.getConfig().isEnabled()) return;
        if (client.player == null) return;
        TextRenderer textRenderer = client.textRenderer;

        Stream<ItemStack> stream = client.player.getInventory().main.stream().filter(i -> i.isItemEqual(SPLASH_POT));
        List<ItemStack> items = new ArrayList<>();
        if (config.isShowUpgrades()) {
            stream.collect(Collectors.groupingBy(PotionUtil::getPotion, Collectors.counting()))
                    .entrySet().stream()
                    .filter(e -> e.getKey().getEffects().stream().noneMatch(s -> config.getDisabledPotions().contains(s.getEffectType().getTranslationKey())))
                    .map(e -> PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION, e.getValue().intValue()), e.getKey()))
                    .forEach(items::add);
        } else {
            stream.filter(i -> !PotionUtil.getPotionEffects(i).isEmpty())
                    .map(PotionUtil::getPotionEffects)
                    .map(l -> l.get(0))
                    .map(StatusEffectInstance::getEffectType)
                    .collect(Collectors.groupingBy(e -> e, Collectors.counting()))
                    .entrySet().stream()
                    .filter(e -> !config.getDisabledPotions().contains(e.getKey().getTranslationKey()))
                    .map(e -> {
                        Potion potion = Registry.POTION.stream()
                                .filter(p -> p.getEffects().stream().anyMatch(s -> effectEquals(s.getEffectType(), e.getKey())))
                                .findFirst().orElse(null);
                        return PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION, e.getValue().intValue()), potion);
                    })
                    .filter(i -> !PotionUtil.getPotion(i).equals(Potions.EMPTY))
                    .forEach(items::add);
        }

        Position position = config.getPosition();
        for (int i = 0; i < items.size(); i++) {
            ItemStack item = items.get(i);
            String baseName = Optional.ofNullable(item.getNbt()).map(nbt -> nbt.getString(PotionUtil.POTION_KEY)).orElse(null);

            int textOffset = 0;
            int x = position.isRight() ? this.client.getWindow().getScaledWidth() - 5 : 5;
            int y = (position.isBottom() ? this.client.getWindow().getScaledHeight() - 5 : 5) + 18 * i * (position.isBottom() ? -1 : 1);

            if (baseName != null) {
                if (baseName.contains("long")) {
                    textOffset += 16;
                    itemRenderer.renderGuiItemIcon(new ItemStack(Items.REDSTONE), position.isRight() ? x - 16 - textOffset : x + textOffset, position.isBottom() ? y - 16 : y);
                }
                if (baseName.contains("strong")) {
                    textOffset += 16;
                    itemRenderer.renderGuiItemIcon(new ItemStack(Items.GLOWSTONE_DUST), position.isRight() ? x - 16 - textOffset : x + textOffset, position.isBottom() ? y - 16 : y);
                }
            }

            renderItem(matrices, textRenderer, item, x, y, textOffset, position);
        }
    }

    private void renderItem(MatrixStack matrices, TextRenderer textRenderer, ItemStack item, int x, int y, int textOffset, Position position) {
        String text = String.valueOf(item.getCount());
        int textWidth = textRenderer.getWidth(text);

        itemRenderer.renderGuiItemIcon(item, position.isRight() ? x - 16 : x, position.isBottom() ? y - 16 : y);
        textRenderer.draw(matrices, text, position.isRight() ? x - 18 - textWidth - textOffset : x + 18 + textOffset, (position.isBottom() ? y - 16 : y) + textRenderer.fontHeight / 2f, Color.WHITE.getRGB());
    }

    private boolean effectEquals(StatusEffect first, StatusEffect other) {
        return first.getTranslationKey().equalsIgnoreCase(other.getTranslationKey()) && first.getColor() == other.getColor();
    }
}
