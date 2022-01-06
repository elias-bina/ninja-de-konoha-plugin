package org.ensimag.konohaninja.jutsu;



import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.MetadataStore;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.api.trait.trait.Equipment.EquipmentSlot;

public class MulticloneCommand implements CommandExecutor {

    private static final double MULTICLONE_RANGE = 15.0;
    private static final int CLONE_LIFETIME = 30*20;
    public static final int CLONE_NUMBER = 9;

    private static MulticloneCommand instance;

    private final Map<String, NPC> cachedPlayerClone;
    private final Map<String, List<NPC>> clonesPerPlayer;

    private MulticloneCommand() {
        this.clonesPerPlayer = new HashMap<>();
        this.cachedPlayerClone = new HashMap<>();
    }

    public static MulticloneCommand getInstance() {
        if (instance == null) {
            instance = new MulticloneCommand();
        }
        return instance;
    }

    public static void deleteInstance() {
        instance = null;
    }

    public void cacheNPC(String playerName){
        if(cachedPlayerClone.get(playerName) == null){
            cachedPlayerClone.put(playerName, CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, playerName));
        }
        if(clonesPerPlayer.get(playerName) == null){
            clonesPerPlayer.put(playerName, new LinkedList<>());
        }
    }

    public List<NPC> getPlayerCloneList(String playerName){
        return clonesPerPlayer.get(playerName);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length != 0){
            return false;
        }
        if (sender instanceof Player player && clonesPerPlayer.get(player.getDisplayName()).isEmpty()) {

            List<NPC> l = clonesPerPlayer.get(player.getDisplayName());

            // Sets the center of the clones circle
            Vector direction = player.getLocation().getDirection();
            direction.setY(0);
            direction.normalize();
            direction.multiply(MULTICLONE_RANGE);
            Location center = player.getLocation().add(direction);
            
            // Sets the direction to be on the player
            direction.multiply(-1);
            direction.normalize();
            center.setDirection(direction);
            direction.multiply(MULTICLONE_RANGE);

            

            for(int i = 0; i < CLONE_NUMBER;i++){

                // Rotates the clones direction to make a circle
                direction.rotateAroundY(2*Math.PI / (CLONE_NUMBER + 1));

                // Places the clone
                Location clone_loc = center.add(direction);
                
                // Change where the clone is looking, in order to make him look in the center of the circle
                direction.multiply(-1);
                clone_loc.setDirection(direction);
                direction.multiply(-1);


                // Creates the NPC instance that will be the clone
                NPC e = cachedPlayerClone.get(player.getDisplayName()).copy();
                MetadataStore d = e.data();
                d.set("DeathDate", clone_loc.getWorld().getGameTime() + CLONE_LIFETIME);
                
                // Copies the player equipement
                Equipment equipmentTrait = new Equipment();
                e.addTrait(equipmentTrait);

                ItemStack helmet = player.getEquipment().getHelmet();
                ItemStack chestplate = player.getEquipment().getChestplate();
                ItemStack leggings = player.getEquipment().getLeggings();
                ItemStack boots = player.getEquipment().getBoots();
                ItemStack mainHandItem = player.getEquipment().getItemInMainHand();
                ItemStack offHandItem = player.getEquipment().getItemInOffHand();
                equipmentTrait.set(EquipmentSlot.HELMET, helmet);
                equipmentTrait.set(EquipmentSlot.CHESTPLATE, chestplate);
                equipmentTrait.set(EquipmentSlot.LEGGINGS, leggings);
                equipmentTrait.set(EquipmentSlot.BOOTS, boots);
                equipmentTrait.set(EquipmentSlot.HAND, mainHandItem);
                equipmentTrait.set(EquipmentSlot.OFF_HAND, offHandItem);


                l.add(e);
                e.spawn(clone_loc);
                
                player.getWorld().playSound(clone_loc, Sound.ENTITY_ILLUSIONER_CAST_SPELL, SoundCategory.PLAYERS, 1.0f, 0.0f);
                
                // Resets the center position (YES, the add function is in place AND returns its result)
                direction.multiply(-1);
                center.add(direction);
                direction.multiply(-1);
            }

        }
        return true;
    }
}
