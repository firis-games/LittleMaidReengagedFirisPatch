# LittleMaidReengaged
Again, on the new stage.

## What's this?
A MOD of Minecraft; LittleMaidMob for MC1.9./1.10. Cute and little maid girls will be helpful for your life on Minecraft.

## LICENSE
Read LICENSE.md

## Mod Installation
See [Releases](https://github.com/Verclene/LittleMaidReengaged/releases)

## Related links
* [LittleMaidMob(for MC 1.6.2 or more older)](http://forum.minecraftuser.jp/viewtopic.php?t=176) - by MMM
* [LittleMaidMobX(for MC 1.7.x)](http://forum.minecraftuser.jp/viewtopic.php?t=23347) - by EMB4
* [LittleMaidMobNX(for MC 1.8.x)](http://6docvc.net/) - by Verclene
  + \*Click "Minecraft"->"LittleMaidMobNX"

## Setting up the developing environment

1. Make your GradleForge workspace multi-project
2. Install git
3. Run this command on project root: `git clone --recursive https://github.com/Verclene/LittleMaidReengaged.git`
4. Add this line to root/settings.gradle: `include 'LittleMaidReengaged'`
5. Then run this command on root: `./gradlew setupDecompWorkspace --refresh-dependencies`

###　To run client on the dev environment

- Run this command: `./gradlew :LittleMaidReengaged:runClient` or `./gradlew :LittleMaidReengaged:runServer`
- Or launch GradleStart or GradleStartServer from your IDE.
- To generate changelog, add `-PgenCL` to the program args.

## Writing build.gradle for multiproject

```gradle:build.gradle
sourceSets.main {
	java.srcDirs project.projectDir.name
	resources.srcDirs project.projectDir.name
}
jar {
	doFirst {
		archivesBaseName = "[1.9.4-1.10.x]LittleMaidReengaged"
	}
	
	manifest {
		attributes 'FMLCorePlugin' : 'net.blacklab.lmr.util.coremod.LMRECoremod'
		attributes 'FMLCorePluginContainsFMLMod' : 'true'
	}
}
tasks.withType(Jar) {compileJava.options.encoding = 'UTF-8'}
tasks.withType(Jar) {compileApiJava.options.encoding = 'UTF-8'}

// Replace ':EBLib***' to the project of EBLib in your workspace 
dependencies { compile project(':EBLib194')}

version = "8.1.1.102"```
