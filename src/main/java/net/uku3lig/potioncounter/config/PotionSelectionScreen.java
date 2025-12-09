package net.uku3lig.potioncounter.config;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.option.CyclingOption;
import net.uku3lig.ukulib.config.option.WidgetCreator;
import net.uku3lig.ukulib.config.screen.AbstractConfigScreen;

public class PotionSelectionScreen extends AbstractConfigScreen<PotionCounterConfig> {
    public PotionSelectionScreen(Screen parent, ConfigManager<PotionCounterConfig> manager) {
        super("potioncounter.togglePotions", parent, manager);
    }

    @Override
    protected WidgetCreator[] getWidgets(PotionCounterConfig config) {
        return BuiltInRegistries.POTION.stream()
                .flatMap(p -> p.getEffects().stream())
                .map(MobEffectInstance::getEffect)
                .map(Holder::value)
                .map(MobEffect::getDescriptionId)
                .distinct()
                .map(key -> CyclingOption.ofBoolean(key, !config.getDisabledPotions().contains(key), value -> {
                    if (value) config.getDisabledPotions().remove(key);
                    else config.getDisabledPotions().add(key);
                }))
                .toArray(CyclingOption[]::new);
    }
}
