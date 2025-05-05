package com.adi3000.aquarium.main;

import com.adi3000.aquarium.math.Vector2;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MouseHandler implements MouseMotionListener {
    
    private static MouseHandler instance;
    
    private MouseHandler() {}
    
    
    public Vector2 mousePos = new Vector2();
    
    
    public void mouseDragged(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {
        mousePos.set((double) e.getX() / Game.SCREEN_SCALE, (double) e.getY() / Game.SCREEN_SCALE);
    }
    
    public static MouseHandler getInstance() {
        if (instance == null) {
            instance = new MouseHandler();
        }
        return instance;
    }
   
}
