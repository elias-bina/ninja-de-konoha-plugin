package org.ensimag.konohaninja.jutsu;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class MulticloneCommand implements CommandExecutor {

    private static MulticloneCommand instance;

    private final Map<String, NPC> cachedPlayerClone;
    private final Map<String, List<HumanEntity>> clonesPerPlayer;

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
        if(clonesPerPlayer.get(playerName) == null){
            cachedPlayerClone.put(playerName, CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, playerName));
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length != 0){
            return false;
        }
        if (sender instanceof Player player) {

           NPC e = cachedPlayerClone.get(player.getDisplayName());

            // SkinTrait skinTrait = new SkinTrait();
            // e.addTrait(skinTrait);


            
            // for(Trait t : e.getTraits()){
            //     Bukkit.getLogger().info(t.toString());
            // }

            e.spawn(player.getLocation().add(2.0, 0.0, 0.0));
            //e.getEquipment().setHelmet(head , false);
        }
        return true;
    }
}
