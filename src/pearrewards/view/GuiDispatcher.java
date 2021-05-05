
package pearrewards.view;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;


public class GuiDispatcher {
        
    public static IGui getGUI(FileConfiguration config, Player p, int slots) {
        
        String type = config.getString("guy_type");
        
        switch(type) {
            case "chest":
                System.out.println("[PearRewards] Using chest gui.");
                return new GuiChest(p, slots);
            case "daily":
                System.out.println("[PearRewards] Using daily gui.");
                return new GuiDaily(p, slots);
            default:
                System.err.println("[PearRewards] The type \"" + type + "\" it's invalid. Check the file config.yml");
                System.err.println("[PearRewards] Using chest gui.");
                return new GuiChest(p, slots);
        }
        
    }

    
}
