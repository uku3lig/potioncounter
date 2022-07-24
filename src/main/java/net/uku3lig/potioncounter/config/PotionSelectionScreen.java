package net.uku3lig.potioncounter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.Option;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.Registry;

import java.util.Objects;

public class PotionSelectionScreen extends AbstractConfigScreen {
    public PotionSelectionScreen(Screen parent) {
        super(parent, new TranslatableText("potioncounter.togglePotions"));
    }

    @Override
    protected Option[] getOptions() {
        if (config.isMorePotions()) {
            return Registry.STATUS_EFFECT.stream()
                    .map(StatusEffect::getTranslationKey)
                    .map(key -> CyclingOption.create(key, opt -> !config.getDisabledPotions().contains(key), (opt, option, value) -> {
                        if (Boolean.TRUE.equals(value)) config.getDisabledPotions().remove(key);
                        else config.getDisabledPotions().add(key);
                    }))
                    .toArray(Option[]::new);
        } else {
            return Registry.POTION.stream()
                    .flatMap(p -> p.getEffects().stream())
                    .map(StatusEffectInstance::getEffectType)
                    .map(StatusEffect::getRawId)
                    .distinct()
                    .map(StatusEffect::byRawId)
                    .filter(Objects::nonNull)
                    .map(StatusEffect::getTranslationKey)
                    .map(key -> CyclingOption.create(key, opt -> !config.getDisabledPotions().contains(key), (opt, option, value) -> {
                        if (Boolean.TRUE.equals(value)) config.getDisabledPotions().remove(key);
                        else config.getDisabledPotions().add(key);
                    }))
                    .toArray(Option[]::new);
        }
    }
}
