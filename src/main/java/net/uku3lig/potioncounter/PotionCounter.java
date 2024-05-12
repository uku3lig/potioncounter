package net.uku3lig.potioncounter;

import com.google.common.collect.Iterables;
import lombok.Getter;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;
import net.uku3lig.potioncounter.config.PotionCounterConfig;
import net.uku3lig.ukulib.config.ConfigManager;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PotionCounter {
    @Getter
    private static final ConfigManager<PotionCounterConfig> manager = ConfigManager.createDefault(PotionCounterConfig.class, "potioncounter");
    public static final ItemStack SPLASH_POT = new ItemStack(Items.SPLASH_POTION);

    public static List<ItemStack> getPotions(PlayerInventory inventory) {
        Stream<RegistryEntry<Potion>> stream = inventory.main.stream()
                .filter(stack -> stack.isOf(SPLASH_POT.getItem()))
                .map(stack -> stack.get(DataComponentTypes.POTION_CONTENTS))
                .filter(Objects::nonNull)
                .filter(comp -> comp.potion().isPresent())
                .map(comp -> comp.potion().get());

        if (manager.getConfig().isShowUpgrades()) {
            return stream.collect(Collectors.groupingBy(pot -> pot, Collectors.counting()))
                    .entrySet().stream()
                    .filter(entry -> Iterables.all(entry.getKey().value().getEffects(), eff -> !manager.getConfig().getDisabledPotions().contains(eff.getEffectType().value().getTranslationKey())))
                    .map(entry -> PotionContentsComponent.createStack(Items.SPLASH_POTION, entry.getKey()).copyWithCount(entry.getValue().intValue()))
                    .toList();
        } else {
            return stream
                    .map(pot -> pot.value().getEffects())
                    .filter(effs -> !effs.isEmpty())
                    .collect(Collectors.groupingBy(l -> l.getFirst().getEffectType(), Collectors.counting()))
                    .entrySet().stream()
                    .filter(entry -> !manager.getConfig().getDisabledPotions().contains(entry.getKey().value().getTranslationKey()))
                    .map(entry -> {
                        ItemStack stack = new ItemStack(Items.SPLASH_POTION, entry.getValue().intValue());
                        stack.set(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT.with(new StatusEffectInstance(entry.getKey())));
                        return stack;
                    })
                    .toList();
        }
    }

    public static void renderPotions(DrawContext context, List<ItemStack> items, int x, int y, int scaledWidth, int scaledHeight, TextRenderer textRenderer) {
        if (x == -1 || y == -1) {
            x = 5;
            y = 5;
        }

        boolean isBottom = y > (scaledHeight / 2f);
        boolean isRight = x > (scaledWidth / 2f);

        for (int i = 0; i < items.size(); i++) {
            ItemStack item = items.get(i);
            String baseName = Optional.ofNullable(item.get(DataComponentTypes.POTION_CONTENTS))
                    .flatMap(PotionContentsComponent::potion)
                    .map(RegistryEntry::getIdAsString)
                    .orElse(null);

            int textOffset = 0;
            int ly = y + 18 * i * (int) Math.signum((scaledHeight / 2f) - y);

            if (baseName != null && manager.getConfig().isShowUpgrades()) {
                if (baseName.contains("long")) {
                    textOffset += 16;
                    context.drawItem(new ItemStack(Items.REDSTONE), isBottom ? x - 16 - textOffset : x + textOffset, isRight ? ly - 16 : ly);
                }
                if (baseName.contains("strong")) {
                    textOffset += 16;
                    context.drawItem(new ItemStack(Items.GLOWSTONE_DUST), isBottom ? x - 16 - textOffset : x + textOffset, isRight ? ly - 16 : ly);
                }
            }

            String text = String.valueOf(item.getCount());
            int textWidth = textRenderer.getWidth(text);

            context.drawItem(item, isRight ? x - 16 : x, isBottom ? ly - 16 : ly);
            context.drawText(textRenderer, text, isRight ? x - 18 - textWidth - textOffset : x + 18 + textOffset, (isBottom ? ly - 16 : ly) + textRenderer.fontHeight / 2, Color.WHITE.getRGB(), true);
        }
    }

    private PotionCounter() {
    }
}
