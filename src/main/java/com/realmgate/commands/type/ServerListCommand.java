package com.realmgate.commands.type;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.realmgate.RealmGatePlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.concurrent.CompletableFuture;

public class ServerListCommand extends AbstractCommand {

    public ServerListCommand() {
        super("list", "List all servers");
    }

    @Override
    @Nullable
    protected CompletableFuture<Void> execute(@Nonnull CommandContext context) {

        var services = RealmGatePlugin.getInstance().getServices();

        CommandSender sender = context.sender();

        sender.sendMessage(Message.raw("Servers:").color(Color.RED));

        services.getMysqlService().getRepository().findAll().forEach(server -> {

            boolean online = services.getRedisService().isServerOnline(server.getName());

            sender.sendMessage(
                    Message.join(
                            Message.raw("- ").color(Color.DARK_GRAY),
                            Message.raw(server.getName()).color(Color.WHITE),
                            Message.raw(" (" + server.getAddress() + ":" + server.getPort() + ") ")
                                    .color(Color.GRAY),
                            online
                                    ? Message.raw("ONLINE").color(Color.GREEN)
                                    : Message.raw("OFFLINE").color(Color.RED)
                    )
            );
        });

        return CompletableFuture.completedFuture(null);
    }
}
