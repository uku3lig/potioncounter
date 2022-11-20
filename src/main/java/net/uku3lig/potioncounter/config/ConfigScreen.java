package net.uku3lig.potioncounter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.Option;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.Position;
import net.uku3lig.ukulib.config.screen.AbstractConfigScreen;

import java.util.EnumSet;

public class ConfigScreen extends AbstractConfigScreen<PotionCounterConfig> {

    public ConfigScreen(Screen parent, ConfigManager<PotionCounterConfig> manager) {
        super(parent, Text.of("PotionCounter Config"), manager);
    }

    @Override
    protected Option[] getOptions(PotionCounterConfig config) {
        return new Option[]{
                CyclingOption.create("potioncounter.enabled", opt -> config.isEnabled(), (opt, option, value) -> config.setEnabled(value)),
                CyclingOption.create("potioncounter.showUpgrades", opt -> config.isShowUpgrades(), (opt, option, value) -> config.setShowUpgrades(value)),
                Position.getOption(EnumSet.complementOf(EnumSet.of(Position.MIDDLE)), config::getPosition, config::setPosition),
                CyclingOption.create("potioncounter.morePotions", opt -> config.isMorePotions(), (opt, option, value) -> config.setMorePotions(value))
        };
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void drawFooterButtons() {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 155, this.height - 27, 150, 20,
                new TranslatableText("potioncounter.togglePotions"), button -> this.client.setScreen(new PotionSelectionScreen(this, manager))));
        this.addDrawableChild(new ButtonWidget(this.width / 2 + 5, this.height - 27, 150, 20, ScreenTexts.DONE, button -> this.client.setScreen(this.parent)));
    }
}
