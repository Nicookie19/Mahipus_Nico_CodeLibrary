package com.mycompany.turnbased_rpg;

import java.util.ArrayList;
import java.util.List;

public class Enemies {
    private List<Enemy> enemies;

    public Enemies() {
        enemies = new ArrayList<>();
        initializeEnemies();
    }

    private void initializeEnemies() {
        // Initialize enemies here...
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }
}