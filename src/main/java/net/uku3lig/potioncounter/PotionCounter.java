package net.uku3lig.potioncounter;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class PotionCounter implements ModInitializer {
    @Override
    public void onInitialize() {
        AutoConfig.register(PotionCounterConfig.class, Toml4jConfigSerializer::new);
    }
}
