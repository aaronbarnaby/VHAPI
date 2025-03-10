package xyz.iwolfking.vhapi.api.loaders.titles;

import iskallia.vault.config.PlayerTitlesConfig;
import iskallia.vault.init.ModConfigs;
import xyz.iwolfking.vhapi.api.events.VaultConfigEvent;
import xyz.iwolfking.vhapi.api.loaders.titles.lib.CustomTitleConfig;
import xyz.iwolfking.vhapi.api.loaders.titles.lib.GsonPlayerTitle;
import xyz.iwolfking.vhapi.api.loaders.lib.core.VaultConfigProcessor;

public class CustomTitleConfigLoader extends VaultConfigProcessor<CustomTitleConfig> {

    public CustomTitleConfigLoader() {
        super(new CustomTitleConfig(), "player_titles");
    }

    @Override
    public void afterConfigsLoad(VaultConfigEvent.End event) {
        for(CustomTitleConfig config : this.CUSTOM_CONFIGS.values()) {
            for(GsonPlayerTitle title : config.PREFIXES) {
                ModConfigs.PLAYER_TITLES.getAll(PlayerTitlesConfig.Affix.PREFIX).put(title.id, title.title());
                ModConfigs.ASCENSION_FORGE.getListings().add(title.titleStack(PlayerTitlesConfig.Affix.PREFIX));
            }

            for(GsonPlayerTitle title : config.SUFFIXES) {
                ModConfigs.PLAYER_TITLES.getAll(PlayerTitlesConfig.Affix.SUFFIX).put(title.id, title.title());
                ModConfigs.ASCENSION_FORGE.getListings().add(title.titleStack(PlayerTitlesConfig.Affix.SUFFIX));
            }
        }
        super.afterConfigsLoad(event);
    }


}
