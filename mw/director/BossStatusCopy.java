package mw.director;

import net.minecraft.entity.boss.BossStatus;

public class BossStatusCopy {

	private static float	healthScale;
	private static int	statusBarLength;
	private static String	bossName;
	private static boolean	field_82825_d;

	public static void backup() {
		healthScale = BossStatus.healthScale;
		statusBarLength = BossStatus.statusBarLength;
		bossName = BossStatus.bossName;
		field_82825_d = BossStatus.field_82825_d;
	}

	public static void restore() {
		BossStatus.healthScale = healthScale;
		BossStatus.statusBarLength = statusBarLength;
		BossStatus.bossName = bossName;
		BossStatus.field_82825_d = field_82825_d;
	}
}
