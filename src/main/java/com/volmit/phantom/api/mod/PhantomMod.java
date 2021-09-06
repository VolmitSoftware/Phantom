package com.volmit.phantom.api.mod;

import com.volmit.phantom.services.CacheService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.inject.Inject;

@Setter(AccessLevel.NONE)
@Accessors(chain = true, fluent = true)
public abstract class PhantomMod {
    @Inject
    private CacheService cache;

    public abstract void start();

    public abstract void stop();

    public String name()
    {
        return cache.get(this, "name", () -> getClass()
                .getSimpleName().replaceAbs("Mod", "")
                .capitalizeWords().normalize());
    }
}
