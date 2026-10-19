# Zian Mobs 0.3.2 — English guide

**Language notice:** The current in-game interface, preset dialogues and administrator messages are primarily Spanish. English localization is incomplete. The English guide explains the controls and includes their Spanish button names. Changing Minecraft to English does not translate administrator-written NPC names, dialogues or configuration text.

## Administrators: create a simple wave zone

1. Run `/zianmobs` with OP level 2 or administrative permission. Create/select a dungeon-suitable mob and configure its rewards. Entity selectors focus on supported dungeon creatures and included models.
2. Stand on the intended **center floor block**, create a zone and set the range around that fixed center. Check the central marker and wireframe. You build the dungeon; the mod does not generate the building/world.
3. Choose the mob group once and **1–16 waves**. Simple mode repeats that group without editing every wave separately.
4. Automatic population scaling starts with **1–3 mobs**, adds **2 per extra player**, capped at **8**. This is mob-count scaling, not a Cobblemon Pokémon level-cap rule.
5. Set **time between waves** and **zone reuse time** independently. Reuse starts after the entire encounter, including an optional final boss.
6. Choose safe automatic spawning or **Usar mis puntos guardados**. Points must fit safely inside the selected region. Safe slabs and one-block suspended floors are supported; no deep floor is required. Add points/reduce composition if the selected group lacks safe positions.
7. Add the **optional final boss**, save, preview/test and activate when ready. Installing the mod does not enable encounters/natural spawning automatically.

## Advanced stages and Manager import

The advanced editor adds an **oleada** (wave) or **jefe** (boss), sets its group, reorders and confirms removal. Boss-first, interleaved and boss-only encounters are supported. Returning to simple repeated waves replaces the advanced plan when saved. Do not edit active encounters.

To migrate, back up the world, then open tools and **Importar combate antiguo de Manager…**. Review and confirm the guarded preview. Existing Mobs IDs are not overwritten; collisions receive new references. Imported zones start disabled. Review center, size, spawn points, stages and loot before activation. Manager keeps its chest tables, NPCs, vaults and pending delivery receipts.

## Rewards and companions

MOB/BOSS reward tables belong in Mobs. Entry weights are relative draws. Independent-percentage mode, where supported, follows its displayed rules. Rewards are recorded before delivery; `/zianmobs pending` inspects personal pending rewards. Uncertain results need review rather than another roll.

Players open `/companions`, `/ziancompanions` or `/zianmobs companions`. Access must first be granted. Search/filter, summon, recall, store or inspect revival/upgrades; only one companion is active per owner. Manually storing a living companion heals it; automatic withdrawal does not. Death/revival waits survive reconnects. Upgrades require an eligible unlocked, living, stored companion. Normal materials add one level, Awakened materials two, within each track's cap; capped attempts consume nothing.

From the refuge, owners set a **free name up to 32 Unicode characters**, without formatting/control codes, or restore the original. Search accepts nickname/model. Renaming does not heal or reset health, upgrades or cooldowns.

## Administrative access and equipment

- `/zianmobs companion grant <player|UUID> <companion> [seconds]`: omit seconds/use 0 for permanent access; timed access needs at least 10 seconds.
- `/zianmobs companion revoke <player|UUID> <companion>`: revoke/withdraw.
- `/zianmobs companion status <player|UUID> <companion>`: inspect. Use an online name or offline UUID.

Read equipment lore and cooldowns in the catalog. Upgrade materials have no automatic recipes/drops merely from installation. GeckoLib is required separately; Manager/Cobblemon/MythicMobs/ModelEngine/Curios/Accessories are not. Current zone schema **7**, companion collection schema **4**, names stored separately. Protocol **mobs-ux-12**.

## Installation, updating and recovery

Use Minecraft **1.21.1**, **Java 21** and **NeoForge**, with the separate required libraries listed in the README. Stop the server and back up the entire world, configuration and mod list before replacing a version. Install the same current JAR on the server and every client. Remove the previous JAR, keep the required libraries on both sides, start and inspect the log, then verify your existing configuration and progress. For rollback, stop and restore the complete matching backup and mod versions together. Do not edit schema numbers to force migration.

Youer has been tested; this does not guarantee every hybrid server/plugin combination. Command-whitelist plugins may separately block public commands. Allow the needed public roots while keeping administrative operations restricted. LuckPerms is optional; administrator configuration remains protected.

Keep world data and configuration together. Optional SQLite maintenance is explicit, not required automatically by an update. Never edit a live database or delete a journal to clear an error. For an uncertain payment/delivery, keep its reference, reconcile the recorded outcome with inventory/PC/wallet state and use the administrator review flow. Confirmation records a verified result; it must never be used to guess or generate a second reward.

Report the mod/library versions, reproduction steps and sanitized logs through this repository's Issues. Do not upload credentials, tokens, player databases or private world backups.
