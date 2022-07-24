package net.uku3lig.potioncounter.config;

import com.mojang.serialization.Codec;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import java.util.Arrays;

public class ConfigScreen extends AbstractConfigScreen {

    public ConfigScreen(Screen parent) {
        super(parent, Text.of("PotionCounter Config"));
    }

    @Override
    protected SimpleOption<?>[] getOptions() {
        return new SimpleOption<?>[]{
                SimpleOption.ofBoolean("potioncounter.enabled", config.isEnabled(), config::setEnabled),
                SimpleOption.ofBoolean("potioncounter.showUpgrades", config.isShowUpgrades(), config::setShowUpgrades),
                new SimpleOption<>("potioncounter.position", SimpleOption.emptyTooltip(), SimpleOption.enumValueText(), new SimpleOption.PotentialValuesBasedCallbacks<>(Arrays.asList(Position.values()), Codec.STRING.xmap(Position::valueOf, Position::name)), config.getPosition(), config::setPosition),
                SimpleOption.ofBoolean("potioncounter.morePotions", config.isMorePotions(), config::setMorePotions)
        };
    }

    @Override
    protected void drawFooterButtons() {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 155, this.height - 27, 150, 20, ScreenTexts.DONE, button -> this.client.setScreen(this.parent)));
        this.addDrawableChild(new ButtonWidget(this.width / 2 + 5, this.height - 27, 150, 20,
                Text.translatable("potioncounter.togglePotions"), button -> this.client.setScreen(new PotionSelectionScreen(this))));
    }
}
