package com.adi3000.aquarium.objects;

import com.adi3000.aquarium.main.Game;
import com.adi3000.aquarium.math.Vector2;

import java.awt.*;
import java.util.ArrayList;

public class Fish {
    
    public Vector2 position;
    public Vector2 velocity;
    public Vector2 target;
    
    private Color color;
    
    private double maxVelocity = 2.5;
    private double acceleration = 0.2;
    
    
    public boolean isLeader = true;
    public ArrayList<Fish> group = new ArrayList<>();
    public Fish leader;
    
    
    public Fish(Vector2 position, Color color) {
        this.position = position;
        this.color = color;
        
        velocity = new Vector2();
        target = new Vector2();
    }
    
    
    public void tick() {
        
        if (isLeader) {
            ArrayList<Fish> fishNearby = Game.gameManager.getFishInRange(position, 60);
            fishNearby.remove(this);
            if (!fishNearby.isEmpty()) {
                fishNearby.get(0).joinGroup(this);
            }
        }
        
        if (!isLeader && leader.group.size() > 10) {
            leaveGroup();
        }
        
        
        if (target.equals(new Vector2()) || position.distance(target) < 20) {
            if (isLeader) {
                target.set(new Vector2(Math.random() * Game.WIDTH, Math.random() * Game.HEIGHT));
            } else {
                target.set(leader.target.add(position.sub(leader.position)).add(new Vector2(Math.random() * 20 - 10, Math.random() * 20 - 10)));
            }
        }
        if (!isLeader && position.distance(leader.position) > 40) {
            target.set(leader.target);
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
    
    private void joinGroup(Fish fish) {
        if (fish.equals(this)) return;
        
        if (!isLeader) {
            leader.joinGroup(fish);
            return;
        }
        
        group.add(fish);
//        group.addAll(fish.group);
//        for (Fish groupFish : fish.group) {
//            groupFish.leader = this;
//        }
//        fish.group.clear();
        fish.leaveGroup();
        fish.isLeader = false;
        fish.leader = this;
    }
    
    private void leaveGroup() {
        if (isLeader) {
            if (!group.isEmpty()) {
                Fish newLeader = group.get((int) (Math.random() * group.size()));
                group.remove(newLeader);
                newLeader.group.addAll(group);
                group.clear();
                newLeader.isLeader = true;
            }
            return;
        }
        leader.group.remove(this);
        isLeader = true;
    }
    
    public void render(Graphics2D g2d) {
        if (isLeader) {
            g2d.setColor(Color.GREEN);
        } else {
            g2d.setColor(color);
//            g2d.drawLine((int) position.x, (int) position.y, (int) leader.position.x, (int) leader.position.y);
        }
        
        g2d.fillOval((int) position.x, (int) position.y, 10, 10);

//        g2d.setColor(Color.MAGENTA);
//        g2d.drawLine((int) position.x, (int) position.y, (int) target.x, (int) target.y);
    }
    
}
