package main;

import command.Piggy;
import listener.PiggyListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {
    private Main instance;
    private PiggyManager piggyManager;
    private Economy econ = null;

    @Override
    public void onEnable() {
        instance = this;

        if (!this.getDataFolder().mkdir()) {
            this.getDataFolder().mkdir();
        }

        File file = new File(getDataFolder() + File.separator + "config.yml");

        if (!file.exists()) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        } else {
            saveConfig();
            reloadConfig();
        }

        if (!setupEconomy()) {
            getLogger().info("[ 경고 ] Vault 플러그인이 발견되지 않았습니다!");
        }
        Bukkit.getLogger().info("꿀꿀이 은행 V1이 켜지는 중입니다!");
        piggyManager = new PiggyManager(instance);
        getCommand("piggy").setExecutor(new Piggy(piggyManager));
        Bukkit.getPluginManager().registerEvents(new PiggyListener(piggyManager, instance), this);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public Economy getEcon() {
        return econ;
    }

    @Override
    public void onDisable() {
        piggyManager.saveData();
        Bukkit.getLogger().info("꿀꿀이 은행 V1이 종료되는 중입니다!");
    }

}
