package net.runelite.client.plugins.multicombatlines;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(
	keyName = "multicombatLines",
	name = "Multicombat lines",
	description = "Configuration for the multicombat lines plugin"
)
public interface MulticombatLinesConfig extends Config
{
	@ConfigItem(
		keyName = "outlineColor",
		name = "Color to outline with",
		description = "Choose color to use for the lines",
		position = 1
	)
	default Color outlineColor()
	{
		return new Color(200, 200, 200);
	}

	@ConfigItem(
		keyName = "onlyPvpLines",
		name = "Only show lines in PvP",
		description = "Configure if you want to only see multicombat lines in PvP",
		position = 2
	)
	default boolean onlyShowLinesInPvp()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showMinimapLines",
		name = "Show lines on minimap",
		description = "Configure if you also want the multicombat lines to show on the minimap",
		position = 3
	)
	default boolean showMinimapLines()
	{
		return true;
	}

	@ConfigItem(
		keyName = "testCollision",
		name = "Test collision",
		description = "Only show lines where they can be walked through",
		position = 4
	)
	default boolean testCollision()
	{
		return true;
	}
}
