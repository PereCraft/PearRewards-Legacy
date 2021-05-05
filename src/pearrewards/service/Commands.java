
package pearrewards.service;

import pearrewards.control.ServiceManager;
import pearrewards.view.GuiDispatcher;
import pearrewards.view.IGui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;


public class Commands implements CommandExecutor {

    private FileConfiguration config;
    private ArrayList<ConfigurationSection> elements;
    private ServiceManager sm;
    
    public Commands() {}
    
    public void setServiceManager(ServiceManager sm) {
        this.sm = sm;
    }
    
    public void setConfig(FileConfiguration config) {
        this.config = config;
    }
    
    public void setElements(List<ConfigurationSection> elements) throws Exception {
        this.elements = (ArrayList<ConfigurationSection>)elements;
        
        if(elements.isEmpty())
            throw new Exception("[PereRewards] Error on the configuration file, check the rewards section");
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String name, String[] args) {
        
        if(cs instanceof Player) {
            Player p = (Player)cs;
            IGui gui = GuiDispatcher.getGUI(config, p, elements.size());
            
            if(cmd.getName().equalsIgnoreCase("daily") && p.hasPermission("pearrewards.shortdaily")) {
                
                if(args.length == 0) {                
                    if(sm.getNumRewards(p.getName()) == sm.getReedemRewards(p.getName())
                    		&& Boolean.parseBoolean(config.getString("used-rewards-menu"))) {
                        
                    	gui.usedRewardMenu(config.getString("used_rewards"));
                        return false;
                        
                    }else if(p.getInventory().firstEmpty() == -1) {
                        p.sendMessage(ChatColor.RED + config.getString("empty_message"));
                        return false;
                    }

                    gui.openMenu(String.valueOf(sm.getNumRewards(p.getName())));
                    return true;
                }
                
            } else if(cmd.getName().equalsIgnoreCase("pearrewards")) {                
                
                if((args.length == 0) || ((args.length == 1) && (args[0].equalsIgnoreCase("help")))) {
                    
                    p.sendMessage(net.md_5.bungee.api.ChatColor.GREEN + "----- PearRewards help page ------------");
                
                    p.spigot().sendMessage(new ComponentBuilder("/daily")
                                .color(net.md_5.bungee.api.ChatColor.YELLOW)
                                .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/daily"))
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("Permette di riscattare i reward!")))
                                .create()
                    );

                    p.spigot().sendMessage(new ComponentBuilder("/pr (help)")
                                .color(net.md_5.bungee.api.ChatColor.YELLOW)
                                .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pr help"))
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("Mostra questa schermata!")))
                                .create()
                    );
                    
                    p.sendMessage(ChatColor.RED + "Find more: " + ChatColor.GRAY + "https://github.com/PereCraft");

                    p.sendMessage(net.md_5.bungee.api.ChatColor.GREEN + "----- PearRewards help page -----");
                    
                    return true;
                
                } else if((args.length == 1) && p.hasPermission("pearrewards.admin")) {

                	if(args[0].equalsIgnoreCase("adminhelp")) {
                    	
                    	p.spigot().sendMessage(new ComponentBuilder("/pr restart")
                                .color(net.md_5.bungee.api.ChatColor.YELLOW)
                                .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pr restart"))
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("Riavvia plugin!")))
                                .create()
                    	);
                    	
                    	p.spigot().sendMessage(new ComponentBuilder("/pr refresh")
                                .color(net.md_5.bungee.api.ChatColor.YELLOW)
                                .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pr refresh"))
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("Ricarica file config!")))
                                .create()
                    	);
                    	
                    	return true;
                    	
                    }else if(args[0].equalsIgnoreCase("restart")) {
                        sm.restart();
                        p.sendMessage("[PearRewards] Plugin restarted!");
                        return true;
                    } else if(args[0].equalsIgnoreCase("refresh")) {
                        boolean ref = sm.refreshConfig();
                        
                        if(ref) p.sendMessage("[PearRewards] config.yml reloaded");
                        else p.sendMessage("[PearRewards] There was an error!");
                        
                        return ref;
                    }
                    
                }
                                
            }
            
            return false;
            
        } else if(cs instanceof ConsoleCommandSender) {
            
            if(cmd.getName().equalsIgnoreCase("pearrewards")) {                
                
                if(args.length == 1) {
                    
                	if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("adminhelp")) {
                    	
                		cs.sendMessage("§e/daily §7Riscatta i rewards (eseguibile solo in gioco).");
                    	cs.sendMessage("§e/pr help §7Mostra questo messaggio.");
                    	cs.sendMessage("§e/pr refresh §7Ricarica il file config.");
                    	cs.sendMessage("§e/pr restart §7Riavvia il plugin.");
                    	
                    	return true;
                    }else if(args[0].equalsIgnoreCase("restart")) {
                        sm.restart();
                        cs.sendMessage("[PearRewards] Plugin restarted!");
                        return true;
                    } else if(args[0].equalsIgnoreCase("refresh")) {
                        boolean ref = sm.refreshConfig();
                        
                        if(ref) cs.sendMessage("[PearRewards] config.yml reloaded");
                        else cs.sendMessage("[PearRewards] There was an error!");
                        
                        return ref;
                    }
                    
                }else {
                	cs.sendMessage("[PearRewards] §cArgomento sconosciuto. Digita '/pr help' per l'elenco dei comandi.");
                	return true;
                }
            }
        }
        
        return false;
    }

}
