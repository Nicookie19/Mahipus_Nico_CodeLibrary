package com.mycompany.turnbased_rpg;

import java.io.Serializable;
import java.util.*;

public class Enemy implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Tier { WEAK, NORMAL, STRONG }
    private Tier tier;

    Random random;
    int hp;
    int maxHP;
    int minDmg;
    int maxDmg;
    int level; // Added to scale with player

    int gluttonyCooldown = 0;
    boolean bleedingActive = false;
    public boolean stunnedForNextTurn = false;
    boolean nextAttackIsDoubleDamage = false;

    LinkedList<String> enemyNames;
    String currentName;
    private Map<String, Runnable> specialAbilities = new HashMap<>();
    private String selectedAbility; // Current ability for this enemy

    String[] attackNames = {
        "Savage Strike", "Venomous Bite", "Roar",
        "Shadow Heal", "Rend", "Frenzy", "Gluttony"
    };

    public Enemy() {
        this(Tier.NORMAL);
    }

    public Enemy(Tier tier) {
        this(tier, 1); // Default level 1
    }

    public Enemy(Tier tier, int playerLevel) {
        this.tier = tier;
        this.random = new Random();
        this.level = playerLevel;
        initializeStats();
        initializeEnemyNames();
        this.currentName = getRandomEnemyName();
        initializeSpecialAbilities();
        selectRandomAbility();
        // Scale stats based on player level
        maxHP *= (1 + level * 0.1);
        minDmg *= (1 + level * 0.1);
        maxDmg *= (1 + level * 0.1);
        hp = maxHP;
        System.out.println("A " + getDisplayName() + " (Level " + level + ") has appeared!");
    }

    private void initializeStats() {
        switch (tier) {
            case WEAK:
                maxHP = 50 + random.nextInt(20); // 50-70
                minDmg = 5 + random.nextInt(5); // 5-10
                maxDmg = 10 + random.nextInt(5); // 10-15
                break;
            case NORMAL:
                maxHP = 80 + random.nextInt(30); // 80-110
                minDmg = 10 + random.nextInt(10); // 10-20
                maxDmg = 20 + random.nextInt(10); // 20-30
                break;
            case STRONG:
                maxHP = 120 + random.nextInt(50); // 120-170
                minDmg = 15 + random.nextInt(15); // 15-30
                maxDmg = 30 + random.nextInt(15); // 30-45
                break;
        }
        hp = maxHP;
    }

    private void initializeSpecialAbilities() {
        specialAbilities.put("Minor Heal", () -> {
            int heal = (int)(maxHP * 0.15);
            hp = Math.min(hp + heal, maxHP);
            System.out.println(currentName + " uses Minor Heal, restoring " + heal + " HP!");
        });
        specialAbilities.put("Poison Strike", () -> {
            System.out.println(currentName + " inflicts poison! You lose 5 HP next turn.");
            // Handled in takeTurn
        });
        specialAbilities.put("Multi Attack", () -> {
            System.out.println(currentName + " attacks twice!");
            // Handled in takeTurn
        });
    }

    private void selectRandomAbility() {
        List<String> abilityKeys = new ArrayList<>(specialAbilities.keySet());
        selectedAbility = abilityKeys.get(random.nextInt(abilityKeys.size()));
    }

    private void initializeEnemyNames() {
        enemyNames = new LinkedList<>();
        
        // Hostile enemies
        enemyNames.addAll(Arrays.asList(
            "Goblin", "Orc", "Wolf", "Bandit", "Troll", "Vampire",
            "Werewolf", "Wyvern", "Giant", "Demon", "Phantom", "Wraith",
            "Banshee", "Mummy", "Golem", "Hydra", "Minotaur", "Chimera",
            "Griffin", "Kraken", "Cerberus", "Basilisk", "Gargoyle", "Harpy"
        ));

        // Expanded docile NPCs
        enemyNames.addAll(Arrays.asList(
            "Nymph", "Sprite", "Centaur", "Selkie", "Phoenix",
            "Pixie Trickster", "Eldertree Ent", "Will-o'-Wisp",
            "Dreamweaver", "Frost Naiad",
            "Herbalist", "Bard", "Scholar", "Blacksmith", "Alchemist",
            "Seer", "Ranger", "Monk", "Traveler", "Merchant"
        ));
    }

    public String getCurrentName() {
        return currentName;
    }

    private String getRandomEnemyName() {
        return enemyNames.get(random.nextInt(enemyNames.size()));
    }

    public void changeName(String newName) {
        if (enemyNames.contains(newName)) {
            this.currentName = newName;
        } else {
            this.currentName = getRandomEnemyName();
        }
    }

    public void receiveDamage(int dmg) {
        hp -= dmg;
        if (hp < 0) {
            hp = 0;
        }
        System.out.println("Enemy (" + currentName + ") takes " + dmg + " damage.");
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void takeTurn(Hero player) {
        if (gluttonyCooldown > 0) {
            gluttonyCooldown--;
        }

        if (stunnedForNextTurn) {
            System.out.println("Enemy (" + currentName + ") is stunned and cannot act!");
            stunnedForNextTurn = false;
            return;
        }

        // Apply special ability
        if (random.nextInt(100) < 30) { // 30% chance to use ability
            specialAbilities.get(selectedAbility).run();
            if (selectedAbility.equals("Poison Strike")) {
                player.receiveDamage(5); // Poison damage
            } else if (selectedAbility.equals("Multi Attack")) {
                for (int i = 0; i < 2; i++) {
                    int dmg = random.nextInt(maxDmg - minDmg + 1) + minDmg;
                    System.out.println("Enemy (" + currentName + ") attacks for " + dmg + " damage!");
                    player.receiveDamage(dmg);
                }
                return;
            }
        }

        int attackIdx = random.nextInt(attackNames.length);
        String attack = attackNames[attackIdx];

        switch (attack) {
            case "Savage Strike":
                int savageDmg = random.nextInt(20) + 20; // Reduced damage
                System.out.println("Enemy (" + currentName + ") uses Savage Strike and deals " + savageDmg + " damage!");
                player.receiveDamage(savageDmg);
                break;
            case "Venomous Bite":
                int venomDmg = random.nextInt(15) + 15;
                System.out.println("Enemy (" + currentName + ") uses Venomous Bite and deals " + venomDmg + " damage!");
                player.receiveDamage(venomDmg);
                if (random.nextInt(100) < 20) {
                    System.out.println("You have been poisoned! (You lose 5 HP next turn)");
                    player.receiveDamage(5);
                }
                break;
            case "Roar":
                int roarDmg = random.nextInt(10) + 5;
                System.out.println("Enemy (" + currentName + ") uses Roar and deals " + roarDmg + " damage!");
                player.receiveDamage(roarDmg);
                if (random.nextInt(100) < 15) {
                    System.out.println("You are stunned and will miss your next turn!");
                    // Implement player stun if desired
                }
                break;
            case "Shadow Heal":
                int healAmt = random.nextInt(20) + 10;
                hp += healAmt;
                if (hp > maxHP) {
                    hp = maxHP;
                }
                System.out.println("Enemy (" + currentName + ") uses Shadow Heal and restores " + healAmt + " HP!");
                break;
            case "Rend":
                int rendDmg = random.nextInt(15) + 20;
                System.out.println("Enemy (" + currentName + ") uses Rend and deals " + rendDmg + " damage! You are bleeding!");
                player.receiveDamage(rendDmg);
                System.out.println("You lose 5 HP from bleeding.");
                player.receiveDamage(5);
                break;
            case "Frenzy":
                System.out.println("Enemy (" + currentName + ") uses Frenzy and strikes twice!");
                for (int i = 0; i < 2; i++) {
                    int frenzyDmg = random.nextInt(15) + 10;
                    player.receiveDamage(frenzyDmg);
                }
                break;
            case "Gluttony":
                if (gluttonyCooldown == 0 && player.hp < 150) {
                    int chance = random.nextInt(100);
                    if (chance < 30) {
                        int steal = (int) (player.hp * 0.1);
                        player.receiveDamage(steal);
                        hp += steal;
                        if (hp > maxHP) {
                            hp = maxHP;
                        }
                        System.out.println("Enemy (" + currentName + ") uses Gluttony! Steals " + steal + " HP and heals itself.");
                        gluttonyCooldown = 4;
                    } else {
                        System.out.println("Enemy (" + currentName + ") tries to use Gluttony but fails!");
                    }
                } else {
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

    public boolean isHostile() {
        Set<String> alwaysHostile = new HashSet<>(Arrays.asList(
            "Goblin", "Orc", "Wolf", "Bandit", "Troll", "Vampire",
            "Werewolf", "Wyvern", "Giant", "Demon", "Phantom", "Wraith",
            "Banshee", "Mummy", "Golem", "Hydra", "Minotaur", "Chimera",
            "Griffin", "Kraken", "Cerberus", "Basilisk", "Gargoyle", "Harpy"
        ));
        return alwaysHostile.contains(currentName);
    }

    public boolean isDocile() {
        Set<String> alwaysDocile = new HashSet<>(Arrays.asList(
            "Nymph", "Sprite", "Centaur", "Selkie", "Phoenix",
            "Pixie Trickster", "Eldertree Ent", "Will-o'-Wisp",
            "Dreamweaver", "Frost Naiad",
            "Herbalist", "Bard", "Scholar", "Blacksmith", "Alchemist",
            "Seer", "Ranger", "Monk", "Traveler", "Merchant"
        ));
        return alwaysDocile.contains(currentName);
    }

    public void setHostile(boolean hostile) {
        if (hostile == isHostile()) {
            return; // No change needed
        }
        // Pick a new name from the appropriate set
        Set<String> hostileNames = new HashSet<>(Arrays.asList(
            "Goblin", "Orc", "Wolf", "Bandit", "Troll", "Vampire",
            "Werewolf", "Wyvern", "Giant", "Demon", "Phantom", "Wraith",
            "Banshee", "Mummy", "Golem", "Hydra", "Minotaur", "Chimera",
            "Griffin", "Kraken", "Cerberus", "Basilisk", "Gargoyle", "Harpy"
        ));
        Set<String> docileNames = new HashSet<>(Arrays.asList(
            "Nymph", "Sprite", "Centaur", "Selkie", "Phoenix",
            "Pixie Trickster", "Eldertree Ent", "Will-o'-Wisp",
            "Dreamweaver", "Frost Naiad",
            "Herbalist", "Bard", "Scholar", "Blacksmith", "Alchemist",
            "Seer", "Ranger", "Monk", "Traveler", "Merchant"
        ));
        List<String> targetNames = new ArrayList<>(hostile ? hostileNames : docileNames);
        currentName = targetNames.get(random.nextInt(targetNames.size()));
    }

    public String getDisplayName() {
        return (isHostile() ? "[Hostile] " : "[Friendly] ") + currentName + " (" + tier + ")";
    }
}