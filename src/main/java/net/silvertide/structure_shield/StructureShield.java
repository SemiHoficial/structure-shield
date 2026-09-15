package net.silvertide.structure_shield;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.silvertide.structure_shield.config.ServerConfigs;
import net.silvertide.structure_shield.registry.EffectRegistry;
import net.silvertide.structure_shield.util.StructureShieldUtil;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(StructureShield.MODID)
public class StructureShield {
    public static final String MODID = "structure_shield";

    public StructureShield(IEventBus modEventBus, ModContainer modContainer) {
        EffectRegistry.register(modEventBus);
        modEventBus.addListener(this::onConfigReload);
        modEventBus.addListener(this::addDefaultsDatapack);

        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfigs.SPEC, String.format("%s-server.toml", StructureShield.MODID));
    }

    private void addDefaultsDatapack(AddPackFindersEvent event) {
        event.addPackFinders(
                ResourceLocation.fromNamespaceAndPath(MODID, "datapacks/defaults"),
                PackType.SERVER_DATA,
                Component.translatable("pack.structure_shield.defaults"),
                PackSource.BUILT_IN,
                false,
                Pack.Position.TOP);
    }

    private void onConfigReload(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() != ServerConfigs.SPEC) return;

        var server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        server.execute(() -> StructureShieldUtil.setupModData(server.registryAccess()));
    }
}
