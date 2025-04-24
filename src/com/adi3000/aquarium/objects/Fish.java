package com.adi3000.aquarium.objects;

import com.adi3000.aquarium.math.Vector2;

import java.awt.*;

public class Fish {
    
    public Vector2 position;
    public Vector2 velocity;
    private Color color;
    
    public Fish(Vector2 position, Vector2 velocity, Color color) {
        this.position = position;
        this.velocity = velocity;
        this.color = color;
    }
    
    
    public void tick() {
        position.inc(velocity);
    }
    
    public void render(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillOval((int) position.x, (int) position.y, 10, 10);
    }
    
}
