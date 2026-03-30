package net.uku3lig.potioncounter.neoforge;

import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.uku3lig.potioncounter.PotionCounter;
import net.uku3lig.potioncounter.UkulibHook;
import net.uku3lig.ukulib.neoforge.UkulibNFProvider;

@Mod(value = "potioncounter", dist = Dist.CLIENT)
public class PotionCounterNeoForge {
    public PotionCounterNeoForge(ModContainer container, IEventBus modBus) {
        container.registerExtensionPoint(UkulibNFProvider.class, UkulibHook::new);
        modBus.addListener(this::registerPotionCounterRenderer);
    }

    private void registerPotionCounterRenderer(RegisterGuiLayersEvent event) {
        event.registerAboveAll(Identifier.fromNamespaceAndPath("potioncounter", "gui_potion_counter"),
                (g, _) -> PotionCounter.extractCurrentPlayerPotions(g));
    }
}
