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

public class HomeCommand extends CommandBase {
    private final LightweightTeleport _plugin;

    public HomeCommand(LightweightTeleport plugin) {
        _plugin = plugin;
        _commandName = "home";
    }

    public boolean execute(@NotNull Player player, @Nullable String[] args) {
        if (args.length == 0) {
            player.sendMessage(_plugin.configurationHandler.getString("empty_home_name"));
            return false;
        }

        Location home = _plugin.databaseHandler.getHome(player.getUniqueId().toString(), args[0]);

        if (home != null) {
            _plugin.teleportPlayer(player, home);
            return true;
        } else {
            player.sendMessage(_plugin.configurationHandler.getString("home_name_not_found"));
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            if ((sender instanceof Player)) {
                Player player = (Player) sender;
                return _plugin.databaseHandler.getHomeNames(player.getUniqueId().toString());
            }

        }

        return null;
    }

    @Override
    public PluginCommand getPluginCommand() {
        return _plugin.getCommand(_commandName);
    }
}