package com.superiornetworks.pegasus.commands;

import com.superiornetworks.pegasus.PM_Rank;
import com.superiornetworks.pegasus.PM_SqlHandler;
import com.superiornetworks.pegasus.PM_Utils;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name = "tag", description = "Set your tag.", usage = "/<command> <set:playername> <tag>", rank = PM_Rank.Rank.OP)
public class Command_tag
{

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        try
        {
            if (args.length < 2)
            {
                return false;
            }

            if (args[0].equalsIgnoreCase("set"))
            {
                if (sender instanceof ConsoleCommandSender)
                {
                    sender.sendMessage(ChatColor.DARK_RED + "You can only set your tag in game.");
                    return true;
                }
                String tag = PM_Utils.buildMessage(args, 1);

                if (tag.contains("`") || tag.contains("'"))
                {
                    sender.sendMessage(ChatColor.DARK_RED + "For security reasons, you cannot use these charecters: ` or '");
                    return true;
                }
                if (tag.length() > 25)
                {
                    sender.sendMessage(ChatColor.DARK_RED + "Tags cannot be larger than 25 charecters.");
                    return true;
                }
                PM_SqlHandler.setTag(sender.getName(), tag);
                sender.sendMessage(ChatColor.GREEN + "Your tag is now: " + tag);
            }
            else
            {

                if (!PM_Rank.isRankOrHigher(sender, PM_Rank.Rank.SUPER))
                {
                    sender.sendMessage(PM_Utils.NO_PERMS_MESSAGE);
                }

                if (!PM_SqlHandler.playerExists(args[0]))
                {
                    sender.sendMessage(ChatColor.DARK_RED + "That player has not joined before.");
                    return true;
                }

                String tag = PM_Utils.buildMessage(args, 1);
                if (tag.contains("`") || tag.contains("'"))
                {
                    sender.sendMessage(ChatColor.DARK_RED + "For security reasons, you cannot use these charecters: ` or '");
                    return true;
                }
                if (tag.length() > 25)
                {
                    sender.sendMessage(ChatColor.DARK_RED + "Tags cannot be larger than 25 charecters.");
                    return true;
                }
                PM_SqlHandler.setTag(args[0], tag);
                sender.sendMessage(ChatColor.GREEN + "You set that player's tag to: " + tag);
                Player p = Bukkit.getServer().getPlayer(args[0]);
                if (p != null)
                {
                    p.sendMessage(ChatColor.GREEN + "Your tag was changed by: " + sender.getName() + " to: " + tag);
                }
            }
        }
        catch (SQLException ex)
        {
            sender.sendMessage(ChatColor.DARK_RED + "Something went horribly wrong, please notify a developer of IcarusMod.");
            Logger.getLogger(Command_tag.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
}
