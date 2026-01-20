package com.realmgate.commands.type;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import com.realmgate.RealmGatePlugin;
import com.realmgate.server.ServerMoveException;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.concurrent.CompletableFuture;

public class ServerMoveOtherCommand extends AbstractAsyncCommand {

    private final RequiredArg<String> playerArg;
    private final RequiredArg<String> serverArg;

    public ServerMoveOtherCommand() {
        super("move", "Move a player to a server");

        playerArg = withRequiredArg("player", "Target player", ArgTypes.STRING);
        serverArg = withRequiredArg("server", "Target server", ArgTypes.STRING);
    }

    @Override
    protected CompletableFuture<Void> executeAsync(@Nonnull CommandContext context) {

        String player = playerArg.get(context);
        String server = serverArg.get(context);

        try {
            RealmGatePlugin.getInstance()
                    .getServices()
                    .getRedisService()
                    .sendMoveRequest(player, server);
        } catch (ServerMoveException e) {
            throw new RuntimeException(e);
        }

        context.sender().sendMessage(Message.join(Message.raw("Moving ").color(Color.GRAY), Message.raw(player).color(Color.YELLOW), Message.raw(" to ").color(Color.GRAY), Message.raw(server).color(Color.CYAN)));

        return CompletableFuture.completedFuture(null);
    }
}
