# Guía de Zian Mobs 0.2.0

Mod para Minecraft 1.21.1, NeoForge 21.1.259 y GeckoLib 4.9.3 en **cliente y servidor**. Mod independiente: no necesita Zian Manager, MythicMobs ni ModelEngine. Reutiliza la apariencia de NPC y la interfaz carbón/dorado de Manager sin modificar ese mod.

## Novedades de 0.2.0

Incluye 29 compañeros voladores, 14 materiales de mejora permanente, Snowbone y el conjunto completo del Titán con armadura encantable, espada y guantelete. Consulta la [guía de compañeros, materiales y permisos](COMPANIONS_ES.md) la [guía del Titán](TITAN_ES.md) y los [pasos de actualización y copias](UPDATING.md). Los compañeros se abren con `/companions` o `/ziancompanions` y requieren desbloqueo del administrador.

## Instalación

Guarda una copia del mundo, reemplaza el JAR anterior por 0.2.0 tanto en cliente como en servidor y reinicia. No dejes dos versiones juntas. Los registros retirados desaparecen al cargar: para recuperarlos necesitas el mundo y el JAR anteriores. Actualiza cliente y servidor juntos, conservando los datos y configuraciones del mundo.

Abre **`/zianmobs`** con operador de nivel 2. Los jugadores pueden usar `/zianmobs pending`. No hay generación natural ni zonas activadas automáticamente.

## Guardado automático y vista de zonas

Ya no necesitas escribir un identificador para crear una **zona, criatura o jefe**. El servidor lo genera a partir del nombre visible, normaliza acentos y espacios y añade un sufijo si ya existe. Los identificadores de las definiciones existentes se mantienen. Los errores de validación conservan el formulario para corregirlo sin perder lo escrito.

En **Zonas y oleadas → zona → Tamaño → Ver límites y puntos (60 s)** puedes previsualizar el área incluso antes de guardar. El contorno azul de líneas continuas, igual que Manager, marca el volumen usado por la zona; los marcadores dorados con coordenadas indican sus puntos de aparición guardados. Cambiar los radios del formulario permite previsualizar el nuevo tamaño sin modificar la zona real. Puedes cerrar el panel para construir y usar **Ocultar vista de zona** para terminar antes.

La vista es personal del administrador, dura 60 segundos y exige estar en el mismo mundo y a menos de 256 bloques del centro. No coloca bloques ni entidades y no carga chunks. Se limpia al desconectarte, cambiar de mundo, perder el permiso, morir o terminar el tiempo. Añade puntos separados en los lugares concretos donde quieres que aparezcan los mobs.

## Habilidades de colección

Los 46 objetos tienen nombres de fantasía con un pequeño emblema y colores por colección. El lore usa tres líneas cortas: colección y dos frases, sin guiones ni separadores horizontales. Se mantienen los nombres personalizados de yunque. Los objetos existentes se actualizan al pasar por el inventario o las recompensas; mantienen sus nombres de yunque y encantamientos. Las características y habilidades se consultan en el catálogo.

- Guadaña Carnivoret: **14 de daño total**, velocidad 1,6. Un crítico cura 10% de la vida máxima y concede regeneración I 5 s; espera 30 s. No aplica veneno.
- Carnivoret restante: veneno I 2 s al causar daño.
- Rubí Carmesí: sangrado 2 s, 1 punto de daño mágico por segundo.
- Reina Oscura: Wither I 2 s al causar daño.
- Mundo Púrpura: debilidad I 2 s al causar daño.
- San Valentín: crítico cura 10% y concede regeneración I 5 s; espera 30 s.
- Espada Maestra: crítico cura 20% y concede regeneración I 10 s; espera 30 s.
- Escudo Hyliano: bloqueo válido concede resistencia I y regeneración I 10 s; espera 30 s.

Los báculos lanzan con clic derecho, con una espera compartida de 30 s: Carnivoret usa un eco del Warden (alcance 24 bloques); Rubí y Reina Oscura lanzan calaveras del Wither; San Valentín lanza aliento de dragón (radio 3 bloques). Hacen 10 de daño antes de las reducciones aplicables y también aplican el efecto de su colección. Los ataques respetan obstáculos, equipos y las protecciones del servidor; no explotan, no prenden bloques y no modifican el terreno.

