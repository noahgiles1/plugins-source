
package net.runelite.client.plugins.fishing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.Instant;
import javax.inject.Inject;
import lombok.AccessLevel;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.GraphicID;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.game.FishingSpot;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.components.ProgressPieComponent;
import net.runelite.client.util.ImageUtil;

class FishingSpotOverlay extends Overlay
{
	int x, y;
	private static final Duration MINNOW_MOVE = Duration.ofSeconds(15);
	private static final Duration MINNOW_WARN = Duration.ofSeconds(3);
	private static final int ONE_TICK_AERIAL_FISHING = 3;

	private final FishingPlugin plugin;
	private final FishingConfig config;
	private final Client client;
	private final ItemManager itemManager;

	@Setter(AccessLevel.PACKAGE)
	private boolean hidden;

	@Inject
	private FishingSpotOverlay(FishingPlugin plugin, FishingConfig config, Client client, ItemManager itemManager)
	{
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
		this.plugin = plugin;
		this.config = config;
		this.client = client;
		this.itemManager = itemManager;
	}

	@Override
	public Dimension render(Graphics2D graphics)

	{
		if (hidden)
		{
			return null;
		}

		FishingSpot previousSpot = null;
		WorldPoint previousLocation = null;
		for (NPC npc : plugin.getFishingSpots())
		{
			FishingSpot spot = FishingSpot.findSpot(npc.getId());

			if (spot == null)
			{
				continue;
			}

			if (config.onlyCurrentSpot() && plugin.getCurrentSpot() != null && plugin.getCurrentSpot() != spot)
			{
				continue;
			}

			// This relies on the sort order to keep identical npcs on the same tile adjacent to each other
			if (previousSpot == spot && previousLocation.equals(npc.getWorldLocation()))
			{
				continue;
			}

			Color color;
			if (npc.getGraphic() == GraphicID.FLYING_FISH)
			{
				color = config.getMinnowsOverlayColor();
			}
			else if (spot == FishingSpot.COMMON_TENCH && npc.getWorldLocation().distanceTo2D(client.getLocalPlayer().getWorldLocation()) <= ONE_TICK_AERIAL_FISHING)
			{
				color = config.getAerialOverlayColor();
			}
			else
			{
				color = config.getOverlayColor();
			}

			if (spot == FishingSpot.MINNOW && config.showMinnowOverlay())
			{
				MinnowSpot minnowSpot = plugin.getMinnowSpots().get(npc.getIndex());
				if (minnowSpot != null)
				{
					long millisLeft = MINNOW_MOVE.toMillis() - Duration.between(minnowSpot.getTime(), Instant.now()).toMillis();
					if (millisLeft < MINNOW_WARN.toMillis())
					{
						color = Color.ORANGE;
					}

					LocalPoint localPoint = npc.getLocalLocation();
					Point location = Perspective.localToCanvas(client, localPoint, client.getPlane());

					if (location != null)
					{
						ProgressPieComponent pie = new ProgressPieComponent();
						pie.setFill(color);
						pie.setBorderColor(color);
						pie.setPosition(location);
						pie.setProgress((float) millisLeft / MINNOW_MOVE.toMillis());
						pie.render(graphics);
					}
				}
			}

			if (config.showSpotTiles())

			{
				Polygon poly = npc.getCanvasTilePoly();

				if (poly != null)
				{
					x = (int)poly.getBounds().getCenterX() - this.config.boxSize() / 2;
					y = (int)poly.getBounds().getCenterY() - this.config.boxSize() / 2;
					graphics.setColor(color);
					graphics.fillRect(x, y, this.config.boxSize(), this.config.boxSize());
				}
			}

			if (config.showSpotIcons())
			{
				BufferedImage fishImage = itemManager.getImage(spot.getFishSpriteId());

				if (spot == FishingSpot.COMMON_TENCH
					&& npc.getWorldLocation().distanceTo2D(client.getLocalPlayer().getWorldLocation()) <= ONE_TICK_AERIAL_FISHING)
				{
					fishImage = ImageUtil.outlineImage(itemManager.getImage(spot.getFishSpriteId()), color);
				}

				if (fishImage != null)
				{
					Point imageLocation = npc.getCanvasImageLocation(fishImage, npc.getLogicalHeight());
					if (imageLocation != null)
					{
						OverlayUtil.renderImageLocation(graphics, imageLocation, fishImage);
					}
				}
			}

			if (config.showSpotNames())
			{
				String text = spot.getName();
				Point textLocation = npc.getCanvasTextLocation(graphics, text, npc.getLogicalHeight() + 40);

				if (textLocation != null)
				{
					OverlayUtil.renderTextLocation(graphics, textLocation, text, color.darker());
				}
			}

			previousSpot = spot;
			previousLocation = npc.getWorldLocation();
		}

		return null;
	}
}
