package org.ensimag.konohaninja.jutsu;

import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import io.papermc.paper.event.player.PlayerArmSwingEvent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class SubstituteListener implements Listener{

    private static final double TELEPORTATION_LENGTH = 10.0;

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
    public void onWoodLeftClick(PlayerArmSwingEvent event) {
        Player player = event.getPlayer();
        EquipmentSlot hand = event.getHand();
        ItemStack mainItem = player.getInventory().getItemInMainHand();
        ItemStack offItem = player.getInventory().getItemInOffHand();

        if((hand == EquipmentSlot.HAND && mainItem != null && (mainItem.getType().toString().contains("LOG") ||
                                                               mainItem.getType().toString().contains("WARPED_STEM")||
                                                               mainItem.getType().toString().contains("CRIMSON_STEM"))) ||
           (hand == EquipmentSlot.OFF_HAND && offItem != null && (offItem.getType().toString().contains("LOG") ||
                                                                  offItem.getType().toString().contains("WARPED_STEM")||
                                                                  offItem.getType().toString().contains("CRIMSON_STEM"))) ){
            
            player.getInventory().getHeldItemSlot();
            player.getInventory().getItemInMainHand();
            player.getInventory().getItemInOffHand();

            Location loc = player.getLocation();

            Vector dir = loc.getDirection();
            //Vector diff = dir.multiply(TELEPORTATION_LENGTH);

            Bukkit.getLogger().info(dir.toString() + " SPEED " + player.getFlySpeed());

            //loc.add(diff);
            
            //player.teleport(loc);
            player.setVelocity(dir.multiply(TELEPORTATION_LENGTH));


            
            

            Bukkit.getLogger().info( player.getInventory().getItemInMainHand().getType().toString());
        }
    }
}
