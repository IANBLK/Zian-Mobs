package com.ianblk.zianmobs.storage;
import com.ianblk.zianmobs.core.*;
import java.nio.file.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
class StorageIsolationTest {
 @TempDir Path world;
 @AfterEach void close()throws Exception{StorageFiles.closeAll();}
 @Test void sqliteMigrationNeverConsumesManagerFiles()throws Exception{
  Path manager=world.resolve("data/zianmanager/claims.json");Files.createDirectories(manager.getParent());Files.writeString(manager,"manager-private-sentinel");
  Path own=world.resolve("data/zianmobs/claims.json");AtomicJson.write(own,java.util.Map.of("schemaVersion",1,"claims",java.util.List.of()));
  StorageMaintenance.migrateOffline(world);
  assertEquals("manager-private-sentinel",Files.readString(manager));
  assertTrue(Files.isRegularFile(world.resolve("zian-storage/mobs.db")));
  assertFalse(Files.exists(world.resolve("zian-storage/manager.db")));
  assertTrue(StorageFiles.exists(own));
 }
}
