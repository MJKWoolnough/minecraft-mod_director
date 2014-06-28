package mw.director;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.Player;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class Commands implements ICommand, IScheduledTickHandler  {
	
	public static final HashMap<Integer, CommandsEngine> engines = new HashMap<Integer, CommandsEngine>();
	
	private int tickGap = 0;
	
	//ICommand
	
	@Override
	public int compareTo(Object o) {
		return o instanceof DirectorMod ? 0 : 1;
	}

	@Override
	public String getCommandName() {
		return "director";
	}

	@Override
	public String getCommandUsage(ICommandSender ics) {
		return "/dir[ector] start | stop | /path/to/file.js\n";
	}

	@Override
	public List getCommandAliases() {
		return (List) Arrays.asList("dir");
	}

	@Override
	public void processCommand(ICommandSender ics, String[] args) {
		EntityPlayer player = (EntityPlayer) ics;
		if (player == null) {
			return;
		}
		CommandsEngine thisEngine = this.engines.get(player.entityId);
		String message = ""; 
		if (args.length > 0) {
			if (args[0].equals("start")) {
				if (thisEngine == null) {
					message = "No file loaded";
				} else if(!thisEngine.start()) {
					message = "No actions to start";
				}
				return;
			} else if (args[0].equals("stop")) {
				if (thisEngine == null) {
					message = "No actions to stop";
				} else if (thisEngine.stop()) {
					message = "Actions stopped";
				} else {
					message = "Actions already stopped";
				}
			} else {
			String jsFile = "";
			for (int i = 0; i < args.length; i++) {
				if (i > 1) {
					jsFile += " ";
				}
				jsFile += args[i];
			}
			PacketHandler.sendJSFileRequest((Player) player, jsFile);
			message = "Loading file \"" + jsFile + "\"";
			}
		}
		if (message.length() == 0) {
			message = this.getCommandUsage(ics);
		}
		player.addChatMessage(message);
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender ics) {
		if (ics instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) ics;
			return player.capabilities.isCreativeMode;
		}
		return false;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender icommandsender, String[] astring) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] astring, int i) {
		return false;
	}

	//IScheduledTickHandler

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.SERVER);
	}

	@Override
	public String getLabel() {
		return "DirectorMod";
	}

	@Override
	public int nextTickSpacing() {
		int oldTickGap = this.tickGap;
		this.tickGap = 20;
		for(Integer dce : this.engines.keySet()) {
			int gap = this.engines.get(dce).nextTickSpacing(oldTickGap);
			if (gap == -1) {
				this.engines.remove(dce);
			} else if (gap < this.tickGap) {
				this.tickGap = gap;
			}
		}
		return this.tickGap;
	}

}
