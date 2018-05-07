package net.runelite.client.plugins.multicombatlines;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Provides;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.Constants;
import net.runelite.api.GameState;
import net.runelite.api.Perspective;
import net.runelite.api.Tile;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.MapLocations;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ConfigChanged;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.geometry.Geometry;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.Overlay;

@PluginDescriptor(
	name = "Multicombat lines"
)
public class MulticombatLinesPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private MulticombatLinesConfig config;

	@Inject
	private MulticombatLinesOverlay overlay;

	@Inject
	private MulticombatLinesMinimapOverlay minimapOverlay;

	@Getter
	private List<Area> areasToDisplay;

	@Getter
	private GeneralPath pathToDisplay;

	@Provides
	MulticombatLinesConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(MulticombatLinesConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		this.areasToDisplay = new ArrayList<>();
		if (client.getGameState() == GameState.LOGGED_IN)
		{
			findLinesInScene();
		}
		overlay.updateColor();
		minimapOverlay.updateColor();
	}

	@Override
	protected void shutDown() throws Exception
	{
		this.areasToDisplay = null;
	}

	@Override
	public Collection<Overlay> getOverlays()
	{
		return Arrays.asList(overlay, minimapOverlay);
	}

	/*
	private List<WorldLine> splitLineBasedOnCollision(WorldLine line)
	{
		List<WorldLine> result = new ArrayList<>();

		// We don't want to draw lines over water etc so we
		// only want the parts of the line that don't go over that

		int startX = line.getStart().getX();
		int startY = line.getStart().getY();
		int endX = line.getEnd().getX();
		int endY = line.getEnd().getY();
		if (startX > endX)
		{
			int temp = endX;
			endX = startX;
			startX = temp;
		}
		if (startY > endY)
		{
			int temp = endY;
			endY = startY;
			startY = temp;
		}

		startX = Math.max(startX, client.getBaseX());
		startY = Math.max(startY, client.getBaseY());
		endX = Math.min(endX, client.getBaseX() + Constants.REGION_SIZE);
		endY = Math.min(endY, client.getBaseY() + Constants.REGION_SIZE);

		int lineDx = Integer.signum(endX - startX);
		int lineDy = Integer.signum(endY - startY);
		int x = startX;
		int y = startY;

		int nextLineStartX = startX;
		int nextLineStartY = startY;
		while (x < endX || y < endY)
		{
			WorldArea area1 = new WorldArea(new WorldPoint(x, y, client.getPlane()), 1, 1);
			WorldArea area2 = new WorldArea(new WorldPoint(x - lineDy, y - lineDx, client.getPlane()), 1, 1);
			if (!area1.canTravelInDirection(client, -lineDy, -lineDx) ||
				!area2.canTravelInDirection(client, lineDy, lineDx))
			{
				if (nextLineStartX != x || nextLineStartY != y)
				{
					result.add(new WorldLine(
						new WorldPoint(nextLineStartX, nextLineStartY, client.getPlane()),
						new WorldPoint(x, y, client.getPlane())));
				}
				nextLineStartX = x + lineDx;
				nextLineStartY = y + lineDy;
			}

			x += lineDx;
			y += lineDy;
		}

		if (nextLineStartX != x || nextLineStartY != y)
		{
			result.add(new WorldLine(
				new WorldPoint(nextLineStartX, nextLineStartY, client.getPlane()),
				new WorldPoint(x, y, client.getPlane())));
		}

		return result;
	}*/

	private void findLinesInScene()
	{
		Rectangle currentScene = new Rectangle(
			client.getBaseX() + 1, client.getBaseY() + 1,
			Constants.REGION_SIZE - 2, Constants.REGION_SIZE - 2);
		Area area = new Area(currentScene);
		area.intersect(MapLocations.MULTI);
		if (config.onlyShowLinesInPvp())
		{
			area.intersect(MapLocations.ROUGH_WILDERNESS);
		}
		GeneralPath lines = new GeneralPath(area);
		lines = Geometry.unitifyPath(lines, 1);
		/*
		lines = Geometry.transformPath(lines, coords ->
		{
			coords[0] = Math.round(coords[0]);
			coords[1] = Math.round(coords[1]);
		});
		*/
		if (config.testCollision())
		{
			lines = Geometry.filterPath(lines, (p1, p2) ->
			{
				WorldArea wa1 = new WorldArea(new WorldPoint(
					(int)p1[0], (int)p1[1], client.getPlane()), 1, 1);
				WorldArea wa2 = new WorldArea(new WorldPoint(
					(int)p2[0], (int)p2[1], client.getPlane()), 1, 1);
				return
					wa1.canTravelInDirection(client, (int)(p2[0] - p1[0]), (int)(p2[1] - p1[1])) ||
					wa2.canTravelInDirection(client, (int)(p1[0] - p2[0]), (int)(p1[1] - p2[1]));
			});
		}

		lines = Geometry.transformPath(lines, coords ->
		{
			LocalPoint lp = LocalPoint.fromWorld(client, (int)coords[0], (int)coords[1]);
			coords[0] = lp.getX() - Perspective.LOCAL_TILE_SIZE / 2;
			coords[1] = lp.getY() - Perspective.LOCAL_TILE_SIZE / 2;
		});

		/*AffineTransform worldToLocalTransform = new AffineTransform();
		worldToLocalTransform.translate(client.getBaseX(), client.getBaseY());
		worldToLocalTransform.scale(Perspective.LOCAL_TILE_SIZE, Perspective.LOCAL_TILE_SIZE);
		worldToLocalTransform.translate(-Perspective.LOCAL_TILE_SIZE / 2, -Perspective.LOCAL_TILE_SIZE / 2);
		area.transform(worldToLocalTransform);*/

		pathToDisplay = lines;
/*
		linesToDisplay.clear();
		MapLocations.MULTICOMBAT_AREAS.forEach(x ->
		{
			int prevIndex = x.getPoints().size() - 1;
			for (int i = 0; i < x.getPoints().size(); i++)
			{
				WorldLine worldLine = new WorldLine(x.getPoints().get(i), x.getPoints().get(prevIndex));
				if (config.onlyShowLinesInPvp())
				{
					if (!MapLocations.ROUGH_WILDERNESS.intersects(worldLine) &&
						!MapLocations.ROUGH_WILDERNESS.contains(worldLine.getStart()) &&
						!MapLocations.ROUGH_WILDERNESS.contains(worldLine.getEnd()))
					{
						// The line is not within the wilderness
						return;
					}

					worldLine = worldLine.clamp(MapLocations.ROUGH_WILDERNESS);
				}

				if (currentScene.intersects(worldLine))
				{
					if (config.testCollision())
					{
						splitLineBasedOnCollision(worldLine).forEach(y ->
						{
							linesToDisplay.add(y);
						});
					}
					else
					{
						linesToDisplay.add(worldLine);
					}
				}
				prevIndex = i;
			}
		});*/
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getKey().equals("outlineColor"))
		{
			overlay.updateColor();
			minimapOverlay.updateColor();
		}
		if (event.getKey().equals("onlyPvpLines") ||
			event.getKey().equals("testCollision"))
		{
			findLinesInScene();
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOGGED_IN)
		{
			findLinesInScene();
		}
	}
}
