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

public class ServerMoveSelfCommand extends AbstractAsyncCommand {

    private final RequiredArg<String> serverArg;

    public ServerMoveSelfCommand() {
        super("teleport", "Move yourself to a server");
        serverArg = withRequiredArg("server", "Target server", ArgTypes.STRING);
    }

    @Override
    protected CompletableFuture<Void> executeAsync(@Nonnull CommandContext context) {

        if (!context.isPlayer()) {
            context.sender().sendMessage(Message.raw("Console must specify a player").color(Color.RED));
            return CompletableFuture.completedFuture(null);
        }

        String server = serverArg.get(context);
        String player = context.sender().getDisplayName();

        try {
            RealmGatePlugin.getInstance().getServices().getRedisService().sendMoveRequest(player, server);
        } catch (ServerMoveException e) {
            throw new RuntimeException(e);
        }

        context.sendMessage(Message.join(Message.raw("Moving ").color(Color.GRAY), Message.raw("you").color(Color.YELLOW), Message.raw(" to ").color(Color.GRAY), Message.raw(server).color(Color.CYAN)));


        return CompletableFuture.completedFuture(null);
    }


}
