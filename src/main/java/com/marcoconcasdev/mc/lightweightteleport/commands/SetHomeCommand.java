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

public class SetHomeCommand extends CommandBase {
    private final LightweightTeleport _plugin;

    public SetHomeCommand(LightweightTeleport plugin) {
        _plugin = plugin;
        _commandName = "sethome";
    }

    public boolean execute(@NotNull Player player, @Nullable String[] args) {
        if (args.length == 0) {
            player.sendMessage(_plugin.configurationHandler.getString("empty_home_name"));
            return false;
        }

        String playerUuid = player.getUniqueId().toString();
        Location home = _plugin.databaseHandler.getHome(playerUuid, args[0]);

        if (home == null) {
            assert args[0] != null;
            player.sendMessage(_plugin.configurationHandler.getString("set_home").replace("%home%", args[0]));
            return _plugin.databaseHandler.addHome(playerUuid, args[0], player.getWorld().getName(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
        } else {
            player.sendMessage(_plugin.configurationHandler.getString("home_name_already_exists"));
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