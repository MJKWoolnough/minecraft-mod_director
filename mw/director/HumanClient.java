package mw.director;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class HumanClient extends AbstractClientPlayer {

	public HumanClient(World world) {
		super(world, "Steve");
	}

	@Override
	public String getTranslatedEntityName() {
		return "";
	}
	
	@Override
	public void sendChatToPlayer(ChatMessageComponent chatmessagecomponent) {}

	@Override
	public boolean canCommandSenderUseCommand(int i, String s) {
		return false;
	}

	@Override
	public ChunkCoordinates getPlayerCoordinates() {
		return new ChunkCoordinates(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY + 0.5D), MathHelper.floor_double(this.posZ));
	}

}
