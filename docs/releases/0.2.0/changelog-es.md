# Zian Mobs 0.2.0 · Cambios

- 29 compañeros voladores con menú para invocar, guardar/curar, traer al lado, consultar resurrección y mejorar. Primero aparecen los tuyos; máximo uno activo por jugador.
- Ataques a distancia sincronizados con sus animaciones, respuesta de enemigos y uno o dos apoyos aleatorios con mensajes decorados.
- Seguimiento a 16 bloques, vuelo sobre agua y guardado automático al superar 32 bloques, morir el dueño, desconectarse o perder el acceso.
- Accesos permanentes o temporales administrados con comandos; quitar y devolver acceso conserva mejoras. Datos y tiempos persisten tras reinicios.
- 14 materiales con seis estadísticas de mejora, máximo 10 niveles: normales +1 y Despiertos +2. El menú conserva la pestaña y selección; no consume si está al máximo.
- Vida base habitual 36, armadura 6 y daño 4; máximos 72/12/8. Kit tiene 45–90 de vida; Espíritu 24–48. Apoyos 15–30 s, reutilización 90–45 s y resurrección 600–300 s.
- Snowbone como criatura de encuentros; conjunto completo del Titán con cuatro piezas encantables, espada doble y guantelete.
- Combo de espada principal y guantelete secundario: regeneración II, absorción IV, resistencia I, resistencia al fuego I y visión nocturna I durante 30 s, espera 60 s. Con armadura completa: duración y espera 45 s.
- Guantelete con gemas emisivas al activar la bendición y protección de tótem en segunda mano: se consume una unidad al salvar de la muerte. Armadura adaptada a skins anchas y delgadas.
- Se conservan los NPC, zonas, recompensas personales y equipo de fantasía existentes.

## Instalación y migración

Minecraft 1.21.1, Java 21, NeoForge 21.1.259+ y GeckoLib 4.9.3–4.x. Sustituir el JAR en cliente y servidor con copia del mundo antes de actualizar. No dejar dos versiones juntas.

Los datos de compañeros pasan al esquema 4 con respaldo. El +50% de vida y armadura base se aplica una sola vez, conservando daño, niveles, accesos y tiempos ya iniciados. No cura automáticamente la vida actual. Para volver a una prueba anterior hace falta su copia correspondiente.

64 pruebas unitarias y el escenario nativo de servidor aprobados. El administrador comprobó combate, vuelo, agua, multijugador, permisos, mejoras, resurrección y reinicios. El JAR final conserva todas las clases de la prueba 10 sin cambios; solo actualiza metadatos y créditos.

El código original es MIT; los recursos de terceros conservan sus licencias.
