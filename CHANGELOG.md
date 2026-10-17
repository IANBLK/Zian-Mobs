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

# 0.1.0

First public release for Minecraft 1.21.1 / NeoForge / Java 21.

- Includes the features and gameplay validated in 0.1.0-beta.8; no changes to combat balance or saved identifiers.
- Animated Cursed One variants and Oni Brute, seven combat NPC appearances, configurable wave zones, personal rewards and 46 fantasy equipment items.
- In-game configuration, zone wireframes and countdowns, item abilities, enhanced bows and three-line fantasy lore.
- MIT license for original code; third-party artwork retains its original licenses and acknowledgements.
- Updated public documentation, original project icon and release metadata.
- Automated native server/client checks passed in beta.8. The maintainer confirmed the complete gameplay checks. Release archive verification compares every gameplay class and existing resource against that tested build.

# 0.1.0-beta.8

- Nombres propios de fantasía en los 46 objetos, con emblema discreto y colores por colección.
- Lore de tres líneas breves, sin guiones ni separadores horizontales; frases por colección y tipo de reliquia.
- Martillo Carnivoret: 13 de daño. Martillo San Valentín: 11 de daño. Velocidades y durabilidad conservadas.
- Escudo Hyliano: reutilización reducida de 90 a 30 s. Se migra la espera guardada para evitar conservar el plazo anterior.
- Los objetos existentes reciben nombres de objeto y lore actuales; los nombres personales de yunque y encantamientos se conservan.
- Se mantiene el protocolo mobs7, las habilidades y el contorno de zonas de beta.7.

# 0.1.0-beta.7

- Vista de zonas con el mismo renderizado de líneas azules y marcadores dorados de Zian Manager; sin partículas de contorno y sin modificar el mundo.
- Lore de fantasía, sin etiquetas de netherita; se conserva el balance de netherita +2 y se normalizan las descripciones antiguas.
- Guadaña con daño total 14 y robo de vida en críticos. Curación de San Valentín y Espada Maestra con regeneración y esperas compartidas.
- Veneno Carnivoret (salvo guadaña), sangrado Rubí, Wither Reina Oscura y debilidad Mundo Púrpura: 2 s.
- Báculos de clic derecho con ataques sónicos, calaveras o aliento; espera 30 s, sin destrucción de bloques.
- Escudo Hyliano: resistencia I y regeneración I por bloqueo durante 10 s, espera 90 s.
- Efectos tras daño aceptado, arma de origen guardada en proyectiles, esperas persistidas por jugador y detalles visibles en el catálogo.
- Protocolo mobs7: reemplazar el JAR en cliente y servidor juntos.

# 0.1.0-beta.6

- Identificadores automáticos para guardar zonas, criaturas y jefes sin rellenar ese campo; sufijos únicos para no sobrescribir nombres repetidos.
- Los formularios conservan sus valores tras errores y al pedir una previsualización.
- Vista personal del área de aparición durante 60 s: contorno dorado y puntos verdes, disponible antes de guardar, sin bloques, entidades ni carga de chunks.
- Retirado el editor de estadísticas de equipo; catálogo informativo con botón para recibir ejemplares.
- Armas y herramientas basadas en netherita con +2 de daño, velocidad nativa y 2031 usos. Escudos, cañas, arcos y ballesta también usan 2031 de durabilidad.
- Arcos con tensión completa a 16 ticks y flechas +2 de daño base; ballesta con +2 de daño base en flechas.
- Rupia Toffy comestible: efectos de manzana encantada con duración ×1,30 y consumo de 40 ticks (×1,25).
- Nombres en español y lore de colección con propiedades; eliminados los prefijos repetidos.
- Copia de ajustes anteriores de objetos y normalización de componentes antiguos, respetando nombres de yunque y encantamientos.
- Pruebas de guardado sin ID, colisiones, límites de partículas, disparos, consumo y atributos reales. Sin cambios en los registros de objetos ni en las texturas.
- Protocolo de interfaz mobs6: actualizar cliente y servidor juntos.

# 0.1.0-beta.5

