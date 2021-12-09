package org.ensimag.konohaninja;

import org.bukkit.plugin.java.JavaPlugin;
import org.ensimag.konohaninja.jutsu.*;

public class PluginMain extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("Konoha no Shinobi enabled");

        // this.getCommand("uwu").setExecutor(new CommandBasic());
        // this.getCommand("sortchest").setExecutor(SortingCommand.getInstance());
        this.getCommand("multiclone").setExecutor(MulticloneCommand.getInstance());
        getServer().getPluginManager().registerEvents(ListenerBasic.getInstance(), this);
        getServer().getPluginManager().registerEvents(SubstituteListener.getInstance(), this);
        // getServer().getPluginManager().registerEvents(ItemReplacingListener.getInstance(), this);
        
    }
    @Override
    public void onDisable() {
        getLogger().info("Konoha no Shinobi disabled");
    }
}