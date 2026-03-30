package net.uku3lig.potioncounter;

import com.google.common.collect.Iterables;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.uku3lig.potioncounter.config.PotionCounterConfig;
import net.uku3lig.ukulib.config.ConfigManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PotionCounter {
    @Getter
    private static final ConfigManager<PotionCounterConfig> manager = ConfigManager.createDefault(PotionCounterConfig.class, "potioncounter");
    public static final ItemStackTemplate SPLASH_POT = new ItemStackTemplate(Items.SPLASH_POTION);
    public static final ItemStackTemplate GLOWSTONE = new ItemStackTemplate(Items.GLOWSTONE_DUST);
    public static final ItemStackTemplate REDSTONE = new ItemStackTemplate(Items.REDSTONE);

    public static List<ItemStack> getPotions(Inventory inventory) {
        Stream<Holder<@NotNull Potion>> stream = inventory.getNonEquipmentItems().stream()
                .filter(stack -> stack.is(SPLASH_POT.item()))
                .map(stack -> stack.get(DataComponents.POTION_CONTENTS))
                .filter(Objects::nonNull)
                .filter(comp -> comp.potion().isPresent())
                .map(comp -> comp.potion().get());

        if (manager.getConfig().isShowUpgrades()) {
            return stream.collect(Collectors.groupingBy(pot -> pot, Collectors.counting()))
                    .entrySet().stream()
                    .filter(entry -> Iterables.all(entry.getKey().value().getEffects(), eff -> !manager.getConfig().getDisabledPotions().contains(eff.getEffect().value().getDescriptionId())))
                    .map(entry -> PotionContents.createItemStack(Items.SPLASH_POTION, entry.getKey()).copyWithCount(entry.getValue().intValue()))
                    .toList();
        } else {
            return stream
                    .map(pot -> pot.value().getEffects())
                    .filter(effs -> !effs.isEmpty())
                    .collect(Collectors.groupingBy(l -> l.getFirst().getEffect(), Collectors.counting()))
                    .entrySet().stream()
                    .filter(entry -> !manager.getConfig().getDisabledPotions().contains(entry.getKey().value().getDescriptionId()))
                    .map(entry -> {
                        ItemStack stack = new ItemStack(Items.SPLASH_POTION, entry.getValue().intValue());
                        stack.set(DataComponents.POTION_CONTENTS, PotionContents.EMPTY.withEffectAdded(new MobEffectInstance(entry.getKey())));
                        return stack;
                    })
                    .toList();
        }
    }

    public static void extractCurrentPlayerPotions(GuiGraphicsExtractor graphics) {
        Minecraft minecraft = Minecraft.getInstance();
        PotionCounterConfig config = manager.getConfig();
        if (!config.isEnabled()) return;
        if (minecraft.player == null) return;

        List<ItemStack> items = PotionCounter.getPotions(minecraft.player.getInventory());
        PotionCounter.extractPotions(graphics, items, config.getX(), config.getY(), graphics.guiWidth(), graphics.guiHeight(), minecraft.font);
    }

    public static void extractPotions(GuiGraphicsExtractor graphics, List<ItemStack> items, int x, int y, int scaledWidth, int scaledHeight, Font textRenderer) {
        if (x == -1 || y == -1) {
            x = 5;
            y = 5;
        }

        boolean isBottom = y > (scaledHeight / 2f);
        boolean isRight = x > (scaledWidth / 2f);

        for (int i = 0; i < items.size(); i++) {
            ItemStack item = items.get(i);
            String baseName = Optional.ofNullable(item.get(DataComponents.POTION_CONTENTS))
                    .flatMap(PotionContents::potion)
                    .map(Holder::getRegisteredName)
                    .orElse(null);

            int textOffset = 0;
            int ly = y + 18 * i * (int) Math.signum((scaledHeight / 2f) - y);

            if (baseName != null && manager.getConfig().isShowUpgrades()) {
                if (baseName.contains("long")) {
                    textOffset += 16;
                    graphics.item(REDSTONE.create(), isRight ? x - 16 - textOffset : x + textOffset, isBottom ? ly - 16 : ly);
                }
                if (baseName.contains("strong")) {
                    textOffset += 16;
                    graphics.item(GLOWSTONE.create(), isRight ? x - 16 - textOffset : x + textOffset, isBottom ? ly - 16 : ly);
                }
            }

            String text = String.valueOf(item.getCount());
            int textWidth = textRenderer.width(text);

            graphics.item(item, isRight ? x - 16 : x, isBottom ? ly - 16 : ly);
            graphics.text(textRenderer, text, isRight ? x - 18 - textWidth - textOffset : x + 18 + textOffset, (isBottom ? ly - 16 : ly) + textRenderer.lineHeight / 2, 0xFFFFFFFF, true);
        }
    }

    private PotionCounter() {
    }
}
