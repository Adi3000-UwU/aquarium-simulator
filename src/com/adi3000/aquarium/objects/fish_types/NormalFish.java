package com.adi3000.aquarium.objects.fish_types;

import com.adi3000.aquarium.main.Game;
import com.adi3000.aquarium.objects.GroupFish;

import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class NormalFish extends GroupFish {
    
    public NormalFish() {
        super();
        
        color = new Color(0xE54848);
        size = 10;
        
        viewDistance = 60;
        maxGroupSize = 15;
        minGroupSize = 10;
    }
    
    
    @Override
    protected ArrayList<GroupFish> getNearbyFish() {
        return Game.gameManager.getFishInRange(position, viewDistance).stream()
                .filter(NormalFish.class::isInstance)
                .map(NormalFish.class::cast).collect(Collectors.toCollection(ArrayList::new));
    }
}
