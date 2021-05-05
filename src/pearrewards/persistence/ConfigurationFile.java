
package pearrewards.persistence;

import pearrewards.PearRewards;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;



public class ConfigurationFile {
    
    private static ConfigurationFile config = null;
    
    private File file;
    private YamlConfiguration configFile;
    
    private ConfigurationFile() throws IOException {
        file = new File(PearRewards.getPlugin().getDataFolder().getPath() + "/config.yml");
                
        if(!file.exists()) {
            file.getParentFile().mkdirs();
            
            System.out.println("[PearRewards] Il file di configurazione non esiste, creazione del file config.yml");
            InputStream in = PearRewards.class.getResourceAsStream("/config.yml");
            OutputStream out = new FileOutputStream(file);
            
            try { 
                int n; 

                while ((n = in.read()) != -1) { 
                    out.write(n); 
                } 
            } finally { 
                if (in != null) in.close();
                if (out != null) out.close(); 
            } 
            
        }
        
        configFile = YamlConfiguration.loadConfiguration(file);
                
    }
    
    public static ConfigurationFile getConfigFile() throws IOException {
        
        if(config == null)
            config = new ConfigurationFile();
        
        return config;
    }
    
    public void refreshConfig() {
        configFile = YamlConfiguration.loadConfiguration(file);
    }
    
    public FileConfiguration getConfig() {
        return configFile;
    }

    public List<ConfigurationSection> getDailyElements() {
        List<ConfigurationSection> list = new ArrayList<>();
        
        configFile.getConfigurationSection("rewards").getKeys(false).forEach((day) -> {
            list.add(configFile.getConfigurationSection("rewards."+day));
        });
        
        return list;
    }
    
}
