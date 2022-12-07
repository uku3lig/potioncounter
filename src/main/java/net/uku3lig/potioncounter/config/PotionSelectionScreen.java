package net.uku3lig.potioncounter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.screen.AbstractConfigScreen;

import java.util.Objects;

public class PotionSelectionScreen extends AbstractConfigScreen<PotionCounterConfig> {
    public PotionSelectionScreen(Screen parent, ConfigManager<PotionCounterConfig> manager) {
        super(parent, Text.translatable("potioncounter.togglePotions"), manager);
    }

    @Override
    protected SimpleOption<?>[] getOptions(PotionCounterConfig config) {
        if (config.isMorePotions()) {
            return Registries.STATUS_EFFECT.stream()
                    .map(StatusEffect::getTranslationKey)
                    .map(key -> SimpleOption.ofBoolean(key, !config.getDisabledPotions().contains(key), value -> {
                        if (value) config.getDisabledPotions().remove(key);
                        else config.getDisabledPotions().add(key);
                    }))
                    .toArray(SimpleOption[]::new);
        } else {
            return Registries.POTION.stream()
                    .flatMap(p -> p.getEffects().stream())
                    .map(StatusEffectInstance::getEffectType)
                    .map(StatusEffect::getRawId)
                    .distinct()
                    .map(StatusEffect::byRawId)
                    .filter(Objects::nonNull)
                    .map(StatusEffect::getTranslationKey)
                    .map(key -> SimpleOption.ofBoolean(key, !config.getDisabledPotions().contains(key), value -> {
                        if (value) config.getDisabledPotions().remove(key);
                        else config.getDisabledPotions().add(key);
                    }))
                    .toArray(SimpleOption[]::new);
        }
    }
}
