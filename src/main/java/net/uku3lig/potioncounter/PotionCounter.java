package net.uku3lig.potioncounter;

import lombok.Getter;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.util.registry.Registry;
import net.uku3lig.potioncounter.config.PotionCounterConfig;
import net.uku3lig.ukulib.config.ConfigManager;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PotionCounter {
    @Getter
    private static final ConfigManager<PotionCounterConfig> manager = ConfigManager.create(PotionCounterConfig.class, "potioncounter");
    public static final ItemStack SPLASH_POT = new ItemStack(Items.SPLASH_POTION);

    public static List<ItemStack> getPotions(PlayerInventory inventory) {
        Stream<ItemStack> stream = inventory.main.stream().filter(i -> i.isItemEqual(SPLASH_POT));
        if (manager.getConfig().isShowUpgrades()) {
            return stream.collect(Collectors.groupingBy(PotionUtil::getPotion, Collectors.counting()))
                    .entrySet().stream()
                    .filter(e -> e.getKey().getEffects().stream().noneMatch(s -> manager.getConfig().getDisabledPotions().contains(s.getEffectType().getTranslationKey())))
                    .map(e -> PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION, e.getValue().intValue()), e.getKey()))
                    .toList();
        } else {
            return stream.filter(i -> !PotionUtil.getPotionEffects(i).isEmpty())
                    .map(PotionUtil::getPotionEffects)
                    .map(l -> l.get(0))
                    .map(StatusEffectInstance::getEffectType)
                    .collect(Collectors.groupingBy(e -> e, Collectors.counting()))
                    .entrySet().stream()
                    .filter(e -> !manager.getConfig().getDisabledPotions().contains(e.getKey().getTranslationKey()))
                    .map(e -> {
                        Potion potion = Registry.POTION.stream()
                                .filter(p -> p.getEffects().stream().anyMatch(s -> effectEquals(s.getEffectType(), e.getKey())))
                                .findFirst().orElse(null);
                        return PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION, e.getValue().intValue()), potion);
                    })
                    .filter(i -> !PotionUtil.getPotion(i).equals(Potions.EMPTY))
                    .toList();
        }
    }

    public static boolean effectEquals(StatusEffect first, StatusEffect other) {
        return first.getTranslationKey().equalsIgnoreCase(other.getTranslationKey()) && first.getColor() == other.getColor();
    }

    public static void renderPotions(MatrixStack matrices, List<ItemStack> items, int x, int y, int scaledWidth, int scaledHeight, ItemRenderer itemRenderer, TextRenderer textRenderer) {
        if (x == -1 || y == -1) {
            x = 5;
            y = 5;
        }

        // position.isBottom() <=> y >= height / 2 <=> (height / 2) - y <= 0
        boolean isBottom = (scaledWidth / 2f) <= x;
        // position.isRight() <=> x >= width / 2
        boolean isRight = (scaledHeight / 2f) <= y;

        for (int i = 0; i < items.size(); i++) {
            ItemStack item = items.get(i);
            String baseName = Optional.ofNullable(item.getNbt()).map(nbt -> nbt.getString(PotionUtil.POTION_KEY)).orElse(null);

            int textOffset = 0;
            int ly = y + 18 * i * (int) Math.signum((scaledHeight / 2f) - y);

            if (baseName != null && manager.getConfig().isShowUpgrades()) {
                if (baseName.contains("long")) {
                    textOffset += 16;
                    itemRenderer.renderGuiItemIcon(new ItemStack(Items.REDSTONE), isBottom ? x - 16 - textOffset : x + textOffset, isRight ? ly - 16 : ly);
                }
                if (baseName.contains("strong")) {
                    textOffset += 16;
                    itemRenderer.renderGuiItemIcon(new ItemStack(Items.GLOWSTONE_DUST), isBottom ? x - 16 - textOffset : x + textOffset, isRight ? ly - 16 : ly);
                }
            }

            String text = String.valueOf(item.getCount());
            int textWidth = textRenderer.getWidth(text);

            itemRenderer.renderGuiItemIcon(item, isBottom ? x - 16 : x, isRight ? ly - 16 : ly);
            textRenderer.draw(matrices, text, isBottom ? x - 18 - textWidth - textOffset : x + 18 + textOffset, (isRight ? ly - 16 : ly) + textRenderer.fontHeight / 2f, Color.WHITE.getRGB());
        }
    }

    private PotionCounter() {
    }
}
