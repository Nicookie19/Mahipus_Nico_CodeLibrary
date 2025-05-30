package com.mycompany.turnbased_rpg;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Random;

public class Enemy implements Serializable {
    private static final long serialVersionUID = 1L;
    Random random;
    int hp;
    int maxHP = 500;
    int minDmg = 10;
    int maxDmg = 90;

    int gluttonyCooldown = 0;
    boolean bleedingActive = false;
    public boolean stunnedForNextTurn = false;
    boolean nextAttackIsDoubleDamage = false;

    LinkedList<String> enemyNames;
    String currentName;

    // Unique attack names for enemies
    String[] attackNames = new String[] {
        "Savage Strike",        // High damage
        "Venomous Bite",        // Chance to poison
        "Roar",                 // Chance to stun
        "Shadow Heal",          // Heals self
        "Rend",                 // Bleed effect
        "Frenzy",               // Multi-hit
        "Gluttony"              // Steal HP (boss ability)
    };

    public Enemy() {
        random = new Random();
        hp = maxHP;
        initializeEnemyNames();
        currentName = getRandomEnemyName();
        System.out.println("A " + currentName + " has appeared!");
    }

    private void initializeEnemyNames() {
        enemyNames = new LinkedList<>();
        // Updated enemy names to include new types
        enemyNames.add("Goblin");
        enemyNames.add("Orc");
        enemyNames.add("Wolf");
        enemyNames.add("Bandit");
        enemyNames.add("Troll");
        enemyNames.add("Vampire");
        enemyNames.add("Werewolf");
        enemyNames.add("Wyvern");
        enemyNames.add("Giant");
        enemyNames.add("Demon");
        enemyNames.add("Phantom");
        enemyNames.add("Wraith");
        enemyNames.add("Banshee");
        enemyNames.add("Mummy");
        enemyNames.add("Golem");
        enemyNames.add("Hydra");
        enemyNames.add("Minotaur");
        enemyNames.add("Chimera");
        enemyNames.add("Griffin");
        enemyNames.add("Phoenix");
        enemyNames.add("Kraken");
        enemyNames.add("Cerberus");
        enemyNames.add("Yeti");
        enemyNames.add("Sphinx");
        enemyNames.add("Nymph");
        enemyNames.add("Sprite");
        enemyNames.add("Imp");
        enemyNames.add("Basilisk");
        enemyNames.add("Gargoyle");
        enemyNames.add("Harpy");
        enemyNames.add("Manticore");
        enemyNames.add("Centaur");
        enemyNames.add("Cyclops");
        enemyNames.add("Djinn");
        enemyNames.add("Lich");
        enemyNames.add("Ogre");
        enemyNames.add("Satyr");
        enemyNames.add("Selkie");
        enemyNames.add("Succubus");
        enemyNames.add("Wendigo");
        enemyNames.add("Zombie Lord");
        enemyNames.add("Dark Elf");
        enemyNames.add("Nightmare");
        enemyNames.add("Shade");
        enemyNames.add("Slime");
        enemyNames.add("Troll King");
        enemyNames.add("Witch");
        enemyNames.add("Harbinger");
        enemyNames.add("Warlock");
    }

    public String getCurrentName() {
        return currentName;
    }

    public void changeName() {
        currentName = getRandomEnemyName();
    }

    private String getRandomEnemyName() {
        return enemyNames.get(random.nextInt(enemyNames.size()));
    }

    public void receiveDamage(int dmg) {
        hp -= dmg;
        if (hp < 0) hp = 0;
        System.out.println("Enemy (" + currentName + ") takes " + dmg + " damage.");
    }

    public boolean isAlive() {
        return hp > 0;
    }

    // Use unique attacks each turn
    public void takeTurn(Hero player) {
        if (gluttonyCooldown > 0) gluttonyCooldown--;

        // If stunned, skip turn
        if (stunnedForNextTurn) {
            System.out.println("Enemy (" + currentName + ") is stunned and cannot act!");
            stunnedForNextTurn = false;
            return;
        }

        int attackIdx = random.nextInt(attackNames.length);
        String attack = attackNames[attackIdx];

        switch (attack) {
            case "Savage Strike":
                int savageDmg = random.nextInt(30) + 60; // High damage
                System.out.println("Enemy (" + currentName + ") uses Savage Strike and deals " + savageDmg + " damage!");
                player.receiveDamage(savageDmg);
                break;
            case "Venomous Bite":
                int venomDmg = random.nextInt(30) + 25;
                System.out.println("Enemy (" + currentName + ") uses Venomous Bite and deals " + venomDmg + " damage!");
                player.receiveDamage(venomDmg);
                if (random.nextInt(100) < 30) {
                    System.out.println("You have been poisoned! (You lose 10 HP at the start of your next turn)");
                    player.hp -= 10;
                    if (player.hp < 0) player.hp = 0;
                }
                break;
            case "Roar":
                int roarDmg = random.nextInt(15) + 10;
                System.out.println("Enemy (" + currentName + ") uses Roar and deals " + roarDmg + " damage!");
                player.receiveDamage(roarDmg);
                if (random.nextInt(100) < 25) {
                    System.out.println("You are stunned and will miss your next turn!");
                    // You can implement a player stun mechanic if desired.
                }
                break;
            case "Shadow Heal":
                int healAmt = random.nextInt(30) + 20;
                hp += healAmt;
                if (hp > maxHP) hp = maxHP;
                System.out.println("Enemy (" + currentName + ") uses Shadow Heal and restores " + healAmt + " HP!");
                break;
            case "Rend":
                int rendDmg = random.nextInt(20) + 35;
                System.out.println("Enemy (" + currentName + ") uses Rend and deals " + rendDmg + " damage! You are bleeding!");
                player.receiveDamage(rendDmg);
                System.out.println("You lose 10 HP from bleeding.");
                player.hp -= 10;
                if (player.hp < 0) player.hp = 0;
                break;
            case "Frenzy":
                System.out.println("Enemy (" + currentName + ") uses Frenzy and strikes twice!");
                for (int i = 0; i < 2; i++) {
                    int frenzyDmg = random.nextInt(25) + 20;
                    player.receiveDamage(frenzyDmg);
                }
                break;
            case "Gluttony":
                if (gluttonyCooldown == 0 && player.hp < 250) {
                    int chance = random.nextInt(100);
                    if (chance < 40) {
                        int steal = (int) (player.hp * 0.15);
                        player.hp -= steal;
                        if (player.hp < 0) player.hp = 0;
                        hp += steal;
                        if (hp > maxHP) hp = maxHP;
                        System.out.println("Enemy (" + currentName + ") uses Gluttony! Steals " + steal + " HP and heals itself.");
                        gluttonyCooldown = 4;
                    } else {
                        System.out.println("Enemy (" + currentName + ") tries to use Gluttony but fails!");
                    }
                } else {
                    // Fallback to a normal attack if Gluttony is on cooldown or player HP is high
                    int fallbackDmg = random.nextInt(maxDmg - minDmg + 1) + minDmg;
                    System.out.println("Enemy (" + currentName + ") attacks and deals " + fallbackDmg + " damage.");
                    player.receiveDamage(fallbackDmg);
                }
                break;
            default:
                int dmg = random.nextInt(maxDmg - minDmg + 1) + minDmg;
                System.out.println("Enemy (" + currentName + ") attacks and deals " + dmg + " damage.");
                player.receiveDamage(dmg);
                break;
        }
    }
}
