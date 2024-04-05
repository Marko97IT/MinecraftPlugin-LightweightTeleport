package com.marcoconcasdev.mc.lightweightteleport.handlers.interfaces;

import org.bukkit.command.CommandExecutor;

public interface ICommandsHandler extends CommandExecutor {
    void registerCommands();
}