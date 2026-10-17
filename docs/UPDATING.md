# Updating to Zian Mobs 0.2.0

## Before installing

1. Stop the server normally and back up the complete world, including its `data` folder. Keep the previous mod JAR and matching client files.
2. Replace Zian Mobs on client and server with the same 0.2.0 JAR. Keep only one version. Keep GeckoLib 4.9.3–4.x for Minecraft 1.21.1 / NeoForge installed on both sides.
3. Start the server and inspect its log. Existing mobs, equipment IDs, zone definitions and rewards from 0.1.0 are retained. Companions are new for users updating directly from 0.1.0.
4. If upgrading from a companion trial, check the collection, upgrade levels, access and revival wait before normal play.

## Companion data migration

Companion state is stored in `<world>/data/zianmobs/companions.json`. The 0.2.0 format is **schema 4**.

- Schemas 1/2 receive the support/revival upgrade balance and a `.before-shadow-upgrades.json` backup.
- Schemas 1/2/3 receive a one-time **50% increase to configured maximum health and armor**, subject to supported caps, and a `.before-companion-vitality.json` backup.
- Damage, access grants, upgrade levels, saved current health and existing deadlines are retained. Current health is not automatically increased/healed by migration; normal manual storage heals a living companion.
- Schema 4 reloads do not apply the increase again. The current data file must retain the new schema.
- New worlds use 36 health / 6 armor / 4 damage for most companions, 45 health for Kit and 24 for Ghost. Custom administrator settings may differ.

Do not edit saved files while the server is running. A full world backup is preferred over individual file recovery.

## Returning to an older version

Older trials do not understand schema 4. Stop the server and restore a matching backup with the matching mod/client version. Restoring an older backup also restores its older progress. Do not change only the schema number or remove backup files to force another migration.

## What is and is not included

Zian Mobs is independent of Zian Manager and does not require Curios, Accessories, MythicMobs or ModelEngine. No third-party pack archive or original plugin is required at runtime. Upgrade materials do not have automatic recipes/loot in this release; administrators choose how players obtain them.

Tested on native NeoForge and the maintainer's Youer test server. This is not a blanket guarantee for every hybrid server or other mod implementation. Use the issue tracker with sanitized logs if a compatibility problem appears.
