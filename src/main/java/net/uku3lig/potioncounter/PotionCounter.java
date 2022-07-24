package net.uku3lig.potioncounter;

import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import net.uku3lig.potioncounter.config.Config;

import java.io.File;

public class PotionCounter implements ModInitializer {
    @Getter
    private static Config config = null;
    public static final File configFile = new File("./config/potioncounter.toml");
    @Override
    public void onInitialize() {
        config = Config.readConfig(configFile);
    }
}
