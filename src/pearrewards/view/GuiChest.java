
package pearrewards.view;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


class GuiChest implements IGui {
 
    private int slots;
    private Player p;
    
    public GuiChest(Player p, int slots) {
        this.p = p;
        this.slots = 27;
    }
         
    @Override
    public void openMenu(String numRewards) {        
        Inventory inv = Bukkit.createInventory(null, slots, "§l§aPearRewards");

        for(int i = 0; i < slots; i++) 
            inv.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 13));

        inv.setItem(slots/2, setReward("§l§a Giorno n. " + numRewards));
      
        p.openInventory(inv);
    }
    
    private ItemStack setReward(String itemName) {
        
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
        ItemMeta meta  = item.getItemMeta();
        
        meta.setDisplayName(itemName);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);        
        item.setItemMeta(meta);
        
        return item;
    }

    @Override
    public void usedRewardMenu(String message) {
        ItemStack errorItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
        ItemMeta meta = errorItem.getItemMeta();        
        meta.setDisplayName("§l§c" + message);
        errorItem.setItemMeta(meta);        
        
        Inventory inv = Bukkit.createInventory(null, slots, "§l§aPearRewards");
        
        for(int i = 0; i < slots; i++) 
            inv.setItem(i, errorItem);
        
        p.openInventory(inv);
    }
        
}