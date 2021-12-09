package org.ensimag.konohaninja.jutsu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class MulticloneListener implements Listener{

    private static MulticloneListener instance;
    private final MulticloneCommand commandInstance;

    private MulticloneListener() {
        this.commandInstance = MulticloneCommand.getInstance();
    }

    public static MulticloneListener getInstance() {
        if (instance == null) {
            instance = new MulticloneListener();
        }
        return instance;
    }

    @EventHandler
    public void onPlayerConnection(PlayerJoinEvent event){
        commandInstance.cacheNPC(event.getPlayer().getDisplayName());
        Bukkit.getLogger().info(event.getPlayer().getDisplayName() + "added to cached NPCs");
    }
    
}
