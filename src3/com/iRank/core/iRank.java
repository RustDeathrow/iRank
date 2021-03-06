package com.iRank.core;

import java.io.IOException;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.iRank.bukkit.Commands;
import com.iRank.bukkit.ListenerSign;
import com.iRank.util.FileManager;



public class iRank extends JavaPlugin
{

	public static Permission perms = null;
	public static Economy econ = null;

	private boolean setupPermissions() 
	{
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

	private boolean setupEconomy() 
	{
        if (getServer().getPluginManager().getPlugin("Vault") == null)
        {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) 
        {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null; 
    }

	@Override
    public void onEnable() 
	{

		FileManager FileManager = new FileManager();

		PluginDescriptionFile pdfFile = this.getDescription();	

		getCommand("rankup").setExecutor(new Commands());

		setupPermissions();
		setupEconomy();

		FileManager.loadFiles(); 

		getServer().getPluginManager().registerEvents(new ListenerSign(), this);

		try 
		{
		    Metrics metrics = new Metrics(this);
		    metrics.start();
		} catch (IOException e)
		{
			getLogger().info( "Unable to send Metrics data." );
		}

		getLogger().info( pdfFile.getName() + " " + pdfFile.getVersion() + " is now enabled." );
	}

	@Override
	public void onDisable() 
	{
		PluginDescriptionFile pdfFile = this.getDescription();	

		getLogger().info( pdfFile.getName() + " " + pdfFile.getVersion() + " is now disabled." );
	}
}