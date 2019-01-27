package inventory;

import main.Data;
import main.Main;
import main.PiggyManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class WithdrawInventory implements CustomGUI, Listener {
    private Main instance;
    private PiggyManager piggyManager;
    private boolean duplicate = false;
    private Player target;
    private Inventory withdraw;

    public WithdrawInventory(PiggyManager piggyManager, Player player, Main instance) {
        this.instance = instance;
        this.piggyManager = piggyManager;
        target = player;

        instance.getServer().getPluginManager().registerEvents(this, instance);

        implementLayout();
        implementMoveItem();
        implementItem();
        openGUI();
    }

    @Override
    public void implementLayout() {
        withdraw = Bukkit.createInventory(null, 27, "출금 메뉴");
    }

    @Override
    public void implementMoveItem() {
        ArrayList<String> lore = new ArrayList<>();
        ItemStack item = new ItemStack(Material.REDSTONE, 1);
        ItemMeta meta = item.getItemMeta();

        lore.add("뒤로가기");
        meta.setLore(lore);
        meta.setDisplayName("뒤로가기");
        item.setItemMeta(meta);
        withdraw.setItem(22, item);
    }

    @Override
    public void implementItem() {
        ArrayList<String> lore = new ArrayList<>();
        ItemStack item = new ItemStack(Material.STAINED_GLASS, 1);
        item.setDurability((short) 14);
        ItemMeta meta = item.getItemMeta();

        lore.add("100원 출금");
        meta.setLore(lore);
        meta.setDisplayName("100원 출금");
        item.setItemMeta(meta);
        withdraw.setItem(11, item);

        lore.clear();

        lore.add("1000원 출금");
        meta.setLore(lore);
        meta.setDisplayName("1000원 출금");
        item.setItemMeta(meta);
        withdraw.setItem(12, item);

        lore.clear();

        lore.add("10000원 출금");
        meta.setLore(lore);
        meta.setDisplayName("10000원 출금");
        item.setItemMeta(meta);
        withdraw.setItem(13, item);

        lore.clear();

        lore.add("100000원 출금");
        meta.setLore(lore);
        meta.setDisplayName("100000원 출금");
        item.setItemMeta(meta);
        withdraw.setItem(14, item);

        lore.clear();

        lore.add("1000000원 출금");
        meta.setLore(lore);
        meta.setDisplayName("1000000원 출금");
        item.setItemMeta(meta);
        withdraw.setItem(15, item);
    }

    @Override
    public void openGUI() {
        target.openInventory(withdraw);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getInventory().getTitle().equalsIgnoreCase("출금 메뉴")) {
            duplicate = true;
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null || !e.getCurrentItem().hasItemMeta() || !e.getCurrentItem().getItemMeta().hasDisplayName()) {
            return;
        }
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) e.getWhoClicked();

        if (e.getInventory().getName().equalsIgnoreCase("출금 메뉴")) {
            if (!duplicate) {
                e.setCancelled(true);
                clickGUI(player, e.getSlot());
            }
        }
    }

    public void clickGUI(Player player, int slot) {
        Data data = piggyManager.searchDataInPiggy(player);

        if (data != null) {
            switch (slot) {
                case 11:
                    data.withdraw(100);
                    break;
                case 12:
                    data.withdraw(1000);
                    break;
                case 13:
                    data.withdraw(10000);
                    break;
                case 14:
                    data.withdraw(100000);
                    break;
                case 15:
                    data.withdraw(1000000);
                    break;
                case 22:
                    new MainInventory(piggyManager, target, instance);
                    break;
                default:
                    break;
            }
        }
    }
}
