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

public class DepositInventory implements CustomGUI, Listener {
    private Main instance;
    private PiggyManager piggyManager;
    private boolean duplicate = false;
    private Player target;
    private Inventory deposit;

    public DepositInventory(PiggyManager piggyManager, Player player, Main instance) {
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
        deposit = Bukkit.createInventory(null, 27, "입금 메뉴");
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
        deposit.setItem(22, item);
    }

    @Override
    public void implementItem() {
        ArrayList<String> lore = new ArrayList<>();
        ItemStack item = new ItemStack(Material.STAINED_GLASS, 1);
        item.setDurability((short) 5);
        ItemMeta meta = item.getItemMeta();

        lore.add("100원 입금");
        meta.setLore(lore);
        meta.setDisplayName("100원 입금");
        item.setItemMeta(meta);
        deposit.setItem(11, item);

        lore.clear();

        lore.add("1000원 입금");
        meta.setLore(lore);
        meta.setDisplayName("1000원 입금");
        item.setItemMeta(meta);
        deposit.setItem(12, item);

        lore.clear();

        lore.add("10000원 입금");
        meta.setLore(lore);
        meta.setDisplayName("10000원 입금");
        item.setItemMeta(meta);
        deposit.setItem(13, item);

        lore.clear();

        lore.add("100000원 입금");
        meta.setLore(lore);
        meta.setDisplayName("100000원 입금");
        item.setItemMeta(meta);
        deposit.setItem(14, item);

        lore.clear();

        lore.add("1000000원 입금");
        meta.setLore(lore);
        meta.setDisplayName("1000000원 입금");
        item.setItemMeta(meta);
        deposit.setItem(15, item);

    }

    @Override
    public void openGUI() {
        target.openInventory(deposit);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getInventory().getTitle().equalsIgnoreCase("입금 메뉴")) {
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

        if (e.getInventory().getName().equalsIgnoreCase("입금 메뉴")) {
            e.setCancelled(true);

            if (!duplicate) {
                clickGUI(player, e.getSlot());
            }
        }
    }

    public void clickGUI(Player player, int slot) {
        Data data = piggyManager.searchDataInPiggy(player);

        if (data != null) {
            switch (slot) {
                case 11:
                    data.deposit(100);
                    break;
                case 12:
                    data.deposit(1000);
                    break;
                case 13:
                    data.deposit(10000);
                    break;
                case 14:
                    data.deposit(100000);
                    break;
                case 15:
                    data.deposit(1000000);
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
