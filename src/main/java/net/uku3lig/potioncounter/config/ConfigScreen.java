package net.uku3lig.potioncounter.config;

import com.mojang.serialization.Codec;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.uku3lig.potioncounter.PotionCounter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ConfigScreen extends GameOptionsScreen {
    private static final Logger logger = LogManager.getLogger("PotCounterConfigScreen");
    private final Config config;
    private ButtonListWidget buttonList;

    public ConfigScreen(Screen parent) {
        super(parent, MinecraftClient.getInstance().options, Text.of("PotionCounter Config"));
        this.config = PotionCounter.getConfig();
    }

    @Override
    protected void init() {
        super.init();
        buttonList = new ButtonListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        buttonList.addAll(new SimpleOption[]{
                SimpleOption.ofBoolean("potioncounter.enabled", config.isEnabled(), config::setEnabled),
                SimpleOption.ofBoolean("potioncounter.showUpgrades", config.isShowUpgrades(), config::setShowUpgrades),
                new SimpleOption<>("potioncounter.position", SimpleOption.emptyTooltip(), SimpleOption.enumValueText(), new SimpleOption.PotentialValuesBasedCallbacks<>(Arrays.asList(Position.values()), Codec.STRING.xmap(Position::valueOf, Position::name)), config.getPosition(), config::setPosition)
        });
        this.addSelectableChild(buttonList);
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, ScreenTexts.DONE, button -> this.client.setScreen(this.parent)));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.buttonList.render(matrices, mouseX, mouseY, delta);
        DrawableHelper.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
        List<OrderedText> list = GameOptionsScreen.getHoveredButtonTooltip(this.buttonList, mouseX, mouseY);
        this.renderOrderedTooltip(matrices, list, mouseX, mouseY);
    }

    @Override
    public void removed() {
        try {
            config.writeConfig(PotionCounter.configFile);
        } catch (IOException e) {
            logger.warn("Could not save configuration file", e);
        }
    }
}
