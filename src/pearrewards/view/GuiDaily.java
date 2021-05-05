
package pearrewards.view;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


class GuiDaily implements IGui {

    private int slots;
    private int elementsSlot;
    private Player p;
    
    public GuiDaily(Player p, int slots) {
        this.p = p;
        this.elementsSlot = slots;
        
        if((slots % 9) != 0)
            slots = (int)((slots/9)+1)*9;        
        
        this.slots = slots;
    }
    
    @Override
    public void openMenu(String numRewards) {
        Inventory inv = Bukkit.createInventory(null, slots, "§l§aPearRewards");
        int num = Integer.valueOf(numRewards);    
        
        for(int i = 0; i < elementsSlot; i++) {
            if(i < (num - 1))
                inv.setItem(i, oldItem(i+1));
            else
                inv.setItem(i, unavailableItem(i+1));
        }
        
        inv.setItem(num - 1, availableItem(num));        
        p.openInventory(inv);
    }
    
    private ItemStack oldItem(int day) {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
        ItemMeta meta  = item.getItemMeta();
        
        meta.setDisplayName("§l§c Giorno n. " + day);
        meta.setLore(Arrays.asList("§l§7 Già  utilizzato!"));
        item.setItemMeta(meta);
        
        return item;
    }
    
    private ItemStack unavailableItem(int day) {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemMeta meta  = item.getItemMeta();
        
        meta.setDisplayName("§l§8 Giorno n. " + day);
        meta.setLore(Arrays.asList("§l§7 Non disponibile!"));
        item.setItemMeta(meta);
        
        return item;
    }
    
    private ItemStack availableItem(int day) {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
        ItemMeta meta  = item.getItemMeta();
        
        meta.setDisplayName("§l§c Giorno n. " + day);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);
        
        return item;
    }
    
    @Override
    public void usedRewardMenu(String message) {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
        ItemMeta meta  = item.getItemMeta();
        meta.setDisplayName("§l§c" + message);
        item.setItemMeta(meta);
        
        Inventory inv = Bukkit.createInventory(null, slots, "§l§aPearRewards");
        
        for(int i = 0; i < slots; i++) {
            inv.setItem(i, item);
        }
        
        p.openInventory(inv);
    }
    
}
