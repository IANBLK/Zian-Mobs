package com.ianblk.zianmobs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonParser;
import com.ianblk.zianmobs.core.UiKeys;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

class LangFilesTest {
    private static final Pattern PLACEHOLDER = Pattern.compile("%(\\d+\\$)?[-#+0,(]*\\d*(\\.\\d+)?[sdfxXeEgG]");
    private static final String STRING = "\"((?:[^\"\\\\]|\\\\.)*)\"";

    private static Map<String, String> load(String name) throws Exception {
        var json = JsonParser.parseString(new String(
            LangFilesTest.class.getResourceAsStream("/assets/zianmobs/lang/" + name).readAllBytes(), StandardCharsets.UTF_8)).getAsJsonObject();
        var map = new TreeMap<String, String>();
        json.entrySet().forEach(e -> map.put(e.getKey(), e.getValue().getAsString()));
        return map;
    }
    private static String placeholders(String text) {
        var found = new java.util.ArrayList<String>();
        var m = PLACEHOLDER.matcher(text);
        while (m.find()) found.add(m.group());
        java.util.Collections.sort(found);
        return String.join(",", found);
    }

    @Test void allLanguagesHaveSameKeysAndPlaceholders() throws Exception {
        var es = load("es_es.json");
        for (var other : new String[] {"en_us.json", "es_mx.json"}) {
            var map = load(other);
            assertEquals(es.keySet(), map.keySet(), "Claves distintas en " + other);
            es.forEach((key, text) -> assertEquals(placeholders(text), placeholders(map.get(key)), key + " en " + other));
        }
    }

    @Test void mexicanSpanishMatchesSpain() throws Exception {
        assertEquals(load("es_es.json"), load("es_mx.json"));
    }

    @Test void uiKeyIsTheShaOfTheSpanishText() {
        assertEquals("ui.zianmobs.a62405e2cefb", UiKeys.key("Buscar"));
    }

    /** Todo ui("...") y translatableWithFallback("ui.zianmobs.x", "...") de las pantallas tiene su clave en es_es con el mismo español. */
    @Test void screenTextsHaveTheirKeys() throws Exception {
        var es = load("es_es.json");
        var calls = Pattern.compile("\\bui\\(" + STRING);
        var fallbacks = Pattern.compile("translatableWithFallback\\(\"(ui\\.zianmobs\\.[0-9a-f]{12})\",\\s*" + STRING);
        try (var files = Files.list(Path.of("src/main/java/com/ianblk/zianmobs/client"))) {
            for (Path file : files.filter(p -> p.toString().endsWith(".java")).toList()) {
                var source = Files.readString(file, StandardCharsets.UTF_8);
                var m = calls.matcher(source);
                while (m.find()) {
                    var text = unescape(m.group(1));
                    assertEquals(text, es.get(UiKeys.key(text)), file.getFileName() + ": falta la clave de \"" + text + "\"");
                }
                m = fallbacks.matcher(source);
                while (m.find()) {
                    var text = unescape(m.group(2));
                    assertEquals(UiKeys.key(text), m.group(1), file.getFileName() + ": clave que no corresponde a \"" + text + "\"");
                    assertTrue(es.containsKey(m.group(1)), file.getFileName() + ": falta " + m.group(1));
                }
            }
        }
    }
    private static String unescape(String java) { return java.replace("\\n", "\n").replace("\\\"", "\""); }
}
