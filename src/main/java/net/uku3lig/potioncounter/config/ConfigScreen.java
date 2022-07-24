package net.uku3lig.potioncounter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.Option;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class ConfigScreen extends AbstractConfigScreen {

    public ConfigScreen(Screen parent) {
        super(parent, Text.of("PotionCounter Config"));
    }

    @Override
    protected Option[] getOptions() {
        return new Option[]{
                CyclingOption.create("potioncounter.enabled", opt -> config.isEnabled(), (opt, option, value) -> config.setEnabled(value)),
                CyclingOption.create("potioncounter.showUpgrades", opt -> config.isShowUpgrades(), (opt, option, value) -> config.setShowUpgrades(value)),
                CyclingOption.create("potioncounter.position", Position.values(), position -> new TranslatableText(position.getTranslationKey()), opt -> config.getPosition(), (opt, option, value) -> config.setPosition(value)),
                CyclingOption.create("potioncounter.morePotions", opt -> config.isMorePotions(), (opt, option, value) -> config.setMorePotions(value))
        };
    }

    @Override
    protected void drawFooterButtons() {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 155, this.height - 27, 150, 20,
                new TranslatableText("potioncounter.togglePotions"), button -> this.client.setScreen(new PotionSelectionScreen(this))));
        this.addDrawableChild(new ButtonWidget(this.width / 2 + 5, this.height - 27, 150, 20, ScreenTexts.DONE, button -> this.client.setScreen(this.parent)));
    }
}
