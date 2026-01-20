package com.realmgate.commands.type;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.realmgate.RealmGatePlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.concurrent.CompletableFuture;

public class ServerDeleteCommand extends AbstractCommand {

    private final RequiredArg<String> nameArg;

    public ServerDeleteCommand() {
        super("delete", "Delete a server");
        nameArg = withRequiredArg("name", "Server name", ArgTypes.STRING);
    }

    @Override
    @Nullable
    protected CompletableFuture<Void> execute(@Nonnull CommandContext context) {

        String name = nameArg.get(context);
        var services = RealmGatePlugin.getInstance().getServices();

        if (services.getRedisService().isServerOnline(name)) {
            context.sender().sendMessage(Message.raw("Cannot delete server '" + name + "': server is ONLINE").color(Color.RED));
            return CompletableFuture.completedFuture(null);
        }

        boolean deleted = services.getMysqlService().getRepository().delete(name);

        if (!deleted) {
            context.sender().sendMessage(Message.raw("§cServer not found: " + name));
            return CompletableFuture.completedFuture(null);
        }

        context.sender().sendMessage(Message.raw("Server deleted: " + name).color(Color.RED));

        return CompletableFuture.completedFuture(null);
    }
}