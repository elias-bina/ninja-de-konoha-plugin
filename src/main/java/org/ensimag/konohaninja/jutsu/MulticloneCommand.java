package org.ensimag.konohaninja.jutsu;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
            player.getWorld().spawnEntity(player.getLocation().add(2.0, 0.0, 0.0), EntityType.ZOMBIE);
        }
        return true;
    }
}
