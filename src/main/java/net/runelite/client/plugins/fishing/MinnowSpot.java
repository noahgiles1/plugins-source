
package net.runelite.client.plugins.fishing;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Value;
import net.runelite.api.coords.WorldPoint;

@AllArgsConstructor
@Value
class MinnowSpot
{
	private final WorldPoint loc;
	private final Instant time;
}
