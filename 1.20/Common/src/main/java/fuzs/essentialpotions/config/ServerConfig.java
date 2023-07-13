package fuzs.essentialpotions.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;

public class ServerConfig implements ConfigCore {
    @Config(description = "Blindness effect is affected by potion level and affects detection range of mobs.")
    public boolean strongerBlindness = true;
    @Config(description = "Fire resistance provides better vision in lava, no longer shows the flame overlay and prevents the entity from burning afte stepping out of the fire source.")
    public boolean betterFireResistanceVision = true;
    @Config(description = "Night vision fades away when ending instead of flashing.")
    public boolean noNightVisionFlashing = true;
    @Config(description = "Allow falling at normal speed while the slow falling effect is active when sneaking. The player will still not take any fall damage.")
    public boolean slowFallingQuickDescent = true;
}
