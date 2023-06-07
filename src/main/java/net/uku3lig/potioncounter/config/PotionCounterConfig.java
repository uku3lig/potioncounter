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
    private boolean enabled = true;
    private boolean showUpgrades = false;
    private int x = -1;
    private int y = -1;
    private boolean morePotions = false;
    private List<String> disabledPotions = new ArrayList<>();

    @Override
    public PotionCounterConfig defaultConfig() {
        return new PotionCounterConfig();
    }
}
