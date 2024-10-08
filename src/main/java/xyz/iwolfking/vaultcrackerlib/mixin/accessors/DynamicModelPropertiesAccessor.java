package xyz.iwolfking.vaultcrackerlib.mixin.accessors;

import iskallia.vault.core.data.key.TemplatePoolKey;
import iskallia.vault.dynamodel.DynamicModelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = DynamicModelProperties.class, remap = false)
public interface DynamicModelPropertiesAccessor {
    @Accessor("discoverOnRoll")
    public void setDiscoverOnRoll(boolean bool);

    @Accessor("allowTransmogrification")
    public void setAllowTransmogrification(boolean bool);
}
