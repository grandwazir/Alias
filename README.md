Alias - easily track a log your player's IP addresses.
====================================

Alias is a plugin for the Minecraft wrapper [Bukkit](http://bukkit.org/) that allows adminstrators and other trusted users to check the IP addresses of players on your server. Additionally it also links every username with an IP address which is useful if you suspect that one user is masqerading as another.

## Features

- Simple and easy to configure. 
- Lookup by IP address or player name.
- Developer API for use in other plugins.

## Requirements

- [Bukkit Persistence](https://github.com/grandwazir/Alias/wiki/database) needs to be configured in bukkit.yml
- If using MySQL for Persistence, you need a MySQL database

## Installation

### Ensure you are using the latest recommended build.

Before installing, you need to make sure you are running at least the latest [recommended build](http://repo.bukkit.org/service/local/artifact/maven/content?r=releases&g=org.bukkit&a=craftbukkit&v=RELEASE) for Bukkit. Support is only given for problems when using a recommended build. This does not mean that the plugin will not work on other versions of Bukkit, the likelihood is it will, but it is not supported.

### Getting Alias

The best way to install Alias is to use the [symbolic link](http://repository.james.richardson.name/symbolic/Alias.jar) to the latest version. This link always points to the latest version of Alias, so is safe to use in scripts or update plugins. Additionally you can to use the [RSS feed](http://dev.bukkit.org/server-mods/Alias/files.rss) provided by BukkitDev as this also includes a version changelog.
    
Alternatively [older versions](http://repository.james.richardson.name/releases/name/richardson/james/bukkit/alias/) are available as well, however they are not supported. If you are forced to use an older version for whatever reason, please let me know why by [opening a issue](https://github.com/grandwazir/Alias/issues/new) on GitHub.

## Configuration

1. [Configure permissions](https://github.com/grandwazir/Alias/wiki/permissions) if necessary.
2. Optionally configure your ban limits (config.yml) and assign them to moderators.

All documentation for Alias is available on the [GitHub wiki](https://github.com/grandwazir/Alias/wiki), including [example usage](https://github.com/grandwazir/Alias/wiki/Instructions) and details on [how to configure](https://github.com/grandwazir/Alias/wiki/Permissions) the plugin. 

