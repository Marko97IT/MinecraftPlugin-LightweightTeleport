package com.marcoconcasdev.mc.lightweightteleport.handlers.interfaces;

public interface IConfigurationHandler {
    Object getConfigurationValue(String key);
    String getString(String key);
}