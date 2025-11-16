# 6.1.6
## Changes
### Crying Ore
- Changed Hardness of Crying Ore from random to 61.8

### Void-Stained Crying Block
- Changed Rarity from Epic to Common
- Now stackable (64), now consistent with the README

### Other
- Hardened Core, Void-Stained Crying Block, Crying Ore, Crying Block and Hard Crying Obsidian are now fireproof and won't burn in lava

# 6.1.5
## Changes
- Buffed Granter, it will wait less to descend upon its player

# 6.1.4
## Changes
### Sanity
- Collapsing is now guaranteed to happen

### Technical
- `crying` command will now give feedback to the player

# 6.1.3
## Changes
### Crying Shield
- Crying Shield's protection when sneaking is changed to 90%
	- You will also take knockback when sneaking
- Crying Shield is now not repairable
- Changed rarity of Crying Shield to Epic
- Crying Shield will give the player 20% knockback resistance
- Crying Shield's recipe is changed

### Technical
- `/crying sanityManager` is renamed to `/crying sanity`
- Bumped Fabric Loom version to 1.11 and Gradle version to 8.14

# 6.1.2
## Additions
- Sanity can now collapse after staying too long in a biome with a temperature of 0.3 or lower

## Changes
- Sanity is no longer updated if the player is not wearing any Crying Armor
- Collapsing reasons are now displayed in their unique colors on the HUD
- Collapsing reasons are no longer displayed on the HUD when Sanity is inactive

# 6.1.1
## Additions
- Added `deactivate` and `activate` arguments to `/crying sanityManager` for activating and deactivating Sanity mechanic

## Changes
- Changed Crying Grappling Hook's recipe
- The purple rope of Crying Grappling Hook is replaced with the default brown leash

## Other
- 1.21.9 support

