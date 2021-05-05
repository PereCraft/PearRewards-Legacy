
package pearrewards.service;

import pearrewards.control.ServiceManager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;


public class ListenerEvent implements Listener {
    
    private ServiceManager sm;
    
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String username = p.getName();
        
        if((sm.checkData(username) == 1) && (sm.getReedemRewards(username) == sm.getNumRewards(username))) {
            
            if(sm.getNumRewards(username) == sm.getDailyElements().size())
                sm.resetRewards(username);
            else
                sm.incrementNumRewards(username);
            
        } else if((sm.checkData(username) > 1) || (sm.getReedemRewards(username) != sm.getNumRewards(username))) {
            sm.resetRewards(username);
        }
        
        sm.updateDate(username);
        
        if(sm.getReedemRewards(username) != sm.getNumRewards(username))
            
        	p.spigot().sendMessage(new ComponentBuilder(sm.getConfig().getString("notify_message").replaceAll("&", "§"))
        			.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§aRiscatta reward!")))
        			.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/daily"))
        			.create()
        	);
        	
    }
    
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        
        if((e.getCurrentItem() == null) || (e.getCurrentItem().getType()==Material.AIR))
            return;
        
        if(e.getView().getTitle().equals("§l§aPearRewards")) {
            if(e.getCurrentItem().getItemMeta().isUnbreakable()) {
            	
            	Player p = (Player)e.getWhoClicked();
                int giorno = sm.getNumRewards(p.getName()) - 1;
                
                if(sm.getNumRewards(p.getName()) == sm.getReedemRewards(p.getName())) {
                	p.sendMessage("§cHai già riscattato il tuo reward!");
                	p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                	p.closeInventory();
                	return;
                }
                
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), 
                        getGiveCommand(
                                sm.getDailyElements().get(giorno).getString("give_command"),
                                p.getName()
                        )
                );
                
                p.sendMessage(sm.getDailyElements().get(giorno).getString("message")
                		.replaceAll("&", "§")
                );
                
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
                
                if(Boolean.parseBoolean(sm.getConfig().getString("close-gui"))) {
                	p.closeInventory();
                }
                
                sm.updateReedemRewards(p.getName());
                
            } 
        
            e.setCancelled(true);
        }
    }
    
    private String getGiveCommand(String command, String username) {
        return command.replace("%p%", username);
    }
    
    public void setServiceManager(ServiceManager sm) {
        this.sm = sm;
    }
    
    
}
