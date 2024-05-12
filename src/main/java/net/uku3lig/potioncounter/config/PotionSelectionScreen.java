package net.uku3lig.potioncounter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
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
        return Registries.POTION.stream()
                .flatMap(p -> p.getEffects().stream())
                .map(StatusEffectInstance::getEffectType)
                .map(RegistryEntry::value)
                .map(StatusEffect::getTranslationKey)
                .distinct()
                .map(key -> CyclingOption.ofBoolean(key, !config.getDisabledPotions().contains(key), value -> {
                    if (value) config.getDisabledPotions().remove(key);
                    else config.getDisabledPotions().add(key);
                }))
                .toArray(CyclingOption[]::new);
    }
}
