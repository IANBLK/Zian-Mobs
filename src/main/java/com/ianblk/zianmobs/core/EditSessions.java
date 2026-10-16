package com.ianblk.zianmobs.core;
import java.util.*;
/** One current, single-use editor grant per administrator. All calls are on the server thread. */
public final class EditSessions {
 public record Session(UUID token,String section,String id,long expires,Object revision){}
 private final Map<UUID,Session> sessions=new HashMap<>();
 public Session issue(UUID owner,String section,String id,long now,Object revision){var s=new Session(UUID.randomUUID(),section,id,now+300000,revision);sessions.put(owner,s);return s;}
 public Session consume(UUID owner,UUID token,long now,Object revision){var s=sessions.get(owner);if(s==null||!s.token().equals(token))throw new IllegalArgumentException("Esta confirmación ya no es válida; vuelve a abrir el editor");sessions.remove(owner);if(now>s.expires()||s.revision()!=revision)throw new IllegalArgumentException("La configuración cambió; vuelve a abrir el editor");return s;}
 public void remove(UUID owner){sessions.remove(owner);}public void clear(){sessions.clear();}
}
