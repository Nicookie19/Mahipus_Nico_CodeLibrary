package com.mycompany.turnbased_rpg;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class TurnBased_RPG {
    Scanner scan = new Scanner(System.in);

    Hero player;
    Enemy enemy;

    Stack<Integer> lastPlayerHP = new Stack<>();
    Stack<Integer> lastEnemyHP = new Stack<>();

    int gameTimer = 2;

    // Player inventory
    List<String> inventory = new ArrayList<>();

    // Quest system
    boolean questActive = false;
    String currentQuest = "";

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
            System.out.println("2. Travel");
            System.out.println("3. View Inventory");
            System.out.println("4. View Gold");
            System.out.println("5. View Quest");
            System.out.println("6. Exit");
            System.out.print("Choose an option (1-6): ");

            String input = scan.nextLine().trim();
            if (input.equals("1")) {
                startGame();
            } else if (input.equals("2")) {
                travel();
            } else if (input.equals("3")) {
                viewInventory();
            } else if (input.equals("4")) {
                viewGold();
            } else if (input.equals("5")) {
                viewQuest();
            } else if (input.equals("6")) {
                System.out.println("Thank you for playing! Goodbye!");
                break;
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public void travel() {
        System.out.println("===================================");
        System.out.println("          Travel Locations         ");
        System.out.println("===================================");
        System.out.println("1. Forest");
        System.out.println("2. Cave");
        System.out.println("3. Village");
        System.out.println("4. Mountain");
        System.out.println("5. Desert");
        System.out.println("6. River");
        System.out.println("7. Lake");
        System.out.println("8. Swamp");
        System.out.println("9. Jungle");
        System.out.println("10. City");
        System.out.print("Choose a location to travel to (1-10): ");

        String input = scan.nextLine().trim();
        switch (input) {
            case "1":
                encounterNPC("Forest", new String[]{"Docile: Traveler", "Hostile: Goblin"});
                break;
            case "2":
                encounterNPC("Cave", new String[]{"Hostile: Troll", "Docile: Miner"});
                break;
            case "3":
                encounterNPC("Village", new String[]{"Docile: Villager", "Docile: Shopkeeper"});
                break;
            case "4":
                encounterNPC("Mountain", new String[]{"Hostile: Yeti", "Docile: Climber"});
                break;
            case "5":
                encounterNPC("Desert", new String[]{"Hostile: Sand Wraith", "Docile: Nomad"});
                break;
            case "6":
                encounterNPC("River", new String[]{"Docile: Fisherman", "Hostile: River Monster"});
                break;
            case "7":
                encounterNPC("Lake", new String[]{"Docile: Swimmer", "Hostile: Lake Monster"});
                break;
            case "8":
                encounterNPC("Swamp", new String[]{"Hostile: Swamp Monster", "Docile: Hermit"});
                break;
            case "9":
                encounterNPC("Jungle", new String[]{"Hostile: Jungle Beast", "Docile: Explorer"});
                break;
            case "10":
                encounterNPC("City", new String[]{"Docile: Guard", "Docile: Merchant"});
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

        System.out.print("Do you want to interact with an NPC? (yes/no): ");
        String choice = scan.nextLine().trim();
        if (choice.equalsIgnoreCase("yes")) {
            System.out.print("Enter the name of the NPC you want to interact with: ");
            String npcName = scan.nextLine().trim();
            interactWithNPC(npcName, npcs);
        }
    }

    private void interactWithNPC(String npcName, String[] npcs) {
        boolean found = false;
        for (String npc : npcs) {
            if (npc.toLowerCase().contains(npcName.toLowerCase())) {
                found = true;
                if (npc.startsWith("Hostile")) {
                    System.out.println("You encountered a hostile NPC: " + npcName + "!");
                    // Start a fight with the hostile NPC
                    Enemy hostileEnemy = new Enemy(); // Create a new enemy instance
                    this.enemy = hostileEnemy; // Set the enemy to the hostile NPC
                    encounter(); // Start the encounter (implement encounter() logic!)
                } else {
                    System.out.println("You encountered a docile NPC: " + npcName + "!");
                    tradeWithNPC(npcName);
                }
                break;
            }
        }
        if (!found) {
            System.out.println("NPC not found in this location.");
        }
    }

    private void tradeWithNPC(String npcName) {
        System.out.println("You can trade with " + npcName + ".");
        System.out.println("1. Buy Item");
        System.out.println("2. Sell Item");
        System.out.print("Choose an option (1-2): ");
        String choice = scan.nextLine().trim();
        if (choice.equals("1")) {
            buyItem(npcName);
        } else if (choice.equals("2")) {
            sellItem(npcName);
        } else {
            System.out.println("Invalid option. You left the trade.");
        }
    }

    private void buyItem(String npcName) {
        System.out.println("Items available for purchase from " + npcName + ":");
        System.out.println("1. Health Potion - 10 Gold");
        System.out.println("2. Mana Potion - 15 Gold");
        System.out.println("3. Strength Elixir - 20 Gold (Increases attack for one battle)");
        System.out.println("4. Defense Shield - 25 Gold (Reduces damage taken for one battle)");
        System.out.print("Choose an item to buy (1-4): ");
        String itemChoice = scan.nextLine().trim();
        switch (itemChoice) {
            case "1":
                if (player.spendGold(10)) {
                    inventory.add("Health Potion");
                    System.out.println("You bought a Health Potion!");
                }
                break;
            case "2":
                if (player.spendGold(15)) {
                    inventory.add("Mana Potion");
                    System.out.println("You bought a Mana Potion!");
                }
                break;
            case "3":
                if (player.spendGold(20)) {
                    inventory.add("Strength Elixir");
                    System.out.println("You bought a Strength Elixir!");
                }
                break;
            case "4":
                if (player.spendGold(25)) {
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
            player.addGold(value);
            System.out.println("You sold " + itemToSell + " to " + npcName + " for " + value + " gold!");
        } else {
            System.out.println("You don't have that item.");
        }
    }

    private void viewInventory() {
        System.out.println("Your inventory: " + inventory);
    }

    private void viewGold() {
        if (player != null) {
            System.out.println("Your gold: " + player.gold);
        } else {
            System.out.println("You have no gold yet. Start a new game!");
        }
    }

    private void viewQuest() {
        if (questActive) {
            System.out.println("Current quest: " + currentQuest);
        } else {
            System.out.println("No active quest.");
        }
    }

    // Character/hero selection logic and game start
    private void startGame() {
        System.out.println("Choose your class:");
        System.out.println("1. Knight - Balanced melee, unique shield and bash skills (Passive: Phalanx - chance to block)");
        System.out.println("2. Mage - Powerful spells, can heal (Passive: Arcane Recovery - heals every turn)");
        System.out.println("3. Archer - High crit, multi-shot skill (Passive: Rapid Volley - chance for double attack)");
        System.out.println("4. Rook - Tanky, stun ability (Passive: Fortress - reduces all damage taken)");
        System.out.print("Enter your choice (1-4): ");
        String choice = scan.nextLine().trim();
        switch (choice) {
            case "1":
                player = new Knight();
                break;
            case "2":
                player = new Mage();
                break;
            case "3":
                player = new Archer();
                break;
            case "4":
                player = new Rook();
                break;
            default:
                System.out.println("Invalid choice. Defaulting to Knight.");
                player = new Knight();
                break;
        }
        System.out.println("You are now a " + player.getClassName() + "!");
    }

    // Combat system with skills and passives
    private void encounter() {
        System.out.println("Combat starts!");

        while (player.hp > 0 && enemy.isAlive()) {
            // Display status
            System.out.println("\nYour HP: " + player.hp + "/" + player.maxHP + " | Mana: " + player.mana + "/" + player.maxMana);
            System.out.println("Enemy (" + enemy.getCurrentName() + ") HP: " + enemy.hp + "/" + enemy.maxHP);

            // Player turn
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

            // Unique skill logic (call class-specific methods if available)
            player.useSkill(attackIndex, enemy);

            // Passive/unique mechanics (triggered after turn)
            player.decrementCooldowns();

            // Check enemy defeat
            if (!enemy.isAlive()) {
                System.out.println("You defeated the " + enemy.getCurrentName() + "!");
                player.addGold(20); // Reward gold
                break;
            }

            // Enemy turn
            if (enemy.stunnedForNextTurn) {
                System.out.println("Enemy is stunned and skips their turn!");
                enemy.stunnedForNextTurn = false;
            } else {
                enemy.takeTurn(player);
            }

            // Check player defeat
            if (player.hp <= 0) {
                System.out.println("You have been defeated by the " + enemy.getCurrentName() + "...");
                break;
            }
        }
    }
}