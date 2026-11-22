package com.ianblk.zianmobs.permission;

import com.ianblk.zianmobs.permission.LuckPermsLookup.Decision;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class MobsPermissionsTest {
    private static MobsPermissions.Lookup of(Map<String, Decision> m) { return n -> m.getOrDefault(n, Decision.UNDEFINED); }
    private static boolean d(String action, Map<String, Decision> m, boolean op) { return MobsPermissions.decide(action, of(m), () -> op); }

    @Test void specificFalseBeatsParentTrue() {
        assertFalse(d("admin.reload", Map.of("zianmobs.admin.reload", Decision.FALSE, "zianmobs.admin", Decision.TRUE), true));
    }
    @Test void parentFalseBeatsSpecificTrue() {
        assertFalse(d("admin.reload", Map.of("zianmobs.admin.reload", Decision.TRUE, "zianmobs.admin", Decision.FALSE), true));
    }
    @Test void adminTrueFromSpecificOrParent() {
        assertTrue(d("admin.reload", Map.of("zianmobs.admin.reload", Decision.TRUE), false));
        assertTrue(d("admin.reload", Map.of("zianmobs.admin", Decision.TRUE), false));
    }
    @Test void adminWithoutDecisionNeedsOp() {
        assertFalse(d("admin.reload", Map.of(), false));
        assertTrue(d("admin.reload", Map.of(), true));
    }
    @Test void nonAdminNeverQueriesParent() {
        MobsPermissions.Lookup l = n -> { if (n.equals("zianmobs.admin")) fail("parent queried"); return Decision.TRUE; };
        assertTrue(MobsPermissions.decide("companions.manage", l, () -> false));
    }
    @Test void nonAdminParentTrueDoesNotGrant() {
        assertFalse(d("companions.manage", Map.of("zianmobs.admin", Decision.TRUE), false));
    }
    @Test void nonAdminFalseDeniesEvenPublicOrOp() {
        assertFalse(d("loot", Map.of("zianmobs.loot", Decision.FALSE), true));
    }
    @Test void publicActionsNeedNoOp() {
        assertTrue(d("companions.open", Map.of(), false));
        assertTrue(d("loot", Map.of(), false));
    }
    @Test void nonPublicWithoutDecisionFallsBackToOp() {
        assertFalse(d("companions.manage", Map.of(), false));
        assertTrue(d("companions.manage", Map.of(), true));
    }
    @Test void lookupFailureDeniesEvenForOp() {
        assertFalse(MobsPermissions.decide("loot", n -> { throw new IllegalStateException(); }, () -> true));
        assertFalse(MobsPermissions.decide("loot", n -> { throw new NoClassDefFoundError(); }, () -> true));
    }
}
