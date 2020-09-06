I'm not good at English.

The sentence me to translate Mr. btr50.
Please see here.

//----------------------------------------------------------


Little Maid Mob 1.1.0 Rev2

This mod for minecraft is based on the character designed by
Mr. >>92 at "Minecraft Texture/MOD general.0.0.3" thread in 2ch PCgames board,
and I adjusted Rana model data.
The Little maid mob "screams" very cute voice by default.
And this MOD contains maid textures drawn by >>473,>>484,>>509 in
"Minecraft Texture/MOD general.0.0.4" thread.

Earlier versions of this MOD was modification of kodachi & Mr.30 designed 
Rana based MOD, but now, it has became almost solitary product. 
I added their functions one and after another, so this MOD is very "buggy".
Make frequent backup is recommended.
In case you encounterd bugs, please report them to the 2ch threads or japanese minecraft forums.


Terms of use
 *Please thank to the people who made textures and resources.
 *You can use this mod in your videos (ex. Youtube, Veoh, youku and so on)
 *You can redestibute this MOD.
 *You can modify this MOD.
 *You can't use this MOD commercially.
 *Use this MOD at your sole risk. Programmer of this MOD is not liable for any
damages. 



How to use
 Necessary: ModLoader
 Strongly recommend: AudioMod

 Just place the littleMaidMob-1_1_0-2.zip file to "%appdata%/.mincraft/mods".
 Automatically, configuration file of this MOD is generated at "%appdata%/.mincraft/config" .
 You can change the property of this MOD to edit this .cfg file.

 A file named littleMaidMob-IFF.iff is also generated at "%appdata%/.mincraft/mods"
 This file defines identification of friend/foe about maids.
 Maids equipments data is also recored in this file.

 -Configuration of voices
 AudioMod allows you to set your favorite sound files for maids voices. 
 Directory named "%appdata%/.mincraft/resources/mod/sound/littleMaidMob" and 
 "littleMaidMob.cfg" are automatically generated.
 Open the .cfg file by editors and edit them.
 example;
 se_hurt=littleMaidMob.littlemaidh
 In this case, "littlemaidh1.ogg -littlemaidh9.ogg" files are randomly selected 
 when maid mob hurt. Detailed is commented below "#" in .cfg files.

 In recent update, sound pack system is mounted and voice configuration get easier.
 Please refer the readme-sound.txt

 -Configuration of textures
 To add maid textures, add certain texture file to "/mob/littleMaid/"in the .zip file. 

 The texture files must follow the naming rules below.
 The file name is ended as"_xx.png" in hexadecimal number (00-FF).
 _0.png - _f.png/_00.png - _0f.png:each colored maid
 _10.png :Wild maid（equal to _3c.png）
 _11.png-_12.png :Armor（equal to default_40.png and default_50.png）
 _20.png :Maid inventory
 _30.png-_3f.png :Wild and colored maid
 _40.png-_49.png :Armor（inside）
 _50.png-_59.png :Armor（outside）

 *Please refer the readme-texture for further information of armor textures. 


New features

 *For users
 - Anti-"buried" fix.
 - Fix for the contracted maids attack the wild maids while snowballing.
 - The Torcher maid is enable to place the torches in snowfield.
 - Fixed some textures not completely transparent.

 *For developers
 - Fixed flat parts (addPlate) behavior.
 - Added maid models internal unique IDs. 


