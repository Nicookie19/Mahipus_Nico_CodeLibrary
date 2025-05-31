/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.turnbased_rpg;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Assasin extends Hero {
    private int backstabCooldown = 0;
    private int smokeBombCooldown = 0;
    boolean smokeBombActive = false;

    public Assasin() {
        super(new Random());
        this.maxHP = 200;
        this.hp = this.maxHP;
        this.minDmg = 20;
        this.maxDmg = 40;
        this.maxMana = 70;
        this.mana = this.maxMana;
        this.attackNames = new String[]{"Backstab", "Quick Strike", "Smoke Bomb", "Assassinate"};
    }

    @Override
    public String getClassName() {
        return "Assassin";
    }

    @Override
    protected List<String> getAllowedWeapons() {
        return Arrays.asList("Iron Dagger", "Steel Dagger", "Mithril Dagger", "Elven Dagger", "Glass Dagger", "Daedric Dagger");
    }

    @Override
    protected List<String> getAllowedArmors() {
        return Arrays.asList("Leather Armor", "Cloak of Shadows");
    }

    @Override
    public void decrementCooldowns() {
        if (backstabCooldown > 0) backstabCooldown--;
        if (smokeBombCooldown > 0) {
            smokeBombCooldown--;
            if (smokeBombCooldown == 0) {
                smokeBombActive = false;
            }
        }
    }

    @Override
    public void applyPassiveEffects() {
        // 10% chance to dodge enemy attacks, handled in encounter
    }

    @Override
    public void useSkill(int skillIdx, Enemy enemy) {
        double multiplier = getSkillMultiplier();
        switch (skillIdx) {
            case 0: // Backstab
                if (backstabCooldown == 0 && mana >= 15) {
                    int baseDamage = minDmg + random.nextInt(maxDmg - minDmg + 1);
                    boolean isCrit = random.nextInt(100) < 30; // 30% crit chance
                    int damage = (int)(baseDamage * (isCrit ? 3 : 2) * multiplier);
                    System.out.println("You use Backstab and deal " + damage + (isCrit ? " critical" : "") + " damage!");
                    enemy.receiveDamage(damage);
                    mana -= 15;
                    backstabCooldown = 4;
                } else {
                    System.out.println("Backstab is on cooldown or insufficient mana! Using normal attack.");
                    super.useSkill(1, enemy);
                }
                break;
            case 1: // Quick Strike
                super.useSkill(1, enemy);
                break;
            case 2: // Smoke Bomb
                if (smokeBombCooldown == 0 && mana >= 10) {
                    System.out.println("You use Smoke Bomb, reducing enemy accuracy!");
                    smokeBombActive = true;
                    mana -= 10;
                    smokeBombCooldown = 3;
                } else {
                    System.out.println("Smoke Bomb is on cooldown or insufficient mana! Using normal attack.");
                    super.useSkill(1, enemy);
                }
                break;
            case 3: // Assassinate
                if (mana >= 25) {
                    int baseDamage = minDmg + random.nextInt(maxDmg - minDmg + 1);
                    int damage = (int)(baseDamage * 2.5 * multiplier);
                    if (enemy.hp < enemy.maxHP * 0.3 && random.nextInt(100) < 20) {
                        damage = enemy.hp; // Instant kill
                        System.out.println("You use Assassinate and instantly kill the enemy!");
                    } else {
                        System.out.println("You use Assassinate and deal " + damage + " damage!");
                    }
                    enemy.receiveDamage(damage);
                    mana -= 25;
                } else {
                    System.out.println("Insufficient mana for Assassinate! Using normal attack.");
                    super.useSkill(1, enemy);
                }
                break;
            default:
                super.useSkill(1, enemy);
                break;
        }
    }
}
