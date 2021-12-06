package org.ensimag.konohaninja.jutsu;

import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import io.papermc.paper.event.player.PlayerArmSwingEvent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class SubstituteListener implements Listener{

    private static final double TELEPORTATION_LENGTH = 12.0;

    private static SubstituteListener instance;

    private SubstituteListener() {
    }

    public static SubstituteListener getInstance() {
        if (instance == null) {
            instance = new SubstituteListener();
        }
        return instance;
    }

    @EventHandler
    public void onWoodLeftClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        EquipmentSlot hand = event.getHand();
        ItemStack mainItem = player.getInventory().getItemInMainHand();
        ItemStack offItem = player.getInventory().getItemInOffHand();
        if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK){
            if((hand == EquipmentSlot.HAND && mainItem != null && (mainItem.getType().toString().contains("LOG") ||
                                                                mainItem.getType().toString().contains("WARPED_STEM")||
                                                                mainItem.getType().toString().contains("CRIMSON_STEM"))) ||
            (hand == EquipmentSlot.OFF_HAND && offItem != null && (offItem.getType().toString().contains("LOG") ||
                                                                    offItem.getType().toString().contains("WARPED_STEM")||
                                                                    offItem.getType().toString().contains("CRIMSON_STEM"))) ){
                
                player.getInventory().getHeldItemSlot();
                player.getInventory().getItemInMainHand();
                player.getInventory().getItemInOffHand();

                // TODO : Add suffocating protection

                Location loc = player.getLocation();

                Vector dir = loc.getDirection();
                Vector diff = dir.multiply(-TELEPORTATION_LENGTH);

                loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 5);
                Material entity_material = hand == EquipmentSlot.HAND ? player.getInventory().getItemInMainHand().getType() : player.getInventory().getItemInOffHand().getType();
                loc.getWorld().spawnFallingBlock(loc, Bukkit.createBlockData(entity_material));

                loc.add(diff);            
                player.teleport(loc);

                
                

                Bukkit.getLogger().info( player.getInventory().getItemInMainHand().getType().toString());
            }
        }
    }
}
