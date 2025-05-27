package com.mycompany.turnbased_rpg;

import java.util.Random;

public class Archer extends Hero {
    public Archer() {
        super(new Random());
        maxHP = 500;
        hp = maxHP;
        maxMana = 120;
        mana = maxMana;
        minDmg = 25;
        maxDmg = 85;
        attackNames = new String[] {"Quick Shot", "Power Shot", "Multi Arrow", "Poison Arrow", "Piercing Shot"};
    }

    @Override
    public String getClassName() {
        return "Archer";
    }

    // Rapid Volley passive: 25% chance for double attack
    @Override
    public void useSkill(int skillIdx, Enemy enemy) {
        int dmg = minDmg + random.nextInt(maxDmg - minDmg + 1);
        boolean rapidVolley = random.nextInt(100) < 25;
        switch (attackNames[skillIdx]) {
            case "Multi Arrow":
                System.out.println("You unleash Multi Arrow for two hits!");
                for (int i = 0; i < 2; i++) {
                    int admg = minDmg + random.nextInt(maxDmg - minDmg + 1);
                    enemy.receiveDamage(admg);
                }
                break;
            case "Poison Arrow":
                System.out.println("You shoot Poison Arrow for " + dmg + " damage and poison the enemy!");
                enemy.receiveDamage(dmg);
                // You could add a poison effect here
                break;
            default:
                System.out.print("You use " + attackNames[skillIdx] + " and deal " + dmg + " damage!");
                if (rapidVolley) {
                    System.out.print(" Rapid Volley passive triggers! Double attack!");
                    enemy.receiveDamage(dmg);
                }
                System.out.println();
                enemy.receiveDamage(dmg);
                break;
        }
    }
}