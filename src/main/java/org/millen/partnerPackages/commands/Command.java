package org.millen.partnerPackages.commands;

import org.millen.partnerPackages.PackagePlugin;
import org.millen.partnerPackages.util.Color;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public class Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {

            if (!sender.hasPermission("partnerpackage.admin")) {
                sender.sendMessage(Color.translate("&cNo Permission."));
                return true;
            }

            if (args.length < 2) {
                sender.sendMessage(Color.translate("&c/package <player> <amount>" +
                        "\n/package giveall <amount>"));

                return true;
            }

            if (args[0].equalsIgnoreCase("giveall")){
                // give all a partner package
                if (!StringUtils.isNumeric(args[1])){
                    sender.sendMessage(Color.translate("&c" + args[1] + " must be a number."));
                    return true;
                }

                Player senderPlayer = (Player) sender;

                int amt = Integer.parseInt(args[1]);
                sender.sendMessage(Color.translate("&aYou have given everyone a partner package."));

                String worldName = senderPlayer.getLocation().getWorld().getName();
                for (Player p : Bukkit.getOnlinePlayers()){
                    if (p.getWorld().getName().equalsIgnoreCase(worldName)){
                        p.getInventory().addItem(PackagePlugin.getInstance().packageItem(amt));
                        p.sendMessage(Color.translate("&fYou have been given &a" + amt + "&ax&f " + "Partner Package" + (amt > 1 ? "s&f" : "") + " from a &aGIVEALL!&f"));
                    }
                }
                return true;
            }

            Player player = Bukkit.getPlayer(args[0]);

            if (player == null) {
                sender.sendMessage(Color.translate("&c" + args[0] + " is not online."));
                return true;
            }

            if (!StringUtils.isNumeric(args[1])) {
                sender.sendMessage(Color.translate("&c" + args[1] + " must be a number."));
                return true;
            }

            int amt = Integer.parseInt(args[1]);

            player.getInventory().addItem(PackagePlugin.getInstance().packageItem(amt));
            sender.sendMessage(Color.translate("&fYou have given &a" + player.getName() + " " + amt + "&ax" + " &aPartner Package" + (amt > 1 ? "s" : "") + "&f."));
            player.sendMessage(Color.translate("&fYou have been given &a" + amt + "&ax" + " &aPartner Package" + (amt > 1 ? "s" : "") + "&f."));
            return true;
        }
}