Las habilidades ofensivas y la curación necesitan un impacto que quite vida. Los golpes bloqueados, anulados o absorbidos por completo no activan esas habilidades. Los críticos de flechas siguen el indicador nativo de disparo completo, usando el arma original aunque cambies de objeto. Todas las habilidades tienen 30 s de reutilización, incluido el Escudo Hyliano. La espera anterior del escudo se migra automáticamente. El enfriamiento de curación es compartido entre guadaña, San Valentín y Espada Maestra. Los plazos se guardan por jugador, sobreviven a reconexiones y muerte, y transcurren en tiempo real; cambiar de arma no los reinicia.

## Equipo con valores fijos

Se eliminó el editor de estadísticas de objetos. **Catálogo de equipo** muestra información de lectura y permite recibir un ejemplar de prueba. Las estadísticas de las criaturas siguen siendo configurables.

Espadas, hachas y herramientas usan netherita, conservando su velocidad nativa y sumando **+2 puntos al daño de ataque**: espada 10, hacha 12, pico 8, pala 8,5 y azada 3. La guadaña usa los 14 de daño indicados arriba. Los martillos suman otro punto: Carnivoret tiene 13 de daño y San Valentín 11, con su velocidad anterior. Los objetos con durabilidad del catálogo usan **2031 usos**. Son resistentes al fuego como netherita; los consumibles y las alas conservan su funcionamiento propio.

Los arcos alcanzan la tensión completa en **16 ticks (0,8 s)**, un 20% antes que los 20 ticks normales. Sus flechas tienen **+2 de daño base**; el daño final depende de la velocidad, el impacto y los encantamientos. La animación de tensado acompaña la nueva velocidad. Las ballestas también reciben +2 de daño base en sus flechas, manteniendo su tiempo nativo de carga. Se conserva el consumo nativo de munición y durabilidad.

La **Rupia bendita de Hyrule** (`toffy_rupee`) se puede comer incluso con el hambre llena. Consume una unidad en **40 ticks (2 s)**, un 25% más lento que la manzana encantada, y concede sus mismos niveles de efectos con **un 30% más de duración**:

- Regeneración II: 26 segundos.
- Resistencia I y resistencia al fuego: 6 minutos y 30 segundos.
- Absorción IV: 2 minutos y 36 segundos.

Se mantienen la nutrición y saturación de la manzana encantada. El 30% se aplica a duración; no añade niveles fraccionarios de poción.

El equipo recibe nombres en español, colores por colección y lore con ambientación y sus propiedades. Se eliminan los prefijos duplicados del catálogo. Los nombres personalizados de yunque y los encantamientos se conservan.

Los ajustes antiguos de estadísticas de objetos dejan de aplicarse y se guardan una vez en la copia lógica **`tuning-before-beta6.json`**. Las armas antiguas se normalizan al pasar por el inventario o por las entregas y equipamiento del mod: atributos, durabilidad y propiedades de herramienta vuelven a los valores fijos. No se cambian los identificadores de los objetos ni se retiran más recursos.

## Nuevo panel y avisos de zona

El inicio separa **NPC de combate, criaturas normales, jefes, zonas, recompensas y objetos**. Los ataques generales, ajustes y revisión de premios están en **Ajustes y herramientas**. El recorrido recomendado es crear la criatura, asignar su equipo y añadirla a una zona.

Los formularios se organizan por pestañas: **Básico, Combate, Equipo, Premios y Avanzado**, según el tipo. Las zonas separan **Tamaño, Oleadas y Puntos**; los NPC tienen una pestaña de **Habilidades**. Cada pestaña incluye una explicación breve. Los valores se conservan al cambiar de pestaña y **Guardar todas las pestañas** aplica el formulario completo. Salir con cambios pendientes exige confirmar que quieres descartarlos. **Guardar y abrir habilidades** guarda la plantilla antes de abrir su editor de skin y ataques.

Cada zona muestra un letrero flotante en su centro, a tres bloques de altura. Indica el nombre, la disponibilidad, **Oleada X de Y**, criaturas restantes, espera entre oleadas y cuenta atrás del enfriamiento final. También distingue zonas desactivadas, apariciones pausadas y encuentros en revisión. Se actualiza cada segundo usando el plazo guardado del encuentro, de modo que reiniciar no comienza otro enfriamiento.

