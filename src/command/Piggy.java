package command;

import main.PiggyManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Piggy implements CommandExecutor {
    private PiggyManager piggyManager;

    public Piggy(PiggyManager piggyManager) {
        this.piggyManager = piggyManager;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (label.equalsIgnoreCase("piggy")) {
            if (arg.length < 1) {
                helpMessage(player);
                return false;
            }

            if (arg[0].equalsIgnoreCase("create")) {
                piggyManager.addNewPiggy(player);
                return true;
            } else if (arg[0].equalsIgnoreCase("delete")) {
                piggyManager.removePiggy(player);
                return true;
            } else if (arg[0].equalsIgnoreCase("on")) {
                piggyManager.turnOnPiggy(player);
                return true;
            } else if (arg[0].equalsIgnoreCase("off")) {
                piggyManager.turnOffPiggy(player);
            }

        }
        return false;
    }

    public void helpMessage(Player player) {
        player.sendMessage("/piggy create");
        player.sendMessage("/piggy delete");
        player.sendMessage("/piggy on");
        player.sendMessage("/piggy off");
    }

}
