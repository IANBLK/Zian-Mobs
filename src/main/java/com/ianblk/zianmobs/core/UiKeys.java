package com.ianblk.zianmobs.core;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** Clave de lang de un texto fijo de pantalla: ui.zianmobs.&lt;12 hex del SHA-1 del español&gt;. Sin Minecraft para poder probarla. */
public final class UiKeys {
 public static String key(String es){
  try{var sha=MessageDigest.getInstance("SHA-1").digest(es.getBytes(StandardCharsets.UTF_8));var out=new StringBuilder("ui.zianmobs.");for(int i=0;i<6;i++)out.append(String.format("%02x",sha[i]));return out.toString();}
  catch(NoSuchAlgorithmException error){throw new IllegalStateException(error);}
 }
 private UiKeys(){}
}
