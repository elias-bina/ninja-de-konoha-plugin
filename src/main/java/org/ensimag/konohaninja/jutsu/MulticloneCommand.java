package org.ensimag.konohaninja.jutsu;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.api.trait.trait.Equipment.EquipmentSlot;

public class MulticloneCommand implements CommandExecutor {

    private static MulticloneCommand instance;

    private final Map<String, List<HumanEntity>> clonesPerPlayer;

    private MulticloneCommand() {
        this.clonesPerPlayer = new HashMap<>();
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

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length != 0){
            return false;
        }
        if (sender instanceof Player player) {

            NPC e = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "UwU");

            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta headMeta = (SkullMeta) head.getItemMeta();
            headMeta.setOwningPlayer(player);
            head.setItemMeta(headMeta);
            Equipment equipmentTrait = new Equipment();
            equipmentTrait.set(EquipmentSlot.HELMET, head);
            e.addTrait(equipmentTrait);
            
            for(Trait t : e.getTraits()){
                Bukkit.getLogger().info(t.toString());
            }
            //e.getEquipment().setHelmet(head , false);
        }
        return true;
    }
}
