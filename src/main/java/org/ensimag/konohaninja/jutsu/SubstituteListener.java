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
import net.md_5.bungee.api.chat.hover.content.Item;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class SubstituteListener implements Listener{

    private static final double TELEPORTATION_LENGTH = 12.0;

    private long lastUsed = 0;

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

        // TODO : Better workaround for multi-tick activation

        if(lastUsed != player.getWorld().getGameTime() && (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)){
            if((hand == EquipmentSlot.HAND && mainItem != null) ||
               (hand == EquipmentSlot.OFF_HAND && offItem != null)){

                ItemStack usedItem;
                if(hand == EquipmentSlot.HAND){
                    usedItem = mainItem;
                } else{
                    usedItem = offItem;
                }


                if((usedItem.getType().toString().contains("LOG") ||
                    usedItem.getType().toString().contains("WARPED_STEM")||
                    usedItem.getType().toString().contains("CRIMSON_STEM"))
                 && usedItem.getAmount() >= 2){
                    lastUsed = player.getWorld().getGameTime();
                    
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
                    loc = loc.add(0.0, 1.0, 0.0);
                    loc.getWorld().spawnFallingBlock(loc, Bukkit.createBlockData(entity_material));
                    loc.getWorld().playSound(loc, Sound.ENTITY_GHAST_SHOOT, SoundCategory.PLAYERS, 1.0f, 0.0f);

                    loc.add(diff);            
                    player.teleport(loc);

                    usedItem.setAmount(usedItem.getAmount() - 2);
                    Bukkit.getLogger().info(loc.getWorld().getGameTime() + "");

                }
            }
        }
    }
}
