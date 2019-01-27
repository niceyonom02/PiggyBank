package listener;

import inventory.MainInventory;
import main.Data;
import main.Main;
import main.PiggyManager;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;

public class PiggyListener implements Listener {
    private Main instance;
    private PiggyManager piggyManager;

    public PiggyListener(PiggyManager piggyManager, Main instance) {
        this.instance = instance;
        this.piggyManager = piggyManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        piggyManager.turnOnPiggy(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        piggyManager.turnOffPiggy(player);
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Pig) {
            Pig pig = (Pig) e.getEntity();

            if (piggyManager.isPiggy(pig)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Pig) {
            Pig pig = (Pig) e.getEntity();

            if (piggyManager.isPiggy(pig)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();

        if (e.getRightClicked() instanceof Pig) {
            Pig pig = (Pig) e.getRightClicked();

            if (piggyManager.searchDataInPiggy(player) != null) {
                Data data = piggyManager.searchDataInPiggy(player);

                if (e.getHand().equals(EquipmentSlot.HAND)) {
                    if (data.getPig() == null) {
                        return;
                    }
                    if (data.getPig().equals(pig)) {
                        new MainInventory(piggyManager, player, instance);
                    }
                }
            }
        }
    }


}
