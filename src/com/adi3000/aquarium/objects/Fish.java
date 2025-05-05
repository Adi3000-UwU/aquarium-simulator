package com.adi3000.aquarium.objects;

import com.adi3000.aquarium.main.MouseHandler;
import com.adi3000.aquarium.math.Vector2;

import java.awt.*;

public class Fish {
    
    public Vector2 position;
    public Vector2 velocity;
    public Vector2 target;
    
    private Color color;
    
    private double maxCelocity = 4;
    private double acceleration = 1;
    
    
    public Fish(Vector2 position, Color color) {
        this.position = position;
        this.color = color;

        velocity = new Vector2();
        target = new Vector2();
    }
    
    
    public void tick() {
        target = MouseHandler.getInstance().mousePos;
        
        goToTarget(target);
    }
    
    private void goToTarget(Vector2 target) {
        velocity.inc(target.sub(position).setMagnitude(acceleration));
        
        if(velocity.mag() > maxCelocity) {
            velocity.setMagnitude(maxCelocity);
        }
        
        position.inc(velocity);
    }
    
    public void render(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillOval((int) position.x, (int) position.y, 10, 10);
    }
    
}
