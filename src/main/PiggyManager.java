package main;

import net.minecraft.server.v1_12_R1.EntityInsentient;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.UUID;

public class PiggyManager {
    private Main instance;
    private ArrayList<Data> piggyList = new ArrayList<>();

    public PiggyManager(Main instance) {
        this.instance = instance;
        loadData();
        followPlayer();
    }

    public boolean isPlayerHasPiggy(Player player) {
        for (Data data : piggyList) {
            if (data.getUuid().equals(player.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    public void addNewPiggy(Player player) {
        if (!isPlayerHasPiggy(player)) {
            Pig pig = (Pig) player.getWorld().spawnEntity(player.getLocation(), EntityType.PIG);
            piggyList.add(new Data(player.getUniqueId(), pig, 0.0, instance));
            player.sendMessage("§e꿀꿀이 은행을 만들었습니다!");
            player.sendMessage("§e꿀꿀!");
        } else {
            player.sendMessage("§c이미 꿀꿀이 은행이 있습니다!");
        }
    }

    /**
     * determine if pig is not natural entity, but is bank
     */
    public boolean isPiggy(@Nullable Pig pig) {
        for (Data data : piggyList) {
            if (data.getPig() != null) {
                if (data.getPig().equals(pig)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void turnOnPiggy(Player player) {
        if (isPlayerHasPiggy(player)) {
            Data data = searchDataInPiggy(player);
            if (data != null) {
                if (data.getPig() == null) {
                    Pig pig = (Pig) player.getWorld().spawnEntity(player.getLocation(), EntityType.PIG);
                    data.setPig(pig);
                } else {
                    player.sendMessage("§c이미 켜진 기능입니다!");
                }
            }
        } else {
            player.sendMessage("§c꿀꿀이 은행이 없습니다!");
        }
    }

    public void removePiggy(Player player) {
        if (isPlayerHasPiggy(player)) {
            Data data = searchDataInPiggy(player);
            if (data != null) {
                data.setPig(null);
                instance.getEcon().depositPlayer(player, data.getMoney());
                piggyList.remove(data);
                player.sendMessage("§e꿀꿀 ㅜㅜㅜ");
            }
        } else {
            player.sendMessage("§c꿀꿀이 은행이 없습니다!");
        }
    }

    public void turnOffPiggy(Player player) {
        if (isPlayerHasPiggy(player)) {
            Data data = searchDataInPiggy(player);
            if (data != null) {
                if (data.getPig() != null) {
                    data.getPig().remove();
                    data.setPig(null);
                } else {
                    player.sendMessage("§c이미 꺼진 기능입니다!");
                }
            }
        } else {
            player.sendMessage("§c꿀꿀이 은행이 없습니다!");
        }
    }

    public Data searchDataInPiggy(Player player) {
        for (Data data : piggyList) {
            if (data.getUuid().equals(player.getUniqueId())) {
                return data;
            }
        }
        return null;
    }

    private void loadData() {
        if (instance.getConfig().getConfigurationSection("piggy") != null) {
            for (String uuid : instance.getConfig().getConfigurationSection("piggy").getKeys(false)) {
                Double money = (Double) (instance.getConfig().get("piggy." + uuid));

                if (money == null) {
                    money = 0.0;
                }
                Data temp = new Data(UUID.fromString(uuid), null, money, instance);
                piggyList.add(temp);
            }
        }
    }

    public void saveData() {
        instance.getConfig().set("piggy", null);
        for (Data data : piggyList) {
            UUID uuid = data.getUuid();
            Double money = data.getMoney();

            if (data.getPig() != null) {
                data.getPig().remove();
                data.setPig(null);
            }
            instance.getConfig().set("piggy." + uuid, money);
        }
        instance.saveConfig();
    }

    private double getDistance(Player player, LivingEntity entity) {
        Location playerLOC = player.getLocation();
        Location entityLOC = entity.getLocation();

        double distance = Math.sqrt(Math.pow((playerLOC.getX() - entityLOC.getX()), 2) + Math.pow((playerLOC.getY() - entityLOC.getY()), 2) + Math.pow((playerLOC.getZ() - entityLOC.getZ()), 2));
        return distance;
    }

    private void followPlayer() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable() {
            @Override
            public void run() {
                for (Data data : piggyList) {
                    final LivingEntity e = data.getPig();
                    final Player p = Bukkit.getPlayer(data.getUuid());
                    final float f = (float) 1.5;

                    if (e != null && p != null && getDistance(p, e) > 3) {
                        ((EntityInsentient) ((CraftEntity) e).getHandle()).getNavigation().a(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), f);

                        if (getDistance(p, e) > 20) {
                            e.teleport(p);
                        }
                    }
                }
            }
        }, 0, 20);
    }
}
