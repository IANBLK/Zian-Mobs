# Zian Mobs 0.3.2

**Language notice:** The current in-game interface, preset dialogues and administrator messages are primarily Spanish. English localization is incomplete. The English guide explains the controls and includes their Spanish button names. Changing Minecraft to English does not translate administrator-written NPC names, dialogues or configuration text.

[English guide](docs/GUIDE.md) · [Guía en español](docs/GUIA_ES.md) · [Updating and recovery](docs/UPDATING.md) · [Current changes](CHANGELOG.md) · [Download](https://github.com/IANBLK/Zian-Mobs/releases/latest)

## Dungeon configuration moved to Zian Mobs / Configuración de dungeons trasladada

Dungeon zones, mobs, bosses, waves, encounters and mob/boss loot tables have moved from **Zian Manager to Zian Mobs**. Configure combat in Zian Mobs. Manager retains chest loot, vaults, NPCs/trades, entrances and world time limits. For existing Manager definitions, use the guarded import preview in Mobs after backing up the world; pending delivery receipts are not moved.

La configuración de zonas de dungeon, mobs, jefes, oleadas, encuentros y tablas de loot de mobs/jefes pasó de **Zian Manager a Zian Mobs**. Las definiciones antiguas se conservan para importarlas con la vista previa protegida de Mobs; respalda el mundo antes y no elimines los datos antiguos. Los recibos de entregas pendientes no se trasladan.

Create animated creatures, bosses and dungeon encounters through native in-game menus. Includes fantasy equipment and a personal collection of 29 companions. No encounters or natural spawning are enabled automatically.

## Custom enemies and combat rewards
Configure hostile creatures and combat NPCs, attributes, equipment, abilities, boss bars and personal mob/boss reward tables. Entity selectors focus on dungeon-suitable mobs and included custom models. Rewards use persistent delivery records, a personal pending-rewards view and administrative review of uncertain operations.

## Simple zones, optional bosses
Choose a center floor block, set the region and select the mob group once. Repeat it for 1–16 waves, with automatic group scaling: 1–3 mobs initially, two more per additional player and a maximum of eight. Configure time between waves and zone reuse separately. Add a final boss when desired. The advanced encounter editor can order, insert and remove wave/boss stages, including boss-first encounters.

Use safe area spawning or saved points inside the chosen region. The center marker and wireframe reflect that region. Safe slabs and one-block suspended platforms work without a deep floor. Zone setup does not build a dungeon or generate a world for you.

## Companions and equipment
Unlock, summon, recall, heal and upgrade companions from the collection/refuge menu. One active companion per owner, persistent health and revival timers. Owners can set a free personal name of up to 32 Unicode characters and reset it later; names survive reconnects and restarts without resetting stats or health. Equipment includes animated fantasy weapons, defensive tools and cooldown-based abilities.

## Installation and commands
Minecraft 1.21.1, Java 21, NeoForge 21.1.259+ for Minecraft 1.21.1, and GeckoLib 4.9.3–4.x on client and server. Zian Manager, Cobblemon, MythicMobs and ModelEngine are not required. Administrators open /zianmobs; players can consult /zianmobs pending and use their companion menu. Back up the world before updates or guarded Manager imports. Import previews avoid overwriting IDs and do not move pending delivery receipts.

## License and artwork
Original code uses MIT; third-party art remains under its separate notices and permissions. The maintainer identifies https://es.namemc.com/ as the source of the 27 additional NPC skins and states they are publicly distributed. This does not attribute authorship to NameMC or Zian and does not relicense those skins as MIT. GeckoLib is a separate required dependency; SQLite is bundled privately.

## Latest update — 0.3.2

Unified custom mobs, bosses, dungeon zones, mob/boss reward tables, waves and ordered encounters in Zian Mobs, with guarded preview/import of legacy Manager definitions. Simple zones repeat the selected mob group for 1–16 waves: 1–3 initial mobs, +2 per additional player, maximum 8, with separate wave and zone-reuse delays and an optional final boss. Advanced encounters can interleave waves and bosses. Restored the center-floor marker and matching region preview; saved spawn points support slabs and one-block suspended platforms. NPC skin selectors include named appearances. Companion owners can now set a free personal name of up to 32 Unicode characters, search by nickname and restore the original name. Names persist separately without changing health, stats, cooldowns or collection ownership. Protocol mobs-ux-12: update both sides.

## Novedades — Español

Mobs, jefes, zonas, oleadas, encuentros y tablas de loot de combate quedan reunidos en Zian Mobs. Configuración simple con un grupo repetido, 1–16 oleadas, 1–3 mobs iniciales, +2 por jugador adicional hasta 8, pausas y reutilización independientes y jefe final opcional. Admite puntos seguros sobre losas y plataformas de un bloque. Compañeros con nombre personal gratuito, persistente y restablecimiento del nombre original, sin alterar vida ni mejoras.

Minecraft 1.21.1 · NeoForge · Java 21. Install the same mod version on client and server. Back up your world before upgrading; replace the old JAR instead of keeping both copies.


## Fantasy equipment

The original **46 fantasy equipment and decorative items** remain available, alongside four Titan armor pieces, the Titan Double Sword and Titan Gauntlet. Fantasy names and short decorated lore preserve existing anvil names and enchantments.

![Equipment catalogue](https://raw.githubusercontent.com/IANBLK/Zian-Mobs/v0.2.0/docs/images/equipment-catalog.png)

*   Carnivoret: poison for 2 seconds, except scythes. Scythes deal **14 total damage**; critical hits heal **10% maximum health** and grant regeneration I for **5 seconds**, with a **30-second cooldown**.
*   Crimson Ruby: bleeding for 2 seconds. Dark Queen: Wither for 2 seconds. Purple World: weakness for 2 seconds.
*   Valentines: critical hits heal 10% maximum health and grant regeneration I for 5 seconds. Master Sword: heal 20% and regeneration I for 10 seconds. Both use a 30-second cooldown; weapon healing shares a persistent cooldown.
*   Hylian Shield: a valid block grants resistance I and regeneration I for 10 seconds; cooldown 30 seconds.
*   Right-click staffs cast sonic, Wither-skull or dragon attacks; cooldown 30 seconds, without terrain destruction. Enhanced bows have stronger shots and shorter draw time.
*   Revival wings and the edible blessed Toffy Rupee retain their special abilities.

### Titan equipment

The armor uses native netherite-based properties and supports enchantments. The chestpiece adjusts to **wide and slim skins**. The double sword deals **10 total damage** at **1.6 attack speed**.

Use the sword in the main hand and gauntlet in the offhand. A valid melee critical hit against a mob or a melee mob kill grants **regeneration II, absorption IV, resistance I, fire resistance I and night vision I** for **30 seconds**, with a **60-second cooldown**. All four Titan armor pieces change duration and cooldown to **45 seconds each**.

Gems illuminate while the blessing is active; bloom depends on the shader. The offhand gauntlet can also save its wearer through native totem behavior: **one gauntlet is consumed**, subject to native damage bypasses and cancellation rules.

![Titan armor on wide and slim skins](https://raw.githubusercontent.com/IANBLK/Zian-Mobs/v0.2.0/docs/images/thanos-anchos-y-delgados.png)

## Commands and permissions

*   `/companions`, `/ziancompanions`, `/zianmobs companions`: public collection menu.
*   `/zianmobs`: operator interface, administration requires **permission level 2**.
*   `/zianmobs pending`: pending personal rewards.
*   `/zianmobs companion grant <player|UUID> <companion> [seconds]`: unlock. Omit duration or use `0` for permanent access; timed grants require at least 10 seconds.
*   `/zianmobs companion revoke <player|UUID> <companion>`: remove access and withdraw an active companion.
*   `/zianmobs companion status <player|UUID> <companion>`: access, levels and revival wait.

Example: `/zianmobs companion grant Steve cubee_fire 3600` unlocks Ascua for one hour. Player names require an online player; use a UUID for offline management. Unlocks are managed by Zian Mobs; ranks are not automatically synchronized. A command-whitelist plugin may separately require allowing the public menu commands. Keep administrative commands restricted.



[Spanish guide](https://github.com/IANBLK/Zian-Mobs/blob/main/docs/GUIA_ES.md) · [Issues](https://github.com/IANBLK/Zian-Mobs/issues)

![Flying companion](https://raw.githubusercontent.com/IANBLK/Zian-Mobs/v0.2.0/docs/images/vuelo-ascua.png)
