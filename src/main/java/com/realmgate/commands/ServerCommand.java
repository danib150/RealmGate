package com.realmgate.commands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;
import com.realmgate.commands.type.ServerDeleteCommand;
import com.realmgate.commands.type.ServerListCommand;
import com.realmgate.commands.type.ServerMoveOtherCommand;
import com.realmgate.commands.type.ServerMoveSelfCommand;


public class ServerCommand extends AbstractCommandCollection {

    public ServerCommand() {
        super("server", "Manage RealmGate servers");
        addSubCommand(new ServerListCommand());
        addSubCommand(new ServerDeleteCommand());
        addSubCommand(new ServerMoveSelfCommand());
        addSubCommand(new ServerMoveOtherCommand());
    }

}