- Recuperado el letrero de Manager para zonas de Mobs: nombre, oleada X de Y, criaturas restantes, pausa entre oleadas y enfriamiento hasta estar lista.
- Cuenta atrás basada en el encuentro persistido; no se reinicia por volver a abrir el mundo. Displays temporales sin guardar duplicados ni cargar chunks.
- Avisos locales de inicio de oleada, oleada completada y zona completada en chat y barra de acceso rápido.
- Inicio con categorías separadas para NPC, criaturas normales, jefes, zonas, recompensas y objetos; herramientas adicionales en su propio menú.
- Formularios divididos en pestañas con ayudas, ajustes avanzados aparte y conservación de todas las ediciones entre pestañas.
- Confirmación al descartar cambios; guardado de plantilla antes de abrir skin y habilidades. Se mantiene carbón/dorado y escalado ×2, ×3 y ×4.
- Listas de zonas muestran el estado legible del encuentro; búsquedas por nombre y creación de NPC, jefe o zona con botones específicos.
- Pruebas del letrero con encuentros persistidos y recreación sin duplicados, más capturas del panel real del servidor y navegación en las tres escalas.
- Protocolo de interfaz actualizado: instalar beta.5 tanto en cliente como en servidor.

# 0.1.0-beta.4

- Eliminadas todas las armaduras y sombreros restantes: 13 objetos adicionales, 28 retiradas acumuladas y dos entidades retiradas.
- Alas de Carnivoret convertidas en tótem consumible con comportamiento nativo y visión nocturna en mano secundaria, preservando pociones externas.
- Añadido NPC humanoide con siete skins originales, equipo visible y combate con puños, espadas, hachas, arco y ballesta.
- Añadidas habilidades configurables de Warden, dragón, Wither y Blaze con preparación y sin destrucción ni incendio del terreno.
- Nueva sección NPC de combate y editor de skin, brazos, agresividad, alcance, daño y esperas, con interfaz de Manager y escalas ×2, ×3 y ×4.
- NPC integrados con zonas, oleadas y recompensas personales existentes. Siete plantillas iniciales; configuración propia sin dependencia de Manager.
- Migración persistente de retiradas y configuración, copias previas beta.4 y revisión de premios incompatibles sin nuevos sorteos.
- Catálogo actual: 46 objetos de equipo y decoración más cinco huevos; cero armaduras.
- Pruebas nativas de alas, efectos, NPC, guardado, ataques e impactos; comprobación de siete PNG originales y 51 modelos de inventario.

# 0.1.0-beta.3

- Eliminados los 13 objetos señalados y los dos huevos de aparición del duende y la larva.
- Eliminadas ambas entidades del registro y del panel; quedan las tres variantes Cursed One y Oni Brute.
- Retirados modelos, texturas, animaciones, entradas de etiquetas y traducciones de ese contenido. Se conservan los recursos compartidos necesarios para los objetos restantes.
- Catálogo actual: 59 objetos de equipo y decoración, más cuatro huevos.
- Migración de mundos anteriores: copia de configuración previa, limpieza de equipo y loot retirados, desactivación solo de zonas afectadas y cancelación persistente de sus encuentros.
- Los premios reservados se conservan; los objetos retirados requieren revisión manual, sin nuevos sorteos ni compensaciones automáticas.
- Se mantiene el editor y su ajuste a escalas ×2, ×3 y ×4.

# 0.1.0-beta.2

- Corregidos modelos de huevos y texturas de Barrel Knight en inventario; reparación de referencia rota del carcaj Valentines.
- Añadidos Goofy Goblin y Armored Grub con ataques nativos, animaciones, huevos de aparición y parámetros editables.
- Añadidos 70 objetos de los archivos recibidos, con modelos JSON sin necesidad de BBModel, texturas animadas y estados de uso nativos.
- Kraber adaptada a arco con flechas; Trifuerza como trofeo decorativo.
- Nuevo panel `/zianmobs` con criaturas, ataques, equipo, zonas, recompensas, ajustes y revisión de entregas.
- Reutilizado el estilo carbón/dorado y el ajuste de pantalla e interacción de Manager para ×2, ×3 y ×4.
- Estadísticas de equipo configurables y persistentes; defensa acumulable por ranura de armadura.
- Validación de permisos, confirmaciones de un uso y rechazo de editores desactualizados.
- Conservación de las zonas y recompensas existentes al migrar desde beta.1.

Beta privada; pendiente validación jugable con el modpack habitual.