Los jugadores dentro de la zona reciben avisos en el chat y sobre la barra de acceso rápido al empezar una oleada, terminarla y completar la zona. No se repite un mensaje de chat cada segundo. El letrero solo se crea en chunks que ya están cargados; no carga terreno y no se guarda como entidad permanente. Se elimina al quitar la zona o descargar el área y se recrea cuando corresponde.

## NPC de combate

En **NPC de combate**, selecciona una de las siete plantillas o crea otra. Cada plantilla permite configurar nombre, vida, daño base, defensa, efectos, barra de jefe y recompensas personales. Para equiparla, sostén una espada, hacha o arco, selecciona **Mano principal** y pulsa **Asignar objeto de mi mano**. Sin arma golpea con las manos. Las armas usan daño y encantamientos nativos; el arco y la ballesta disparan flechas sin munición recogible.

Después de guardar, abre **Guardar y abrir habilidades**. Puedes elegir skin, brazos normales o delgados, agresividad, espera de ataques, alcance, daño especial y estas habilidades independientes:

- **Warden:** disparo sónico con preparación, sonido y partículas.
- **Dragón:** proyectil con daño mágico en un radio de tres bloques al impactar.
- **Wither:** calavera con daño y efecto Wither durante cuatro segundos.
- **Blaze:** bola de fuego que prende a la entidad golpeada durante cuatro segundos.

Las habilidades tienen un segundo de preparación y alternan entre las activadas. Los tiempos de combate se expresan en ticks: **20 ticks = 1 segundo**. Empiezan desactivadas para que elijas la combinación. Los proyectiles especiales no crean explosiones, no rompen bloques y no prenden el terreno. Se excluyen aliados y criaturas del mismo encuentro, además de jugadores creativos y espectadores. Desactivar la agresividad evita buscar jugadores automáticamente; el NPC todavía puede defenderse si lo atacan.

Las siete skins originales se conservaron sin alterarlas y recibieron estos nombres: **Guardián abisal, Centinela del eclipse, Espadachín glacial, Caballero solar, Espectro de escarcha, Guardián esmeralda y Caballero de obsidiana**. Su origen y huella están en `npc-skins.json`.

Para usar las plantillas, añádelas al equipo de una zona y configura puntos de aparición. El huevo genérico crea el NPC abisal predeterminado; las variantes personalizadas se eligen mediante las plantillas de zona. Las estadísticas y el equipo de plantilla se aplican en las próximas apariciones; los ajustes de skin y habilidades se consultan durante el combate.

## Alas de Carnivoret y retirada de armaduras

Las **Alas de Carnivoret** funcionan como un **tótem de inmortalidad consumible**, en cualquiera de las manos, siguiendo la prioridad y las excepciones del tótem normal. Conceden la resurrección y los efectos nativos; no evitan daños que ignoran tótems. La animación de activación muestra las alas.

Mientras estén en la **mano secundaria**, conceden visión nocturna. Al quitarlas desaparece únicamente la visión nocturna concedida por las alas; se conservan las pociones externas. No conceden vuelo y no necesitan equiparse como armadura.

Se eliminaron las **13 piezas de armadura y sombreros que quedaban**, de Carnivoret, Valentines y Toffy. No quedan armaduras registradas por Zian Mobs. Se mantienen las retiradas de beta.3: Barrel Knight, objetos defectuosos señalados, duende y larva. Total acumulado: **28 identificadores de objetos y dos entidades retirados**. Los atributos defensivos configurables de las criaturas siguen disponibles.

El catálogo actual contiene **46 objetos de equipo y decoración y cinco huevos: 51 objetos**. Se conservan las funciones nativas de espadas, hachas, herramientas, arcos, ballesta, cañas y escudos, así como sus estados de uso y texturas animadas. Los paquetes sin `.bbmodel` usan sus modelos JSON y PNG originales.

## Panel, zonas y recompensas

