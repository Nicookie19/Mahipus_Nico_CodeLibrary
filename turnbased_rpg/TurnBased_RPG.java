package com.mycompany.turnbased_rpg;

import java.io.*;
import java.util.*;

public class TurnBased_RPG {
    Scanner scan = new Scanner(System.in);

    Hero player;
    Enemy enemy;

    Stack<Integer> lastPlayerHP = new Stack<>();
    Stack<Integer> lastEnemyHP = new Stack<>();

    int gameTimer = 2;
    List<String> inventory = new ArrayList<>();
    boolean questActive = false;
    String currentQuest = "";
    List<String> questLog = new ArrayList<>();
    List<String> completedQuests = new ArrayList<>();

    String equippedWeapon = "Basic Sword";
    String equippedArmor = "Cloth Armor";

    public static void main(String[] args) {
        TurnBased_RPG game = new TurnBased_RPG();
        game.displayMainMenu();
    }

    public void displayMainMenu() {
        while (true) {
            System.out.println("===================================");
            System.out.println("     Legends of the Four Realms    ");
            System.out.println("===================================");
            System.out.println("1. Start New Game");
            System.out.println("2. Load Game");
            System.out.println("3. Exit");
            System.out.print("Choose an option (1-3): ");

            String input = scan.nextLine().trim();
            if (input.equals("1")) {
                startGame();
            } else if (input.equals("2")) {
                loadGame();
            } else if (input.equals("3")) {
                System.out.println("Thank you for playing! Goodbye!");
                break;
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void startGame() {
        System.out.println("Oh good, you're awake!"); // New starting message
        System.out.println("Choose your character:");
        System.out.println("1. Knight: Bjorn Ironmark The Dragonborn");
        System.out.println("2. Mage: Aurelion Thalor Arch Mage of Winterhold");
        System.out.println("3. Archer: Sorin Sandpiercer Nigthingale ");
        System.out.println("4. Rook: Ghorzug Stonefist Champion of Talos");
        System.out.print("Enter your choice (1-4): ");
        String choice = scan.nextLine().trim();
        switch (choice) {
            case "1": player = new Knight(); break;
            case "2": player = new Mage(); break;
            case "3": player = new Archer(); break;
            case "4": player = new Rook(); break;
            default: System.out.println("Invalid choice. Defaulting to Knight."); player = new Knight();
        }
        System.out.println("You are now a " + player.getClassName() + "!");
        // Give a sample quest
        questActive = true;
        currentQuest = "Defeat a Goblin in the Forest.";
        questLog.add(currentQuest);
        inGameMenu();
    }

    private void inGameMenu() {
        while (true) {
            System.out.println("\n===== In-Game Menu =====");
            System.out.println("1. Travel");
            System.out.println("2. View Inventory");
            System.out.println("3. View Equipment");
            System.out.println("4. Equip Item");
            System.out.println("5. View Gold");
            System.out.println("6. View Quest Log");
            System.out.println("7. View Player Stats");
            System.out.println("8. Rest at Inn");
            System.out.println("9. Save Game");
            System.out.println("10. Open Main Menu");
            System.out.print("Choose an option (1-10): ");
            String input = scan.nextLine().trim();

            switch (input) {
                case "1": travel(); break;
                case "2": viewInventory(); break;
                case "3": viewEquipment(); break;
                case "4": equipItem(); break;
                case "5": viewGold(); break;
                case "6": viewQuestLog(); break;
                case "7": viewPlayerStats(); break;
                case "8": restAtInn(); break;
                case "9": saveGame(); break;
                case "10": System.out.println("Returning to Main Menu..."); return;
                default: System.out.println("Invalid option. Try again.");
            }
        }
    }

    public void travel() {
        System.out.println("===================================");
        System.out.println("          Travel Locations         ");
        System.out.println("===================================");
        System.out.println("1. Whiterun");
        System.out.println("2. Riften");
        System.out.println("3. Solitude");
        System.out.println("4. Windhelm");
        System.out.println("5. Bleak Falls Barrow");
        System.out.println("6. Dwemer Ruins");
        System.out.println("7. Blackreach");
        System.out.println("8. The Throat of the World");
        System.out.println("9. Falkreath Forest");
        System.out.println("10. The Reach");
        System.out.print("Choose a location to travel to (1-10): ");

        String input = scan.nextLine().trim();
        switch (input) {
            case "1":
                encounterNPC("Whiterun", new String[]{
                    "Docile: Guard", "Docile: Merchant", "Hostile: Bandit"
                });
                break;
            case "2":
                encounterNPC("Riften", new String[]{
                    "Docile: Shopkeeper", "Hostile: Thief"
                });
                break;
            case "3":
                encounterNPC("Solitude", new String[]{
                    "Docile: Noble", "Hostile: Assassin"
                });
                break;
            case "4":
                encounterNPC("Windhelm", new String[]{
                    "Docile: Blacksmith", "Hostile: Stormcloak"
                });
                break;
            case "5":
                encounterNPC("Bleak Falls Barrow", new String[]{
                    "Hostile: Draugr", "Docile: Explorer"
                });
                break;
            case "6":
                encounterNPC("Dwemer Ruins", new String[]{
                    "Hostile: Dwarven Spider", "Docile: Researcher"
                });
                break;
            case "7":
                encounterNPC("Blackreach", new String[]{
                    "Hostile: Falmer", "Docile: Lost Explorer"
                });
                break;
            case "8":
                encounterNPC("The Throat of the World", new String[]{
                    "Docile: Greybeard", "Hostile: Dragon"
                });
                break;
            case "9":
                encounterNPC("Falkreath Forest", new String[]{
                    "Docile: Hunter", "Hostile: Wolf"
                });
                break;
            case "10":
                encounterNPC("The Reach", new String[]{
                    "Docile: Herbalist", "Hostile: Giant"
                });
                break;
            default:
                System.out.println("Invalid location. Please try again.");
                break;
        }
    }

    private void encounterNPC(String location, String[] npcs) {
        System.out.println("You travel to the " + location + ".");
        System.out.println("You see the following NPCs:");
        for (String npc : npcs) {
            System.out.println(npc);
        }

        while (true) {
            System.out.print("Do you want to interact with an NPC? (yes/no): ");
            String choice = scan.nextLine().trim();
            if (choice.equalsIgnoreCase("yes")) {
                System.out.print("Enter the name of the NPC you want to interact with: ");
                String npcName = scan.nextLine().trim();
                boolean found = false;
                for (String npc : npcs) {
                    if (npc.toLowerCase().contains(npcName.toLowerCase())) {
                        found = true;
                        if (npc.startsWith("Hostile")) {
                            interactWithHostileNPC(npcName, location, npc.startsWith("Hostile: Goblin"));
                        } else {
                            tradeWithNPC(npcName);
                        }
                        break;
                    }
                }
                if (!found) {
                    System.out.println("NPC not found in this location.");
                }
            } else if (choice.equalsIgnoreCase("no")) {
                System.out.println("You decide not to interact. You can choose another action.");
                break;
            } else {
                System.out.println("Invalid input.");
            }
        }
    }

    private void interactWithHostileNPC(String npcName, String location, boolean isQuestObjective) {
        System.out.println("You encountered a hostile NPC: " + npcName + "!");
        Enemy hostileEnemy = new Enemy(); // Replace with custom logic for enemy type
        this.enemy = hostileEnemy;
        encounter();
        // Quest progress check
        if (questActive && isQuestObjective) {
            System.out.println("Quest completed: " + currentQuest);
            completedQuests.add(currentQuest);
            questLog.remove(currentQuest);
            questActive = false;
            currentQuest = "";
        }
    }

    private void tradeWithNPC(String npcName) {
        System.out.println("You encountered a docile NPC: " + npcName + "!");
        while (true) {
            System.out.println("You can trade with " + npcName + ".");
            System.out.println("1. Buy Item");
            System.out.println("2. Sell Item");
            System.out.println("3. Leave");
            System.out.print("Choose an option (1-3): ");
            String choice = scan.nextLine().trim();
            if (choice.equals("1")) {
                buyItem(npcName);
            } else if (choice.equals("2")) {
                sellItem(npcName);
            } else if (choice.equals("3")) {
                System.out.println("You left the trade.");
                break;
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void buyItem(String npcName) {
        System.out.println("Items available for purchase from " + npcName + ":");
        System.out.println("1. Health Potion - 10 Gold");
        System.out.println("2. Mana Potion - 15 Gold");
        System.out.println("3. Strength Elixir - 20 Gold");
        System.out.println("4. Defense Shield - 25 Gold");
        System.out.print("Choose an item to buy (1-4): ");
        String itemChoice = scan.nextLine().trim();
        switch (itemChoice) {
            case "1":
                if (player != null && player.spendGold(10)) {
                    inventory.add("Health Potion");
                    System.out.println("You bought a Health Potion!");
                }
                break;
            case "2":
                if (player != null && player.spendGold(15)) {
                    inventory.add("Mana Potion");
                    System.out.println("You bought a Mana Potion!");
                }
                break;
            case "3":
                if (player != null && player.spendGold(20)) {
                    inventory.add("Strength Elixir");
                    System.out.println("You bought a Strength Elixir!");
                }
                break;
            case "4":
                if (player != null && player.spendGold(25)) {
                    inventory.add("Defense Shield");
                    System.out.println("You bought a Defense Shield!");
                }
                break;
            default:
                System.out.println("Invalid choice. You left the shop.");
                break;
        }
    }

    private void sellItem(String npcName) {
        System.out.println("Your inventory: " + inventory);
        System.out.print("Enter the name of the item you want to sell: ");
        String itemToSell = scan.nextLine().trim();
        if (inventory.contains(itemToSell)) {
            inventory.remove(itemToSell);
            int value = 5;
            if (itemToSell.equalsIgnoreCase("Health Potion")) value = 5;
            else if (itemToSell.equalsIgnoreCase("Mana Potion")) value = 7;
            else if (itemToSell.equalsIgnoreCase("Strength Elixir")) value = 10;
            else if (itemToSell.equalsIgnoreCase("Defense Shield")) value = 12;
            if (player != null) player.addGold(value);
            System.out.println("You sold " + itemToSell + " to " + npcName + " for " + value + " gold!");
        } else {
            System.out.println("You don't have that item.");
        }
    }

    private void viewInventory() {
        System.out.println("Your inventory: " + inventory);
    }

    private void viewEquipment() {
        System.out.println("Equipped Weapon: " + equippedWeapon);
        System.out.println("Equipped Armor: " + equippedArmor);
    }

    private void equipItem() {
        System.out.println("Your inventory: " + inventory);
        System.out.print("Enter the item name to equip as weapon or armor: ");
        String item = scan.nextLine().trim();
        if (inventory.contains(item)) {
            if (item.toLowerCase().contains("sword") || item.toLowerCase().contains("bow") || item.toLowerCase().contains("staff")) {
                equippedWeapon = item;
                System.out.println("You equipped " + item + " as your weapon!");
            } else if (item.toLowerCase().contains("armor") || item.toLowerCase().contains("shield")) {
                equippedArmor = item;
                System.out.println("You equipped " + item + " as your armor!");
            } else {
                System.out.println("Item can't be equipped.");
            }
        } else {
            System.out.println("You don't have that item.");
        }
    }

    private void viewGold() {
        if (player != null) {
            System.out.println("Your gold: " + player.gold);
        } else {
            System.out.println("You have no gold yet. Start a new game!");
        }
    }

    private void viewQuestLog() {
        System.out.println("=== Quest Log ===");
        if (!questLog.isEmpty()) {
            for (String q : questLog) System.out.println("Active: " + q);
        } else {
            System.out.println("No active quests.");
        }
        if (!completedQuests.isEmpty()) {
            for (String q : completedQuests) System.out.println("Completed: " + q);
        }
    }

    private void viewPlayerStats() {
        if (player != null) {
            System.out.println(player.getStatsString());
        }
    }

    private void restAtInn() {
        int innCost = 10;
        if (player != null) {
            if (player.gold >= innCost) {
                player.hp = player.maxHP;
                player.mana = player.maxMana;
                player.spendGold(innCost);
                System.out.println("You rest at the inn and recover to full health and mana.");
            } else {
                System.out.println("Not enough gold to rest!");
            }
        }
    }

    private void saveGame() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("savegame.sav"))) {
            out.writeObject(player);
            out.writeObject(inventory);
            out.writeObject(equippedWeapon);
            out.writeObject(equippedArmor);
            out.writeObject(questLog);
            out.writeObject(completedQuests);
            System.out.println("Game saved!");
        } catch (Exception e) {
            System.out.println("Failed to save game.");
        }
    }

    @SuppressWarnings("unchecked")
    private void loadGame() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("savegame.sav"))) {
            player = (Hero) in.readObject();
            inventory = (List<String>) in.readObject();
            equippedWeapon = (String) in.readObject();
            equippedArmor = (String) in.readObject();
            questLog = (List<String>) in.readObject();
            completedQuests = (List<String>) in.readObject();
            System.out.println("Game loaded!");
            inGameMenu();
        } catch (Exception e) {
            System.out.println("Failed to load game.");
        }
    }

