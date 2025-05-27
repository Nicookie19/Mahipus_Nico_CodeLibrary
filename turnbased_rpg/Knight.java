package com.mycompany.turnbased_rpg;

import java.util.Random;

public class Knight extends Hero {
    public Knight() {
        super(new Random());
        maxHP = 600;
        hp = maxHP;
        maxMana = 100;
        mana = maxMana;
        minDmg = 30;
        maxDmg = 70;
        attackNames = new String[] {"Slash", "Heavy Slash", "Shield Bash", "Piercing Thrust", "Whirlwind"};
    }

    @Override
    public String getClassName() {
        return "Knight";
    }

    // Phalanx passive: 20% chance to block all damage each enemy turn
    @Override
    public void receiveDamage(int dmg) {
        if (random.nextInt(100) < 20) {
            System.out.println("Phalanx passive! You block the attack completely!");
        } else {
            super.receiveDamage(dmg);
        }
    }

    @Override
    public void useSkill(int skillIdx, Enemy enemy) {
        int dmg = minDmg + random.nextInt(maxDmg - minDmg + 1);
        switch (attackNames[skillIdx]) {
            case "Shield Bash":
                System.out.println("You smash with Shield Bash! Enemy is stunned and takes " + dmg + " damage!");
                enemy.receiveDamage(dmg);
                enemy.stunnedForNextTurn = true;
                break;
            case "Whirlwind":
                System.out.println("You spin with Whirlwind, hitting twice!");
                int dmg2 = minDmg + random.nextInt(maxDmg - minDmg + 1);
                enemy.receiveDamage(dmg);
                enemy.receiveDamage(dmg2);
                break;
            default:
                System.out.println("You use " + attackNames[skillIdx] + " and deal " + dmg + " damage!");
                enemy.receiveDamage(dmg);
                break;
        }
    }
}