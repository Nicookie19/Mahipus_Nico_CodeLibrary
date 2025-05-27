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
        heroes.add(new Knight());
        heroes.add(new Mage());
        heroes.add(new Archer());
        heroes.add(new Rook());
    }

    public List<Hero> getHeroes() {
        return heroes;
    }
}