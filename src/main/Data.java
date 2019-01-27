package main;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Data {
    private Pig myPig;
    private UUID uuid;
    private Double money;
    private Economy economy;
    private Player player;

    public Data(UUID uuid, Pig pig, Double money, Economy economy) {
        this.myPig = pig;
        this.uuid = uuid;
        this.money = money;
        this.economy = economy;

        if (Bukkit.getPlayer(uuid) != null) {
            player = Bukkit.getPlayer(uuid);
        }
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
        if (money <= 0) {
            player.sendMessage("저금한 돈이 없습니다!");
        } else if (money < amount) {
            player.sendMessage("출금할 금액이 저금한 금액보다 많습니다!");
        } else {
            economy.depositPlayer(player, amount);
            money -= amount;
            player.sendMessage(amount + "원을 출금하였습니다! 꿀꿀!");
        }
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            player.sendMessage("돈은 1원 이상이어야 합니다!");
        } else if (amount > economy.getBalance(player)) {
            player.sendMessage("돈이 부족합니다!");
        } else {
            economy.withdrawPlayer(player, amount);
            money += amount;
            player.sendMessage(amount + "원을 저금하였습니다! 꿀꿀!");
        }
    }
}
