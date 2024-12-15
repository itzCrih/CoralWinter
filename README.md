# CoralWinter
A simple CoralMC snowball fight lobby plugin replica. 
Credits to Ytnoos and [CoralMC](https://www.coralmc.it/) for the idea and for inspiring me (This is an external plugin, I am not part of the CoralMC team or affiliated in any way)
Made this for fun, coding is one of my hobby and I don't do it for commercial purposes.
Depends on [ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/) (only if you want to have extra features, otherwise the plugin will still work)

Links: [Polymart](https://polymart.org/resource/coralwinter.5194), [Contact me](https://t.me/itz_Crih)
## What will this do?
Assuming that you already have a snowy lobby, this plugin will let player break just the snow (it will get replaced some seconds later), which will give the player a snowball. Throwing the snowball at a player will hit them and apply knock back only if both players have the shovel or snowballs in their hands.
## Extra features
- Snowfall (basically snow particles) (only working if [ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/) is installed correctly).
- Shovel item, title and lore customizable in configuration.
- Break the snow blocks with a diamond shovel and get a snowball.
- Broken snow blocks will respawn after a few seconds. 
- PvP only enabled if you have a snowball in your hand.
- Santashovel command will give you a custom shovel with custom display name and lore.
- Break & place blocks protection included! (you can disable it on the config).
## Terms of services
You will only have to comply with these terms if you use the code or fork the repo.
If you simply use the compiled version (i.e. the release on GitHub/Polymart) you won't have to worry!
- You can only use this for private and personal use, not commercial use.
- You cannot claim that this code was created by you.
- You cannot remove the comments and the credits from the code.
- If you use it, you must credit me (itzCrih), and you must link this repository.
## FAQ
1. Why I don't see snow falling in the world? You have to install ProtocolLib in order to see that. Also, make sure you have optifine animations all enabled
2. How come I can break all the blocks with any tool? This plugin was invented to be placed in a lobby, so you need to make sure you have a core that limits block breaking or enable block protection from the configuration.
## Commands & Permissions
- /coralwinter - `coralwinter.command.help` - Main command;
- /coralwinter santashovel - `coralwinter.command.santashovel` - Get your own shovel;
- /coralwinter reload - `coralwinter.command.reload` - Reloads configurations;
- /coralwinter snow <player> - `coralwinter.command.snow` - Generates snow particles near the selected player.
- `coralwinter.bypass.blockprotection` - Bypass block place & break protection if enabled.
## Install
To install the plugin follow the steps below.
1. Download `CoralWinter.jar` from [repository's release](https://github.com/itzCrih/CoralWinter/releases);
2. Put `CoralWinter.jar` into your plugins' folder and change the configuration to your liking.
3. Download `ProtocolLib.jar` from [SpigotMC](https://www.spigotmc.org/resources/protocollib.1997/) (optional)
4. Put `ProtocolLib.jar` into your plugins' folder. (optional)
5. Start your server and have fun!
## TODO (Next updates?)
- Advent Calendar (GUI)

Suggest me something to add to this plugin! [Contact me](https://t.me/itz_Crih)
***