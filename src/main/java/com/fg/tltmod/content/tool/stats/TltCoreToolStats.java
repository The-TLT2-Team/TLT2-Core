package com.fg.tltmod.content.tool.stats;

import com.fg.tltmod.TltCore;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStatId;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class TltCoreToolStats {
    public static final FloatToolStat SOUL_CONSUMPTION = ToolStats.register(new FloatToolStat(new ToolStatId(TltCore.getResource("soul_consumption")),0xA5FFFD,1,0,Integer.MAX_VALUE,null));
}
