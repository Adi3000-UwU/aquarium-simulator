package com.adi3000.aquarium.objects;

import com.adi3000.aquarium.main.Game;
import com.adi3000.aquarium.math.Vector2;

import java.awt.*;
import java.util.ArrayList;

public class Fish {
    
    public Vector2 position;
    public Vector2 velocity;
    public Vector2 target;
    
    public Color color;
    
    private double maxCelocity = 4;
    private double acceleration = 1;
    
    
    public Fish(Vector2 position, Color color) {
        this.position = position;
        this.color = color;
        
        velocity = new Vector2();
        target = new Vector2();
    }
    
    
    public void tick() {
//        Vector2 newTarget = MouseHandler.getInstance().mousePos.add(new Vector2(Math.random() * 150 - 75, Math.random() * 150 - 75));
//        if(newTarget.distance(target) > 120){
//            target = newTarget;
//        }
//
//        goToTarget(target);
//
        
        
        ArrayList<Fish> nearbyFishes = Game.gameManager.getFishInRange(position, 60);
        
        nearbyFishes.remove(this);
        
        if (nearbyFishes.size() > 1) {
            Vector2 groupMiddle = new Vector2();
            Vector2 averageVelocity = new Vector2();
            for (Fish fish : nearbyFishes) {
                groupMiddle.inc(fish.position);
                averageVelocity.inc(fish.velocity);
            }
            groupMiddle.set(groupMiddle.div(nearbyFishes.size()));
            averageVelocity.set(averageVelocity.div(nearbyFishes.size()));
            
            Vector2 newTarget = groupMiddle.add(averageVelocity.getNormilized());
            
            target = (newTarget.add(target).div(2).add(new Vector2(velocity.getNormilized().x * 10, 0)));
            
        } else {
            target = new Vector2(Math.random() * Game.WIDTH, Math.random() * Game.HEIGHT);
        }
        
        if (target.x < 0 || target.x > Game.WIDTH) {
            target.x = Math.random() * Game.WIDTH;
        }
        if (target.y < 0 || target.y > Game.HEIGHT) {
            target.y = Math.random() * Game.HEIGHT;
        }
        
        goToTarget(target);
        
        
    }
    
    private void goToTarget(Vector2 target) {
        velocity.inc(target.sub(position).getNormilized().mult(acceleration));
        
        if (velocity.mag() > maxCelocity) {
            velocity.setMagnitude(maxCelocity);
        }
        
        position.inc(velocity);
    }
    
    public void render(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillOval((int) position.x, (int) position.y, 10, 10);
        
        g2d.setColor(Color.BLACK);
//        g2d.fillOval((int) target.x, (int) target.y, 2, 2);
    }
    
}