El panel conserva el ajuste de interfaz y ratón de Manager en escalas **×2, ×3 y ×4**, búsqueda y paginación. Permite gestionar criaturas, ataques, equipo, recompensas, zonas, oleadas, ajustes y revisión de entregas. Los cambios requieren permiso del servidor, confirmaciones de un uso y una vista actualizada. Cancela un encuentro antes de editar su zona.

Se mantienen Cursed One de bosque, hielo y cristal y Oni Brute, con sus ataques y animaciones. El Oni conserva su animación de muerte completa. No se ejecutan scripts ni comandos de los paquetes importados.

Las zonas usan puntos cargados, suelo sólido y espacio libre; no fuerzan carga de chunks. Los tiempos de zona están en segundos. El drop es personal para quien derrota al mob; el premio final se reserva para participantes vivos que sigan dentro. El sorteo se guarda antes de entregar. Un inventario lleno deja premios pendientes; una entrega incierta pasa a revisión y no se duplica automáticamente.

Los datos están aislados por mundo, bajo `data/zianmobs` mediante la capa de almacenamiento y `zian-storage/mobs.db` cuando SQLite está disponible. No se leen ni modifican datos de Manager. Siguen disponibles los comandos `mob`, `loot`, `zone`, `reload`, `pending`, `review` y `resolve`.

## Actualizar mundos anteriores

La migración retira equipo y entradas de recompensas inválidos, elimina tablas vacías y desvincula sus referencias. Solo desactiva las zonas afectadas y cancela sus encuentros, conservando las zonas ajenas a la retirada. Guarda copias lógicas como `config-before-beta4.json` y, cuando corresponde, `tuning-before-beta4.json`; un registro persistente permite completar cancelaciones tras una interrupción.

Los premios reservados no se borran ni se sortean otra vez. Las partes con objetos retirados pasan a revisión; el administrador puede comprobarlas o compensarlas manualmente. Confirmar la compensación marca la revisión como resuelta y no entrega objetos adicionales.

Las configuraciones anteriores reciben las siete plantillas nuevas una sola vez al migrar al esquema 5. Después puedes borrarlas sin que reaparezcan en cada reinicio.

## Validación y desarrollo

Consulta `VALIDACION.json` y los registros de la entrega. Las pruebas cubren identificadores automáticos, límites de la vista, daño relativo a netherita y la excepción de la guadaña, migración de objetos, disparo nativo de arcos a 15 y 16 ticks, consumo y efectos reales de la rupia, persistencia, recompensas, permisos de edición, escalado, migración, tótem y visión nocturna, siete perfiles de NPC, golpes, ciclo de disparo del arco e impactos especiales sin alterar bloques. El cliente aislado comprueba modelos horneados y sprites y captura el inicio y los formularios reales del servidor en ×2, ×3 y ×4, incluida la navegación entre pestañas sin perder una edición y la confirmación al salir. El servidor comprueba el letrero durante ambas oleadas, la espera y el enfriamiento, y que recrearlo no duplique entidades.

La prueba automática del cliente cubre el catálogo y los formularios. El mantenedor confirmó que las pruebas jugables del modpack también pasaron. La compatibilidad con otros modpacks y shaders puede variar.

Para compilar: Java 21 y `gradlew build`. Las propiedades `-PgeckolibJar=ruta` y `-PsqliteJdbcJar=ruta` permiten usar bibliotecas locales; SQLite se incorpora como recurso privado. Los recursos finales ya están incluidos. `python tools/validate-assets.py` comprueba modelos, atlas, animaciones y las huellas de las siete skins. Los importadores de paquetes son herramientas de desarrollo y requieren los originales; después de importar hay que aplicar las retiradas vigentes y conservar el tipo `WING_TOTEM` de las alas.

`runMobsSmoke` debe apuntar con `-PsmokeDir=build/mobs-smoke-nuevo` a un mundo aislado nuevo con EULA aceptada y propiedades de servidor de prueba. `runMobsSmoke` genera también las vistas de prueba que necesita el cliente. Ejecuta después `runClientSmoke`, que usa únicamente `build/mobs-client-smoke-beta8`, sin cuenta. Los comprobadores solo se activan en esos procesos de desarrollo.

Código original bajo MIT. Los recursos de terceros conservan sus licencias y créditos; consulta ARTWORK-NOTICE.txt.
