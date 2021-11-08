package org.ensimag.konohaninja;

import org.bukkit.plugin.java.JavaPlugin;

public class PluginMain extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("Konoha no Shinobi enabled");

        // this.getCommand("uwu").setExecutor(new CommandBasic());
        // this.getCommand("sortchest").setExecutor(SortingCommand.getInstance());
        // this.getCommand("ninjamode").setExecutor(NinjaModeCommand.getInstance());
        getServer().getPluginManager().registerEvents(ListenerBasic.getInstance(), this);
        // getServer().getPluginManager().registerEvents(ChestSortingListener.getInstance(), this);
        // getServer().getPluginManager().registerEvents(ItemReplacingListener.getInstance(), this);
        
    }
    @Override
    public void onDisable() {
        getLogger().info("Konoha no Shinobi disabled");
    }
}