package com.adi3000.aquarium.objects;

import com.adi3000.aquarium.main.Game;
import com.adi3000.aquarium.math.Vector2;

import java.awt.*;

public class Fish {
    
    public Vector2 position;
    public Vector2 velocity;
    public Vector2 target;
    
    private Color color;
    
    private double maxVelocity = 2.5;
    private double acceleration = 0.2;
    
    
    public Fish(Vector2 position, Color color) {
        this.position = position;
        this.color = color;
        
        velocity = new Vector2();
        target = new Vector2();
    }
    
    
    public void tick() {
        if (target.equals(new Vector2()) || position.distance(target) < 30) {
            target = new Vector2(Math.random() * Game.WIDTH, Math.random() * Game.HEIGHT);
        }
        
        goToTarget(target);
    }
    
    private void goToTarget(Vector2 target) {
        velocity.inc(target.sub(position).getNormilized().mult(acceleration));
        
        if (velocity.mag() > maxVelocity) {
            velocity.setMagnitude(maxVelocity);
        }
        
        position.inc(velocity);
    }
    
    public void render(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillOval((int) position.x, (int) position.y, 10, 10);
    }
    
}
