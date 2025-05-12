package com.adi3000.aquarium.objects.fish_types;

import com.adi3000.aquarium.main.Game;
import com.adi3000.aquarium.math.Vector2;
import com.adi3000.aquarium.objects.GroupFish;

import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class DeepFish extends GroupFish {
    
    public DeepFish() {
        super();
        
        color = new Color(0x6548E5);
        size = 8;
        
        viewDistance = 60;
        maxGroupSize = 10;
        minGroupSize = 5;
    }
    
    
    @Override
    protected Vector2 getDefaultTarget() {
        return new Vector2(Math.random() * Game.WIDTH, Math.random() * ((double) Game.HEIGHT / 3) + ((double) Game.HEIGHT / 3 * 2));
    }
    
    @Override
    protected ArrayList<GroupFish> getNearbyFish() {
        return Game.gameManager.getFishInRange(position, viewDistance).stream()
                .filter(DeepFish.class::isInstance)
                .map(DeepFish.class::cast).collect(Collectors.toCollection(ArrayList::new));
    }
}
