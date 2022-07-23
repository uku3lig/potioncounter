package net.uku3lig.potioncounter;

import lombok.Getter;
import lombok.Setter;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "potioncounter")
@Getter @Setter
public class PotionCounterConfig implements ConfigData {
    private boolean showUpgrades = false;
}
