
package net.runelite.client.plugins.fishing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import lombok.AccessLevel;
import lombok.Setter;
import net.runelite.api.GraphicID;
import net.runelite.api.NPC;
import net.runelite.client.game.FishingSpot;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

class FishingSpotMinimapOverlay extends Overlay
{
	private final FishingPlugin plugin;
	private final FishingConfig config;

	@Setter(AccessLevel.PACKAGE)
	private boolean hidden;

	@Inject
	public FishingSpotMinimapOverlay(FishingPlugin plugin, FishingConfig config)
	{
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		this.plugin = plugin;
		this.config = config;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (hidden)
		{
			return null;
		}

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

			Color color = npc.getGraphic() == GraphicID.FLYING_FISH
				? config.getMinnowsOverlayColor()
				: config.getOverlayColor();

			net.runelite.api.Point minimapLocation = npc.getMinimapLocation();
			if (minimapLocation != null)
			{
				OverlayUtil.renderMinimapLocation(graphics, minimapLocation, color.darker());
			}
		}

		return null;
	}
}
