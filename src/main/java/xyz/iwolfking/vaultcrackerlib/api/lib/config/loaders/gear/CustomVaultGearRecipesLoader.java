package xyz.iwolfking.vaultcrackerlib.api.lib.config.loaders.gear;

import iskallia.vault.config.recipe.ForgeRecipesConfig;
import iskallia.vault.config.recipe.GearRecipesConfig;
import iskallia.vault.init.ModConfigs;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import xyz.iwolfking.vaultcrackerlib.api.events.VaultConfigEvent;
import xyz.iwolfking.vaultcrackerlib.api.lib.loaders.VaultConfigDataLoader;
import xyz.iwolfking.vaultcrackerlib.api.patching.configs.Loaders;
import java.util.HashMap;

@Mod.EventBusSubscriber(modid = "vaultcrackerlib", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CustomVaultGearRecipesLoader extends VaultConfigDataLoader<GearRecipesConfig> {
    public CustomVaultGearRecipesLoader(String namespace) {
        super(ModConfigs.GEAR_RECIPES, "gear_recipes", new HashMap<>(), namespace);
    }

    @SubscribeEvent
    public static void afterConfigsLoad(VaultConfigEvent.End event) {
        for(GearRecipesConfig config : Loaders.GEAR_RECIPES_LOADER.CUSTOM_CONFIGS.values()) {
            ModConfigs.GEAR_RECIPES.getConfigRecipes().addAll(config.getConfigRecipes());
        }
        syncRecipes();
    }

    public static void syncRecipes() {
        MinecraftServer srv = ServerLifecycleHooks.getCurrentServer();
        if (srv != null) {
            srv.getPlayerList().getPlayers().forEach((player) -> {
                ModConfigs.GEAR_RECIPES.syncTo(ModConfigs.GEAR_RECIPES, player);
            });
        }
    }




    @SubscribeEvent
    public static void onAddListeners(AddReloadListenerEvent event) {
        event.addListener(Loaders.GEAR_RECIPES_LOADER);
    }


}
