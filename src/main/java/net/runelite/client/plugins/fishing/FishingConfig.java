 net.runelite.client.plugins.fishing;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Units;

@ConfigGroup("fishing")
public interface FishingConfig extends Config
{
	@ConfigItem(
		position = 0,
		keyName = "onlyCurrent",
		name = "Display only currently fished fish",
		description = "Configures whether only current fished fish's fishing spots are displayed"
	)
	default boolean onlyCurrentSpot()
	{
		return false;
	}

	@ConfigItem(
		position = 1,
		keyName = "showTiles",
		name = "Display spot tiles",
		description = "Configures whether tiles for fishing spots are highlighted"
	)
	default boolean showSpotTiles()
	{
		return true;
	}

	@ConfigItem(
		position = 2,
		keyName = "showIcons",
		name = "Display spot icons",
		description = "Configures whether icons for fishing spots are displayed"
	)
	default boolean showSpotIcons()
	{
		return true;
	}

	@ConfigItem(position = 18, keyName = "boxSize", name = "Box size", description = "The size of the box displayed in the centre of the marked ground tile.", hidden = true)
	default int boxSize() {
		return 4;
	}

	@ConfigItem(
		position = 3,
		keyName = "showNames",
		name = "Display spot names",
		description = "Configures whether names for fishing spots are displayed"
	)
	default boolean showSpotNames()
	{
		return false;
	}

	@ConfigItem(
		keyName = "overlayColor",
		name = "Overlay Color",
		description = "Color of overlays",
		position = 4
	)
	default Color getOverlayColor()
	{
		return Color.CYAN;
	}

	@ConfigItem(
		keyName = "minnowsOverlayColor",
		name = "Minnows Overlay Color",
		description = "Color of overlays for Minnows",
		position = 5
	)
	default Color getMinnowsOverlayColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		keyName = "aerialOverlayColor",
		name = "Aerial Overlay Color",
		description = "Color of overlays when 1-tick aerial fishing",
		position = 6
	)
	default Color getAerialOverlayColor()
	{
		return Color.GREEN;
	}

	@ConfigItem(
		position = 7,
		keyName = "statTimeout",
		name = "Reset stats",
		description = "The time until fishing session data is reset in minutes."
	)
	@Units(Units.MINUTES)
	default int statTimeout()
	{
		return 5;
	}

	@ConfigItem(
		position = 8,
		keyName = "showFishingStats",
		name = "Show Fishing session stats",
		description = "Display the fishing session stats."
	)
	default boolean showFishingStats()
	{
		return true;
	}

	@ConfigItem(
		position = 9,
		keyName = "showMinnowOverlay",
		name = "Show Minnow Movement overlay",
		description = "Display the minnow progress pie overlay."
	)
	default boolean showMinnowOverlay()
	{
		return true;
	}

	@ConfigItem(
		position = 10,
		keyName = "trawlerNotification",
		name = "Trawler activity notification",
		description = "Send a notification when fishing trawler activity drops below 15%."
	)
	default boolean trawlerNotification()
	{
		return true;
	}

	@ConfigItem(
		position = 11,
		keyName = "trawlerTimer",
		name = "Trawler timer in MM:SS",
		description = "Trawler Timer will display a more accurate timer in MM:SS format."
	)
	default boolean trawlerTimer()
	{
		return true;
	}
}
