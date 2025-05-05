package com.adi3000.aquarium.main;

import com.adi3000.aquarium.math.Vector2;
import com.adi3000.aquarium.objects.Fish;

import java.awt.*;
import java.util.ArrayList;

public class GameManager {
    
    public ArrayList<Fish> fishes = new ArrayList<>();
    
    public GameManager() {
        
        for (int i = 0; i < 100; i++) {
            fishes.add(new Fish(new Vector2(Math.random() * Game.WIDTH, Math.random() * Game.HEIGHT), new Color(0xE54848)));
        }
        
    }
    
    
    public void tick() {
        for (Fish fish : fishes) {
            fish.tick();
            
        }
        
    }
    
    public void render(Graphics2D g2d) {
        for (Fish fish : fishes) {
            fish.render(g2d);
        }
        
    }
    
    
    public ArrayList<Fish> getFishInRange(Vector2 position, double range) {
        ArrayList<Fish> fishesInRange = new ArrayList<>();
        
        for (Fish fish : fishes) {
            if (fish.position.distance(position) <= range) fishesInRange.add(fish);
        }
        
        return fishesInRange;
    }
    
    
}
