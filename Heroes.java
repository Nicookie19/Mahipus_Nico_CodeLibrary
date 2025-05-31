package com.mycompany.turnbased_rpg;

import java.util.ArrayList;
import java.util.List;

public class Heroes {
    private List<Hero> heroes;

    public Heroes() {
        heroes = new ArrayList<>();
        initializeHeroes();
    }

    private void initializeHeroes() {
        // Initialize the heroes with all classes
        heroes.add(new Knight());   // Knight class
        heroes.add(new Mage());     // Mage class
        heroes.add(new Archer());   // Archer class
        heroes.add(new Rook());     // Rook class
        heroes.add(new Assasin()); // Assassin class
        heroes.add(new Cleric());   // Cleric class
    }

    public List<Hero> getHeroes() {
        return heroes;
    }
}