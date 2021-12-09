package org.ensimag.konohaninja.jutsu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;


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
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        Player p = event.getPlayer();
        Bukkit.getLogger().info("Avant : " + p.getLocation().getX() + " " + p.getLocation().getY() + " " + p.getLocation().getZ() + 
                            " Apres : " + event.getTo().getX() + " " + event.getTo().getY() + " " + event.getTo().getZ());
    }

}
