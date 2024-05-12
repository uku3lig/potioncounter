package net.uku3lig.potioncounter.config;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.uku3lig.potioncounter.PotionCounter;
import net.uku3lig.ukulib.config.screen.PositionSelectScreen;

import java.util.List;

public class PotionPositionSelectScreen extends PositionSelectScreen {
    private static final List<ItemStack> POTIONS = List.of(
            PotionContentsComponent.createStack(Items.SPLASH_POTION, Potions.STRONG_HEALING).copyWithCount(32),
            PotionContentsComponent.createStack(Items.SPLASH_POTION, Potions.LONG_STRENGTH).copyWithCount(7),
            PotionContentsComponent.createStack(Items.SPLASH_POTION, Potions.SWIFTNESS)
    );

    protected PotionPositionSelectScreen(Screen parent, PotionCounterConfig config) {
        super("Position Select", parent, config.getX(), config.getY(), PotionCounter.getManager(), (x, y) -> {
            config.setX(x);
            config.setY(y);
        });
    }

    @Override
    protected void draw(DrawContext context, int mouseX, int mouseY, float delta, int x, int y) {
        PotionCounter.renderPotions(context, POTIONS, x, y, width, height, textRenderer);
    }
}
