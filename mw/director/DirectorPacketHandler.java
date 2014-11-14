package mw.director;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import mw.library.PacketData;
import mw.library.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public class DirectorPacketHandler extends PacketHandler {

	public static final String CHANNEL = "directormod";
	
	private static final byte JSFILEREQUEST = 0;
	private static final byte JSFILE = 1;
	private static final byte SETPARTROTATION = 2;
	private static final byte SETPARTPOSITION = 3;
	private static final byte SETSCALE = 4;
	private static final byte SETSKIN = 5;
	private static final byte SETLABEL = 6;
	private static final byte SETSPEED = 7;
	private static final byte SETCAMERA = 8;
	private static final byte SETHURT = 9;
	private static final byte SETBOBBING = 10;
	private static final byte SETFOV = 11;
	private static final byte SHOWGUI = 12;
	
	private static DirectorPacketHandler instance;
	
	public DirectorPacketHandler() {
		super(CHANNEL);
		this.instance = this;
	}
	
	@Override
	public void handlePacket(byte packetId, ByteArrayDataInput in, Player player) {
		EntityPlayer entityPlayer = (EntityPlayer) player;
		if (entityPlayer != null && entityPlayer.capabilities.isCreativeMode) {
			World world = entityPlayer.worldObj;
			switch (packetId) {
			case JSFILEREQUEST:
				this.handleJSFileRequest(in);
				break;
			case JSFILE:
				this.handleJSFile(entityPlayer, in);
				break;
			case SETPARTROTATION:
				this.handleSetPartRotation(world, in);
				break;
			case SETPARTPOSITION:
				this.handleSetPartPosition(world, in);
				break;
			case SETSCALE:
				this.handleSetScale(world, in);
				break;
			case SETSKIN:
				this.handleSetSkin(world, in);
				break;
			case SETLABEL:
				this.handleSetLabel(world, in);
				break;
			case SETSPEED:
				this.handleSetSpeed(world, in);
				break;
			case SETCAMERA:
				this.handleSetCamera(world, in);
				break;
			case SETHURT:
				this.handleSetHurt(world, in);
				break;
			case SETBOBBING:
				this.handleSetBobbing(in);
				break;
			case SETFOV:
				this.handleSetFOV(in);
				break;
			case SHOWGUI:
				this.handleShowGUI(in);
				break;
			}
		}
	}
	
	private void handleJSFileRequest(ByteArrayDataInput in) {
		String jsFile = in.readUTF();
		if (jsFile.startsWith("~")) {
			jsFile = System.getProperty("user.home").concat(jsFile.substring(1));
		}
		int status = 0;
		String content;
		try {
			content = new Scanner(new File(jsFile), "UTF-8").useDelimiter("\\A").next();
		} catch (FileNotFoundException e) {
			content = jsFile;
			status = 1;
		}
		PacketData pd = new PacketData(1 + 1 + (2 + content.length()));
		try {
			pd.writeByte(JSFILE);
			pd.writeByte(status);
			pd.writeUTF(content);
		} catch (IOException e) {
			return;
		}
		DirectorPacketHandler.instance.sendPacket(pd, false, null);
	}

	private void handleJSFile(EntityPlayer player, ByteArrayDataInput in) {
		if (in.readByte() == 0) { 
			Commands.engines.put(player.entityId, new CommandsEngine(player, in.readUTF()));
		} else {
			player.addChatMessage("Failed to load file " + in.readUTF());
		}
	}
	
	private void handleSetPartRotation(World world, ByteArrayDataInput in) {
		int directorEntityId = in.readInt();
		int partId = in.readInt();
		int pyr = in.readByte();
		float start = in.readFloat();
		float end = in.readFloat();
		int ticks = in.readInt();
		EntityDirector director = this.getDirector(world, directorEntityId);
		if (director == null) {
			return;
		}
		director.aod.addOverride(partId, pyr, start, end, ticks);
	}
	
	private void handleSetPartPosition(World world, ByteArrayDataInput in) {
		int directorEntityId = in.readInt();
		int partId = in.readInt();
		int xyz = in.readByte();
		float start = in.readFloat();
		float end = in.readFloat();
		int ticks = in.readInt();
		EntityDirector director = this.getDirector(world, directorEntityId);
		if (director == null) {
			return;
		}
		director.pod.addOverride(partId, xyz, start, end, ticks);
	}
	
	private void handleSetScale(World world, ByteArrayDataInput in) {
		int directorEntityId = in.readInt();
		int whd = in.readByte();
		float start = in.readFloat();
		float end = in.readFloat();
		int ticks = in.readInt();
		EntityDirector director = this.getDirector(world, directorEntityId);
		if (director == null) {
			return;
		}
		OverrideScale scod = director.scod;
		if (whd > 3) {
			scod.addOverride(0, 0, start, end, ticks);
			scod.addOverride(0, 1, start, end, ticks);
			scod.addOverride(0, 2, start, end, ticks);
		} else {
			scod.addOverride(0, whd, start, end, ticks);
		}
	}
	
	private void handleSetSkin(World world, ByteArrayDataInput in) {
		int directorEntityId = in.readInt();
		String skin = in.readUTF();
		EntityDirector director = this.getDirector(world, directorEntityId);
		if (director == null) {
			return;
		}
		director.setSkin(skin);
	}
	
	private void handleSetLabel(World world, ByteArrayDataInput in) {
		int directorEntityId = in.readInt();
		String label = in.readUTF();
		EntityDirector director = this.getDirector(world, directorEntityId);
		if (director == null) {
			return;
		}
		director.setLabel(label);
	}
	
	private void handleSetSpeed(World world, ByteArrayDataInput in) {
		int directorEntityId = in.readInt();
		int xyz = in.readByte();
		float start = in.readFloat();
		float stop = in.readFloat();
		int ticks = in.readInt();
		EntityDirector director = this.getDirector(world, directorEntityId);
		if (director == null) {
			return;
		}
		director.sod.addOverride(0, xyz, start, stop, ticks);
	}
	
	private void handleSetCamera(World world, ByteArrayDataInput in) {
		int directorEntityId = in.readInt();
		EntityDirector director = this.getDirector(world, directorEntityId);
		Minecraft mc = Minecraft.getMinecraft();
		mc.entityRenderer.camRoll = 0;
		if (director == null) {
			mc.renderViewEntity = mc.thePlayer;
		} else {
			mc.renderViewEntity = director;
		}
	}
	
	private void handleSetHurt(World world, ByteArrayDataInput in) {
		int directorEntityId = in.readInt();
		EntityDirector director = this.getDirector(world, directorEntityId);
		if (director == null) {
			return;
		}
		director.performHurtAnimation();
	}
	
	private void handleSetBobbing(ByteArrayDataInput in) {
		Minecraft.getMinecraft().gameSettings.viewBobbing = in.readBoolean();
	}
	
	private void handleSetFOV(ByteArrayDataInput in) {
		Minecraft.getMinecraft().gameSettings.fovSetting = in.readFloat();
	}
	
	private void handleShowGUI(ByteArrayDataInput in) {
		Minecraft.getMinecraft().gameSettings.hideGUI = !in.readBoolean();
	}
	
	private EntityDirector getDirector(World world, int directorEntityId) {
		return (EntityDirector) world.getEntityByID(directorEntityId);
	}

	public static void sendJSFileRequest(Player player, String jsFile) {
		PacketData pd = new PacketData(1 + (2 + jsFile.length()));
		try {
			pd.writeByte(JSFILEREQUEST);
			pd.writeUTF(jsFile);
		} catch (IOException e) {
			return;
		}
		DirectorPacketHandler.instance.sendPacket(pd, true, player);
	}
	
	public static void sendPartRotation(int directorEntityId, int partId, int pyr, float start, float end, int ticks) {
		PacketData pd = new PacketData(1 + 4 + 4 + 1 + 4 + 4 + 4);
		try {
			pd.writeByte(SETPARTROTATION);
			pd.writeInt(directorEntityId);
			pd.writeInt(partId);
			pd.writeByte(pyr);
			pd.writeFloat(start);
			pd.writeFloat(end);
			pd.writeInt(ticks);
		} catch (IOException e) {
			return;
		}
		DirectorPacketHandler.instance.sendPacket(pd, true);
	}
	
	public static void sendPartPosition(int directorEntityId, int partId, int xyz, float start, float end, int ticks) {
		PacketData pd = new PacketData(1 + 4 + 4 + 1 + 4 + 4 + 4);
		try {
			pd.writeByte(SETPARTPOSITION);
			pd.writeInt(directorEntityId);
			pd.writeInt(partId);
			pd.writeByte(xyz);
			pd.writeFloat(start);
			pd.writeFloat(end);
			pd.writeInt(ticks);
		} catch (IOException e) {
			return;
		}
		DirectorPacketHandler.instance.sendPacket(pd, true);
	}
	
	public static void sendSetScale(int directorEntityId, int whd, float start, float end, int ticks) {
		PacketData pd = new PacketData(1 + 4 + 1 + 4 + 4 + 4);
		try {
			pd.writeByte(SETSCALE);
			pd.writeInt(directorEntityId);
			pd.writeByte(whd);
			pd.writeFloat(start);
			pd.writeFloat(end);
			pd.writeInt(ticks);
		} catch (IOException e) {
			return;
		}
		DirectorPacketHandler.instance.sendPacket(pd, true);
	}
	
	public static void sendSetSkin(int directorEntityId, String skin) {
		PacketData pd = new PacketData(1 + 4 + (2 + skin.length()));
		try {
			pd.writeByte(SETSKIN);
			pd.writeInt(directorEntityId);
			pd.writeUTF(skin);
		} catch (IOException e) {
			return;
		}
		DirectorPacketHandler.instance.sendPacket(pd, true);
	}
	
	public static void sendSetLabel(int directorEntityId, String label) {
		PacketData pd = new PacketData(1 + 4 + (2 + label.length()));
		try {
			pd.writeByte(SETLABEL);
			pd.writeInt(directorEntityId);
			pd.writeUTF(label);
		} catch (IOException e) {
			return;
		}
		DirectorPacketHandler.instance.sendPacket(pd, true);
	}

	public static void sendSetSpeed(int directorEntityId, int xyz, float start, float stop, int ticks) {
		PacketData pd = new PacketData(1 + 4 + 1 + 4 + 4 + 4);
		try {
			pd.writeByte(SETSPEED);
			pd.writeInt(directorEntityId);
			pd.writeByte(xyz);
			pd.writeFloat(start);
			pd.writeFloat(stop);
			pd.writeInt(ticks);
		} catch (IOException e) {
			return;
		}
		DirectorPacketHandler.instance.sendPacket(pd, true);
	}
	
	public static void sendSetCamera(Player player, int directorEntityId) {
		PacketData pd = new PacketData(1 + 4);
		try {
			pd.writeByte(SETCAMERA);
			pd.writeInt(directorEntityId);
		} catch (IOException e) {
			return;
		}
		DirectorPacketHandler.instance.sendPacket(pd, true, player);
	}

	public static void sendSetHurt(int directorEntityId) {
		PacketData pd = new PacketData(1 + 4);
		try {
			pd.writeByte(SETHURT);
			pd.writeInt(directorEntityId);
		} catch (IOException e) {
			return;
		}
		DirectorPacketHandler.instance.sendPacket(pd, true);
	}

	public static void sendSetBobbing(Player player, boolean bob) {
		PacketData pd = new PacketData(1 + 1);
		try {
			pd.writeByte(SETBOBBING);
			pd.writeBoolean(bob);
		} catch (IOException e) {
			return;
		}
		DirectorPacketHandler.instance.sendPacket(pd, true, player);
	}
	
	public static void sendSetFOV(Player player, float fov) {
		PacketData pd = new PacketData(1 + 4);
		try {
			pd.writeByte(SETFOV);
			pd.writeFloat(fov);
		} catch (IOException e) {
			return;
		}
		DirectorPacketHandler.instance.sendPacket(pd, true, player);
	}
	
	public static void sendShowGUI(Player player, boolean show) {
		PacketData pd = new PacketData(1 + 1);
		try {
			pd.writeByte(SHOWGUI);
			pd.writeBoolean(show);
		} catch (IOException e) {
			return;
		}
		DirectorPacketHandler.instance.sendPacket(pd, true, player);
	}
}
