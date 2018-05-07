package net.runelite.api.coords;

import java.awt.Polygon;
import java.awt.geom.Area;

public class MapLocations
{
	public static final Area MULTI = new Area();
	public static final Area ROUGH_WILDERNESS = new Area();

	static
	{
		// ----- Multicombat areas -----
		// Main Wilderness
		addPolygon(MULTI,
			3200, 3968,
			3392, 3968,
			3392, 4096,
			3328, 4096,
			3328, 3520,
			3136, 3520,
			3136, 3648,
			3192, 3648,
			3192, 3752,
			3152, 3752,
			3152, 3840,
			3136, 3840,
			3136, 3872,
			3112, 3872,
			3112, 3880,
			3072, 3880,
			3072, 3896,
			3048, 3896,
			3048, 3872,
			3056, 3872,
			3056, 3864,
			3048, 3864,
			3048, 3856,
			3008, 3856,
			3008, 3904,
			3200, 3904);

		// South of wildy agility training arena
		addPolygon(MULTI,
			2984, 3928,
			3008, 3928,
			3008, 3912,
			2984, 3912);

		// Wildy zamorak temple
		addPolygon(MULTI,
			2944, 3832,
			2960, 3832,
			2960, 3816,
			2944, 3816);

		// Wildy bandit camp
		addPolygon(MULTI,
			3008, 3712,
			3072, 3712,
			3072, 3600,
			3008, 3600);

		// Chaos temple north of Falador
		addPolygon(MULTI,
			2928, 3520,
			2944, 3520,
			2944, 3512,
			2928, 3512);

		// Burthorpe
		addPolygon(MULTI,
			2880, 3544,
			2904, 3544,
			2904, 3520,
			2880, 3520);

		// White Wolf Mountain
		addPolygon(MULTI,
			2816, 3520,
			2560, 3520,
			2560, 3456,
			2816, 3456);

		// Death Plateu
		addPolygon(MULTI,
			2848, 3608,
			2880, 3608,
			2880, 3600,
			2848, 3600);

		// Trollheim/Godwars
		addPolygon(MULTI,
			2880, 3776,
			2912, 3776,
			2912, 3696,
			2920, 3696,
			2920, 3688,
			2896, 3688,
			2896, 3696,
			2880, 3696,
			2880, 3728,
			2888, 3728,
			2888, 3774,
			2880, 3774);

		// Northen Rellekka
		addPolygon(MULTI,
			2656, 3736,
			2736, 3736,
			2736, 3760,
			2656, 3760);

		// Northen Fremennik Isles
		addPolygon(MULTI,
			2304, 3904,
			2432, 3904,
			2432, 3840,
			2368, 3840,
			2368, 3816,
			2352, 3816,
			2352, 3824,
			2304, 3824);

		// Pirates Cove
		addPolygon(MULTI,
			2176, 3840,
			2240, 3840,
			2240, 3776,
			2176, 3776);

		// Lunar Isle
		addPolygon(MULTI,
			2048, 3968,
			2176, 3968,
			2176, 3840,
			2048, 3840);

		// Piscatoris Fishing Colony
		addPolygon(MULTI,
			2304, 3712,
			2368, 3712,
			2368, 3648,
			2304, 3648);

		// Ranging Guild
		addPolygon(MULTI,
			2656, 3448,
			2680, 3448,
			2680, 3440,
			2688, 3440,
			2688, 3416,
			2680, 3416,
			2680, 3408,
			2656, 3408,
			2656, 3416,
			2648, 3416,
			2648, 3440,
			2656, 3440);

		// Necromancer house, southeast of Ardy
		addPolygon(MULTI,
			2656, 3256,
			2680, 3256,
			2680, 3216,
			2664, 3216,
			2664, 3232,
			2656, 3232);

		// Battlefield noth of Tree Gnome Village
		addPolygon(MULTI,
			2504, 3248,
			2544, 3248,
			2544, 3232,
			2552, 3232,
			2552, 3208,
			2504, 3208);

		// Castle Wars
		addPolygon(MULTI,
			2368, 3136,
			2432, 3136,
			2432, 3072,
			2368, 3072);

		// Jiggig
		addPolygon(MULTI,
			2456, 3056,
			2496, 3056,
			2496, 3032,
			2456, 3032);

		// Ape Atoll
		addPolygon(MULTI,
			2688, 2816,
			2816, 2816,
			2816, 2688,
			2688, 2688);

		// Pest Control
		addPolygon(MULTI,
			2624, 2624,
			2688, 2624,
			2688, 2560,
			2624, 2560);

		// Desert Bandit Camp
		addPolygon(MULTI,
			3152, 3000,
			3192, 3000,
			3192, 2960,
			3152, 2960);

		// Al Kharid
		addPolygon(MULTI,
			3264, 3200,
			3328, 3200,
			3328, 3136,
			3264, 3136);

		// Wizards Tower
		addPolygon(MULTI,
			3094, 3176,
			3126, 3176,
			3126, 3144,
			3094, 3144);

		// Draynor Village
		addPolygon(MULTI,
			3112, 3264,
			3136, 3264,
			3136, 3232,
			3104, 3232,
			3104, 3256,
			3112, 3256);

		// Falador
		addPolygon(MULTI,
			2944, 3456,
			3008, 3456,
			3008, 3328,
			3016, 3328,
			3016, 3304,
			2944, 3304);

		// Barbarian Village
		addPolygon(MULTI,
			3072, 3456,
			3136, 3456,
			3136, 3392,
			3048, 3392,
			3048, 3408,
			3056, 3408,
			3056, 3440,
			3064, 3440,
			3064, 3448,
			3072, 3448);

		// ----- Wildy -----
		addPolygon(ROUGH_WILDERNESS,
			3944, 3523,
			3392, 3523,
			3392, 3075,
			3944, 3075);
	}

	private static void addPolygon(Area area, int... coords)
	{
		assert coords.length % 2 == 0;

		Polygon poly = new Polygon();
		for (int i = 0; i < coords.length; i += 2)
		{
			poly.addPoint(coords[i], coords[i + 1]);
		}
		area.add(new Area(poly));
	}
}
