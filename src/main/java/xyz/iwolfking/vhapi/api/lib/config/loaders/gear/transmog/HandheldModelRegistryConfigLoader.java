package xyz.iwolfking.vhapi.api.lib.config.loaders.gear.transmog;

import iskallia.vault.dynamodel.DynamicModel;
import iskallia.vault.dynamodel.baked.JsonFileBakedModel;
import iskallia.vault.dynamodel.model.item.HandHeldModel;
import iskallia.vault.dynamodel.registry.DynamicModelRegistry;
import iskallia.vault.init.ModDynamicModels;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import xyz.iwolfking.vhapi.api.events.VaultConfigEvent;
import xyz.iwolfking.vhapi.api.lib.config.loaders.gear.transmog.lib.GsonHandheldModel;
import xyz.iwolfking.vhapi.api.lib.config.loaders.gear.transmog.lib.HandheldModelConfig;
import xyz.iwolfking.vhapi.api.lib.loaders.VaultConfigDataLoader;

import java.util.HashMap;

@Mod.EventBusSubscriber(modid = "vhapi", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HandheldModelRegistryConfigLoader<T extends DynamicModelRegistry<HandHeldModel>> extends VaultConfigDataLoader<HandheldModelConfig> {

    public static final HandheldModelConfig instance = new HandheldModelConfig();

    public static boolean loaded = false;

    T registry;

    Item item;
    public HandheldModelRegistryConfigLoader(String namespace, DynamicModelRegistry<HandHeldModel> registry, @NotNull Item item) {
        super(new HandheldModelConfig(), "gear/handheld_models/" + item.getRegistryName().getPath(), new HashMap<>(), namespace);
        this.registry = (T) registry;
        this.item = item;
    }

    @Override
    public void afterConfigsLoad(VaultConfigEvent.End event) {
        if(loaded) {
            return;
        }
        for(HandheldModelConfig config : this.CUSTOM_CONFIGS.values()) {
            for(GsonHandheldModel model : config.MODELS) {
                HandHeldModel handHeldModel = model.getModel();
                registry.register(handHeldModel);
                DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> this.bakeModel(handHeldModel));
            }

        }
        loaded = true;
        this.CUSTOM_CONFIGS.clear();
    }


    @OnlyIn(Dist.CLIENT)
    public void bakeModel(HandHeldModel handHeldModel) {
        ForgeModelBakery modelLoader = ForgeModelBakery.instance();
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        handHeldModel.getAssociatedModelLocations().forEach(modelResourceLocation -> {
            BlockModel unbakedModel = (BlockModel)modelLoader.getModel(modelResourceLocation);
            BakedModel bakedIcon;

            if(ModDynamicModels.jsonModelExists(resourceManager, DynamicModel.prependToId("item/", handHeldModel.getId()))) {
                bakedIcon = unbakedModel.bake(modelLoader, unbakedModel, ForgeModelBakery.defaultTextureGetter(), SimpleModelState.IDENTITY, modelResourceLocation, false);;
                bakedIcon = new JsonFileBakedModel(bakedIcon);
            }
            else {
                bakedIcon = handHeldModel.bakeModel(modelResourceLocation, modelLoader, unbakedModel);
            }
            ResourceLocation bakeId = new ResourceLocation(modelResourceLocation.getNamespace(), modelResourceLocation.getPath());
            this.registry.bakeIcon(bakeId, bakedIcon);
        });

    }




}
