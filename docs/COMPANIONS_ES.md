# Compañeros · Zian Mobs 0.2.0

## Uso del refugio

Abre `/companions`, `/ziancompanions` o `/zianmobs companions`. Por defecto aparecen tus compañeros desbloqueados. **Ver todos** abre el catálogo completo; la búsqueda y la paginación permiten localizar cada compañero.

**Invocar** trae al seleccionado y guarda el anterior: solo puede haber uno por jugador. **Traer a mi lado** lo recoloca en aire libre. **Guardar y curar** recupera la vida de un compañero vivo. **Mejorar vínculo** abre los materiales y los valores actuales. El guardado automático por distancia, desconexión, muerte del dueño o pérdida de acceso no cura. Si el compañero muere hay que esperar su resurrección; no puede mejorarse mientras está muerto.

Todos vuelan. Siguen al dueño a un máximo de 16 bloques, recuperan distancia si se quedan atrás y se guardan si superan 32 bloques. Sobre lagos buscan aire libre; al sumergirse el dueño permanecen sobre la superficie. No atraviesan bloques y necesitan espacio para aparecer.

Atacan a distancia para defender a su dueño. Los jugadores, compañeros y entidades domesticables quedan excluidos. Los mobs hostiles alcanzados pueden responder contra el compañero. Los apoyos se seleccionan al azar según el tipo y la necesidad, aplicando uno o dos efectos elegibles; el chat muestra cuáles. Los niveles de armadura son un atributo defensivo, no piezas equipables.

## Valores base y límites

- Vida habitual **36**, máxima mejorada **72**. Kit: **45 → 90**. Espíritu: **24 → 48**.
- Armadura **6 → 12**; daño **4 → 8**. No se aumentó el daño al elevar la resistencia base.
- Alcance de objetivo: **12 bloques**. Espera base de ataque: **4 s**, además de la animación y validación del objetivo.
- Duración del apoyo **15 → 30 s**; reutilización **90 → 45 s**.
- Resurrección **600 → 300 s**.

Estos valores usan los ajustes de fábrica. Un administrador puede personalizarlos desde el panel. Hay topes efectivos de seguridad de 4000 de vida, 30 de armadura y 64 de daño.

## Materiales de mejora

Cada uso exitoso consume una unidad. Cada estadística tiene **10 niveles**: un normal da +1 y un Despierto da +2. Desde cero son 10 normales, 5 Despiertos o una mezcla. Si estás en nivel 9, un Despierto consume una unidad y llega al 10; el nivel sobrante no se guarda. Si el efecto ya está al máximo, no consume material.

Vida, daño y armadura ganan un 10% del valor base por nivel. La duración del apoyo aumenta hasta 30 s; sus esperas y la resurrección disminuyen hasta la mitad. Las esperas ya iniciadas no se reinician ni se acortan retroactivamente por mejorar. Los niveles son permanentes para ese jugador y ese compañero y se conservan al quitar y devolver el acceso.

Los materiales están en la pestaña creativa y pueden entregarse por comandos de administrador. Esta versión no añade recetas ni botín automático para ellos.

- **Geoda del Umbral**, mejora armadura: normal `zianmobs:shadow_dark_geode_0a`; Despierto `zianmobs:shadow_dark_geode_1a`.
- **Cráneo del Retorno**, mejora resurrección: normal `zianmobs:shadow_hallow_skull_0a`; Despierto `zianmobs:shadow_hallow_skull_1a`.
- **Huevo del Renacer**, mejora vida: normal `zianmobs:shadow_infested_egg_0a`; Despierto `zianmobs:shadow_infested_egg_1a`.
- **Núcleo del Eclipse**, mejora daño: normal `zianmobs:shadow_magical_core_0a`; Despierto `zianmobs:shadow_magical_core_1a`.
- **Esencia Crepuscular**, mejora duración del apoyo: normal `zianmobs:shadow_magical_substance_0a`; Despierto `zianmobs:shadow_magical_substance_1a`.
- **Nido del Instante**, mejora reutilización del apoyo: normal `zianmobs:shadow_nano_nest_0a`; Despierto `zianmobs:shadow_nano_nest_1a`.
- **Diamante del Vínculo**, mejora todas las estadísticas que no estén al máximo: normal `zianmobs:shadow_shadow_diamond_0a`; Despierto `zianmobs:shadow_shadow_diamond_1a`.