    // Combat system with skills and passives
    private void encounter() {
        System.out.println("Combat starts!");

        while (player.hp > 0 && enemy.isAlive()) {
            System.out.println("\nYour HP: " + player.hp + "/" + player.maxHP + " | Mana: " + player.mana + "/" + player.maxMana);
            System.out.println("Enemy (" + enemy.getCurrentName() + ") HP: " + enemy.hp + "/" + enemy.maxHP);

            System.out.println("\nYour turn! Choose your attack:");
            player.showAttacks();
            System.out.print("Enter attack number (1-" + player.attackNames.length + "): ");
            int attackIndex = 0;
            try {
                attackIndex = Integer.parseInt(scan.nextLine().trim()) - 1;
                if (attackIndex < 0 || attackIndex >= player.attackNames.length) attackIndex = 0;
            } catch (Exception e) {
                attackIndex = 0;
            }
            player.useSkill(attackIndex, enemy);
            player.decrementCooldowns();

            if (!enemy.isAlive()) {
                System.out.println("You defeated the " + enemy.getCurrentName() + "!");
                player.addGold(20);
                player.addXP(15); // Add XP for defeating enemy
                break;
            }

            if (enemy.stunnedForNextTurn) {
                System.out.println("Enemy is stunned and skips their turn!");
                enemy.stunnedForNextTurn = false;
            } else {
                enemy.takeTurn(player);
            }

            if (player.hp <= 0) {
                System.out.println("You have been defeated by the " + enemy.getCurrentName() + "...");
                break;
            }
        }

        // Level up check (example: call player.levelUp() if XP threshold met)
        if (player.checkLevelUp()) {
            player.levelUp();
        }
    }
}