Elements of little maid mob

 -Contraction
 You can tame wild maids like wolves.
 Hold cake and right click, then they became contracted.
 The contracted maid is dressed herself with white brim (hair accessory).
 -Maids are tidy mobs !
 The maid mobs collect dropped items.
 -Maids are made of sugar, cocoa and something amorphous.
 Drop items has been changed.
 -Maids skirt are full of dreams.
 Maid inventory (named "inside skirt") is open when you right click 
 the contracted maid without special items (sugar, feather or something).
 -Give me Sugar !
 When you hold the sugar, maids come across to you and ask you to give it.
 Each time you give her sugar, her waiting mode and activated mode is switched.
 When some certain items are placed at the left upper slot of maid inventory,
 the maid changed into special activated mode (see below). 
 In addition, 0.5 heart recovers per 1 sugar.

 --Wings for freedom
 When you give the feather maids by right click, maids turns into freedom mode,
 and walk around about +/-10 block square, and if she has got away from the start
 point 17 blocks, she teleports to the start point.
 --Handmade dish by maids
 When you place the combustibles (coal, charcoal, woods or something) 
 in the upper left slot of maid inventory, and activate her, 
 maids changes to the "Cooking" mode.
 When she find furnace, she start to smelting items inside her skirt.
 --Axes are weapon for maids.
 When you set the weapon entities (swords and axes) or healing items in the
 upper left slot of maid inventory, and activate her, they turn into 
 certain fighting mode ("Fencer", "Bloodsucker" and "Healer").
 --Now, it's time to rip.
 When you set the shears in the upper left slot of maid inventory, 
 and activate her, she changes into the "Ripper" and rip the wool of sheep.
 --Ka-mabo-ko-*1
 When you place the torches the upper left slot of maid inventory, and activate her, 
 she get into the torcher mode.
 --This mansion is mine.
 When you give the "freedom" maid redstone powder, she get into the "Tracer" mode.
 In this mode, her start point is set at the redstone power she detected. 


 -Contraction needs consideration
 Contraction period is 7 days in the game, and 1 sugar let you extend another 7 days.
 If you place the sugars in the maid inventory, the contraction periods are automatically extended.
 When you forgot her salary, maid gets into strike, until you give her another cake once more. 
 Maid in strike remains and never despawn.

 -Ancient creature*1
 Mr.>>473's intense desire makes me mount twin tail (bunches) type model.

 -Thank you dotters !
 Dotter's textures used in maids color variations.

 -Clock announcing maid, powered
 Placing the clock in the maid inventory allows her announce the time inside the game.
 It is necessary to add certain voices and configure the .cfg files to work this function.
 Some new parameters are added for voice. Please check your configuration.

 -Mask de maid
 When helmet placed in the maid inventory, head parts of armor texture is reflected. 
 It has no armor points. 
 So, let's put more armor textures !

 -Maid is ruler clan.
 Configuration of .cfg file allows you to make it possible to spawn maid mobs in any condition. 
 This mode is off in default mode.

 -Too much baggage 
 "Freedom" maids put their baggage into the chest, when their inventory filled full.

 -That..that's JAM!
 Hold book and right click maid, then maid-IFF configurationGUI is open.
 Used book consumed.
 In this GUI, each time you click the subject, maids reaction properties will change
 ENEMY ：Enemy. Attack them and counter attacks
 UNKNOWN ：Unknown mobs. Counter attack when maids are attacked.
 FRIENDLY：Friend. Never attack/counter attack.

 -Snow makes me child.
 When "freedom" maids released to the snowfield, they starts snowballing.
 Their playtime will end when sun sets.

 -Return of colored-maid
 You can dye maids.
 The maids textures are set in each colors. I've got many textures from dotters in 2ch.
 Wild maid also can be dyed, however, no default textures are set, so you have to make
 it by yourself, indeed.

 -Do you like weathering?
 Damaged armor textures are enabled. And each materials reflect each textures,
 Of course no default textures exist, and you need to picture them by yourself.

 -Maids want makeover herself !
 Many crafters enable it easier to change maids texture, voices and so on.
 To change the textures added, click right or left click the maid in the 
 maid inventory.
 Add any dresses you want !
 And when use your custom made models allow infinite 
 Details are written in other readme files.
 Background of maid inventory can change.

 -The Predator
 Half-transparent display is enabled using alpha channel of textures.
 Alphablend set "On" by default.
 In case you cut off this process, open .cfg file and change "AlphaBlend=false"

 -My armaments accumulated to 108.
 Choose "trigger items" in MaidIFF, then you can add the trigger items to 
 each battle modes.


*1: Torture Action voice in action game BAYONETTA. Torture resembles torcher, isn't it ?
*2: http://ja.wikipedia.org/wiki/%E3%83%84% ... %E9%81%BF&#41;