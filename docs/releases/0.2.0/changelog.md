# Zian Mobs 0.2.0

For Minecraft 1.21.1, NeoForge 21.1.259+ and Java 21. GeckoLib 4.9.3–4.x is required on client and server.

## New companions and progression

- Added 29 companions: Sleepy the Otter, Kit, Clank, Ghost, Gift and 24 CUBEES variants.
- Added a player collection menu with unlocked companions first, search, all/owned filtering, summon, recall, store/heal, upgrades and revival countdowns.
- One active companion per owner; flying follow behavior, free-air movement over water, 16-block follow range and automatic storage beyond 32 blocks.
- Ranged defense, hostile retaliation, synchronized attack animations and random support with decorated chat messages. Owner death, logout, dimension changes and access expiration safely withdraw companions.
- Persistent unlocks, saved health, revival waits, support/attack deadlines and upgrades. Manual storage heals living companions; automatic storage does not heal and death cannot bypass revival waits.
- Added operator grant/revoke/status commands with permanent or timed access and offline management by UUID.
- Added 14 Shadow Magic upgrade materials. Normal +1 and Awakened +2 levels, six tracks capped at 10; capped attempts consume nothing. Menu tab and material selection survive upgrades.
- Final balance: most companions start at 36 health, 6 armor and 4 damage. Maximum upgrades reach 72/12/8; Kit health is 45–90, Ghost health 24–48. Support is 15–30 seconds, support wait 90–45 seconds, revival 600–300 seconds.
- Capture protections and tested Supplementaries compatibility; shader transparency corrections preserve original textures and selective emissive features.

## New creatures and equipment

- Added Snowbone as an encounter creature with a native ranged attack.
- Added four enchantable Titan armor pieces, Titan Double Sword and offhand Titan Gauntlet, including original blocking models and emissive gems.
- Sword + gauntlet melee critical hits or mob kills grant regeneration II, absorption IV, resistance I, fire resistance I and night vision I for 30 seconds; cooldown 60 seconds.
- Wearing the full Titan armor set changes blessing duration and cooldown to 45 seconds each.
- The offhand gauntlet saves its wearer with native totem rules and consumes one gauntlet on activation. Active blessing illuminates its gems.
- Titan armor adapts to wide and slim player skins. Existing fantasy equipment, encounters, NPCs and personal rewards remain available.

## Updating

- Replace the mod on both client and server; keep one JAR and back up the world first.
- Companion data migrates to schema 4 with automatic backups. The final +50% base health/armor increase applies once to older companion settings; damage, levels, access and existing deadlines are preserved. Custom values are subject to supported attribute caps.
- Saved current health is preserved, not automatically healed by migration. Use normal manual storage/healing for a living companion.
- Returning to a trial or older build that uses an earlier companion schema requires the matching backup. See the updating guide.
- Original code remains MIT; included third-party artwork retains its original licensing and credits.

## Validation

- 64 unit tests and the native server scenario passed in the final trial; release verification compares all gameplay classes to that tested JAR.
- Maintainer verified flight, water, combat/support, revival across restarts, upgrades and caps, revoked/timed access, multiple-player isolation, and Titan armor/gauntlet behavior on the test server.
- Native wide/slim armor and flight previews were checked in the trial. Tests do not guarantee every third-party shader, capture mod or hybrid server implementation.
