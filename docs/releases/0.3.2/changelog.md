# Zian Mobs 0.3.2 — 2026-10-19

## Dungeon configuration moved to Zian Mobs / Configuración de dungeons trasladada

Dungeon zones, mobs, bosses, waves, encounters and mob/boss loot tables have moved from **Zian Manager to Zian Mobs**. Configure combat in Zian Mobs. Manager retains chest loot, vaults, NPCs/trades, entrances and world time limits. For existing Manager definitions, use the guarded import preview in Mobs after backing up the world; pending delivery receipts are not moved.

La configuración de zonas de dungeon, mobs, jefes, oleadas, encuentros y tablas de loot de mobs/jefes pasó de **Zian Manager a Zian Mobs**. Las definiciones antiguas se conservan para importarlas con la vista previa protegida de Mobs; respalda el mundo antes y no elimines los datos antiguos. Los recibos de entregas pendientes no se trasladan.

Unified custom mobs, bosses, dungeon zones, mob/boss reward tables, waves and ordered encounters in Zian Mobs, with guarded preview/import of legacy Manager definitions. Simple zones repeat the selected mob group for 1–16 waves: 1–3 initial mobs, +2 per additional player, maximum 8, with separate wave and zone-reuse delays and an optional final boss. Advanced encounters can interleave waves and bosses. Restored the center-floor marker and matching region preview; saved spawn points support slabs and one-block suspended platforms. NPC skin selectors include named appearances. Companion owners can now set a free personal name of up to 32 Unicode characters, search by nickname and restore the original name. Names persist separately without changing health, stats, cooldowns or collection ownership. Protocol mobs-ux-12: update both sides.

## Español

Mobs, jefes, zonas, oleadas, encuentros y tablas de loot de combate quedan reunidos en Zian Mobs. Configuración simple con un grupo repetido, 1–16 oleadas, 1–3 mobs iniciales, +2 por jugador adicional hasta 8, pausas y reutilización independientes y jefe final opcional. Admite puntos seguros sobre losas y plataformas de un bloque. Compañeros con nombre personal gratuito, persistente y restablecimiento del nombre original, sin alterar vida ni mejoras.

## Installation / Instalación

Minecraft 1.21.1 · NeoForge · Java 21. Install the same mod version on client and server. Back up your world before upgrading; replace the old JAR instead of keeping both copies.
