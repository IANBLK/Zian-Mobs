package com.ianblk.zianmobs.core;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;
class EditSessionsTest{
 @Test void grantsAreBoundToOwnerSingleUseAndRevision(){var gate=new EditSessions();var owner=UUID.randomUUID();var revision=new Object();var s=gate.issue(owner,"zone","trial",1000,revision);assertThrows(IllegalArgumentException.class,()->gate.consume(UUID.randomUUID(),s.token(),1001,revision));assertEquals("trial",gate.consume(owner,s.token(),1001,revision).id());assertThrows(IllegalArgumentException.class,()->gate.consume(owner,s.token(),1002,revision));}
 @Test void staleEditorCannotOverwriteNewState(){var gate=new EditSessions();var owner=UUID.randomUUID();var s=gate.issue(owner,"loot","loot",0,new Object());assertThrows(IllegalArgumentException.class,()->gate.consume(owner,s.token(),1,new Object()));}
 @Test void expiryDisconnectAndReplacementRevokeGrants(){var gate=new EditSessions();var owner=UUID.randomUUID();var revision=new Object();var a=gate.issue(owner,"mob","a",0,revision);var b=gate.issue(owner,"mob","b",1,revision);assertThrows(IllegalArgumentException.class,()->gate.consume(owner,a.token(),2,revision));assertThrows(IllegalArgumentException.class,()->gate.consume(owner,b.token(),300002,revision));var c=gate.issue(owner,"mob","c",400000,revision);gate.remove(owner);assertThrows(IllegalArgumentException.class,()->gate.consume(owner,c.token(),400001,revision));}
}
