package net.uku3lig.potioncounter;

import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import net.uku3lig.potioncounter.config.Config;

import java.io.File;

public class PotionCounter implements ModInitializer {
    public static final File configFile = new File("./config/potioncounter.toml");
    @Getter
    private static final Config config = Config.readConfig(configFile);
    @Override
    public void onInitialize() {
        // just to initialize the fields you know
    }
}
