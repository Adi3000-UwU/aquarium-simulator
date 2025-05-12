package com.adi3000.aquarium.objects;

import com.adi3000.aquarium.main.Game;
import com.adi3000.aquarium.math.Vector2;

import java.util.ArrayList;

public abstract class GroupFish extends Fish{
    
    public boolean isLeader = true;
    public ArrayList<GroupFish> group = new ArrayList<>();
    public GroupFish leader;
    
    protected double viewDistance;
    protected int maxGroupSize;
    protected int minGroupSize;
    
    
    @Override
    public void tick() {
        groupManagment();
        
        findTarget();
        goToTarget(target.add(getSeperationOffset()));
    }
    
    
    protected abstract ArrayList<GroupFish> getNearbyFish();
    
    protected void groupManagment() {
        ArrayList<GroupFish> fishNearby = getNearbyFish();
        
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
    
    protected void findTarget() {
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
    
    
    protected void joinGroup(GroupFish fish) {
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
    
    protected void leaveGroup() {
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
    
    
    protected int getCurrentGroupSize() {
        if (isLeader) return group.size() + 1;
        return leader.getCurrentGroupSize();
    }
    
    protected ArrayList<GroupFish> getGroup() {
        if (isLeader) return group;
        return leader.getGroup();
    }
}
