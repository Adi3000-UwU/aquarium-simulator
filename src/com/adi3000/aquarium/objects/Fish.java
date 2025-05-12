package com.adi3000.aquarium.objects;

import com.adi3000.aquarium.main.Game;
import com.adi3000.aquarium.math.Vector2;

import java.awt.*;
import java.util.ArrayList;

public abstract class Fish {
    
    public Vector2 position;
    public Vector2 velocity;
    public Vector2 target;
    
    protected Color color;
    protected int size;
    
    protected double maxVelocity = 2.5;
    protected double acceleration = 0.2;
    protected double swimRandomness = 0.4;
    
    
    public Fish() {
        this.position = getDefaultTarget();
        
        velocity = new Vector2();
        target = new Vector2();
    }
    
    
    protected Vector2 getDefaultTarget() {
        return new Vector2(Math.random() * Game.WIDTH, Math.random() * Game.HEIGHT);
    }
    
    protected Vector2 getSeperationOffset() {
        ArrayList<Fish> fishNearby = Game.gameManager.getFishInRange(position, size);
        fishNearby.remove(this);
        Vector2 seperationOffset = new Vector2();
        for (Fish fish : fishNearby) {
            seperationOffset.inc(fish.position.sub(position));
        }
        return seperationOffset;
    }
    
    protected void goToTarget(Vector2 target) {
        Vector2 randomOffset = new Vector2(Math.random() * (2 * swimRandomness) - swimRandomness, Math.random() * (2 * swimRandomness) - swimRandomness);
        velocity.inc(target.sub(position).getNormilized().mult(acceleration).add(randomOffset));
        
        if (velocity.mag() > maxVelocity) {
            velocity.setMagnitude(maxVelocity);
        }
        
        position.inc(velocity);
    }
    
    
    public abstract void tick();
    
    public void render(Graphics2D g2d) {
        g2d.setColor(color);
        
        g2d.fillOval((int) position.x - size / 2, (int) position.y - size / 2, size, size);
    }
    
}
