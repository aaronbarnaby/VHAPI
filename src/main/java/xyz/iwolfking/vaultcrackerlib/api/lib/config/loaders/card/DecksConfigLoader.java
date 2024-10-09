package xyz.iwolfking.vaultcrackerlib.api.lib.config.loaders.card;

import iskallia.vault.config.card.CardDeckConfig;
import iskallia.vault.init.ModConfigs;
import xyz.iwolfking.vaultcrackerlib.api.events.VaultConfigEvent;
import xyz.iwolfking.vaultcrackerlib.api.lib.loaders.VaultConfigDataLoader;
import xyz.iwolfking.vaultcrackerlib.mixin.accessors.CardDeckConfigAccessor;
import java.util.HashMap;


public class DecksConfigLoader extends VaultConfigDataLoader<CardDeckConfig> {
    public DecksConfigLoader(String namespace) {
        super(new CardDeckConfig(), "card/decks", new HashMap<>(), namespace);
    }

    @Override
    public void afterConfigsLoad(VaultConfigEvent.End event) {
        for(CardDeckConfig config : this.CUSTOM_CONFIGS.values()) {
            ((CardDeckConfigAccessor) ModConfigs.CARD_DECK).getValues().putAll(((CardDeckConfigAccessor)config).getValues());
        }

    }
}
