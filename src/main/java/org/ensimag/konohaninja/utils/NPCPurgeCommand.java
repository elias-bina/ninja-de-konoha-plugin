package org.ensimag.konohaninja.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.npc.NPCRegistry;


public class NPCPurgeCommand implements CommandExecutor{

    private static NPCPurgeCommand instance;

    private NPCPurgeCommand() {
    }

    public static NPCPurgeCommand getInstance() {
        if (instance == null) {
            instance = new NPCPurgeCommand();
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
        NPCRegistry reg = CitizensAPI.getNPCRegistry();
        reg.despawnNPCs(DespawnReason.REMOVAL);
        reg.deregisterAll();

        return true;
    }
}
