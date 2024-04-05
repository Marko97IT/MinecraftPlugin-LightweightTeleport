package com.marcoconcasdev.mc.lightweightteleport.handlers;

import com.marcoconcasdev.mc.lightweightteleport.LightweightTeleport;
import com.marcoconcasdev.mc.lightweightteleport.handlers.interfaces.IDatabaseHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class DatabaseHandler implements IDatabaseHandler {
    private final LightweightTeleport _plugin;
    private Connection _databaseConnection;

    public DatabaseHandler(LightweightTeleport plugin) {
        _plugin = plugin;
        String pluginPath = _plugin.getDataFolder().getAbsolutePath();

        try {
            Files.createDirectories(Paths.get(pluginPath));
            String databasePath = pluginPath + "/data.db";

            try {
                Class.forName("org.sqlite.JDBC");

                try {
                    _databaseConnection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);

                    try (Statement statement = _databaseConnection.createStatement()) {
                        statement.executeUpdate("CREATE TABLE IF NOT EXISTS homes (id INTEGER PRIMARY KEY, user TEXT, home TEXT, world TEXT, x REAL, y REAL, z REAL)");
                        statement.executeUpdate("CREATE TABLE IF NOT EXISTS bans (id INTEGER PRIMARY KEY, user TEXT, banned TEXT)");
                        statement.executeUpdate("CREATE TABLE IF NOT EXISTS backpositions (id INTEGER PRIMARY KEY, user TEXT, world TEXT, x REAL, y REAL, z REAL)");
                        statement.executeUpdate("CREATE TABLE IF NOT EXISTS tparequests (id INTEGER PRIMARY KEY, tpa INTEGER, user TEXT, user2 TEXT, timestamp INTEGER)");
                    }
                } catch (SQLException exception) {
                    _plugin.getLogger().log(Level.SEVERE, "An error occurred while attempting to load the plugin: Unable to initialize the database.");
                    _plugin.getLogger().log(Level.SEVERE, exception.toString());
                }
            } catch (ClassNotFoundException exception) {
                _plugin.getLogger().log(Level.SEVERE, "An error occurred while attempting to load the plugin: Unable to load SQLite driver.");
                _plugin.getLogger().log(Level.SEVERE, exception.toString());
            }
        } catch (IOException exception) {
            _plugin.getLogger().log(Level.SEVERE, "An error occurred while attempting to load the plugin: Unable to create the plugin folder.");
            _plugin.getLogger().log(Level.SEVERE, exception.toString());
        }
    }

    public void closeDatabaseConnection() {
        try {
            _databaseConnection.close();
        } catch (SQLException exception) {
            _plugin.getLogger().log(Level.SEVERE, "An error occurred while attempting to unload the plugin: Unable to close the database connection.");
            _plugin.getLogger().log(Level.SEVERE, exception.toString());
        }
    }

    @Nullable
    public List<String> getHomeNames(String user) {
        try (PreparedStatement statement = _databaseConnection.prepareStatement("SELECT home FROM homes WHERE user = ? LIMIT 10")) {
            statement.setString(1, user);

            try (ResultSet result = statement.executeQuery()) {
                List<String> homes = new ArrayList<>();
                while (result.next()) {
                    homes.add(result.getString("home"));
                }

                return homes;
            }
        } catch (SQLException exception) {
            _plugin.getLogger().log(Level.SEVERE, "An error occurred while attempting to get a home.");
            _plugin.getLogger().log(Level.SEVERE, exception.toString());
        }

        return null;
    }

    @Nullable
    public Location getHome(String user, String home) {
        try (PreparedStatement statement = _databaseConnection.prepareStatement("SELECT world, x, y, z FROM homes WHERE user = ? AND home = ?")) {
            statement.setString(1, user);
            statement.setString(2, home);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    World world = Bukkit.getWorld(result.getString("world"));
                    return new Location(world, result.getFloat("x"),result.getFloat("y"),result.getFloat("z"));
                }
            }
        } catch (SQLException exception) {
            _plugin.getLogger().log(Level.SEVERE, "An error occurred while attempting to get a home.");
            _plugin.getLogger().log(Level.SEVERE, exception.toString());
        }

        return null;
    }

    public boolean addHome(String user, String home, String world, double x, double y, double z) {
        if (getHome(user, home) == null) {
            try (PreparedStatement statement = _databaseConnection.prepareStatement("INSERT INTO homes (user, home, world, x, y, z) VALUES (?, ?, ?, ?, ?, ?)")) {
                statement.setString(1, user);
                statement.setString(2, home);
                statement.setString(3, world);
                statement.setDouble(4, x);
                statement.setDouble(5, y);
                statement.setDouble(6, z);
                statement.executeUpdate();
                return true;
            } catch (SQLException exception) {
                _plugin.getLogger().log(Level.SEVERE, "An error occurred while attempting to add a home.");
                _plugin.getLogger().log(Level.SEVERE, exception.toString());
            }
        }

        return false;
    }

    public boolean removeHome(String user, String home) {
        if (getHome(user, home) != null) {
            try (PreparedStatement statement = _databaseConnection.prepareStatement("DELETE FROM homes WHERE user = ? AND home = ?")) {
                statement.setString(1, user);
                statement.setString(2, home);
                statement.executeUpdate();
                return true;
            } catch (SQLException exception) {
                _plugin.getLogger().log(Level.SEVERE, "An error occurred while attempting to remove a home.");
                _plugin.getLogger().log(Level.SEVERE, exception.toString());
            }
        }

        return false;
    }

    public boolean getBan(String user, String banned) {
        try (PreparedStatement statement = _databaseConnection.prepareStatement("SELECT NULL FROM bans WHERE user = ? AND banned = ?")) {
            statement.setString(1, user);
            statement.setString(2, banned);
            ResultSet result = statement.executeQuery();
            return result.next();
        } catch (SQLException exception) {
            _plugin.getLogger().log(Level.SEVERE, "An error occurred while attempting to get a ban.");
            _plugin.getLogger().log(Level.SEVERE, exception.toString());
        }

        return false;
    }

    public boolean addBan(String user, String banned) {
        if (!getBan(user, banned)) {
            try (PreparedStatement statement = _databaseConnection.prepareStatement("INSERT INTO bans (user, banned) VALUES (?, ?)")) {
                statement.setString(1, user);
                statement.setString(2, banned);
                statement.executeUpdate();
                return true;
            } catch (SQLException exception) {
                _plugin.getLogger().log(Level.SEVERE, "An error occurred while attempting to add a ban.");
                _plugin.getLogger().log(Level.SEVERE, exception.toString());
            }
        }

        return false;
    }

    public boolean removeBan(String user, String banned) {
        if (getBan(user, banned)) {
            try (PreparedStatement statement = _databaseConnection.prepareStatement("DELETE FROM bans WHERE user = ? AND banned = ?")) {
                statement.setString(1, user);
                statement.setString(2, banned);
                statement.executeUpdate();
                return true;
            } catch (SQLException exception) {
                _plugin.getLogger().log(Level.SEVERE, "An error occurred while attempting to remove a ban.");
                _plugin.getLogger().log(Level.SEVERE, exception.toString());
            }
        }

        return false;
    }

    @Nullable
    public Location getBackPosition(String user) {
        try (PreparedStatement statement = _databaseConnection.prepareStatement("SELECT world, x, y, z FROM backpositions WHERE user = ?")) {
            statement.setString(1, user);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    World world = Bukkit.getWorld(result.getString("world"));
                    return new Location(world, result.getFloat("x"),result.getFloat("y"),result.getFloat("z"));
                }
            }
        } catch (SQLException exception) {
            _plugin.getLogger().log(Level.SEVERE, "An error occurred while attempting to get a back position.");
            _plugin.getLogger().log(Level.SEVERE, exception.toString());
        }

        return null;
    }

    public void updateBackPosition(String user, String world, double x, double y, double z) {
        if (getBackPosition(user) == null) {
            try (PreparedStatement statement = _databaseConnection.prepareStatement("INSERT INTO backpositions (user, world, x, y, z) VALUES (?, ?, ?, ?, ?)")) {
                statement.setString(1, user);
                statement.setString(2, world);
                statement.setDouble(3, x);
                statement.setDouble(4, y);
                statement.setDouble(5, z);
                statement.executeUpdate();
            } catch (SQLException exception) {
                _plugin.getLogger().log(Level.SEVERE, "An error occurred while attempting to insert a back position.");
                _plugin.getLogger().log(Level.SEVERE, exception.toString());
            }
        } else {
            try (PreparedStatement statement = _databaseConnection.prepareStatement("UPDATE backpositions SET world = ?, x = ?, y = ?, z = ? WHERE user = ?")) {
                statement.setString(1, world);
                statement.setDouble(2, x);
                statement.setDouble(3, y);
                statement.setDouble(4, z);
                statement.setString(5, user);
                statement.executeUpdate();
            } catch (SQLException exception) {
                _plugin.getLogger().log(Level.SEVERE, "An error occurred while attempting to update a back position.");
                _plugin.getLogger().log(Level.SEVERE, exception.toString());
            }
        }
    }
}