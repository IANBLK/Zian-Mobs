package com.ianblk.zianmobs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import java.util.UUID;
public final class MobsNetwork {
 static MobsAdmin admin;
 public record Request(String section,String id) implements CustomPacketPayload{
  public static final Type<Request> TYPE=new Type<>(ResourceLocation.fromNamespaceAndPath("zianmobs","request"));
  public static final StreamCodec<RegistryFriendlyByteBuf,Request> CODEC=new StreamCodec<>(){public Request decode(RegistryFriendlyByteBuf b){return new Request(b.readUtf(32),b.readUtf(128));}public void encode(RegistryFriendlyByteBuf b,Request v){b.writeUtf(v.section,32);b.writeUtf(v.id,128);}};public Type<Request> type(){return TYPE;}
 }
 public record Action(UUID token,String operation,String json) implements CustomPacketPayload{
  public static final Type<Action> TYPE=new Type<>(ResourceLocation.fromNamespaceAndPath("zianmobs","action"));
  public static final StreamCodec<RegistryFriendlyByteBuf,Action> CODEC=new StreamCodec<>(){public Action decode(RegistryFriendlyByteBuf b){return new Action(b.readUUID(),b.readUtf(32),b.readUtf(32768));}public void encode(RegistryFriendlyByteBuf b,Action v){b.writeUUID(v.token);b.writeUtf(v.operation,32);b.writeUtf(v.json,32768);}};public Type<Action> type(){return TYPE;}
 }
 public record View(String json) implements CustomPacketPayload{
  public static final Type<View> TYPE=new Type<>(ResourceLocation.fromNamespaceAndPath("zianmobs","view"));
  public static final StreamCodec<RegistryFriendlyByteBuf,View> CODEC=new StreamCodec<>(){public View decode(RegistryFriendlyByteBuf b){return new View(b.readUtf(131072));}public void encode(RegistryFriendlyByteBuf b,View v){b.writeUtf(v.json,131072);}};public Type<View> type(){return TYPE;}
 }
 public record PetAction(UUID token,String operation,String json) implements CustomPacketPayload{
  public static final Type<PetAction> TYPE=new Type<>(ResourceLocation.fromNamespaceAndPath("zianmobs","companion_action"));
  public static final StreamCodec<RegistryFriendlyByteBuf,PetAction> CODEC=new StreamCodec<>(){public PetAction decode(RegistryFriendlyByteBuf b){return new PetAction(b.readUUID(),b.readUtf(24),b.readUtf(8192));}public void encode(RegistryFriendlyByteBuf b,PetAction v){b.writeUUID(v.token);b.writeUtf(v.operation,24);b.writeUtf(v.json,8192);}};public Type<PetAction> type(){return TYPE;}
 }
 public static void register(IEventBus bus){bus.addListener((RegisterPayloadHandlersEvent e)->{var channel=e.registrar("mobs-ux-12");channel.playToServer(Request.TYPE,Request.CODEC,(v,c)->c.enqueueWork(()->{if(c.player() instanceof ServerPlayer p){if(v.section().equals("my_rewards")){PersonalRewards.open(p);return;}if(v.section().startsWith("companion")&&Companions.current!=null)Companions.current.request(p,v.section(),v.id());else if(admin!=null)admin.request(p,v.section(),v.id());}}));channel.playToServer(Action.TYPE,Action.CODEC,(v,c)->c.enqueueWork(()->{if(c.player() instanceof ServerPlayer p&&admin!=null){if(PersonalRewards.handles(p,v.token()))PersonalRewards.claim(p,v.token(),v.operation(),v.json());else admin.action(p,v);}}));channel.playToServer(PetAction.TYPE,PetAction.CODEC,(v,c)->c.enqueueWork(()->{if(c.player() instanceof ServerPlayer p&&Companions.current!=null)Companions.current.action(p,v);}));channel.playToClient(View.TYPE,View.CODEC,(v,c)->c.enqueueWork(()->com.ianblk.zianmobs.client.ZoneLines.receive(v.json())));});}
 static void send(ServerPlayer player,JsonView view){String json=view.json();if(json.length()>131072)throw new IllegalArgumentException("Vista demasiado grande");PacketDistributor.sendToPlayer(player,new View(json));}
 record JsonView(String json){}
}
