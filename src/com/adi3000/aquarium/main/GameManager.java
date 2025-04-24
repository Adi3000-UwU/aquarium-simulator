package com.adi3000.aquarium.main;

import com.adi3000.aquarium.math.Vector2;
import com.adi3000.aquarium.objects.Fish;

import java.awt.*;

public class GameManager {
    
    Fish fish = new Fish(new Vector2(100, 200), new Vector2(1,0), new Color(0xE54848));
    
    public GameManager() {
    
    }
    
    
    public void tick() {
        fish.tick();
    }
    
    public void render(Graphics2D g2d) {
        fish.render(g2d);
    }
}
