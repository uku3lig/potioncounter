package net.uku3lig.potioncounter;

import lombok.Getter;
import net.uku3lig.potioncounter.config.PotionCounterConfig;
import net.uku3lig.ukulib.config.ConfigManager;

public class PotionCounter {
    @Getter
    private static final ConfigManager<PotionCounterConfig> manager = ConfigManager.create(PotionCounterConfig.class, "potioncounter");

    private PotionCounter() {
    }
}
