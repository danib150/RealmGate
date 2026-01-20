package com.realmgate.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncCommand;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.realmgate.RealmGatePlugin;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.concurrent.CompletableFuture;

public class WhoAmICommand extends AbstractAsyncCommand {

    public WhoAmICommand() {
        super("whoami", "Show your current server");
    }

    @Override
    protected CompletableFuture<Void> executeAsync(@Nonnull CommandContext context) {

        if (!context.isPlayer()) {
            context.sender().sendMessage(Message.raw("This command can only be used by players").color(Color.RED));
            return CompletableFuture.completedFuture(null);
        }

        var services = RealmGatePlugin.getInstance().getServices();
        CommandSender sender = context.sender();

        var uuid = sender.getUuid();
        String server = services.getRedisService().getPlayerServer(uuid);

        if (server == null) {
            sender.sendMessage(Message.raw("You are not registered on any server").color(Color.RED));
        } else {
            sender.sendMessage(Message.join(Message.raw("You are on server ").color(Color.GRAY), Message.raw(server).color(Color.CYAN)));
        }

        return CompletableFuture.completedFuture(null);
    }
}
