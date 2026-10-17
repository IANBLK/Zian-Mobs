# Contributing

Use GitHub Issues for reproducible bugs and suggestions. Include Minecraft, NeoForge, GeckoLib and Zian Mobs versions, a minimal reproduction, expected/actual behavior and a sanitized relevant log. State whether shaders or a hybrid server are involved. Do not include credentials, player addresses or private pack archives.

## Build

Use Java 21 and the included Gradle wrapper: `./gradlew build` or `gradlew.bat build` on Windows. GeckoLib and SQLite JDBC are resolved through Gradle. Optional local dependency overrides are `-PgeckolibJar=/absolute/path/geckolib.jar` and `-PsqliteJdbcJar=/absolute/path/sqlite-jdbc.jar`.

The distributable is `build/libs/zianmobs-0.2.0.jar`. SQLite JDBC is bundled as a runtime library; GeckoLib remains a separate mod dependency. Build output, worlds, local libraries and private archives must not be committed.

## Validation

`./gradlew test` runs unit tests. Native integration scenarios use `runMobsSmoke` and need a disposable server directory configured through `-PsmokeDir=...`, an accepted Minecraft EULA and local test properties. They are destructive to their test world; never point them at a real world. A successful run writes `world/smoke-result.txt` starting with PASS.

Client render checks use `runClientSmoke` and `runClientSceneSmoke` with a local graphical Minecraft client and disposable fixtures/worlds. These are development tools, not normal player setup. See the release validation record for completed checks. Avoid changing gameplay between final tests and release; compare gameplay archive entries when only release metadata changes.

Original code contributions follow MIT. Third-party artwork retains its own license; see ARTWORK-NOTICE.txt before changing assets or redistributing them separately.
