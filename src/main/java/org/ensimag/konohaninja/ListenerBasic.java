package org.ensimag.konohaninja;

import org.bukkit.event.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;



public class ListenerBasic implements Listener{

    private static ListenerBasic instance;

    private ListenerBasic() {
    }

    public static ListenerBasic getInstance() {
        if (instance == null) {
            instance = new ListenerBasic();
        }
        return instance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        //event.joinMessage(Component.text("Bienvenue " + event.getPlayer().getName() + "-san"));
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        //event.quitMessage(Component.text("Retournes voir orochimaru " + event.getPlayer().getName() + "-san è-é"));
    }


    //  // Executes before the second method because it has a much lower priority.
    //  @EventHandler (priority = EventPriority.LOWEST)
    //  public void onPlayerChat1(AsyncPlayerChatEvent event) {
    //      event.setCancelled(true);
    //  }

    //  // Will not execute unless another listener with a  lower priority has uncancelled the event.
    //  @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    //  public void onPlayerChat2(AsyncPlayerChatEvent event) {
    //      System.out.println("This shouldn't be executing.");
    //  }
}
