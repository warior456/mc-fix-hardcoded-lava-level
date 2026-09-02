# Fix Hardcoded Lava Level

---
### mc-fix-hardcoded-lava-level

Minecraft mod to fix [MC-237017](https://bugs.mojang.com/browse/MC-237017) 
(All caves are filled with lava at and below Y-Value `-54`).


Doesn't break vanilla lava gen like [https://github.com/Andrew6rant/MC-237017-Fix](https://github.com/Andrew6rant/MC-237017-Fix)

This mod is **Server-Side** only, and does not require anything on the client.

## Caveats: 
The Y-Value for lava filling has been changed,
from a hardcoded value to one that references sea level.
This method should fix most custom dimensions but some extreme ones may not be fixed by this.

## Usage
Install the mod. By default, the config value is set to have the same level as Vanilla. To see any change in lava level, there are two methods:
* The Mod's Config File 
* Worldgen Datapack Key (in the Overworld's file, see below).

**The Datapack Key takes priority if it is defined.**


<details>
<summary>Technical Details</summary>
<ul>
<li>Vanilla Value: <code>-54</code></li>
<li>Default Mod Config: <code>Sea Level</code> - 117 (Default Sea Level: <code>63</code>)</li>
<li>Config File
    <ul>
    <li><code>vertical_Reference_Type</code>: The imaginary "surface" to reference the value off of.
        <ul>
        <li><code>BELOW_SEA_LEVEL</code>: Reference the Sea Level, count down below it.</li>
        <li><code>ABOVE_BOTTOM</code>: Reference the Lowest Y-Value, count up above it.</li>
        <li><code>ABSOLUTE</code>: Use the configured value as the actual Y-value.</li>
        </ul>
    </li>
    <li><code>vertical_Reference_To_Lava_Separation</code>: The offset from the Vertical Reference, in BLOCKS.</li>
    </ul>
</li>
<li>Datapack Key
    <ul>
    <li>Key: <code>lava_level</code> must be in <code>data/minecraft/worldgen/noise_settings/overworld.json</code></li>
    <li>Value: Must be an integer. Behaves identically to the <code>ABSOLUTE</code> reference type in the config file.</li>
    </ul>
</li>
</ul>
</details>






