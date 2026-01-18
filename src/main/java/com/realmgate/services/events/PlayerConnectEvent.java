package com.realmgate.services.events;

import ch.jalu.configme.SettingsManager;
import com.hypixel.hytale.server.core.event.events.player.PlayerSetupConnectEvent;
import com.realmgate.RealmGatePlugin;
import com.realmgate.services.types.config.RealmGateSettings;
import com.realmgate.core.RealmGateCore;


public class PlayerConnectEvent {

    public static void onPlayerConnect(PlayerSetupConnectEvent event) {
        RealmGatePlugin plugin = RealmGatePlugin.getInstance();
        RealmGateCore core = plugin.getCore();

        SettingsManager settings =
                plugin.getServices()
                        .getConfigurationService()
                        .getSettings();

        boolean redirectOnly = settings.getProperty(RealmGateSettings.REDIRECT_ONLY);

        if (redirectOnly) {

            byte[] payload = event.getReferralData();

            if (payload == null || payload.length == 0) {
                event.setReason("Direct connections are not allowed.");
                event.setCancelled(true);
                return;
            }

            if (!core.getPayloadSigner().verify(payload)) {
                event.setReason("Invalid referral data.");
                event.setCancelled(true);
                return;
            }

            if (!core.getPayloadSigner().isFresh(payload, 10_000)) {
                event.setReason("Expired referral.");
                event.setCancelled(true);
                return;
            }
            return;
        }

        // -------------------------
        // ENTRYPOINT SERVER
        // -------------------------
        String defaultServer =
                settings.getProperty(RealmGateSettings.DEFAULT);

        core.getServerRouter().route(event, defaultServer);

    }

}
