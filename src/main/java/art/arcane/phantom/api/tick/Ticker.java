/*------------------------------------------------------------------------------
 -   Adapt is a Skill/Integration plugin  for Minecraft Bukkit Servers
 -   Copyright (c) 2022 Arcane Arts (Volmit Software)
 -
 -   This program is free software: you can redistribute it and/or modify
 -   it under the terms of the GNU General Public License as published by
 -   the Free Software Foundation, either version 3 of the License, or
 -   (at your option) any later version.
 -
 -   This program is distributed in the hope that it will be useful,
 -   but WITHOUT ANY WARRANTY; without even the implied warranty of
 -   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 -   GNU General Public License for more details.
 -
 -   You should have received a copy of the GNU General Public License
 -   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 -----------------------------------------------------------------------------*/

package art.arcane.phantom.api.tick;

import art.arcane.multiburst.BurstExecutor;
import art.arcane.multiburst.MultiBurst;
import art.arcane.phantom.util.J;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Ticker {
    private final List<Ticked> ticklist;
    private final List<Ticked> newTicks;
    private final List<String> removeTicks;
    private volatile boolean ticking;
    private final Random random;

    public Ticker() {
        random = new Random();
        this.ticklist = new ArrayList<>(4096);
        this.newTicks = new ArrayList<>(128);
        this.removeTicks = new ArrayList<>(128);
        ticking = false;
        J.ar(() -> {
            if (!ticking) {
                tick();
            }
        }, 0);
    }

    public void register(Ticked ticked) {
        synchronized (newTicks) {
            newTicks.add(ticked);
        }
    }

    public void unregister(Ticked ticked) {
        synchronized (removeTicks) {
            removeTicks.add(ticked.getId());
        }
    }

    public void clear() {
        synchronized (ticklist) {
            ticklist.clear();
        }
        synchronized (removeTicks) {
            removeTicks.clear();
        }
        synchronized (newTicks) {
            newTicks.clear();
        }

    }

    private void tick() {
        ticking = true;
//        int ix = 0;
        AtomicInteger tc = new AtomicInteger(0);
        BurstExecutor e = MultiBurst.burst.burst(ticklist.size());
        for (int i = 0; i < ticklist.size(); i++) {
            int ii = i;
//            ix++;
            e.queue(() -> {
                Ticked t = ticklist.get(ii);

                if (t != null && t.shouldTick()) {
                    tc.incrementAndGet();
                    try {
                        t.tick();
                    } catch (Throwable exxx) {
                        exxx.printStackTrace();
                    }
                }
            });
        }

        e.complete();
//        Adapt.info(ix + "");

        synchronized (newTicks) {
            while (!newTicks.isEmpty()) {
                tc.incrementAndGet();
                ticklist.add(newTicks.remove(random.nextInt(newTicks.size())));
            }
        }

        synchronized (removeTicks) {
            while (!removeTicks.isEmpty()) {
                tc.incrementAndGet();
                String id = removeTicks.remove(random.nextInt(removeTicks.size()));

                for (int i = 0; i < ticklist.size(); i++) {
                    if (ticklist.get(i).getId().equals(id)) {
                        ticklist.remove(i);
                        break;
                    }
                }
            }
        }

        ticking = false;
        tc.get();
    }
}
