package com.mycompany.turnbased_rpg;

import java.util.Random;

public class Mage extends Hero {
    int healAmount = 50;

    public Mage() {
        super(new Random());
        maxHP = 450;
        hp = maxHP;
        maxMana = 150;
        mana = maxMana;
        minDmg = 40;
        maxDmg = 80;
        attackNames = new String[] {"Fireball", "Ice Spike", "Lightning Bolt", "Arcane Blast", "Meteor Shower"};
    }

    @Override
    public String getClassName() {
        return "Mage";
    }

    // Arcane Recovery passive: heals every turn
    @Override
    public void decrementCooldowns() {
        if (hp < maxHP) {
            hp += healAmount;
            if (hp > maxHP) hp = maxHP;
            System.out.println("Arcane Recovery passive! Mage heals " + healAmount + " HP.");
        }
    }

    @Override
    public void useSkill(int skillIdx, Enemy enemy) {
        int dmg = minDmg + random.nextInt(maxDmg - minDmg + 1);
        switch (attackNames[skillIdx]) {
            case "Fireball":
                System.out.println("You cast Fireball for " + (dmg + 20) + " damage!");
                enemy.receiveDamage(dmg + 20);
                break;
            case "Meteor Shower":
                System.out.println("You call down Meteor Shower, hitting three times!");
                for (int i = 0; i < 3; i++) {
                    int mdmg = minDmg + random.nextInt(maxDmg - minDmg + 1);
                    enemy.receiveDamage(mdmg);
                }
                break;
            default:
                System.out.println("You use " + attackNames[skillIdx] + " and deal " + dmg + " damage!");
                enemy.receiveDamage(dmg);
                break;
        }
    }
}