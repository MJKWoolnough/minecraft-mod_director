package mw.director;

import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicLong;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import cpw.mods.fml.common.FMLLog;

import net.minecraft.entity.player.EntityPlayer;

public class CommandsEngine {

	private boolean engineRunning = false;
	private final AtomicLong tickCounter = new AtomicLong();
	private final PriorityQueue<APITimer> timers = new PriorityQueue<APITimer>();
	private final API api;
	
	public CommandsEngine(EntityPlayer player, String jsFileData) {
		ScriptEngine jsEngine = new ScriptEngineManager().getEngineByName("javascript");
		if (jsEngine == null) {
		player.addChatMessage("Could not load javascript engine.");
    		FMLLog.severe("Could not load javascript engine.");
    		this.api = null;
    		return;
    	}
		this.api = new API(player, this);
    	jsEngine.put("api", this.api);
    	try {
        	if (Compilable.class.isAssignableFrom(jsEngine.getClass())) {
        		Compilable c = (Compilable) jsEngine;
        		CompiledScript cs;
				cs = c.compile(jsFileData);
        		cs.eval();
        	} else {
        		jsEngine.eval(jsFileData);
        	}
    	} catch (ScriptException e) {
    		player.addChatMessage("Could not fully parse (" + e.getMessage() + ")");
    		return;
    	}
    	player.addChatMessage("Engine ready");
	}
	
	public boolean start() {
		if (this.timers.size() > 0) {
			this.engineRunning = true;
			return true;
		}
		return false;
	}
	
	public boolean stop() {
		if (this.engineRunning) { 
			this.engineRunning = false;
			return true;
		}
		return false;
	}
	
	public int nextTickSpacing(int tickGap) {
		if (this.engineRunning) {
			long thisTick = this.tickCounter.addAndGet(tickGap);
			for (APITimer at = this.timers.peek(); at != null; at = this.timers.peek()) {
				if (!at.ready(thisTick)) {
					return at.tickGap(thisTick);
				}
				this.timers.poll();
				at.run(thisTick);
				if (at.repeat()) {
					at.update(thisTick);
					this.timers.offer(at);
				}
			}
			this.api.camera.reset();
			this.engineRunning = false;
			return -1;
		}
		return 20;
	}

	public APIWaiter newWaiter(IAPICallback function) {
		APIWaiter a = new APIWaiter(this, function);
		a.update(this.tickCounter.get());
		this.timers.offer(a);
		return a;
	}
	
	public APITimer newTicker(IAPICallback function, int ticks, boolean repeat) {
		APITimer a = new APITimer(this, ticks, function, repeat);
		a.update(this.tickCounter.get());
		this.timers.offer(a);
		return a;
	}
	
	public void removeTicker(APITimer a) {
		this.timers.remove(a);
	}
}
