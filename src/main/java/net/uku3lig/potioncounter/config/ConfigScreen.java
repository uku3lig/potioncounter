package net.uku3lig.potioncounter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.Position;
import net.uku3lig.ukulib.config.screen.AbstractConfigScreen;

import java.util.EnumSet;

public class ConfigScreen extends AbstractConfigScreen<PotionCounterConfig> {

    public ConfigScreen(Screen parent, ConfigManager<PotionCounterConfig> manager) {
        super(parent, Text.of("PotionCounter Config"), manager);
    }

    @Override
    protected SimpleOption<?>[] getOptions(PotionCounterConfig config) {
        return new SimpleOption<?>[]{
                SimpleOption.ofBoolean("potioncounter.enabled", config.isEnabled(), config::setEnabled),
                SimpleOption.ofBoolean("potioncounter.showUpgrades", config.isShowUpgrades(), config::setShowUpgrades),
                Position.getOption(EnumSet.complementOf(EnumSet.of(Position.MIDDLE)), config::getPosition, config::setPosition),
                SimpleOption.ofBoolean("potioncounter.morePotions", config.isMorePotions(), config::setMorePotions)
        };
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void drawFooterButtons() {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 155, this.height - 27, 150, 20,
                Text.translatable("potioncounter.togglePotions"), button -> this.client.setScreen(new PotionSelectionScreen(this, manager))));
        this.addDrawableChild(new ButtonWidget(this.width / 2 + 5, this.height - 27, 150, 20, ScreenTexts.DONE, button -> this.client.setScreen(this.parent)));
    }
}
