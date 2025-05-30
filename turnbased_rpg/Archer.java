package com.mycompany.turnbased_rpg;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Archer extends Hero {
    private int multiShotCooldown = 0;
    private int poisonCooldown = 0;
    private boolean poisonedEnemy = false;
    private int poisonTurnsRemaining = 0;
    private int poisonDamagePerTurn;

    public Archer() {
        super(new Random());
        this.maxHP = 220;
        this.hp = this.maxHP;
        this.minDmg = 15;
        this.maxDmg = 35;
        this.maxMana = 80;
        this.mana = this.maxMana;
        this.attackNames = new String[]{"Multi-Shot", "Aimed Shot", "Poison Arrow", "Snipe"};
    }

    @Override
    public String getClassName() {
        return "Archer";
    }

    @Override
    protected List<String> getAllowedWeapons() {
        return Arrays.asList("Hunting Bow", "Longbow", "Composite Bow", "Elven Bow", "Glass Bow", "Daedric Bow");
    }

    @Override
    protected List<String> getAllowedArmors() {
        return Arrays.asList("Leather Armor", "Cloak of Shadows");
    }

    @Override
    public void decrementCooldowns() {
        if (multiShotCooldown > 0) multiShotCooldown--;
        if (poisonCooldown > 0) poisonCooldown--;
        if (poisonTurnsRemaining > 0) {
            poisonTurnsRemaining--;
            if (poisonTurnsRemaining == 0) {
                poisonedEnemy = false;
            }
        }
    }

    @Override
    public void useSkill(int skillIdx, Enemy enemy) {
        double multiplier = getSkillMultiplier();
        if (poisonedEnemy && poisonTurnsRemaining > 0) {
            System.out.println("Enemy takes " + poisonDamagePerTurn + " poison damage!");
            enemy.receiveDamage(poisonDamagePerTurn);
        }
        switch (skillIdx) {
            case 0: // Multi-Shot
                if (multiShotCooldown == 0 && mana >= 15) {
                    int baseDamage = minDmg + random.nextInt(maxDmg - minDmg + 1);
                    int damage = (int)(baseDamage * multiplier);
                    System.out.println("You fire Multi-Shot and deal " + damage + " damage to all enemies!");
                    enemy.receiveDamage(damage);
                    mana -= 15;
                    multiShotCooldown = 4;
                } else {
                    System.out.println("Multi-Shot is on cooldown or insufficient mana! Firing normal shot.");
                    super.useSkill(1, enemy);
                }
                break;
            case 1: // Aimed Shot
                super.useSkill(1, enemy);
                break;
            case 2: // Poison Arrow
                if (poisonCooldown == 0 && mana >= 10) {
                    int baseInitialDamage = minDmg + random.nextInt(10);
                    int initialDamage = (int)(baseInitialDamage * multiplier);
                    System.out.println("You fire a Poison Arrow and deal " + initialDamage + " damage, poisoning the enemy!");
                    enemy.receiveDamage(initialDamage);
                    poisonedEnemy = true;
                    poisonTurnsRemaining = 3;
                    poisonDamagePerTurn = (int)(5 * multiplier);
                    mana -= 10;
                    poisonCooldown = 3;
                } else {
                    System.out.println("Poison Arrow is on cooldown or insufficient mana! Firing normal shot.");
                    super.useSkill(1, enemy);
                }
                break;
            case 3: // Snipe
                if (mana >= 15) {
                    int baseDamage = minDmg + random.nextInt(maxDmg - minDmg + 1);
                    int damage = (int)(baseDamage * 2 * multiplier);
                    System.out.println("You fire Snipe and deal " + damage + " damage!");
                    enemy.receiveDamage(damage);
                    mana -= 15;
                } else {
                    System.out.println("Insufficient mana for Snipe! Firing normal shot.");
                    super.useSkill(1, enemy);
                }
                break;
            default:
                super.useSkill(1, enemy);
                break;
        }
    }
}