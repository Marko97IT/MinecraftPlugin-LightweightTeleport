package com.marcoconcasdev.mc.lightweightteleport.handlers;

import com.marcoconcasdev.mc.lightweightteleport.handlers.interfaces.IConfigurationHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigurationHandler implements IConfigurationHandler {
    private final FileConfiguration _configuration;

    public ConfigurationHandler(JavaPlugin plugin) {
        _configuration = plugin.getConfig();
        _configuration.addDefault("tp_delay", 0);
        _configuration.addDefault("tp_cancel_move", false);
        _configuration.addDefault("tp_cancel_pvp", false);
        _configuration.addDefault("tp_move_cooldown", 10);
        _configuration.addDefault("tp_pvp_cooldown", 10);
        _configuration.addDefault("tp_request_expire", 30);
        _configuration.addDefault("show_prefix", true);
        _configuration.addDefault("strings", new Object());
        _configuration.addDefault("strings.prefix", "[LTP] ");
        _configuration.addDefault("strings.console_player_command_error", "You cannot use this command in the console.");
        _configuration.addDefault("strings.no_permission", "You don't have the permission to use this command.");
        _configuration.addDefault("strings.invalid_args", "Invalid arguments. Digit §6/ltp help§r for more informations.");
        _configuration.addDefault("strings.set_home", "The home §b%home%§r has been set.");
        _configuration.addDefault("strings.delete_home", "The home §b%home%§r has been deleted.");
        _configuration.addDefault("strings.empty_home_name", "You have to specify the home name.");
        _configuration.addDefault("strings.home_name_already_exists", "The specified home name already exists.");
        _configuration.addDefault("strings.home_name_not_found", "The specified home name was not found.");
        _configuration.addDefault("strings.back_not_found", "No previous location found.");
        _configuration.addDefault("strings.tp_request_sent", "Your teleport request has been sent to §b%player%§r.");
        _configuration.addDefault("strings.tp_request_declined", "Your teleport request to §b%player%§r was declined.");
        _configuration.addDefault("strings.tp_request_expired", "Your teleport request to §b%player%§r is expired.");
        _configuration.addDefault("strings.tp_request_banned", "Cannot send the teleport request because §b%player%§r does not want to receive requests from you.");
        _configuration.addDefault("strings.tp_request_no_pending", "You don't have any pending requests.");
        _configuration.addDefault("strings.tp_received_request_ltp", "§b%player%§r requested to teleport to you.\n\nDigit §6/ltp accept§r to accept or §6/ltp decline§r to decline.\nIf you do not wish to receive further requests from a user, digit §6/ltp ban <username>§r.");
        _configuration.addDefault("strings.tp_received_request_ltphere", "§b%player%§r requested to teleport you to him/her.\n\nDigit §6/ltp accept§r to accept or §6/ltp decline§r to decline.\nIf you do not wish to receive further requests from a user, digit §6/ltp ban <username>§r.");
        _configuration.addDefault("strings.tp_delay", "Please wait §b%seconds%§r seconds before being teleported.");
        _configuration.addDefault("strings.tp_canceled_move", "Your teleport request was canceled because you moved.");
        _configuration.addDefault("strings.tp_canceled_pvp", "Your teleport request was canceled because you are fighting.");
        _configuration.addDefault("strings.tp_cooldown", "Please wait §b%seconds%§r seconds before send another teleport request.");
        _configuration.options().copyDefaults(true);
        plugin.saveConfig();
    }

    public Object getConfigurationValue(String key) {
        return _configuration.get(key);
    }

    public String getString(String key) {
        Object showPrefixObj = _configuration.get("show_prefix");
        String prefix = "";

        if (showPrefixObj != null && (boolean)showPrefixObj) {
            prefix = (String) _configuration.get("strings.prefix");
        }

        return prefix + _configuration.get("strings." + key);
    }
}