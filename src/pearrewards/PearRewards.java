
package pearrewards;

import pearrewards.control.ServiceManager;
import pearrewards.persistence.ConfigurationFile;
import pearrewards.service.Commands;
import pearrewards.service.ListenerEvent;

import java.io.IOException;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public class PearRewards extends JavaPlugin {

    private static PearRewards plugin;
    private ServiceManager sm;
    
    @Override
    public void onEnable() {        
        plugin = this;
        
        try {
            sm = ServiceManager.getServiceManager(
                    plugin,
                    new Commands(),
                    new ListenerEvent(),
                    ConfigurationFile.getConfigFile()
            );
            
            getCommand("pearrewards").setExecutor(sm.getCommands());
            getCommand("daily").setExecutor(sm.getCommands());
            Bukkit.getPluginManager().registerEvents(sm.getListener(), plugin);
            
            // elements
            System.out.println("[PearRewards] Setted " + sm.getDailyElements().size() + " items as rewards:");
            for(int i = 0; i < sm.getDailyElements().size(); i++) {
                System.out.println("[" + (i+1) + "] " +
                        sm.getDailyElements().get(i).getString("give_command"));
            }
                
        } catch (IOException|ClassNotFoundException|SQLException ex) {
            System.err.println("[PearRewards] Error: " + ex.getMessage());
            onDisable();
        } catch (Exception ex) {
            System.err.println("[PearRewards] Error: " + ex.getMessage());
            onDisable();
        }
        
    }

    @Override
    public void onDisable() {
        sm.disablePlugin();  
        sm = null;
    }
    
    public void onReload() {
        System.out.println("[PearRewards] Reload plugin...");
        sm.refreshConfig();
        plugin.getServer().getPluginManager().disablePlugin(plugin);
        plugin.getServer().getPluginManager().enablePlugin(plugin);
    }
        
    public static PearRewards getPlugin() {
        return plugin;
    }
    
}
