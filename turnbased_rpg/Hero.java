package com.mycompany.turnbased_rpg;

import java.util.Random;

public abstract class Hero {
    Random random;
    public int hp;
    public int maxHP;
    public int minDmg;
    public int maxDmg;
    public int mana;
    public int maxMana;
    public int gold; // Currency

    int phalanxCooldown = 0;
    boolean thornsActive = false;

    public String[] attackNames = new String[5];

    // Passives can be handled in decrementCooldowns or useSkill

    public Hero(Random random) {
        this.random = random;
        this.gold = 50; // Starting gold for each hero
    }

    public abstract String getClassName();

    public void showAttacks() {
        for (int i = 0; i < attackNames.length; i++) {
            System.out.println((i + 1) + ". " + attackNames[i]);
        }
    }

    public void addGold(int amount) {
        gold += amount;
        System.out.println("You received " + amount + " gold. Total gold: " + gold);
    }

    public boolean spendGold(int amount) {
        if (gold >= amount) {
            gold -= amount;
            System.out.println("You spent " + amount + " gold. Remaining gold: " + gold);
            return true;
        } else {
            System.out.println("Not enough gold!");
            return false;
        }
    }

    public void receiveDamage(int dmg) {
        hp -= dmg;
        if (hp < 0) hp = 0;
        System.out.println(getClassName() + " takes " + dmg + " damage.");
    }

    // For passives like Mage's heal, Knight's block, etc
    public void decrementCooldowns() {
        // Default: do nothing, override in subclasses
    }

    // Each class can override this for unique skill logic
    public void useSkill(int skillIdx, Enemy enemy) {
        int baseDmg = minDmg + random.nextInt(maxDmg - minDmg + 1);
        System.out.println("You use " + attackNames[skillIdx] + " and deal " + baseDmg + " damage!");
        enemy.receiveDamage(baseDmg);
    }
}