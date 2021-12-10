package org.ensimag.konohaninja.jutsu;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.MetadataStore;
import net.citizensnpcs.api.npc.NPC;


public class MulticloneListener implements Listener{

    private static final long CLONE_DISAPPEARING_TICKS = 10;

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
            

            for(NPC npc : new LinkedList<>(l)){
                MetadataStore d = npc.data();
                if((long)d.get("DeathDate") < p.getWorld().getGameTime()){
                    killNPC(npc, l);       
                    continue;
                }
                if(npc.isSpawned()){
                    displacement.rotateAroundY(2*Math.PI / (MulticloneCommand.CLONE_NUMBER + 1));
                    look.rotateAroundY(2*Math.PI / (MulticloneCommand.CLONE_NUMBER + 1));
                    Entity npcEntity = npc.getEntity();
                    npcEntity.teleport(npcEntity.getLocation().setDirection(look));
                    npcEntity.setVelocity(displacement);
                }
            }
        }
        // Avant :  event.getFrom() Après :  event.getTo() Les 2 c'est des loc
    }

    @EventHandler
    public void onCloneHit(EntityDamageByEntityEvent event){
        Entity e = event.getEntity();
        if(e.hasMetadata("NPC")){
            Bukkit.getLogger().info("NPC " + e.getName() + " has been hit");
            NPC npc = CitizensAPI.getNPCRegistry().getNPC(e);
            if(npc != null){
                List<NPC> l = commandInstance.getPlayerCloneList(e.getName());
                killNPC(npc, l);
            }
        }
    }


    private void killNPC(NPC npc, List<NPC> l){
        MetadataStore d = npc.data();
        if(npc.isSpawned()){
            d.set("DeathDate", npc.getEntity().getWorld().getGameTime() + CLONE_DISAPPEARING_TICKS);
            npc.despawn();  
        }else {
            l.remove(npc);
            CitizensAPI.getNPCRegistry().deregister(npc);
        }         
    }

}
