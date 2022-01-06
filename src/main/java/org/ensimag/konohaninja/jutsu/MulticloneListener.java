package org.ensimag.konohaninja.jutsu;

import java.util.LinkedList;
import java.util.List;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.MetadataStore;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.api.trait.trait.Equipment.EquipmentSlot;


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

    // TODO : Sneak + eat + bunch of things to copy (if nms works)
    // @EventHandler
    // public void onPlayerAnim(PlayerArmSwingEvent event){
    //     Player p = event.getPlayer();
        
    //     List<NPC> l = commandInstance.getPlayerCloneList(p.getDisplayName());
    //     if(!l.isEmpty()){
    //         for(NPC npc : new LinkedList<>(l)){
    //             if(npc.isSpawned()){
    //                 Player npcEntity = (Player)npc.getEntity();
    //                 NMS.playAnimation(PlayerAnimation.ARM_SWING, npcEntity, 1);
    //             }
    //         }
    //     }
    // }

    @EventHandler
    public void onPlayerItemChange(PlayerItemHeldEvent event){
        Player p = event.getPlayer();
        
        List<NPC> l = commandInstance.getPlayerCloneList(p.getDisplayName());
        if(!l.isEmpty()){
            for(NPC npc : new LinkedList<>(l)){
                if(npc.isSpawned()){
                    Equipment e = npc.getTraitNullable(Equipment.class);
                    if(e != null){
                        e.set(EquipmentSlot.HAND, p.getInventory().getItem(event.getNewSlot()));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCloneHit(EntityDamageByEntityEvent event){
        Entity e = event.getEntity();
        if(e.hasMetadata("NPC")){
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
            Entity e = npc.getEntity();
            e.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, e.getLocation().add(0.0, 1.0, 0.0), 5);
            e.getWorld().playSound(e.getLocation(), Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, SoundCategory.PLAYERS, 1.0f, 0.0f);

            d.set("DeathDate", npc.getEntity().getWorld().getGameTime() + CLONE_DISAPPEARING_TICKS);
            npc.despawn();  
        }else {
            l.remove(npc);
            CitizensAPI.getNPCRegistry().deregister(npc);
        }         
    }

}
