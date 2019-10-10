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

## READ BEFORE REPORT BUGS
***POST ONE ISSUE FOR EACH REPORT.***

I close issues with each fixing bug. And you can see solved or unsolved problems quickly.

## Setting up the developing environment

1. Make your GradleForge workspace multi-project
2. Install git
3. Run this command on project root: `git clone --recursive https://github.com/Verclene/LittleMaidReengaged.git`
  **WARNING:** if you run this on Windows, you have to **enable symbolic link** before cloning. See: https://github.com/git-for-windows/git/wiki/Symbolic-Links
4. Add these lines to root/settings.gradle:
```
includeFlat 'LittleMaidReengaged' 
include 'LittleMaidReengaged:EBLib'
```
5. Then run this command on root: `./gradlew setupDecompWorkspace --refresh-dependencies`

### To run client on the dev environment

- Run this command: `./gradlew :LittleMaidReengaged:runClient` or `./gradlew :LittleMaidReengaged:runServer`
- Or launch GradleStart or GradleStartServer from your IDE.
- To generate changelog, add `-PgenCL` to the program args.

## Writing build.gradle for multiproject

```
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

   task generateChangeLog << {
   	exec {
   		executable "bash"
   		args '-c', '"git log --date=short --pretty=format:\'%ad %an<%ae>%nHASH=%H%n%s%n%b%n\'' +
   				' | sed -E \'s/HASH=(.+)/\\* \\1/g\'' +
   				' | sed -E \'s/(.+)/\\t\\*\\t\\1/g\'' +
   				' | sed -E \'s/\\t\\*\\t([0-9]+-[0-9]+-[0-9]+.+)/\\1/g\' > ChangeLog"'
   	}
   }

   // Set arguments -PgenCL to generate ChangeLog(git configure required)
   if (project.hasProperty('genCL')) {
   	generateChangeLog.execute()
   }

   tasks.withType(Jar) {compileJava.options.encoding = 'UTF-8'}
   tasks.withType(Jar) {compileApiJava.options.encoding = 'UTF-8'}

   // Replace ':EBLib***' to the project of EBLib in your workspace
   dependencies { compile project(':' + project.name + ':EBLib') }

   version = "8.1.4.132"
   ```