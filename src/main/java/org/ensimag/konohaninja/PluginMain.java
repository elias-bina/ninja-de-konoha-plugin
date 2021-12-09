package org.ensimag.konohaninja;

import org.bukkit.plugin.java.JavaPlugin;
import org.ensimag.konohaninja.jutsu.*;
import org.ensimag.konohaninja.utils.*;

public class PluginMain extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("Konoha no Shinobi enabled");

        this.getCommand("multiclone").setExecutor(MulticloneCommand.getInstance());
        this.getCommand("purgeNPC").setExecutor(NPCPurgeCommand.getInstance());
        getServer().getPluginManager().registerEvents(MulticloneListener.getInstance(), this);
        getServer().getPluginManager().registerEvents(ListenerBasic.getInstance(), this);
        getServer().getPluginManager().registerEvents(SubstituteListener.getInstance(), this);
        
    }
    @Override
    public void onDisable() {
        getLogger().info("Konoha no Shinobi disabled");
    }
}