
package net.runelite.client.plugins.fishing;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

class FishingSession
{
	@Getter
	@Setter
	private Instant lastFishCaught;
}
