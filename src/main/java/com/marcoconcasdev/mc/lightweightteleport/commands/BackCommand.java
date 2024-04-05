package com.marcoconcasdev.mc.lightweightteleport.commands;

import com.marcoconcasdev.mc.lightweightteleport.LightweightTeleport;
import com.marcoconcasdev.mc.lightweightteleport.commands.abstractions.CommandBase;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BackCommand extends CommandBase {
    private final LightweightTeleport _plugin;

    public BackCommand(LightweightTeleport plugin) {
        _plugin = plugin;
        _commandName = "back";
    }

    public boolean execute(@NotNull Player player, @Nullable String[] args) {
        Location backPosition = _plugin.databaseHandler.getBackPosition(player.getUniqueId().toString());

        if (backPosition != null) {
            _plugin.teleportPlayer(player, backPosition);
            return true;
        } else {
            player.sendMessage(_plugin.configurationHandler.getString("back_not_found"));
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        return null;
    }

    @Override
    public PluginCommand getPluginCommand() {
        return _plugin.getCommand(_commandName);
    }
}