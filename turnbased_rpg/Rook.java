package com.mycompany.turnbased_rpg;

import java.util.Random;

public class Rook extends Hero {
    public Rook() {
        super(new Random());
        maxHP = 700;
        hp = maxHP;
        maxMana = 80;
        mana = maxMana;
        minDmg = 20;
        maxDmg = 50;
        attackNames = new String[] {"Hammer Strike", "Shield Slam", "Guard Bash", "Crushing Blow", "Earthquake"};
    }

    @Override
    public String getClassName() {
        return "Rook";
    }

    // Fortress passive: Reduces all damage taken by 30%
    @Override
    public void receiveDamage(int dmg) {
        int reduced = (int)(dmg * 0.7);
        super.receiveDamage(reduced);
        System.out.println("Fortress passive! Damage reduced from " + dmg + " to " + reduced + "!");
    }

    @Override
    public void useSkill(int skillIdx, Enemy enemy) {
        int dmg = minDmg + random.nextInt(maxDmg - minDmg + 1);
        switch (attackNames[skillIdx]) {
            case "Guard Bash":
                System.out.println("You stun the enemy with Guard Bash for " + dmg + " damage!");
                enemy.receiveDamage(dmg);
                enemy.stunnedForNextTurn = true;
                break;
            case "Earthquake":
                System.out.println("You shake the ground with Earthquake, dealing heavy damage!");
                enemy.receiveDamage(dmg + 30);
                break;
            default:
                System.out.println("You use " + attackNames[skillIdx] + " and deal " + dmg + " damage!");
                enemy.receiveDamage(dmg);
                break;
        }
    }
}