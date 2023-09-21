package net.uku3lig.potioncounter.config;

import net.minecraft.client.gui.screen.Screen;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.option.CyclingOption;
import net.uku3lig.ukulib.config.option.ScreenOpenButton;
import net.uku3lig.ukulib.config.option.WidgetCreator;
import net.uku3lig.ukulib.config.screen.AbstractConfigScreen;

public class ConfigScreen extends AbstractConfigScreen<PotionCounterConfig> {

    public ConfigScreen(Screen parent, ConfigManager<PotionCounterConfig> manager) {
        super("PotionCounter Config", parent, manager);
    }

    @Override
    protected WidgetCreator[] getWidgets(PotionCounterConfig config) {
        return new WidgetCreator[] {
                CyclingOption.ofBoolean("potioncounter.enabled", config.isEnabled(), config::setEnabled),
                CyclingOption.ofBoolean("potioncounter.showUpgrades", config.isShowUpgrades(), config::setShowUpgrades),
                new ScreenOpenButton("ukulib.position", parent -> new PotionPositionSelectScreen(parent, config)),
                CyclingOption.ofBoolean("potioncounter.morePotions", config.isMorePotions(), config::setMorePotions),
                new ScreenOpenButton("potioncounter.togglePotions", parent -> new PotionSelectionScreen(parent, manager))
        };
    }
}
