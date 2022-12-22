package net.uku3lig.potioncounter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.Option;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.screen.AbstractConfigScreen;
import net.uku3lig.ukulib.utils.Ukutils;

public class ConfigScreen extends AbstractConfigScreen<PotionCounterConfig> {

    public ConfigScreen(Screen parent, ConfigManager<PotionCounterConfig> manager) {
        super(parent, Text.of("PotionCounter Config"), manager);
    }

    @Override
    protected Option[] getOptions(PotionCounterConfig config) {
        return new Option[]{
                CyclingOption.create("potioncounter.enabled", opt -> config.isEnabled(), (opt, option, value) -> config.setEnabled(value)),
                CyclingOption.create("potioncounter.showUpgrades", opt -> config.isShowUpgrades(), (opt, option, value) -> config.setShowUpgrades(value)),
                Ukutils.createOpenButton("ukulib.position", parent -> new PotionPositionSelectScreen(parent, config)),
                CyclingOption.create("potioncounter.morePotions", opt -> config.isMorePotions(), (opt, option, value) -> config.setMorePotions(value)),
                Ukutils.createOpenButton("potioncounter.togglePotions", parent -> new PotionSelectionScreen(parent, manager))
        };
    }
}