El prefijo doble de `zianmobs:shadow_shadow_diamond_1a` es correcto. Ejemplo: `/give Steve zianmobs:shadow_infested_egg_0a 10`.

## Accesos de administración

Los siguientes comandos requieren operador de nivel 2. En consola no se escribe la barra inicial.

- `/zianmobs companion grant Steve cubee_fire`: Ascua permanente.
- `/zianmobs companion grant Steve cubee_fire 3600`: Ascua durante una hora.
- `/zianmobs companion revoke Steve cubee_fire`: quita el acceso y guarda al compañero si está activo.
- `/zianmobs companion status Steve cubee_fire`: consulta acceso, niveles y resurrección.

Duración omitida o 0 significa permanente. Duración temporal mínima 10 s, máxima 31536000 s. El nombre solo funciona si el jugador está conectado; para uno desconectado usa su UUID. También se puede gestionar el acceso desde el panel de administración. Los desbloqueos los administra el propio mod; no hay enlace automático a rangos de LuckPerms. Para un rango temporal, la herramienta externa debe dar o retirar el desbloqueo mediante estos comandos.

Los jugadores pueden abrir el refugio sin ser operadores. Si un plugin de lista blanca bloquea el comando, permite `companions` y `ziancompanions` en esa lista. En Youer/Bukkit se registran los permisos públicos `minecraft.command.companions` y `minecraft.command.ziancompanions`; una denegación explícita sigue teniendo efecto. No des permisos globales de administración para solucionar el acceso al refugio.

## Catálogo de compañeros e identificadores

- **Sleepy · Nutria de las Mareas**: `otter`.
- **Kit · Guardián del Vínculo**: `kit`.
- **Clank · Corazón de Acero**: `clank`.
- **Espíritu del Lucero**: `ghost`.
- **Regalo del Invierno**: `gift`.
- **Cubee · Pequeño Ghast**: `cubee_baby_ghast`.
- **Cubee · Bombardero**: `cubee_bombardier_creeper`.
- **Cubee · Cañón**: `cubee_cannon`.
- **Cubee · Desierto**: `cubee_desert`.
- **Cubee · Sombra**: `cubee_evil`.
- **Cubee · Ascua**: `cubee_fire`.
- **Cubee · Luz**: `cubee_good`.
- **Cubee · Brote**: `cubee_grass`.
- **Cubee · Zorro arquero**: `cubee_hoody_fox`.
- **Cubee · Jungla**: `cubee_jungle`.
- **Cubee · Llama**: `cubee_llama`.
- **Cubee · Cueva florida**: `cubee_lush`.
- **Cubee · Hongo**: `cubee_mushroom`.
- **Cubee · Pradera**: `cubee_plains`.
- **Cubee · Sabana**: `cubee_savanna`.
- **Cubee · Huesitos**: `cubee_skeleton`.
- **Cubee · Tirachinas**: `cubee_slingshot`.
- **Cubee · Nieve**: `cubee_snow`.
- **Cubee · Roca**: `cubee_stone`.
- **Cubee · Saqueador**: `cubee_swag_pillager`.
- **Cubee · Pantano**: `cubee_swamp`.
- **Cubee · Dinamita**: `cubee_tnt`.
- **Cubee · Brujita**: `cubee_twinkle_witch`.
- **Cubee · Marea**: `cubee_water`.

## Guardado y compatibilidad

La información está en `world/data/zianmobs/companions.json`, dentro del mundo seleccionado por el servidor. Se guardan accesos, niveles, salud y plazos de resurrección/apoyos/ataques. Un reinicio no borra ni reinicia estas esperas. Respeta la copia de seguridad y los pasos de [actualización](UPDATING.md).

La captura normal de compañeros está bloqueada y se probó la integración con Supplementaries. Otros mods con mecanismos propios pueden necesitar integración. No se garantiza el funcionamiento de todos los shaders ni todos los servidores híbridos.
