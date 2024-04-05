package com.marcoconcasdev.mc.lightweightteleport.handlers.interfaces;

import org.bukkit.Location;

import javax.annotation.Nullable;
import java.util.List;

public interface IDatabaseHandler {
    void closeDatabaseConnection();
    @Nullable
    List<String> getHomeNames(String user);
    @Nullable
    Location getHome(String user, String home);
    boolean addHome(String user, String home, String world, double x, double y, double z);
    boolean removeHome(String user, String home);
    boolean getBan(String user, String banned);
    @Nullable
    Location getBackPosition(String user);
    void updateBackPosition(String user, String world, double x, double y, double z);
}