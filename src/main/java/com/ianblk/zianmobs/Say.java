package com.ianblk.zianmobs;

import net.minecraft.network.chat.Component;

/** Error mostrado al jugador: clave de lang con el español como respaldo; getMessage() conserva el español para JSON y registros. */
public final class Say extends IllegalArgumentException {
 private final String key,es;private final Object[] args;
 public Say(String key,String es,Object... args){super(args.length==0?es:String.format(es,args));this.key=key;this.es=es;this.args=args;}
 public Component component(){return Component.translatableWithFallback(key,es,args);}
 /** Texto para el chat: traducible si el error es un Say, el mensaje tal cual si no, o el respaldo si no hay mensaje. */
 public static Component of(Throwable error,String key,String es){return error instanceof Say say?say.component():error.getMessage()==null?Component.translatableWithFallback(key,es):Component.literal(error.getMessage());}
 public static <T> T require(T value,String key,String es){if(value==null)throw new Say(key,es);return value;}
}
