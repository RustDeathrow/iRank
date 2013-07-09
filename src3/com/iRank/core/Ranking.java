package com.iRank.core;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.entity.Player;

import com.iRank.util.Config;
import com.iRank.util.Language;

public class Ranking 
{

	public boolean pay(Player player, Double amount)
	{

		if ( iRank.econ == null )
		{

			Language.send( player, "&cNo valid economy plugin found. Tell an administrator.");
			return false; 

		}

		EconomyResponse r = iRank.econ.withdrawPlayer(player.getName(), amount );

		if(r.transactionSuccess())
		{
			return true; 
		} else
		{
			player.sendMessage(String.format("%s", r.errorMessage));
			return false; 
		}
	}

	public boolean iRank(Player player)
	{

		if( iRank.perms.getGroups().length == 0 || !iRank.perms.hasSuperPermsCompat() )
		{

			Language.send( player, "&cNo valid permissions plugin found. Tell an administrator.");
			return false; 

		}

		if( Config.getRankToGroup( player ) != null )
		{

			String newRank = Config.getRankToGroup( player );
			Double rankPrice = Config.getGroupPrice( newRank );

			if( rankPrice < 0 )
			{
				Language.send( player, "&cYou are not allowed to rank up to " + newRank + ".");
				return false; 
			}

			boolean paid = pay( player, rankPrice );  

			if( paid )
			{

				if( Config.getOverride() )
				{

					for(String b : iRank.perms.getPlayerGroups( player ))
					{
						iRank.perms.playerRemoveGroup(player, b);
					}

				} else 
				{
					iRank.perms.playerRemoveGroup(player, Config.getCurrentRankableGroup( player ));
					System.out.println(Config.getAvailableGroups());
				}
        	
				iRank.perms.playerAddGroup(player, newRank);


				Language.send( player, "&3You have successfully ranked up to &a" + newRank + "." );
				Language.broadcast( "&b" + player.getDisplayName() + "&3 has ranked up to &b" + newRank + "." );

				return true;

			}
		}

		return false; 
	}
}
