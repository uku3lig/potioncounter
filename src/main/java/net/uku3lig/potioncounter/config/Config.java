package net.uku3lig.potioncounter.config;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

@Data
public class Config {
    private static final Logger logger = LogManager.getLogger("PotCounterConfig");

    private boolean enabled;
    private boolean showUpgrades;

    public Config(boolean enabled, boolean showUpgrades) {
        this.enabled = enabled;
        this.showUpgrades = showUpgrades;
    }

    public Config() {
        this(true, false);
    }

    public static Config readConfig(File file) {
        if (!file.exists()) {
            try {
                new Config().writeConfig(file);
            } catch (IOException e) {
                logger.warn("Could not write default configuration file", e);
            }
            return new Config();
        } else {
            Toml toml = new Toml().read(file);
            return new Config(toml.getBoolean("enabled"), toml.getBoolean("showUpgrades"));
        }
    }

    public void writeConfig(File file) throws IOException {
        new TomlWriter().write(this, file);
    }
}
