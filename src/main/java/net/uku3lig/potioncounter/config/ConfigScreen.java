package net.uku3lig.potioncounter.config;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.SimpleOptionsScreen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class ConfigScreen extends SimpleOptionsScreen {
    private static final Logger logger = LogManager.getLogger("PotCounterConfig");

    public static final File configFile = new File("./config/potioncounter.toml");

    public static final SimpleOption<Boolean> enabled = SimpleOption.ofBoolean("potioncounter.enabled", true);
    public static final SimpleOption<Boolean> showUpgrades = SimpleOption.ofBoolean("potioncounter.showUpgrades", false);

    public ConfigScreen(Screen parent) {
        super(parent, MinecraftClient.getInstance().options, Text.of("PotionCounter Config"), new SimpleOption[] {enabled, showUpgrades});
    }

    @Override
    protected void init() {
        super.init();
        if (!configFile.exists()) {
            try {
                writeConfig();
            } catch (IOException e) {
                logger.warn("Could not write default configuration file", e);
            }
        } else {
            Toml toml = new Toml().read(configFile);
            enabled.setValue(toml.getBoolean("enabled"));
            showUpgrades.setValue(toml.getBoolean("showUpgrades"));
        }
    }

    @Override
    public void removed() {
        try {
            writeConfig();
        } catch (IOException e) {
            logger.warn("Could not save configuration file", e);
        }
    }

    private void writeConfig() throws IOException {
        Options opt = new Options(enabled.getValue(), showUpgrades.getValue());
        new TomlWriter().write(opt, configFile);
    }

    private static class Options {
        public final boolean enabled;
        public final boolean showUpgrades;

        private Options(boolean enabled, boolean showUpgrades) {
            this.enabled = enabled;
            this.showUpgrades = showUpgrades;
        }
    }
}
