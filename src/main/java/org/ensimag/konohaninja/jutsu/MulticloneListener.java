package org.ensimag.konohaninja.jutsu;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import net.citizensnpcs.api.npc.NPC;


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
        
        List<NPC> l = commandInstance.getPlayerCloneList(p.getDisplayName());
        if(!l.isEmpty()){
            Vector displacement = event.getTo().clone().subtract(event.getFrom().clone()).toVector();
            Vector look = event.getFrom().getDirection();
            

            for(NPC npc : l){
                displacement.rotateAroundY(-2*Math.PI / (MulticloneCommand.CLONE_NUMBER + 1));
                look.rotateAroundY(-2*Math.PI / (MulticloneCommand.CLONE_NUMBER + 1));
                Entity npcEntity = npc.getEntity();
                npcEntity.teleport(npcEntity.getLocation().setDirection(look));
                npcEntity.setVelocity(displacement);
            }
        }
        // Avant :  event.getFrom() Après :  event.getTo() Les 2 c'est des loc
    }

}
