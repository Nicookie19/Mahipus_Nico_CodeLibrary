package com.mycompany.turnbased_rpg;

import java.io.Serializable;
import java.util.Random;

public abstract class Hero implements Serializable {
    private static final long serialVersionUID = 1L;

    protected transient Random random = new Random();
    public int hp;
    public int maxHP;
    public int minDmg;
    public int maxDmg;
    public int mana;
    public int maxMana;
    public int gold; // Currency

    // RPG Stats
    public int xp = 0;
    public int level = 1;
    public int xpToLevel = 50;

    int phalanxCooldown = 0;
    boolean thornsActive = false;

    public String[] attackNames = new String[5];

    // Passives can be handled in decrementCooldowns or useSkill

    public Hero(Random random) {
        if (random != null)
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

    public void addXP(int amount) {
        xp += amount;
        System.out.println("You gained " + amount + " XP. Total XP: " + xp + "/" + xpToLevel);
    }

    public boolean checkLevelUp() {
        return xp >= xpToLevel;
    }

    public void levelUp() {
        while (xp >= xpToLevel) {
            xp -= xpToLevel;
            level++;
            maxHP += 40;
            maxMana += 10;
            minDmg += 5;
            maxDmg += 10;
            hp = maxHP;
            mana = maxMana;
            xpToLevel = (int) (xpToLevel * 1.4);
            System.out.println("You leveled up to level " + level + "!");
            System.out.println("Stats increased! HP and Mana restored.");
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

    public String getStatsString() {
        return
            "Class: " + getClassName() + "\n" +
            "Level: " + level + "\n" +
            "XP: " + xp + "/" + xpToLevel + "\n" +
            "HP: " + hp + "/" + maxHP + "\n" +
            "Mana: " + mana + "/" + maxMana + "\n" +
            "Attack: " + minDmg + " - " + maxDmg + "\n" +
            "Gold: " + gold;
    }
}