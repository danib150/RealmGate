package com.realmgate.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import com.realmgate.RealmGatePlugin;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class WhereCommand extends AbstractAsyncCommand {

    private final RequiredArg<String> playerArg;

    public WhereCommand() {
        super("where", "Show where a player is");

        playerArg = withRequiredArg("player", "Player name", ArgTypes.STRING);
    }

    @Override
    protected CompletableFuture<Void> executeAsync(@Nonnull CommandContext context) {

        var services = RealmGatePlugin.getInstance().getServices();
        String playerName = playerArg.get(context);

        UUID uuid = services.getRedisService().getPlayerUuidByName(playerName);

        if (uuid == null) {
            context.sender().sendMessage(Message.raw("Player not found: " + playerName).color(Color.RED));
            return CompletableFuture.completedFuture(null);
        }

        String server = services.getRedisService().getPlayerServer(uuid);

        if (server == null) {
            context.sender().sendMessage(Message.join(Message.raw(playerName).color(Color.YELLOW), Message.raw(" is offline").color(Color.GRAY)));
        } else {
            context.sender().sendMessage(Message.join(Message.raw(playerName).color(Color.YELLOW), Message.raw(" is on ").color(Color.GRAY), Message.raw(server).color(Color.CYAN)));
        }

        return CompletableFuture.completedFuture(null);
    }
}
