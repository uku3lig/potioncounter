package net.uku3lig.potioncounter.config;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.uku3lig.potioncounter.PotionCounter;
import net.uku3lig.ukulib.config.screen.PositionSelectScreen;

import java.util.Arrays;
import java.util.List;

public class PotionPositionSelectScreen extends PositionSelectScreen {
    protected PotionPositionSelectScreen(Screen parent, PotionCounterConfig config) {
        super("Position Select", parent, config.getX(), config.getY(), PotionCounter.getManager(), (x, y) -> {
            config.setX(x);
            config.setY(y);
        });
    }

    @Override
    protected void draw(DrawContext context, int mouseX, int mouseY, float delta, int x, int y) {
        List<ItemStack> potions = Arrays.asList(
                PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION, 32), Potions.STRONG_HEALING),
                PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION, 7), Potions.LONG_STRENGTH),
                PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.SWIFTNESS)
        );

        PotionCounter.renderPotions(context, potions, x, y, width, height, textRenderer);
    }
}
