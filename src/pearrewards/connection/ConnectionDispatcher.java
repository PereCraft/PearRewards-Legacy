
package pearrewards.connection;

import pearrewards.persistence.ConfigurationFile;


public class ConnectionDispatcher {
    
    public static IConnection getDB(ConfigurationFile config) throws ClassNotFoundException {
        
        String type = config.getConfig().getString("db_type");
        
        switch(type){
            case "mysql":
                System.out.println("[PearRewards] Creating mysql database.");
                return new ConnectionMySQL(config);
            case "sqlite":
                System.out.println("[PearRewards] Creating sqlite database.");
                return new ConnectionSQLite(config);
            default:
                System.err.println("[PearRewards] The type \"" + type + "\" it's invalid. Check the file config.yml");
                System.err.println("[PearRewards] Creating sqlite database.");
                return new ConnectionSQLite(config);
        }
        
    }
    
}
