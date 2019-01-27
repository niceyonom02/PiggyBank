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

public class MainInventory implements CustomGUI, Listener {
    private Main instance;
    private PiggyManager piggyManager;
    private boolean duplicate = false;
    private Player target;
    private Inventory main;

    public MainInventory(PiggyManager piggyManager, Player player, Main instance) {
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
        main = Bukkit.createInventory(null, 27, "꿀꿀이 은행");
    }

    @Override
    public void implementMoveItem() {

    }

    @Override
    public void implementItem() {
        ArrayList<String> lore = new ArrayList<>();
        ItemStack item = new ItemStack(Material.DIAMOND, 1);
        ItemMeta meta = item.getItemMeta();

        lore.add("입금 메뉴를 엽니다.");
        meta.setLore(lore);
        meta.setDisplayName("입금 메뉴");
        item.setItemMeta(meta);
        main.setItem(10, item);

        lore.clear();

        item = new ItemStack(Material.GOLD_NUGGET, 1);
        meta = item.getItemMeta();

        Data data = piggyManager.searchDataInPiggy(target);
        if (data != null) {
            lore.add("꿀꿀이 안에 있는 돈: " + data.getMoney() + "원");
            meta.setLore(lore);
        }

        meta.setDisplayName("현재 예금액");
        item.setItemMeta(meta);
        main.setItem(13, item);

        lore.clear();

        item = new ItemStack(Material.EMERALD, 1);
        meta = item.getItemMeta();

        lore.add("기타 메뉴를 엽니다.");
        meta.setLore(lore);
        meta.setDisplayName("기타 메뉴");
        item.setItemMeta(meta);
        main.setItem(16, item);
    }

    @Override
    public void openGUI() {
        target.openInventory(main);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getInventory().getTitle().equalsIgnoreCase("꿀꿀이 은행")) {
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

        if (e.getInventory().getTitle().equalsIgnoreCase("꿀꿀이 은행")) {
            e.setCancelled(true);
            if (!duplicate) {
                clickGUI(player, e.getSlot());
            }
        }
    }

    public void clickGUI(Player player, int slot) {
        switch (slot) {
            case 10:
                new DepositInventory(piggyManager, player, instance);
                break;
            case 16:
                new WithdrawInventory(piggyManager, player, instance);
                break;
            default:
                break;
        }
    }
}
