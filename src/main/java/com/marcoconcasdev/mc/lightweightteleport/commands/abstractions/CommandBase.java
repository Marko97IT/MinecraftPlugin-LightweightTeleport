package com.marcoconcasdev.mc.lightweightteleport.commands.abstractions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class CommandBase implements TabCompleter {
    protected String _commandName;

    public abstract boolean execute(@NotNull Player player, @Nullable String[] args);

    public abstract List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args);

    public abstract PluginCommand getPluginCommand();
}