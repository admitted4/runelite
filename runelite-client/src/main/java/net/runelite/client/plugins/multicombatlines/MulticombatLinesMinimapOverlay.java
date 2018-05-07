package net.runelite.client.plugins.multicombatlines;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Constants;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

public class MulticombatLinesMinimapOverlay extends Overlay
{
	private final static int MAX_LOCAL_DRAW_LENGTH = 30 * Perspective.LOCAL_TILE_SIZE;

	@Inject
	private Client client;

	@Inject
	private MulticombatLinesPlugin plugin;

	@Inject
	private MulticombatLinesConfig config;

	@Inject
	public MulticombatLinesMinimapOverlay()
	{
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_MINIMAP);
		setPriority(OverlayPriority.LOW);
	}

	private Color drawColor;

	void updateColor()
	{
		Color color = config.outlineColor();
		drawColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 127);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!config.showMinimapLines())
		{
			return null;
		}
/*
		int lowX = client.getBaseX() + 1;
		int lowY = client.getBaseY() + 1;
		int highX = lowX + Constants.REGION_SIZE - 2;
		int highY = lowY + Constants.REGION_SIZE - 2;

		graphics.setColor(drawColor);

		LocalPoint playerLp = client.getLocalPlayer().getLocalLocation();
		plugin.getLinesToDisplay().forEach(x ->
		{
			// Limit the length of lines so they don't go outside scene
			int startWorldX = Math.max(x.getStart().getX(), lowX);
			int startWorldY = Math.max(x.getStart().getY(), lowY);
			int endWorldX = Math.max(x.getEnd().getX(), lowX);
			int endWorldY = Math.max(x.getEnd().getY(), lowY);
			startWorldX = Math.min(startWorldX, highX);
			startWorldY = Math.min(startWorldY, highY);
			endWorldX = Math.min(endWorldX, highX);
			endWorldY = Math.min(endWorldY, highY);
			LocalPoint startLp = LocalPoint.fromWorld(client, startWorldX, startWorldY);
			LocalPoint endLp = LocalPoint.fromWorld(client, endWorldX, endWorldY);

			int startLocalX = startLp.getX();
			int startLocalY = startLp.getY();
			int endLocalX = endLp.getX();
			int endLocalY = endLp.getY();

			// Move to southwest corner of tile
			startLocalX -= Perspective.LOCAL_TILE_SIZE / 2;
			startLocalY -= Perspective.LOCAL_TILE_SIZE / 2;
			endLocalX -= Perspective.LOCAL_TILE_SIZE / 2;
			endLocalY -= Perspective.LOCAL_TILE_SIZE / 2;

			// Don't draw lines that are too far away from the player
			if (endLocalX == startLocalX)
			{
				if (playerLp.getX() - MAX_LOCAL_DRAW_LENGTH >= startLocalX ||
					playerLp.getX() + MAX_LOCAL_DRAW_LENGTH <= startLocalX)
				{
					return;
				}
			}
			if (endLocalY == startLocalY)
			{
				if (playerLp.getY() - MAX_LOCAL_DRAW_LENGTH >= startLocalY ||
					playerLp.getY() + MAX_LOCAL_DRAW_LENGTH <= startLocalY)
				{
					return;
				}
			}

			// Limit the length of lines so they dont go too far away from the player
			startLocalX = Math.max(startLocalX, playerLp.getX() - MAX_LOCAL_DRAW_LENGTH);
			startLocalY = Math.max(startLocalY, playerLp.getY() - MAX_LOCAL_DRAW_LENGTH);
			endLocalX = Math.max(endLocalX, playerLp.getX() - MAX_LOCAL_DRAW_LENGTH);
			endLocalY = Math.max(endLocalY, playerLp.getY() - MAX_LOCAL_DRAW_LENGTH);
			startLocalX = Math.min(startLocalX, playerLp.getX() + MAX_LOCAL_DRAW_LENGTH);
			startLocalY = Math.min(startLocalY, playerLp.getY() + MAX_LOCAL_DRAW_LENGTH);
			endLocalX = Math.min(endLocalX, playerLp.getX() + MAX_LOCAL_DRAW_LENGTH);
			endLocalY = Math.min(endLocalY, playerLp.getY() + MAX_LOCAL_DRAW_LENGTH);

			// Make the start the lower number to help preventing infinite loops later on
			if (endLocalX < startLocalX)
			{
				int temp = endLocalX;
				endLocalX = startLocalX;
				startLocalX = temp;
			}
			if (endLocalY < startLocalY)
			{
				int temp = endLocalY;
				endLocalY = startLocalY;
				startLocalY = temp;
			}

			int localX = startLocalX;
			int localY = startLocalY;
			int localDx = Integer.signum(endLocalX - startLocalX) * Perspective.LOCAL_TILE_SIZE;
			int localDy = Integer.signum(endLocalY - startLocalY) * Perspective.LOCAL_TILE_SIZE;
			if (localDx == 0 && localDy == 0)
			{
				return;
			}

			// Render first part of a tile if the line is not divisible by whole tiles
			if (startLocalX % Perspective.LOCAL_TILE_SIZE != 0 ||
				startLocalY % Perspective.LOCAL_TILE_SIZE != 0)
			{
				int nextLocalX = (startLocalX + localDx) / Perspective.LOCAL_TILE_SIZE * Perspective.LOCAL_TILE_SIZE;
				int nextLocalY = (startLocalY + localDy) / Perspective.LOCAL_TILE_SIZE * Perspective.LOCAL_TILE_SIZE;
				Point p1 = Perspective.worldToMiniMap(client, startLocalX, startLocalY);
				Point p2 = Perspective.worldToMiniMap(client, nextLocalX, nextLocalY);
				if (p1 != null && p2 != null)
				{
					graphics.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
				}

				// Move the start of the looping so we dont rerender this part of the tile
				if (localDx == 0)
				{
					localY = nextLocalY;
				}
				else if (localDy == 0)
				{
					localX = nextLocalX;
				}
			}

			// Render each tile one by one
			while ((localDx == 0 && localY + localDy <= endLocalY) ||
				(localDy == 0 && localX + localDx <= endLocalX))
			{
				Point p1 = Perspective.worldToMiniMap(client, localX, localY);
				Point p2 = Perspective.worldToMiniMap(client, localX + localDx, localY + localDy);

				if (p1 != null && p2 != null)
				{
					graphics.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
				}

				localX += localDx;
				localY += localDy;
			}

			// Render last part of a tile if the line is not divisible by whole tiles
			if (endLocalX % Perspective.LOCAL_TILE_SIZE != 0 ||
				endLocalY % Perspective.LOCAL_TILE_SIZE != 0)
			{
				int prevLocalX = endLocalX / Perspective.LOCAL_TILE_SIZE * Perspective.LOCAL_TILE_SIZE;
				int prevLocalY = endLocalY / Perspective.LOCAL_TILE_SIZE * Perspective.LOCAL_TILE_SIZE;
				Point p1 = Perspective.worldToMiniMap(client, prevLocalX, prevLocalY);
				Point p2 = Perspective.worldToMiniMap(client, endLocalX, endLocalY);
				if (p1 != null && p2 != null)
				{
					graphics.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
				}
			}
		});
*/
		return null;
	}
}
