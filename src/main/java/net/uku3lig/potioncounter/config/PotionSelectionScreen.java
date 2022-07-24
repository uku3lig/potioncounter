package net.uku3lig.potioncounter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.text.Text;
import net.minecraft.util.registry.Registry;

import java.util.Objects;

public class PotionSelectionScreen extends AbstractConfigScreen {
    public PotionSelectionScreen(Screen parent) {
        super(parent, Text.translatable("potioncounter.togglePotions"));
    }

    @Override
    protected SimpleOption<?>[] getOptions() {
        if (config.isMorePotions()) {
            return Registry.STATUS_EFFECT.stream()
                    .map(StatusEffect::getTranslationKey)
                    .map(key -> SimpleOption.ofBoolean(key, !config.getDisabledPotions().contains(key), value -> {
                        if (value) config.getDisabledPotions().remove(key);
                        else config.getDisabledPotions().add(key);
                    }))
                    .toArray(SimpleOption[]::new);
        } else {
            return Registry.POTION.stream()
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
