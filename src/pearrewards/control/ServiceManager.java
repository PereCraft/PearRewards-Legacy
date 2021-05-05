
package pearrewards.control;

import pearrewards.PearRewards;
import pearrewards.connection.ConnectionDispatcher;
import pearrewards.connection.IConnection;
import pearrewards.persistence.ConfigurationFile;
import pearrewards.service.Commands;
import pearrewards.service.ListenerEvent;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;



public class ServiceManager {
    
    private static ServiceManager sm = null;
    
    private PearRewards plugin;
    private Commands commands;
    private ListenerEvent listener;
    private ConfigurationFile config;
    private IConnection connection;
    
    private ServiceManager(PearRewards plugin, Commands commands, ListenerEvent listener, ConfigurationFile config) throws ClassNotFoundException, SQLException, Exception {
        this.plugin     = plugin;
        this.commands   = commands;
        this.listener   = listener;
        this.config     = config;
        this.connection = ConnectionDispatcher.getDB(
                config
        );
        
        // commands init
        this.commands.setServiceManager(this);
        this.commands.setConfig(this.getConfig());
        this.commands.setElements(this.getDailyElements());
        
        // listener init
        this.listener.setServiceManager(this);
        
        // db connection 
        this.connection.checkDBexist();
    }
    
    public static ServiceManager getServiceManager(PearRewards plugin, Commands commands, ListenerEvent listener, ConfigurationFile config) throws Exception, ClassNotFoundException, SQLException {
        if(sm == null) 
            sm = new ServiceManager(plugin, commands, listener, config);
        
        return sm;
    }
    
    // COMANDI ADMIN //
    
    public Commands getCommands() {
        return commands;
    }
    
    public ListenerEvent getListener() {
        return listener;
    }
    
    public FileConfiguration getConfig() {
        return config.getConfig();
    }
    
    public void restart() {
        plugin.onReload();
    }
    
    public boolean refreshConfig() {
        try {
            config.refreshConfig();
            commands.setConfig(this.getConfig());
            commands.setElements(this.getDailyElements());
        } catch(Exception ex) {
            return false;
        }
        
        return true;
    }
    
    public void disablePlugin() {
        this.plugin     = null;
        this.commands   = null;
        this.listener   = null;
        this.config     = null;
        this.connection = null;
        
        this.sm = null;
    }
    
    // COMANDI COMMANDS //
    
    public List<ConfigurationSection> getDailyElements() {
        return config.getDailyElements();
    }
    
    public int checkData(String username) {
        
        try {
            if(!connection.checkUserExist(username)) {
                connection.createUser(username);
                return 0;
            } else {
                
                LocalDate dateSQL = LocalDate.parse(connection.getUserDate(username).toString());
                LocalDate dateCurrent = LocalDate.now();

                return Period.between(dateSQL, dateCurrent).getDays();
            }
            
        } catch(SQLException ex) {
            System.err.println("Error: " + ex.getMessage());
            return -1;
        }
        
    }
    
    public Date getUserDate(String username) throws SQLException {
        
        try {
            return connection.getUserDate(username);
        } catch(SQLException ex) {
            System.err.println("[PearRewards] Error: " + ex.getMessage());
            plugin.onDisable();
            throw new SQLException();
        }
        
    }
    
    public boolean updateDate(String username) {
        return connection.updateDate(username);
    }
    
    public void incrementNumRewards(String username) {
        connection.incrementNumRewards(username);
    }
    
    public void resetRewards(String username) {
        connection.resetNumRewards(username);
        connection.resetReedemRewards(username);
    }
    
    public int getNumRewards(String username) {
        return connection.getNumRewards(username);
    }
    
    public void updateReedemRewards(String username) {
        connection.incrementReedemRewards(username);
    }
    
    public int getReedemRewards(String username) {
        return connection.getReedemRewards(username);
    }
    
}
