package com.marcoconcasdev.mc.lightweightteleport.handlers;

import com.marcoconcasdev.mc.lightweightteleport.LightweightTeleport;
import com.marcoconcasdev.mc.lightweightteleport.commands.BackCommand;
import com.marcoconcasdev.mc.lightweightteleport.commands.DelHomeCommand;
import com.marcoconcasdev.mc.lightweightteleport.commands.HomeCommand;
import com.marcoconcasdev.mc.lightweightteleport.commands.SetHomeCommand;
import com.marcoconcasdev.mc.lightweightteleport.commands.abstractions.CommandBase;
import com.marcoconcasdev.mc.lightweightteleport.handlers.interfaces.ICommandsHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Hashtable;

public class CommandsHandler implements CommandExecutor, ICommandsHandler {
    private final LightweightTeleport _plugin;
    private final Hashtable<String, CommandBase> _commands;

    public CommandsHandler(LightweightTeleport plugin) {
        _plugin = plugin;

        _commands = new Hashtable<>();
        _commands.put("sethome", new SetHomeCommand(_plugin));
        _commands.put("home", new HomeCommand(_plugin));
        _commands.put("delhome", new DelHomeCommand(_plugin));
        _commands.put("back", new BackCommand(_plugin));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(commandSender instanceof Player)) {
            _plugin.getLogger().warning(_plugin.configurationHandler.getString("console_player_command_error"));
            return false;
        }

        Player player = (Player) commandSender;
        switch (command.getName()) {
            case "sethome":
                if (checkPermission(player, "ltp.command.sethome")) {
                    return _commands.get("sethome").execute(player, args);
                }
                break;
            case "home":
                if (checkPermission(player, "ltp.command.home")) {
                    return _commands.get("home").execute(player, args);
                }
                break;
            case "delhome":
                if (checkPermission(player, "ltp.command.delhome")) {
                    return _commands.get("delhome").execute(player, args);
                }
                break;
            case "back":
                if (checkPermission(player, "ltp.command.back")) {
                    return _commands.get("back").execute(player, args);
                }
                break;
            default:
                return false;
        }

        return false;
    }

    public void registerCommands() {
        for (CommandBase command : _commands.values()) {
            PluginCommand pluginCommand = command.getPluginCommand();
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(command);
        }
    }

    private boolean checkPermission(Player player, String permission) {
        if (permission != null && !player.hasPermission(permission) && !player.isOp()) {
            player.sendMessage(_plugin.configurationHandler.getString("command.no_permission"));
            return false;
        }

        return true;
    }
}