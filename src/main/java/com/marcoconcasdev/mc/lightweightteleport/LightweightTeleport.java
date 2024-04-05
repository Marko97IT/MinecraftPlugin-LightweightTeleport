package com.marcoconcasdev.mc.lightweightteleport;

import com.marcoconcasdev.mc.lightweightteleport.handlers.CommandsHandler;
import com.marcoconcasdev.mc.lightweightteleport.handlers.ConfigurationHandler;
import com.marcoconcasdev.mc.lightweightteleport.handlers.DatabaseHandler;
import com.marcoconcasdev.mc.lightweightteleport.handlers.interfaces.ICommandsHandler;
import com.marcoconcasdev.mc.lightweightteleport.handlers.interfaces.IConfigurationHandler;
import com.marcoconcasdev.mc.lightweightteleport.handlers.interfaces.IDatabaseHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class LightweightTeleport extends JavaPlugin implements Listener {
    public IDatabaseHandler databaseHandler;
    public IConfigurationHandler configurationHandler;
    public ICommandsHandler commandsHandler;

    public LightweightTeleport() {
        configurationHandler = new ConfigurationHandler(this);
        databaseHandler = new DatabaseHandler(this);
        commandsHandler = new CommandsHandler(this);
    }

    @Override
    public void onEnable() {
        commandsHandler.registerCommands();
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        databaseHandler.closeDatabaseConnection();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        databaseHandler.updateBackPosition(player.getUniqueId().toString(), player.getWorld().getName(), player.getX(), player.getY(), player.getZ());
    }

    public void teleportPlayer(Player player, Location location) {
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        databaseHandler.updateBackPosition(player.getUniqueId().toString(), player.getWorld().getName(), player.getX(), player.getY(), player.getZ());
        player.teleport(location);
    }
}