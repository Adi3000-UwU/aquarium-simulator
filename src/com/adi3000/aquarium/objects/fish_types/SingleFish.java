package com.adi3000.aquarium.objects.fish_types;

import com.adi3000.aquarium.main.Game;
import com.adi3000.aquarium.math.Vector2;
import com.adi3000.aquarium.objects.Fish;

import java.awt.*;

public class SingleFish extends Fish {
    
    public SingleFish() {
        super();
        
        color = new Color(0xDBE548);
        size = 15;
    }
    
    
    @Override
    public void tick() {
        if (target.distance(new Vector2()) <= 2 * size || position.distance(target) < 20) {
            target.set(getDefaultTarget());
        }
        
        goToTarget(target.add(getSeperationOffset()));
    }
    
    @Override
    protected Vector2 getDefaultTarget() {
        return new Vector2(Math.random() * Game.WIDTH, Math.random() * ((double) Game.HEIGHT / 3 * 2));
    }
}
