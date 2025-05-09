package com.adi3000.aquarium.objects;

import com.adi3000.aquarium.main.Game;
import com.adi3000.aquarium.math.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GroupFish extends Fish{
    
    public boolean isLeader = true;
    public ArrayList<GroupFish> group = new ArrayList<>();
    public GroupFish leader;
    
    private double viewDistance = 60;
    private int maxGroupSize = 15;
    private int minGroupSize = 10;
    
    public GroupFish(Vector2 position) {
        super(position);
        
        color = new Color(0xE54848);
        size = 10;
    }
    
    @Override
    public void tick() {
        groupManagment();
        
        findTarget();
        goToTarget(target.add(getSeperationOffset()));
    }
    
    
    private void groupManagment() {
        ArrayList<GroupFish> fishNearby = Game.gameManager.getFishInRange(position, viewDistance).stream()
                .filter(GroupFish.class::isInstance)
                .map(GroupFish.class::cast).collect(Collectors.toCollection(ArrayList::new));
        
        fishNearby.remove(this);
        fishNearby.removeAll(getGroup());
        
        GroupFish fishWithBestGroup = null;
        double bestFishScore = Integer.MAX_VALUE;
        
        double myGroupScore = Math.abs(0.3 - (double) (getCurrentGroupSize() - minGroupSize) / (maxGroupSize - minGroupSize));
        
        for (GroupFish fish : fishNearby) {
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
                target.set(getDefaultTarget());
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
    
    
    private void joinGroup(GroupFish fish) {
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
        
        GroupFish newLeader = group.getFirst();
        newLeader.leaveGroup();
        ArrayList<GroupFish> tempGroup = new ArrayList<>(group);
        for (GroupFish fish : tempGroup) newLeader.joinGroup(fish);
    }
    
    
    private int getCurrentGroupSize() {
        if (isLeader) return group.size() + 1;
        return leader.getCurrentGroupSize();
    }
    
    private ArrayList<GroupFish> getGroup() {
        if (isLeader) return group;
        return leader.getGroup();
    }
}
