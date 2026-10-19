package com.ianblk.zianmobs.client;
import com.ianblk.zianmobs.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.InteractionHand;
import java.util.*;
/** Local-only preview using native wide/slim PlayerRenderers and the actual armor layer. */
public final class TitanArmorPreviewScreen extends ManagerThemedScreen {
 private final RemotePlayer wide,slim;private final int pose;
 private static RemotePlayer avatar(boolean thin){var mc=Minecraft.getInstance();return new RemotePlayer(mc.level,new com.mojang.authlib.GameProfile(UUID.randomUUID(),thin?"AlexFit":"SteveFit")){@Override public PlayerSkin getSkin(){return new PlayerSkin(ResourceLocation.withDefaultNamespace("textures/entity/player/"+(thin?"slim/alex.png":"wide/steve.png")),null,null,null,thin?PlayerSkin.Model.SLIM:PlayerSkin.Model.WIDE,true);}};}
 public TitanArmorPreviewScreen(int pose){super(Component.translatableWithFallback("ui.zianmobs.2e6ee3a21e5b", "Thanos · ajuste de brazos"));this.pose=pose;wide=avatar(false);slim=avatar(true);for(var player:List.of(wide,slim)){for(var armor:CompanionContent.ARMOR.values()){var item=(ArmorItem)armor.get();var stack=item.getDefaultInstance();stack.set(net.minecraft.core.component.DataComponents.ENCHANTMENT_GLINT_OVERRIDE,true);player.setItemSlot(item.getEquipmentSlot(),stack);}if(pose==1){player.setShiftKeyDown(true);player.setPose(Pose.CROUCHING);}if(pose==2){player.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(Items.BOW));player.startUsingItem(InteractionHand.MAIN_HAND);}}checkFit();}
 private void checkFit(){var original=new net.minecraft.client.model.HumanoidModel<LivingEntity>(net.minecraft.client.model.geom.builders.LayerDefinition.create(net.minecraft.client.model.HumanoidModel.createMesh(new net.minecraft.client.model.geom.builders.CubeDeformation(1),0),64,32).bakeRoot());original.setAllVisible(false);original.body.visible=true;original.rightArm.visible=true;original.leftArm.visible=true;original.rightArm.xRot=.8f;var item=CompanionContent.ARMOR.get("titan_chestplate").get().getDefaultInstance();var hook=net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(item);if(!(hook instanceof TitanArmorFit))throw new IllegalStateException("Titan armor fit extension not registered");for(int n=0;n<2;n++){var adapted=hook.getGenericArmorModel(slim,item,EquipmentSlot.CHEST,original);hook.setupModelAnimations(slim,item,EquipmentSlot.CHEST,adapted,0,0,0,0,0,0);if(!(adapted instanceof net.minecraft.client.model.HumanoidModel<?> fitted)||Math.abs(fitted.rightArm.xScale-5f/6f)>.0001||fitted.rightArm.xRot!=original.rightArm.xRot||!fitted.body.visible||fitted.head.visible)throw new IllegalStateException("Slim fit lost pose, visibility or arm width");if(hook.getGenericArmorModel(wide,item,EquipmentSlot.CHEST,original)!=original||original.rightArm.xScale!=1||original.rightArm.x!=-5)throw new IllegalStateException("Slim armor mutated shared wide armor model");}if(hook.getGenericArmorModel(slim,item,EquipmentSlot.HEAD,original)!=original)throw new IllegalStateException("Slim chest adjustment affected other slots");}
 @Override protected void init(){fitViewport(680,430);}
 @Override protected void renderCanvas(GuiGraphics g,int mx,int my,float partial){int x=(width-640)/2,y=(height-400)/2;panel(g,x,y,640,400);g.drawCenteredString(font,"Brazos anchos",x+170,y+43,0xFFF2C14E);g.drawCenteredString(font,"Brazos delgados",x+470,y+43,0xFFF2C14E);g.drawCenteredString(font,new String[]{"De pie · texturas originales","Agachado · articulación nativa","Arco · postura de combate"}[pose],x+320,y+375,0xFFBBBBBB);renderAvatar(g,wide,x+170,y+210);renderAvatar(g,slim,x+470,y+210);renderWidgets(g,mx,my,partial);}
 private static void renderAvatar(GuiGraphics g,RemotePlayer player,int x,int y){player.yBodyRot=177;player.setYRot(175);player.yHeadRot=175;player.yHeadRotO=175;InventoryScreen.renderEntityInInventory(g,x,y,115,new org.joml.Vector3f(0,player.getBbHeight()/2+.02f,0),new org.joml.Quaternionf().rotateZ((float)Math.PI),new org.joml.Quaternionf(),player);}
 @Override public boolean isPauseScreen(){return false;}
}
