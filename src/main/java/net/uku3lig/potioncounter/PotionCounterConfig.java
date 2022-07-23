package net.uku3lig.potioncounter;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "potioncounter")
public class PotionCounterConfig implements ConfigData {
    public boolean showUpgrades = false;
}
