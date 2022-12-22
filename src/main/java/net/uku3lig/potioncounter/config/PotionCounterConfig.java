package net.uku3lig.potioncounter.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.uku3lig.ukulib.config.IConfig;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PotionCounterConfig implements IConfig<PotionCounterConfig> {
    private boolean enabled;
    private boolean showUpgrades;
    private int x;
    private int y;
    private boolean morePotions;
    private List<String> disabledPotions;

    @Override
    public PotionCounterConfig defaultConfig() {
        return new PotionCounterConfig(true, false, -1, -1, false, new ArrayList<>());
    }
}