# 6.1.0
## Changes
- Replaced the internal version checker with  [Semantic Version Library](https://github.com/ThomasDarkson/SemanticVerLib)

## Additions
- Added 4 new achievements
- You can now use Crying Carrots to heal a Lost Crier

## Fixes
- Fixed Crier not being able to die from `/kill` command

# 6.0.1
## Fixes
- Fixed Crying Foods decreasing sanity instead of increasing sanity
- Cover Me in Depression advancement's title and description are now fixed

# 6.0.0 - The End 
## Additions
### Crying Dimension
- Added a new dimension, Crying dimension, with 2 new biomes in it
	- Somewhat Weird Island
	- Somewhat Normal Island

The Crying Dimension is an odd place consisting of floating islands with an exclusive entity, Lost Crier, and it's the only place where Crying Foods can be found. It can be travelled to using a Eye Connected To A Stick.

### Crying Mice
Crying Mice is a remix of Living Mice by C418 composed by me (Thomas Darkson) that plays in Somewhat Weird Island. It's the only track that'll play in that biome. This track was originally an extra track in older versions but it was removed. It's now remastered and used as the track for 6.0.0 update.

### Items
- Eye Connected To A Stick

This item is used to travel to the Crying Dimension, or to return to the Overworld when already there. When used, it will search a 618x618 area for a solid block where the player can safely stand without suffocating. It has 1236 durability.

- Crying Grappling Hook

This is a grappling hook that only works in the Crying Dimension. Use the item to shoot out a hook that will pull you when it attaches to a block.

- Hardened Core Piece

An item that drops from Lost Crier's, used to craft Hardened Core.

- Lost Crier Spawn Egg

The spawn egg for the Lost Crier entity.

### Tools
- Added Copper to Core Ingredients

Now you can use Copper to craft Crying Tools. 
Just like copper blocks, tools crafted with Copper Core Ingredient will oxidize over time.

Copper’s tool perk is summoning lightning on the mob it hits. The lightning summoned by the tool will not hurt its owner and it will not cause any fire. The chance of this lightning being summoned is based on the tool's oxidation level.

#### Oxidation and Waxing

Crying Tools with Copper Core Ingredient will always age, also known as oxidizing.
Using a honeycomb and a copper ingot on a Crying Tool with Copper Core Ingredient will turn the tool into its waxed variant and deoxidize it, waxed tools will age 2 times slower.

Waxing is the only way to deoxidize a tool.

| Oxidation Level | Time (Unwaxed)    | Time (Waxed)       | Summoning Lightning Chance |
| --------------- | ----------------- | ------------------ | ------------------ |
| **Unaffected**  | 0 – 1,200 sec     | 0 – 2,400 sec      | 100% |
| **Exposed**     | 1,200 – 2,400 sec | 2,400 – 4,800 sec  | 75% |
| **Weathered**   | 2,400 – 4,800 sec | 4,800 – 9,600 sec  | 50% |
| **Oxidized**    | 4,800 – 9,600 sec | 9,600 – 19,200 sec | 25% |

### Blocks
- Lost Crying Block

Lost Crying Block is a block that spawns only in Somewhat Normal Island biome in Crying Dimension. It's used to spawn Lost Crier. If the block is interacted with gold ingot in hand, the gold ingot is consumed and the block breaks. It has a 10% chance of spawning a Lost Crier.

- Void-Stained Crying Block

The bedrock counterpart of Crying Dimension. It acts as the floor of the Crying Dimension and it'll always spawn at y=0. You will not take fall damage if you fall on it.

- Hardened Core

The unhardened version of the Over-hardened Core. It's used to craft the Over-hardened Core.

### Food

- Enchanted Crying Apple

A better version of the Crying Apple. Very rare chance of spawning in Somewhat Weird Island biome.

- Crying Carrot

The carrot version of the Crying Apple. Very high chance of spawning in Somewhat Weird Island biome.

### Entities

- Lost Crier

The Lost Crier is a passive entity that carries a single Hardened Core Piece. It has 206 health and 50% knockback resistance. When killed, it has a 10% chance to drop a Hardened Core Piece.

A Lost Crier will turn into a Crier if it's in the Overworld.

### Technical
- `/crying` command

`/crying` command is used to manipulate the Sanity system of a player. You can clear your sanity, set your sanity level or collapse your sanity as you wish.

- Version Checker

A version checker is added to the mod, so when there is new update the user will be notified on the title screen of Minecraft.

## Changes
### Sanity
- Collapsing Reason

Prior to 6.0.0, if your sanity collapsed you couldn't see why. With 6.0.0, now you can see why your sanity collapsed on the HUD.

#### Food

With the new Crying foods being added, their purpose is changed as well. Now instead of permanents level that cannot be drained, every player has a counter that tracks how many Crying Foods they have eaten during their journey. As players consume more Crying Foods, their sanity improves and grants extra protection. This protection is capped at 80%. The formula for the extra protection is `(eaten_crying_food_count / 9888) * 0.8`%.

This extra protection is only active when the Sanity mechanic is activated.

### Crying Ingot

Crying Ingot now has 6 variants:
- Crying Ingot (infused with: Netherite)
- Crying Ingot (infused with: Diamond)
- Crying Ingot (infused with: Iron)
- Crying Ingot (infused with: Copper)
- Crying Ingot (infused with: Gold)
- Uninfused Crying Ingot

While most recipes use the Uninfused Crying Ingot, crafting a Crying Tool requires the corresponding infused Crying Ingot. For example, to craft a Crying Sword with Copper Core Ingredients, it's now required to use Crying Ingotinfused with Copper. To infuse a Crying Ingot, simply put a Crying Ingot and the ingredient you're going to infuse the Crying Ingot with in a crafting table.

Uninfused Crying Ingots recipe is changed to 8 Crying Residue's.

### Crier
- Crier now does one less damage
- Crier will die if it's not in the Overworld

### Over-hardened Core

- Now crafted with Hardened Core instead of Heavy Core.

## Removals
### Sanity
- Permanent Levels

Permanent levels are replaced with eaten Crying Food system.

# 5.1.1
## Changes
- Reduced the Crier's varying extra health based on player count by 50%
- Replaced the Crying Ingot in the Crying Shield's recipe with Iron Ingot

## Fixes
- Fixed a bug where players would get knockbacked when hit while holding a Crying Shield

# 5.1.0 - How did we get here? Part 2
## Additions
### Tools
- Crying Shield

The Crying Shield is no ordinary shield. When equipped, it blocks attacks automatically. However, its effectiveness depends on whether the player is sneaking:

If player is sneaking:
- 100% damage protection
- Reflects half the incoming damage back to the attacker

If player is NOT sneaking:
- 25% damage protection
- Does not reflect damage

## Changes
### Crier
- Crier now will always block attacks with its shield
	- However, players can break its shield by attacking it
- Removed Unbreaking III enchantment from Crier's shield
- The number of Eyes dropped by a Crier upon death now depends on how many players attacked it
- Changed Crier spawning pattern to 4 Crying Obsidian, 1 Hard Crying Obsidian and 2 chains
- Crier will now heal more in second phase but less in first
- Fixed Crier moving 2 times as fast when there are no players around

## Other
- Optimized Sanity mechanic
- Fixed achievement names and description

# 5.0.0 - How did we get here?
## Additions
### Tools
- Added 6 tool variants, also known as *Core Ingredient* of the tool; 
	- Over-hardened Core With Eye (only for Crying Pickaxe and Sword)
	- Crying
	- Netherite
	- Diamond
	- Iron
	- Gold

Every Crying Tool type (Pickaxe, Axe, Sword, Shovel, Hoe) can be crafted the Ingredients listed above, resulting in 25+2 unique tools. 

A Crying Tool's damage, durability, mining speed, enchantability, and attack damage bonus depend on its Core Ingredient. While Gold is the lowest tier, it has two perks—unlike every other tier, which has only one—and it's the only way to spawn a Granter Entity.

The Core Ingredient of a Crying Tool is specified by the addition material in its recipe. The recipe for a Crying Tool consists of the addition material (Core Ingredient), a Netherite Tool, and a Crying Ingot.

### Armors

- Added Crying Horse Armor

You can make your horse even stronger with this, and it's craftable! You need a Diamond Horse Armor, an Iron Ingot and a Crying Ingot to craft it.

- Added Crying Chestplate with Elytra

This item combines Crying Chestplate and Elytra into a single item. You need a Crying Chestplate, an Elytra and a Phantom Membrane to craft it.

### Entities
- Added a Granter entity, which spawns above the player's head when they take damage from a hostile mob if they are holding any Crying Tool with **Gold** Core Ingredient

### Items
- Added Crier's Heart, a trophy item dropped by killing a Crier
- Added Granter, which is unobtainable without cheats, it's only purpose is to spawn a Granter Entity

### Sanity
- Added Collapsing mechanic to Sanity, your sanity will collapse if you do certain things
	- When collapsed, you can't regenerate Sanity and you will not get the extra protection Sanity grants you until it recovers
		- Drinking water will speed up the recovering process

## Changes

### Crier
- Crier now flaps its arms to fly 
- Crier will now give players 5 seconds of Weakness II instead of 4
- Changed Crier's step height to 1 since it caused Crier to move oddly

### Sanity
- Any food (except snacks and another few) will now regenerate (or drain if it is bad for you) `nutrition / 2` sanity
- Water now will regenerate 20 sanity, also speeds up the recovering process
- Keeping HP under 50% (changed from 40%) will drain 1 sanity every 10 seconds

### Crying Ore
- Crying Ore's hardness is now random, between 10 and 100
- Will now generate less in the Overworld

## Removals
- Removed Smoothness, Aegis, Bloodlust and Feathered
	- They did not fit the mod
	- They are replaced with perks and Crying Chestplate with Elytra
	
- Removed Crying Rod since with the new tool variants, it was unused
- Removed Love of The Feline effect, check [Cats on Head](https://www.curseforge.com/minecraft/mc-mods/cats-on-head) instead

## Other
- Bug fixes
- Source refactor and cleanup

# 4.1.4
- Crier can now crouch to fit in one block gaps when the player it's attacking to is crawling
- Forlorn Crier attack damage is reduced by 1

# 4.1.3
## Changes
- Crier will now have more HP if there are more players in the world
- Crier will drop Eyes equal to the number of players in the world when it dies

## Removals
- Removed Crying Cat and Furball block, for a mod with very similar features check [Cats on Head](https://www.curseforge.com/minecraft/mc-mods/cats-on-head)

# 4.1.2
- Fixed the error `Missing element ResourceKey[minecraft:enchantment / crying:feathered]`

# 4.1.1
## Changes
- ### Crying Ore
	- Slightly reworked Crying Ore generation on Overworld's surface.
		- It has a now 20% chance to not generate if exposed to air (water is not considered air)
		- Minimum generation height is now 60
        - It's now very biased to bottom between 60 and 72
		- Will generate 33% more in general

# 4.1.0 - Major Changes
## Crier
- Crier Summoner is replaced with Crier Spawn Egg and it's not craftable
- Criers are now summoned with placing two crying obsidians vertically and a hard crying obsidian on top 
- Crier's model is reworked and it'll change appearance when it becomes forlorn
- Crier's initial explosion now won't do any damage
- Crier will now fly faster
- Crier's now won't die unless the fatal hit was from a player
- Crier is now immune to fire and lava

### Forlorn Stage
- A Crier will become forlorn when it's below 20% HP instead of 10%
- Its scale will now stay the same
- Slightly nerfed the last explosion before becoming forlorn

## Changes
### Crying Ore
- Now also generates on the surface between Y level 56 and 72 in the Overworld (biased to bottom)

### Recipes
- Crying Tools and armors now all use Hard Crying Obsidian instead of Crying Upgrade Smithing Template
- Crying Apple is now crafted with one apple and one crying obsidian
- Replaced the iron blocks in Hard Crying Obsidian's recipe with iron ingots
- Replaced the netherite scrap in Crying Ingot's recipe with iron ingots
- Over Hardened Core now requires 3 Reinforced Deepslates instead of 7
- Added a recipe to duplicate Hard Crying Obsidian

### Sanity
- Cooked beef, cooked porkchop and cooked mutton now has a 60% chance instead of 30% to increase sanity
- Rotten flesh, poisonous potato, raw chicken and spider eye now decreases sanity by 6 instead of 4

### Crying Apple
- Crying Apple now has a cooldown of 60 seconds instead 120

### Models
- Reworked Eye's model
- Tweaked The Crying Being's model

## Removals
- Knives and Handle item
Knives didn't really fit the mod and had no purpose, so they are removed
- Crying Upgrade Smithing Template
Is replaced with Hard Crying Obsidian

## Fixes
- Fixed wrong armor durability
- Fixed a bug with Feathered

This update drops 1.21.4 support, I will ignore any issues with 1.21.4 versions of Crying Tools.

# 4.0.5
- Fixed a bug that could break other mods

# 4.0.4
- Reinforced Deepslate is now guaranteed to drop when it's mined with Crying Pickaxe or The Crying Being
- Upated Crying Cat model
- Buffed Crying Pickaxe

# 4.0.3
- Removed Crying Mice

# 4.0.2 - The Happy Being
- The Crying Being now can be enchanted with only sword enchantments
- The Crying Being now can break any block except bedrock, including modded blocks
- The Crying Being now acts like it has Fortune III if player holding it isn't crouching
- The Crying Being now acts like it has Silk Touch if player holding it is crouching
- The Crying Being now can till blocks like a hoe
- Crying Cat will always at least give one gift to her owner

# 4.0.1 - YACHT (Yet Another Crier Heavy Trade-off)
- Reworked Crier's healing mechanic, removed its summoning ability, it won't make itself invisible anymore and more balances
- Crying Ore generates more now
- Fixed Over-hardened Core, Over-hardened Core With Eye and Furball being stackable and their rarities

# 4.0.0 - Crying Cat says meow
- New Mob: Crying Cat, can be put on head
- New Block: Crying Furball, used to spawn Crying Cat
- 3 New Enchantments: Feathered, Bloodlust and Aegis
- New Status Effect: Love of The Feline, activates when Crying Cat is on head
- Remade Crying Mice
- Reworked Crying Apple
- Crier has now less health and less explosion damage
- Changed Crier Summoner recipe
- Buffed Crying Axe and Crying Pickaxe chances 
- Changed The Crying Being recipe
- The Crying Being is now enchantable
- The Crying Being now can't till blocks
- Changed Over-hardened Core recipe
- Eye is now not stackable

# 3.0.3 - Crier changes
- Crier can now **fly** to escape pits and holes
- Crier now initials an explosion that will break **any** block except bedrock
- Crier is now immune to *Mace*
- Crier Spawn Egg is replaced with **Crier Summoner**

# 3.0.2 - Bugfix 2
- Added a second phase to Crier that activates when it has below 10% its HP
- Removed 2048 `ATTACK_DAMAGE` attribute limit so *The Crying Being* can deal maximum damage
- Fixed knives being held incorretly in third person
- Bumped the Gradle and Fabric API version

# 3.0.1 - Bugfix
- Added a resource pack to add Crying Mice to songs list, therefore making the song completely optional
- Fixed items being held incorretly in third person

# 3.0.0 - The Awakening Update
- New Boss Mob: **Crier**, drops 1 *Eye*
- New Track: Crying Mice
- Built in a new resource pack for the track
- 2 New Items: **Eye** and **Crier Spawn Egg**
- 1 New Block: **Over-hardened Core with Eye**, *The Crying Being* crafting recipe now needs one
- Replaced Crying Level system with **Sanity** system, the things you do affect your **Sanity**
- Reworked **Crying Apple**
- Retextured some textures
- *The Crying Being* now protects an entity from projectile, fire, lightning and fall damage
- *The Crying Being* has now 2147483647 durability and 666 attack speed
- *The Crying Being* will not damage any players no matter which entity is using it
- *The Crying Being* now can be used as a hoe to till supported blocks
- *The Crying Being* now cannot be enchanted with the **Smoothness** enchantment
- Removed the Crier **effect**
- Increased the levels drinking water gives
- Fixed permanent levels transfering over to other worlds
- Decreased the chances of Crying Axe and Crying Pickaxe loot

# 2.2.2
- Crying Knife now damages Enderman, Endermite and Ghast more like Crying Sword
- Changed how frequently items blink

# 2.2.1 - Bugfix
- Crying Knife and The Crying Being is now fireproof

# 2.2.0 - The Crying Update
- New *universal* tool: **The Crying Being**
- New Block: **Over-hardened Core**
- New Item: **Crying Rod**
- New Enchantment: **Smoothness**
- **Crying Pickaxe** can now break **Reinforced Deepslate** with 1 in 28 chance
- Crying Residue and Crying Obsidian can now be found in Ruined Portal chests
- Crying Pickaxe, Crying Axe and Crying Hoe can now be found in Ominous Vaults in Trial Chambers
- Bane of Criers book is removed from Ominous Vaults
- Crying Levels now regenerate faster
- Crying Levels will now regenerate in Creative Mode
- **Crying Block** is renamed to **Block of Crying**

# 2.1.1
- Updated Crying Ingot, Crying Upgrade Smithing Template and Hard Crying Obsidian recipes
- Updated Hard Crying Obsidian Texture
- Increased hardness and blast resistance of Hard Crying Obsidian
- Changed the amount of experience "It Was Me, I Am The One Who's Cutting Onions" and "A Hoe That Makes You Cry" advancements give 
- Bumped up the required Fabric API and Yarn mappings

# 2.1.0
- New items: Crying Knife, Knife and Handle
- Crying tools now get damaged based on durability
- Rebalanced Crying Axe and Pickaxe
- Removed tears that drop from hand when holding a Crying tool
- Crying Levels now generate 5 seconds faster
- Crying Apple eating time is reduced
- Fixed enchantability values of armors to match with the README

# 2.0.2
- Fixed permanent levels resetting after death

# 2.0.1
- Reworked Crying Level system
- Added Crying Apple
- Drinking water now regenerates 8 Crying Levels
- Made Crying Ingot recipes shapeless
- All tools now have the same mining speeds and enchantability

# 2.0.0
- Added Crying bar mechanic
- Made tools animated
- Balanced tools and armor
- Reduced the amount of XP advancements give
- Remastered armor textures
- Fixed Ominous Vaults only dropping Bane of Criers

# 1.0.4
- Added a new recipe to craft Crying Ingot
- Buffed the loot chances in Bridge and Treasure chests

# 1.0.3
- Added Bane of Criers
- Added a recipe to duplicate Crying Upgrade

# 1.0.2
- Made Crying Ore drop Crying Residue instead of itself
- Made the items cry
- Made Crying Sword damage Enderman, Endermite and Ghast more
- Fixed the rarities of everything
- Removed useless code

# 1.0.1
- Added armors
- Added Crying Ore 
- Added Hard Crying Obsidian
- Added Crying Residue
- Made existing blocks cry
- Remade the Crying Ingot texture
- Revamped source
- Updated README
- Renamed the mod