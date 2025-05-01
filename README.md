# No Input Lag Tick Rate
Minecraft processes inputs every tick, making input latency inversely proportional to the tick rate (lower tick rate = higher latency). With a tick rate of 1 for example, your inputs are processed only once every second.

This mod moves input processing to the render loop, making input latency consistent regardless of the tick rate (assuming your frame rate is higher than 20 fps).

Here's a table for comparison:

| Tick Rate | Input Latency (Vanilla) | Input Latency (This Mod) |
|:----------|:------------------------|:-------------------------|
| 20 tps (default) | 50 ms            | 50 ms                    |
| 10 tps     | 100 ms                 | 50 ms                    |
| 5 tps      | 200 ms                 | 50 ms                    |
| 1 tps      | 1000 ms                | 50 ms                    |