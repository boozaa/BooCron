package org.shortrip.boozaa.plugins.boocron;

import org.shortrip.boozaa.plugins.boocron.persistence.Cache;
import org.shortrip.boozaa.plugins.boocron.persistence.Database;
import org.shortrip.boozaa.plugins.boocron.persistence.Configuration;
import org.shortrip.boozaa.plugins.boocron.utils.Log;
import java.io.File;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/*
* Copyright (C) 2012  boozaa
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
public class BooCron extends JavaPlugin {

    private static CronScheduler scheduler;
    public static CronScheduler getScheduler(){
    	return scheduler;
    }
	
	private static Cache cache;
	public static Cache getCache(){return cache;}

    private static Database database;
	public static Database getDB(){return database;}

    private static Configuration configuration;
	public static Configuration getConfiguration(){return configuration;}

	private static Boolean debug = false;
	public static Boolean debug(){ return debug;}
	
	
	
	private ServicesManager sm;
	

    public void onEnable() {

    	init();
    	
    	// Instanciate a Cron Scheduler
    	scheduler = new CronScheduler();
    	// Add our listener to it
    	scheduler.addSchedulerListener(new CronSchedulerListener());
    	
    	
    	// Host system TimeZone
    	TimeZone tz = Calendar.getInstance().getTimeZone();
    	
    	// If different TimeZone is define on config, set it up
    	if( configuration.get("config.timezone") != null ){
    		if( !configuration.getString("config.timezone").isEmpty() ){
    			tz = TimeZone.getTimeZone(configuration.getString("config.timezone"));    			
    		}
    	}
    	
    	// Set its TimeZone to this TimeZone
    	scheduler.setTimeZone(tz);
    	// Start the scheduler
    	scheduler.start();
    	Log.debug("Cron Scheduler started with TimeZone set to " + tz.getDisplayName());
    	
    	// Register CronService as a Service   	
    	sm = getServer().getServicesManager();
    	sm.register(CronService.class, new CronService(), this, ServicePriority.Normal);
    	Log.debug("CronService added to ServicesManager");

    }


    public void onDisable() {
        
        // Set all static variable to null
        cache = null;
        database = null;
        configuration = null;		

    }

    
    private void init() {
    	/*
		* Yaml Configuration
		* This line load the config.yml from the plugin folder
		*/
		loadConfig();
		Log.info("Config loaded");    	    	

        /*
		* Cache Map
		* To cache an object : BooCron.getCache().add(String id, Object data)
		* To retrieve this object use getter method ex: BooCron.getCache().getBoolean(id)
		*/
		cache = new Cache();
		Log.info("Cache system created");

        /*
		* Database connection
		*/
		if( configuration.getBoolean("database.mysql.enabled")){
			// Database(String host, String database, String username, String password)
			database = new Database( configuration.getString("database.mysql.server"), configuration.getString("database.mysql.database"), configuration.getString("database.mysql.username"), configuration.getString("database.mysql.password") );
			database.initialise();
			Log.info("Connected to MySQL database");
		}else{
			database = new Database( new File( this.getDataFolder() + File.separator + "BooCron.db" ));
			database.initialise();
			Log.info("Connected to SQLite database");
		}
		
		/*
		 * Debug
		 */
		if( configuration.get("config.debug") != null ){
			debug = configuration.getBoolean("config.debug");
		}		
		
    }
    
    

    private void loadConfig(){
        
        String configPath = getDataFolder() + File.separator + "config.yml";
        configuration = new Configuration(configPath);
		
    	List<String> messages = new ArrayList<String>();
    	Boolean updated = false;
    	    	
    	if( !configuration.exists() ){ configuration.save();}    		   				
    		
    	configuration.load();
	
        // Create a version node in the config.yml
        if( configuration.get("config.version") == null ) {    			
            // Doesn't exist so create it and store as new
        	configuration.set("config.version", (String)getDescription().getVersion());
            updated = true;
            messages.add("config.version - the version of the config");
        }else{
            // Exists so check with current version
            String version = configuration.getString("config.version");
            if( !getDescription().getVersion().equalsIgnoreCase(version) ){
            	configuration.set("config.version", (String)getDescription().getVersion());   
                updated = true;
                messages.add("config.version - updated");
            }					
        }

        // TimeZone
        if( configuration.get("config.timezone") == null  ) {
        	configuration.set("config.timezone", 	(String)"Europe/Paris");
            updated = true;
            messages.add("config.timezone - To fix a custom TimeZone");
        }

        // Database MySQL
        if( configuration.get("database.mysql.enabled") == null  ) {
        	configuration.set("database.mysql.enabled", 	(Boolean)false);
        	configuration.set("database.mysql.server", 	(String)"localhost");
        	configuration.set("database.mysql.database", 	(String)"minecraft");
        	configuration.set("database.mysql.username", 	(String)"admin");
        	configuration.set("database.mysql.password", 	(String)"123456");
            updated = true;
            messages.add("database.mysql - Enable or not storing on MySQL database");
        }

        // Debug
        if( configuration.get("config.debug") == null ) {			
        	configuration.set("config.debug", 	(Boolean)false);
        	updated = true;
            messages.add("config.debug - Enable or not debug console logging");
		}	
        
        
        // Log des modifs
        if( updated ) {	
        	configuration.save();
        	configuration.load();
        	Log.info("- Config - " + getName() + " " + getDescription().getVersion() + " config.yml - new options");
            for(String str : messages){
            	Log.info("- Config - " + str);
            }
        }

    }


}
