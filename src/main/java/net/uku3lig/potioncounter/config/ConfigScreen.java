package net.uku3lig.potioncounter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.screen.AbstractConfigScreen;
import net.uku3lig.ukulib.utils.Ukutils;

public class ConfigScreen extends AbstractConfigScreen<PotionCounterConfig> {

    public ConfigScreen(Screen parent, ConfigManager<PotionCounterConfig> manager) {
        super(parent, Text.of("PotionCounter Config"), manager);
    }

    @Override
    protected SimpleOption<?>[] getOptions(PotionCounterConfig config) {
        return new SimpleOption<?>[]{
                SimpleOption.ofBoolean("potioncounter.enabled", config.isEnabled(), config::setEnabled),
                SimpleOption.ofBoolean("potioncounter.showUpgrades", config.isShowUpgrades(), config::setShowUpgrades),
                Ukutils.createOpenButton("ukulib.position", parent -> new PotionPositionSelectScreen(parent, config)),
                SimpleOption.ofBoolean("potioncounter.morePotions", config.isMorePotions(), config::setMorePotions),
                Ukutils.createOpenButton("potioncounter.togglePotions", parent -> new PotionSelectionScreen(parent, manager))
        };
    }
}
