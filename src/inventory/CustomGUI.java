package inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public interface CustomGUI {
    void implementLayout();

    void implementMoveItem();

    void implementItem();

    void openGUI();

    void clickGUI(Player player, int slot);

    @EventHandler
    void onClose(InventoryCloseEvent e);

    @EventHandler
    void onClick(InventoryClickEvent e);
}
