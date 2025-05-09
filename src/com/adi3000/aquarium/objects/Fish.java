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
    private int size = 10;
    
    private double maxVelocity = 2.5;
    private double acceleration = 0.2;
    private double swimRandomness = 0.4;
    
    
    public boolean isLeader = true;
    public ArrayList<Fish> group = new ArrayList<>();
    public Fish leader;
    
    private double viewDistance = 60;
    private int maxGroupSize = 15;
    private int minGroupSize = 10;
    
    
    public Fish(Vector2 position, Color color) {
        this.position = position;
        this.color = color;
        
        velocity = new Vector2();
        target = new Vector2();
    }
    
    
    public void tick() {
        groupManagment();
        
        findTarget();
        goToTarget(target.add(getSeperationOffset()));
    }
    
    
    private void groupManagment() {
        ArrayList<Fish> fishNearby = Game.gameManager.getFishInRange(position, viewDistance);
        fishNearby.remove(this);
        fishNearby.removeAll(getGroup());
        
        Fish fishWithBestGroup = null;
        double bestFishScore = Integer.MAX_VALUE;
        
        double myGroupScore = Math.abs(0.3 - (double) (getCurrentGroupSize() - minGroupSize) / (maxGroupSize - minGroupSize));
        
        for (Fish fish : fishNearby) {
            double otherGroupScore = Math.abs(0.3 - (double) (fish.getCurrentGroupSize() + 1 - minGroupSize) / (maxGroupSize - minGroupSize));
            
            if (fish.getCurrentGroupSize() < minGroupSize && (getCurrentGroupSize() - fish.getCurrentGroupSize() > minGroupSize || getCurrentGroupSize() < minGroupSize)) {
                fishWithBestGroup = fish;
                bestFishScore = 0;
            }
            
            if (fish.getCurrentGroupSize() < maxGroupSize && otherGroupScore < myGroupScore - 0.4 && otherGroupScore < bestFishScore) {
                fishWithBestGroup = fish;
                bestFishScore = otherGroupScore;
            }
        }
        
        if (fishWithBestGroup != null) {
            fishWithBestGroup.joinGroup(this);
        } else if (getCurrentGroupSize() > maxGroupSize - minGroupSize / 2 && Math.random() < 0.005) {
            leaveGroup();
        }
    }
    
    private void findTarget() {
        if (target.distance(new Vector2()) <= 2 * size || position.distance(target) < 20) {
            if (isLeader) {
                target.set(new Vector2(Math.random() * Game.WIDTH, Math.random() * Game.HEIGHT));
            } else {
                target.set(leader.target.add(position.sub(leader.position)).add(new Vector2(Math.random() * 15 - 7.5, Math.random() * 15 - 7.5)));
            }
        }
        if (!isLeader && position.distance(leader.position) > 40) {
            target.set(leader.target);
            velocity.inc(leader.position.sub(position).getNormilized().mult(0.04));
        }
        
        target.clamp(new Vector2(size, size), new Vector2(Game.WIDTH, Game.HEIGHT));
    }
    
    private Vector2 getSeperationOffset() {
        ArrayList<Fish> fishNearby = Game.gameManager.getFishInRange(position, size);
        fishNearby.remove(this);
        Vector2 seperationOffset = new Vector2();
        for (Fish fish : fishNearby) {
            seperationOffset.inc(fish.position.sub(position));
        }
        return seperationOffset;
    }
    
    private void goToTarget(Vector2 target) {
        Vector2 randomOffset = new Vector2(Math.random() * (2 * swimRandomness) - swimRandomness, Math.random() * (2 * swimRandomness) - swimRandomness);
        velocity.inc(target.sub(position).getNormilized().mult(acceleration).add(randomOffset));
        
        if (velocity.mag() > maxVelocity) {
            velocity.setMagnitude(maxVelocity);
        }
        
        position.inc(velocity);
    }
    
    private void joinGroup(Fish fish) {
        if (fish.equals(this)) return;
        if (getGroup().contains(fish)) return;
        
        if (!isLeader) {
            leader.joinGroup(fish);
            return;
        }
        
        fish.leaveGroup();
        fish.isLeader = false;
        fish.leader = this;
        group.add(fish);
    }
    
    private void leaveGroup() {
        target.set(0,0);
        
        if (!isLeader) {
            leader.group.remove(this);
            leader = null;
            isLeader = true;
            return;
        }
        
        if (group.isEmpty()) return;
        
        Fish newLeader = group.getFirst();
        newLeader.leaveGroup();
        ArrayList<Fish> tempGroup = new ArrayList<>(group);
        for (Fish fish : tempGroup) newLeader.joinGroup(fish);
    }
    
    
    private int getCurrentGroupSize() {
        if (isLeader) return group.size() + 1;
        return leader.getCurrentGroupSize();
    }
    
    private ArrayList<Fish> getGroup() {
        if (isLeader) return group;
        return leader.getGroup();
    }
    
    
    public void render(Graphics2D g2d) {
//        if (isLeader) {
//            g2d.setColor(Color.GREEN);
//        } else {
//            g2d.setColor(Color.CYAN);
//            g2d.drawLine((int) position.x, (int) position.y, (int) leader.position.x, (int) leader.position.y);
        g2d.setColor(color);
//        }
        
        g2d.fillOval((int) position.x, (int) position.y, size, size);
        
//        g2d.setColor(Color.MAGENTA);
//        g2d.drawLine((int) position.x, (int) position.y, (int) target.x, (int) target.y);

//        g2d.drawOval((int) (position.x - viewDistance / 2), (int) (position.y - viewDistance / 2), (int) viewDistance, (int) viewDistance);
    }
    
}
