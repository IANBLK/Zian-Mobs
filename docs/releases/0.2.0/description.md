# Zian Mobs

Animated allies, fantasy creatures and configurable battles for your world. Zian Mobs combines **29 flying companions**, permanent upgrades, combat NPCs, wave encounters and fantasy equipment in one standalone mod.

[Guía en español](https://github.com/IANBLK/Zian-Mobs/blob/main/docs/GUIA_ES.md) · [Changelog](https://github.com/IANBLK/Zian-Mobs/blob/main/CHANGELOG.md) · [Issues](https://github.com/IANBLK/Zian-Mobs/issues)

![Flying companion](https://raw.githubusercontent.com/IANBLK/Zian-Mobs/v0.2.0/docs/images/vuelo-ascua.png)

## Requirements and installation

- Minecraft **1.21.1**, Java **21**, NeoForge **21.1.259 or later for Minecraft 1.21.1**.
- [GeckoLib](https://www.curseforge.com/minecraft/mc-mods/geckolib) **4.9.3 or compatible 4.x**, installed separately.
- Matching Zian Mobs and GeckoLib versions on **both client and server**. Replace the previous JAR; do not keep duplicate versions.
- Standalone: Zian Manager, MythicMobs, ModelEngine, Accessories and Curios are not required. SQLite JDBC is bundled.

Back up your world before updating. Read [updating and storage](https://github.com/IANBLK/Zian-Mobs/blob/main/docs/UPDATING.md) before returning to an older version.

## Companions that fight beside you

Open `/companions` or `/ziancompanions`. Your unlocked companions appear first; **Ver todos** shows the complete collection. Summon, store and heal, bring your companion to your side, check its revival countdown or improve its bond.

- **One active companion per player.** Summoning another replaces the active companion.
- All 29 fly, follow their owner and seek free air over water. They remain above water when the owner dives.
- Follow range **16 blocks**; recovery when left behind; automatic storage with a chat notice beyond **32 blocks**.
- Ranged defense with **12-block target range** and a base **4-second attack cooldown**, subject to animation and valid-target checks. Players, companions and tameable entities are excluded as targets. Hostile mobs can retaliate and companions can take damage and die.
- **One or two eligible support buffs chosen at random** when support is needed. The pool varies by companion; decorated chat messages identify the effects.
- Manually storing a living companion heals it. Automatic storage does not heal. Death requires the revival wait to finish, including across server restarts.
- Owner death, logout, dimension changes and lost access withdraw the active companion. Upgrades and saved waits survive access revocation and restoration.
- Managed companions reject normal capture interactions. Supplementaries container compatibility has been tested; not every third-party capture implementation is guaranteed.

### Permanent upgrades

Most companions start with **36 health, 6 armor and 4 damage**. Kit starts with **45 health**, Ghost with **24**. These are default settings; administrators can configure them.

Each of six tracks caps at **level 10**. Normal materials give **+1 level**, Awakened materials **+2**. Each successful use consumes one item. A capped attempt consumes nothing. The companion must be unlocked, alive and stored.

- Health, damage and armor gain **10% of base per level**, up to twice the base. Typical maxima: **72 health, 12 armor, 8 damage**. Kit reaches 90 health; Ghost reaches 48.
- Support duration: **15 to 30 seconds**.
- Support cooldown: **90 to 45 seconds**.
- Revival wait: **600 to 300 seconds**.

The **14 Shadow Magic materials** offer six individual upgrades and a Diamond of the Bond for every unfinished track. They are available through the creative catalogue and administrator grants; no automatic crafting recipes or loot drops are added for them. [Companion guide, IDs and commands](https://github.com/IANBLK/Zian-Mobs/blob/main/docs/COMPANIONS_ES.md).

## Creatures, NPCs and wave encounters

Three Cursed One variants, Oni Brute, **Snowbone** and combat NPCs with **seven appearances** provide encounter building blocks. NPCs support melee, bows and configurable sonic, dragon, Wither and blaze skills without terrain destruction.

Operators open `/zianmobs` to configure creatures, teams, rewards and zones. Grouped forms, search, automatic identifiers, blue boundary lines and spawn-point previews, public wave/countdown displays and boss bars support in-game editing. Natural spawning and active zones are **not enabled automatically**.

Personal rewards use durable delivery records and pending claims. Players can use `/zianmobs pending`. Data remains isolated per world.

## Fantasy equipment

The original **46 fantasy equipment and decorative items** remain available, alongside four Titan armor pieces, the Titan Double Sword and Titan Gauntlet. Fantasy names and short decorated lore preserve existing anvil names and enchantments.

![Equipment catalogue](https://raw.githubusercontent.com/IANBLK/Zian-Mobs/v0.2.0/docs/images/equipment-catalog.png)

- Carnivoret: poison for 2 seconds, except scythes. Scythes deal **14 total damage**; critical hits heal **10% maximum health** and grant regeneration I for **5 seconds**, with a **30-second cooldown**.
- Crimson Ruby: bleeding for 2 seconds. Dark Queen: Wither for 2 seconds. Purple World: weakness for 2 seconds.
- Valentines: critical hits heal 10% maximum health and grant regeneration I for 5 seconds. Master Sword: heal 20% and regeneration I for 10 seconds. Both use a 30-second cooldown; weapon healing shares a persistent cooldown.
- Hylian Shield: a valid block grants resistance I and regeneration I for 10 seconds; cooldown 30 seconds.
- Right-click staffs cast sonic, Wither-skull or dragon attacks; cooldown 30 seconds, without terrain destruction. Enhanced bows have stronger shots and shorter draw time.
- Revival wings and the edible blessed Toffy Rupee retain their special abilities.

### Titan equipment

The armor uses native netherite-based properties and supports enchantments. The chestpiece adjusts to **wide and slim skins**. The double sword deals **10 total damage** at **1.6 attack speed**.

Use the sword in the main hand and gauntlet in the offhand. A valid melee critical hit against a mob or a melee mob kill grants **regeneration II, absorption IV, resistance I, fire resistance I and night vision I** for **30 seconds**, with a **60-second cooldown**. All four Titan armor pieces change duration and cooldown to **45 seconds each**.

Gems illuminate while the blessing is active; bloom depends on the shader. The offhand gauntlet can also save its wearer through native totem behavior: **one gauntlet is consumed**, subject to native damage bypasses and cancellation rules.

![Titan armor on wide and slim skins](https://raw.githubusercontent.com/IANBLK/Zian-Mobs/v0.2.0/docs/images/thanos-anchos-y-delgados.png)

## Commands and permissions

- `/companions`, `/ziancompanions`, `/zianmobs companions`: public collection menu.
- `/zianmobs`: operator interface, administration requires **permission level 2**.
- `/zianmobs pending`: pending personal rewards.
- `/zianmobs companion grant <player|UUID> <companion> [seconds]`: unlock. Omit duration or use `0` for permanent access; timed grants require at least 10 seconds.
- `/zianmobs companion revoke <player|UUID> <companion>`: remove access and withdraw an active companion.
- `/zianmobs companion status <player|UUID> <companion>`: access, levels and revival wait.

Example: `/zianmobs companion grant Steve cubee_fire 3600` unlocks Ascua for one hour. Player names require an online player; use a UUID for offline management. Unlocks are managed by Zian Mobs; ranks are not automatically synchronized. A command-whitelist plugin may separately require allowing the public menu commands. Keep administrative commands restricted.

## Support, build and credits

[Report a problem](https://github.com/IANBLK/Zian-Mobs/issues) with versions, reproduction steps and sanitized logs. See [Contributing](https://github.com/IANBLK/Zian-Mobs/blob/main/CONTRIBUTING.md) for build instructions.

Original code is **MIT**. Third-party models, textures, animations and skins retain their original licenses and are **not relicensed under MIT**. The maintainer confirmed authorization to publish the included artwork. See [LICENSE](https://github.com/IANBLK/Zian-Mobs/blob/main/LICENSE) and [credits](https://github.com/IANBLK/Zian-Mobs/blob/main/ARTWORK-NOTICE.txt). Inclusion does not authorize independent extraction or resale. Bundled SQLite JDBC retains its own notices.

Not affiliated with or endorsed by Mojang, Microsoft, Nintendo or Marvel. Compatibility with every hybrid server, shader or entity-capture mod is not guaranteed.
