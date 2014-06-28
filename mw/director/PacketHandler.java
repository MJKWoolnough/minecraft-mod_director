package mw.director;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {

	public static final String CHANNEL = "directormod";
	
	private static final byte JSFILEREQUEST = 3;
	private static final byte JSFILE = 4;
	
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		EntityPlayer entityPlayer = (EntityPlayer) player;
		if (entityPlayer != null && entityPlayer.capabilities.isCreativeMode) {
			World world = entityPlayer.worldObj;
			ByteArrayDataInput in = ByteStreams.newDataInput(packet.data);
			int packetId = in.readUnsignedByte();
			try {
				switch (packetId) {
				case JSFILEREQUEST:
					this.handleJSFileRequest(in);
					break;
				case JSFILE:
					this.handleJSFile(entityPlayer, in);
					break;
				}
			} catch(Exception e) {}
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
		ByteArrayOutputStream bos = new ByteArrayOutputStream(1 + 1 + 2 + content.length());
		DataOutputStream os = new DataOutputStream(bos);
		try {
			os.writeByte(JSFILE);
			os.writeByte(status);
			os.writeUTF(content);
		} catch (IOException e) {
			return;
		}
		PacketHandler.sendPacket(bos, false, null);
	}

	private void handleJSFile(EntityPlayer entityPlayer, ByteArrayDataInput in) {
		if (in.readByte() == 0) { 
			Commands.engines.put(entityPlayer.entityId, new CommandsEngine(entityPlayer, in.readUTF()));
		} else {
			entityPlayer.addChatMessage("Failed to load file " + in.readUTF());
		}
		
	}

	private static void sendPacket(ByteArrayOutputStream bos, boolean toPlayer, Player player) {
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = CHANNEL;
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		if (toPlayer) {
			if (player == null) {
				PacketDispatcher.sendPacketToAllPlayers(packet);
			} else {
				PacketDispatcher.sendPacketToPlayer(packet, player);				
			}
		} else {
			PacketDispatcher.sendPacketToServer(packet);
		}
	}

	public static void sendJSFileRequest(Player player, String jsFile) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(3 + jsFile.length());
		DataOutputStream os = new DataOutputStream(bos);
		try {
			os.writeByte(JSFILEREQUEST);
			os.writeUTF(jsFile);
		} catch (IOException e) {
			return;
		}
		PacketHandler.sendPacket(bos, true, player);
	}
}
