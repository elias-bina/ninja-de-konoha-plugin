package org.ensimag.konohaninja.jutsu;



import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.MetadataStore;
import net.citizensnpcs.api.npc.NPC;

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

            Vector direction = player.getLocation().getDirection();
            direction.setY(0);
            direction.normalize();
            direction.multiply(MULTICLONE_RANGE);

            Location center = player.getLocation().add(direction);
            direction.multiply(-1);
            direction.normalize();
            center.setDirection(direction);
            
            direction.multiply(MULTICLONE_RANGE);

            

            for(int i = 0; i < CLONE_NUMBER;i++){
                direction.rotateAroundY(2*Math.PI / (CLONE_NUMBER + 1));

                Location clone_loc = center.add(direction);
                
                direction.multiply(-1);
                clone_loc.setDirection(direction);
                direction.multiply(-1);

                NPC e = cachedPlayerClone.get(player.getDisplayName()).copy();
                MetadataStore d = e.data();
                d.set("DeathDate", clone_loc.getWorld().getGameTime() + CLONE_LIFETIME);
                l.add(e);
                e.spawn(clone_loc);
                
                direction.multiply(-1);
                center.add(direction);
                direction.multiply(-1);

                center.getWorld().spawnFallingBlock(center, Bukkit.createBlockData(Material.ACACIA_PLANKS));
            }

        }
        return true;
    }
}
