package net.uku3lig.potioncounter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
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
        if (config.isMorePotions()) {
            return Registries.STATUS_EFFECT.stream()
                    .map(StatusEffect::getTranslationKey)
                    .map(key -> CyclingOption.ofBoolean(key, !config.getDisabledPotions().contains(key), value -> {
                        if (value) config.getDisabledPotions().remove(key);
                        else config.getDisabledPotions().add(key);
                    }))
                    .toArray(CyclingOption[]::new);
        } else {
            return Registries.POTION.stream()
                    .flatMap(p -> p.getEffects().stream())
                    .map(StatusEffectInstance::getEffectType)
                    .map(StatusEffect::getTranslationKey)
                    .distinct()
                    .map(key -> CyclingOption.ofBoolean(key, !config.getDisabledPotions().contains(key), value -> {
                        if (value) config.getDisabledPotions().remove(key);
                        else config.getDisabledPotions().add(key);
                    }))
                    .toArray(CyclingOption[]::new);
        }
    }
}
