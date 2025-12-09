package net.uku3lig.potioncounter.config;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.uku3lig.potioncounter.PotionCounter;
import net.uku3lig.ukulib.config.screen.PositionSelectScreen;

import java.util.List;

public class PotionPositionSelectScreen extends PositionSelectScreen {
    private static final List<ItemStack> POTIONS = List.of(
            PotionContents.createItemStack(Items.SPLASH_POTION, Potions.STRONG_HEALING).copyWithCount(32),
            PotionContents.createItemStack(Items.SPLASH_POTION, Potions.LONG_STRENGTH).copyWithCount(7),
            PotionContents.createItemStack(Items.SPLASH_POTION, Potions.SWIFTNESS)
    );

    protected PotionPositionSelectScreen(Screen parent, PotionCounterConfig config) {
        super("Position Select", parent, config.getX(), config.getY(), PotionCounter.getManager(), (x, y) -> {
            config.setX(x);
            config.setY(y);
        });
    }

    @Override
    protected void draw(GuiGraphics graphics, int mouseX, int mouseY, float delta, int x, int y) {
        PotionCounter.renderPotions(graphics, POTIONS, x, y, width, height, font);
    }
}
