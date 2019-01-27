package main;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Data {
    private Pig myPig;
    private UUID uuid;
    private Main instance;
    private Double money;
    private Player player;

    public Data(UUID uuid, Pig pig, Double money, Main instance) {
        this.myPig = pig;
        this.uuid = uuid;
        this.money = money;
        this.instance = instance;
    }

    public Pig getPig() {
        return myPig;
    }

    public void setPig(Pig pig) {
        this.myPig = pig;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Double getMoney() {
        return money;
    }

    public void withdraw(double amount) {
        player = Bukkit.getPlayer(uuid);
        if (money <= 0) {
            player.sendMessage("§c저금한 돈이 없습니다!");
        } else if (money < amount) {
            player.sendMessage("§c출금할 금액이 저금한 금액보다 많습니다!");
        } else {
            instance.getEcon().depositPlayer(player, amount);
            player.sendMessage("§e"+ amount + "§7원을 출금하였습니다! 꿀꿀!");
            money -= amount;
        }
    }

    public void deposit(double amount) {
        player = Bukkit.getPlayer(uuid);
        if (amount <= 0) {
            player.sendMessage("§c돈은 1원 이상이어야 합니다!");
        } else if (amount > instance.getEcon().getBalance(player)) {
            player.sendMessage("§c돈이 부족합니다!");
        } else {
            instance.getEcon().withdrawPlayer(player, amount);
            player.sendMessage("§e" + amount + "§7원을 저금하였습니다! 꿀꿀!");
            money += amount;
        }
    }
}
