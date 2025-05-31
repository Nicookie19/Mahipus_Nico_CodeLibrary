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

    private Map<String, Location> worldMap;
    private Random random = new Random();

    public static void main(String[] args) {
        TurnBased_RPG game = new TurnBased_RPG();
        game.displayMainMenu();
    }

    public TurnBased_RPG() {
        initializeWorld();
    }

    private void initializeWorld() {
        worldMap = new HashMap<>();

        Location whiterun = new Location("Whiterun",
            "A bustling city in the heart of the plains", 2, true);
        whiterun.addFeature("Marketplace");
        whiterun.addFeature("Jorrvaskr");
        worldMap.put("Whiterun", whiterun);

        Location blackreach = new Location("Blackreach",
            "An eerie underground cavern glowing with blue crystals", 4, false);
        blackreach.addFeature("Ancient Ruins");
        blackreach.addFeature("Crystal Gardens");
        worldMap.put("Blackreach", blackreach);

        Location riverwood = new Location("Riverwood",
            "A peaceful village nestled along a flowing river", 1, true);
        riverwood.addFeature("Sawmill");
        riverwood.addFeature("Trading Post");
        worldMap.put("Riverwood", riverwood);

        Location falkreath = new Location("Falkreath",
            "A somber town surrounded by dense forests and a misty graveyard", 2, true);
        falkreath.addFeature("Cemetery");
        falkreath.addFeature("Hunter’s Lodge");
        worldMap.put("Falkreath", falkreath);

        Location dragonBridge = new Location("Dragon Bridge",
            "A small settlement with a stone bridge guarded by dragon statues", 2, true);
        dragonBridge.addFeature("Bridge");
        dragonBridge.addFeature("Inn");
        worldMap.put("Dragon Bridge", dragonBridge);

        Location bleakFalls = new Location("Bleak Falls Barrow",
            "An ancient Nordic tomb filled with draugr and forgotten treasures", 3, false);
        bleakFalls.addFeature("Crypts");
        bleakFalls.addFeature("Word Wall");
        worldMap.put("Bleak Falls Barrow", bleakFalls);

        Location winterhold = new Location("Winterhold",
            "A frozen coastal town, home to the College of Mages", 3, true);
        winterhold.addFeature("College of Winterhold");
        winterhold.addFeature("Frozen Shore");
        worldMap.put("Winterhold", winterhold);

        Location shadowfen = new Location("Shadowfen",
            "A treacherous swamp teeming with dangerous creatures", 4, false);
        shadowfen.addFeature("Marsh Ruins");
        shadowfen.addFeature("Witch’s Lair");
        worldMap.put("Shadowfen", shadowfen);

        Location highHrothgar = new Location("High Hrothgar",
            "A sacred monastery atop the Throat of the World", 4, false);
        highHrothgar.addFeature("Seven Thousand Steps");
        highHrothgar.addFeature("Meditation Hall");
        worldMap.put("High Hrothgar", highHrothgar);

        Location solitude = new Location("Solitude",
            "A grand port city, capital of the realm", 3, true);
        solitude.addFeature("Blue Palace");
        solitude.addFeature("Docks");
        worldMap.put("Solitude", solitude);
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
        System.out.println("Oh good, you're awake!");
        System.out.println("Choose your character:");
        System.out.println("1. Knight: Bjorn Ironmark The Dragonborn");
        System.out.println("2. Mage: Aurelion Thalor Arch Mage of Winterhold");
        System.out.println("3. Archer: Sorin Sandpiercer Nightingale");
        System.out.println("4. Rook: Ghorzug Stonefist Champion of Talos");
        System.out.println("5. Assassin: Veyra Shadowblade of the Dark Brotherhood");
        System.out.println("6. Cleric: Lysara Dawnbringer Priestess of Mara");
        System.out.print("Enter your choice (1-6): ");
        String choice = scan.nextLine().trim();
        switch (choice) {
            case "1": player = new Knight(); break;
            case "2": player = new Mage(); break;
            case "3": player = new Archer(); break;
            case "4": player = new Rook(); break;
            case "5": player = new Assasin(); break;
            case "6": player = new Cleric(); break;
            default: System.out.println("Invalid choice. Defaulting to Knight."); player = new Knight();
        }
        System.out.println("You are now a " + player.getClassName() + "!");
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
        System.out.println("\n=== Available Locations ===");
        int i = 1;
        List<String> locations = new ArrayList<>(worldMap.keySet());
        for (String name : locations) {
            System.out.println(i + ". " + name + " (Danger: " + worldMap.get(name).dangerLevel + "/5)");
            i++;
        }
        System.out.print("Choose a location to travel to (1-" + worldMap.size() + "): ");
        try {
            int choice = Integer.parseInt(scan.nextLine().trim()) - 1;
            if (choice >= 0 && choice < worldMap.size()) {
                String destination = locations.get(choice);
                Location loc = worldMap.get(destination);
                enterLocation(loc);
            } else {
                System.out.println("Invalid choice.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }

    private void enterLocation(Location loc) {
        System.out.println("\nYou arrive at " + loc.name);
        System.out.println(loc.description);

        if (loc.hasTown) {
            System.out.println("\nThis location has a town where you can rest and trade.");
            encounterNPC(loc.name);
        }

        int encounters = 1 + random.nextInt(loc.dangerLevel);
        for (int i = 0; i < encounters; i++) {
            if (random.nextFloat() < 0.6f) {
                generateEncounter(loc.dangerLevel);
            } else {
                generateDiscovery(loc);
            }
        }
    }

    private void generateEncounter(int dangerLevel) {
        if (player == null) {
            System.out.println("Error: No player selected. Please start a new game.");
            return;
        }
        Enemy enemy = new Enemy(Enemy.Tier.values()[random.nextInt(3)], player.level);
        enemy.maxHP *= (0.8 + 0.2 * dangerLevel);
        enemy.minDmg *= (0.8 + 0.2 * dangerLevel);
        enemy.maxDmg *= (0.8 + 0.2 * dangerLevel);
        enemy.hp = enemy.maxHP;
        System.out.println("\nYou encounter a " + enemy.getDisplayName());
        this.enemy = enemy;
        encounter();
    }

    private void generateDiscovery(Location loc) {
        String[] discoveries = {
            "You find an abandoned campsite",
            "You discover a hidden cave",
            "You stumble upon an ancient relic",
            "You meet a traveling merchant"
        };

        String discovery = discoveries[random.nextInt(discoveries.length)];
        System.out.println("\n" + discovery + " in " + loc.name);

        if (random.nextBoolean()) {
            String[] loot = {"gold", "a potion", "a weapon", "an armor piece"};
            String found = loot[random.nextInt(loot.length)];

            if (found.equals("gold")) {
                int amount = 10 + random.nextInt(20 * loc.dangerLevel);
                player.addGold(amount);
                System.out.println("You found " + amount + " gold!");
            } else if (found.equals("a potion")) {
                player.addItem("Health Potion", 0.5f);
                System.out.println("You found a Health Potion!");
            } else if (found.equals("a weapon")) {
                String[] classWeapons = player instanceof Knight ? new String[]{"Iron Sword", "Steel Sword", "Mithril Sword", "Elven Sword", "Glass Sword", "Daedric Sword"} :
                                        player instanceof Mage ? new String[]{"Fire Staff", "Ice Wand", "Staff of Fireballs", "Staff of Ice Storms", "Staff of Healing", "Wand of Lightning", "Orb of Elements"} :
                                        player instanceof Archer ? new String[]{"Hunting Bow", "Longbow", "Composite Bow", "Elven Bow", "Glass Bow", "Daedric Bow"} :
                                        player instanceof Rook ? new String[]{"Warhammer", "Battleaxe", "Mace", "Flail"} :
                                        player instanceof Assasin ? new String[]{"Iron Dagger", "Steel Dagger", "Mithril Dagger", "Elven Dagger", "Glass Dagger", "Daedric Dagger"} :
                                        new String[]{"Mace", "Flail", "Staff of Healing", "Holy Scepter"};
                String weapon = classWeapons[random.nextInt(classWeapons.length)];
                player.addItem(weapon, 2.0f);
                System.out.println("You found a " + weapon + "!");
            } else if (found.equals("an armor piece")) {
                String armor = player instanceof Knight ? "Plate Armor" :
                               player instanceof Mage ? "Robe of Protection" :
                               player instanceof Archer ? "Leather Armor" :
                               player instanceof Rook ? "Chainmail" :
                               player instanceof Assasin ? "Cloak of Shadows" : "Robe of Protection";
                player.addItem(armor, 3.0f);
                System.out.println("You found a " + armor + "!");
            }
        }
    }

    private void encounterNPC(String location) {
        System.out.println("\nYou enter the town of " + location + "...");

        int npcCount = 4 + random.nextInt(2);
        List<Enemy> npcs = new ArrayList<>();
        List<String> npcNames = new ArrayList<>();

        for (int i = 0; i < npcCount; i++) {
            Enemy npc = new Enemy();
            npc.setHostile(random.nextFloat() < 0.2f);
            npcs.add(npc);
            npcNames.add(npc.getDisplayName());
            System.out.println((i + 1) + ". " + npc.getDisplayName());
        }

        while (true) {
            System.out.print("\nInteract with which NPC? (1-" + npcCount + " or 'leave'): ");
            String input = scan.nextLine().trim();

            if (input.equalsIgnoreCase("leave")) {
                System.out.println("You leave the town.");
                break;
            }

            try {
                int idx = Integer.parseInt(input) - 1;
                if (idx >= 0 && idx < npcs.size()) {
                    Enemy npc = npcs.get(idx);
                    if (npc.isHostile()) {
                        interactWithHostileNPC(npc.getCurrentName());
                    } else {
                        interactWithDocileNPC(npc.getCurrentName(), worldMap.get(location));
                    }
                } else {
                    System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number or 'leave'.");
            }
        }
    }

    private void interactWithHostileNPC(String npcName) {
        Enemy hostile = new Enemy();
        hostile.changeName(npcName);
        System.out.println(hostile.getDisplayName() + " attacks!");
        this.enemy = hostile;
        encounter();
    }

    private void interactWithDocileNPC(String npcName, Location loc) {
        System.out.println("You meet a friendly NPC: " + npcName + "!");
        while (true) {
            System.out.println("\nWhat would you like to do with " + npcName + "?");
            System.out.println("1. Trade (buy/sell items)");
            System.out.println("2. Talk (learn about " + loc.name + ")");
            System.out.println("3. Leave");
            System.out.print("Choose an option (1-3): ");
            String choice = scan.nextLine().trim();
            if (choice.equals("1")) {
                tradeWithNPC(npcName);
            } else if (choice.equals("2")) {
                talkToNPC(npcName, loc);
            } else if (choice.equals("3")) {
                System.out.println("You leave " + npcName + ".");
                break;
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void tradeWithNPC(String npcName) {
        while (true) {
            System.out.println("\nTrading with " + npcName + ":");
            System.out.println("1. Buy items");
            System.out.println("2. Sell items");
            System.out.println("3. Cancel");
            System.out.print("Choose an option (1-3): ");
            String choice = scan.nextLine().trim();

            if (choice.equals("1")) {
                buyItem(npcName);
            } else if (choice.equals("2")) {
                sellItem(npcName);
            } else if (choice.equals("3")) {
                System.out.println("You stop trading with " + npcName + ".");
                break;
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void buyItem(String npcName) {
        System.out.println("Items available for purchase from " + npcName + ":");
        String[] items;
        int[] prices;
        if (player instanceof Knight) {
            items = new String[]{"Iron Sword", "Steel Sword", "Mithril Sword", "Elven Sword", "Glass Sword", "Daedric Sword", "Health Potion"};
            prices = new int[]{10, 15, 20, 25, 30, 35, 5};
        } else if (player instanceof Mage) {
            items = new String[]{"Fire Staff", "Ice Wand", "Staff of Fireballs", "Staff of Ice Storms", "Staff of Healing", "Wand of Lightning", "Orb of Elements", "Mana Potion"};
            prices = new int[]{15, 20, 25, 30, 35, 40, 45, 7};
        } else if (player instanceof Archer) {
            items = new String[]{"Hunting Bow", "Longbow", "Composite Bow", "Elven Bow", "Glass Bow", "Daedric Bow", "Health Potion"};
            prices = new int[]{10, 15, 20, 25, 30, 35, 5};
        } else if (player instanceof Rook) {
            items = new String[]{"Warhammer", "Battleaxe", "Mace", "Flail", "Health Potion"};
            prices = new int[]{15, 20, 25, 30, 5};
        } else if (player instanceof Assasin) {
            items = new String[]{"Iron Dagger", "Steel Dagger", "Mithril Dagger", "Elven Dagger", "Glass Dagger", "Daedric Dagger", "Health Potion"};
            prices = new int[]{10, 15, 20, 25, 30, 35, 5};
        } else {
            items = new String[]{"Mace", "Flail", "Staff of Healing", "Holy Scepter", "Health Potion", "Mana Potion"};
            prices = new int[]{15, 20, 25, 30, 5, 7};
        }

        for (int i = 0; i < items.length; i++) {
            System.out.println((i + 1) + ". " + items[i] + " - " + prices[i] + " Gold");
        }
        System.out.print("Choose an item to buy (1-" + items.length + ") or 'cancel': ");
        String itemChoice = scan.nextLine().trim();
        if (itemChoice.equalsIgnoreCase("cancel")) {
            return;
        }
        try {
            int idx = Integer.parseInt(itemChoice) - 1;
            if (idx >= 0 && idx < items.length) {
                if (player != null && player.spendGold(prices[idx])) {
                    player.addItem(items[idx], idx < items.length - 1 ? 2.0f : 0.5f);
                    System.out.println("You bought a " + items[idx] + "!");
                }
            } else {
                System.out.println("Invalid choice.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice.");
        }
    }

    private void sellItem(String npcName) {
        if (player.getInventory().isEmpty()) {
            System.out.println("Your inventory is empty!");
            return;
        }
        player.showInventory();
        System.out.print("Choose an item to sell (1-" + player.getInventory().size() + ") or 'cancel': ");
        String itemChoice = scan.nextLine().trim();
        if (itemChoice.equalsIgnoreCase("cancel")) {
            return;
        }
        try {
            int idx = Integer.parseInt(itemChoice) - 1;
            if (idx >= 0 && idx < player.getInventory().size()) {
                Hero.InventoryItem item = player.getInventory().get(idx);
                int value = 5;
                if (item.name.equals("Health Potion")) value = 2;
                else if (item.name.equals("Mana Potion")) value = 3;
                else if (item.name.contains("Sword") || item.name.contains("Bow") || item.name.contains("Staff") || item.name.contains("Wand") || item.name.contains("Orb") || item.name.contains("Dagger") || item.name.contains("Scepter")) value = 10;
                else if (item.name.contains("Armor") || item.name.contains("Robe") || item.name.contains("Cloak")) value = 15;
                player.addGold(value);
                System.out.println("You sold " + item.name + " for " + value + " gold!");
                item.quantity--;
                if (item.quantity <= 0) {
                    player.getInventory().remove(idx);
                }
            } else {
                System.out.println("Invalid choice.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice.");
        }
    }

    private void talkToNPC(String npcName, Location loc) {
        String dialogue;
        switch (loc.name) {
            case "Whiterun":
                dialogue = "Whiterun is a bustling city with a rich history. The Marketplace is great for trading, but be careful of bandits on the plains.";
                break;
            case "Blackreach":
                dialogue = "Blackreach is an underground city filled with ancient ruins and dangerous creatures. The crystals here hold powerful magic.";
                break;
            case "Riverwood":
                dialogue = "Riverwood is a peaceful village along the river. The Sawmill provides wood for the region, and the Trading Post is a hub for travelers.";
                break;
            case "Falkreath":
                dialogue = "Falkreath is known for its dense forests and the nearby graveyard. Many adventurers come here to hunt or explore the ancient tombs.";
                break;
            case "Dragon Bridge":
                dialogue = "Dragon Bridge is a small settlement with a famous stone bridge. It's said to be guarded by the spirits of dragons.";
                break;
            case "Bleak Falls Barrow":
                dialogue = "Bleak Falls Barrow is an ancient Nordic tomb. It's rumored to hold powerful artifacts and be haunted by draugr.";
                break;
            case "Winterhold":
                dialogue = "Winterhold is home to the College of Winterhold, a prestigious school for mages. The frozen shores are beautiful but treacherous.";
                break;
            case "Shadowfen":
                dialogue = "Shadowfen is a swampy area filled with dangerous creatures and ancient ruins. It's a place few dare to explore.";
                break;
            case "High Hrothgar":
                dialogue = "High Hrothgar is a sacred monastery atop the Throat of the World. The monks here are wise and seek enlightenment.";
                break;
            case "Solitude":
                dialogue = "Solitude is the capital of the realm, a grand port city with the Blue Palace as its centerpiece. The Docks are busy with trade.";
                break;
            default:
                dialogue = "This land is full of secrets and dangers. Explore carefully.";
                break;
        }
        System.out.println(npcName + " says: \"" + dialogue + "\"");
        if (!questActive && random.nextBoolean()) {
            currentQuest = "Find a " + (new String[]{"Lost Relic", "Rare Herb", "Ancient Tome"})[random.nextInt(3)] + " for " + npcName + " in " + loc.name + ".";
            questLog.add(currentQuest);
            questActive = true;
            System.out.println("New quest: " + currentQuest);
        }
    }

    private void viewInventory() {
        if (player != null) {
            player.showInventory();
        } else {
            System.out.println("No player selected. Start a new game.");
        }
    }

    private void viewEquipment() {
        if (player != null) {
            System.out.println("Equipped Weapon: " + player.equipment.weapon);
            System.out.println("Equipped Armor: " + player.equipment.armor);
        } else {
            System.out.println("No player selected. Start a new game.");
        }
    }

    private void equipItem() {
        if (player != null) {
            player.showInventory();
            System.out.print("Choose an item to equip (1-" + player.getInventory().size() + ") or 'cancel': ");
            String itemChoice = scan.nextLine().trim();
            if (itemChoice.equalsIgnoreCase("cancel")) {
                return;
            }
            try {
                int idx = Integer.parseInt(itemChoice) - 1;
                if (idx >= 0 && idx < player.getInventory().size()) {
                    Hero.InventoryItem item = player.getInventory().get(idx);
                    if (player.equipItem(item.name)) {
                        System.out.println("Equipped " + item.name + "!");
                    } else {
                        System.out.println("Cannot equip " + item.name + ": not compatible with " + player.getClassName());
                    }
                } else {
                    System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice.");
            }
        } else {
            System.out.println("No player selected. Start a new game.");
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
        } else {
            System.out.println("No player selected. Start a new game.");
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
        } else {
            System.out.println("No player selected. Start a new game.");
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    private void encounter() {
        if (player == null || enemy == null) {
            System.out.println("Error: Combat cannot start. Player or enemy is missing.");
            return;
        }
        System.out.println("Combat starts!");

        while (player.hp > 0 && enemy.isAlive()) {
            player.applyPassiveEffects();

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

            boolean dodgeAttack = false;
            if (player instanceof Archer && random.nextInt(100) < 20) {
                System.out.println("Archer dodges the enemy’s next attack!");
                dodgeAttack = true;
            } else if (player instanceof Assasin && random.nextInt(100) < 10) {
                System.out.println("Assassin dodges the enemy’s next attack!");
                dodgeAttack = true;
            } else {
                player.useSkill(attackIndex, enemy);
            }

            if (player instanceof Rook && random.nextInt(100) < 15) {
                int counterDmg = player.minDmg + random.nextInt(player.maxDmg - player.minDmg + 1);
                System.out.println("Rook counterattacks for " + counterDmg + " damage!");
                enemy.receiveDamage(counterDmg);
            }

            player.decrementCooldowns();

            if (!enemy.isAlive()) {
                System.out.println("You defeated the " + enemy.getCurrentName() + "!");
                player.addGold(20);
                player.addXP(20);
                if (questActive && currentQuest.contains("Defeat a Goblin") && enemy.getCurrentName().equals("Goblin")) {
                    System.out.println("Quest completed: " + currentQuest);
                    completedQuests.add(currentQuest);
                    questLog.remove(currentQuest);
                    questActive = false;
                    currentQuest = "";
                } else if (questActive && currentQuest.contains("Lost Relic") && random.nextInt(100) < 10) {
                    System.out.println("You found a Lost Relic! Quest completed!");
                    completedQuests.add(currentQuest);
                    questLog.remove(currentQuest);
                    questActive = false;
                    currentQuest = "";
                    player.addGold(50);
                }
                break;
            }

            if (dodgeAttack || enemy.stunnedForNextTurn) {
                System.out.println("Enemy is stunned or you dodged, so they skip their turn!");
                enemy.stunnedForNextTurn = false;
            } else {
                // Apply Assassin's Smoke Bomb effect
                if (player instanceof Assasin && ((Assasin)player).smokeBombActive) {
                    System.out.println("Enemy's attack is weakened by Smoke Bomb!");
                    int originalMinDmg = enemy.minDmg;
                    int originalMaxDmg = enemy.maxDmg;
                    enemy.minDmg = (int)(enemy.minDmg * 0.5);
                    enemy.maxDmg = (int)(enemy.maxDmg * 0.5);
                    enemy.takeTurn(player);
                    enemy.minDmg = originalMinDmg;
                    enemy.maxDmg = originalMaxDmg;
                } else {
                    enemy.takeTurn(player);
                }
            }

            if (player.hp <= 0) {
                System.out.println("You have been defeated by the " + enemy.getCurrentName() + "...");
                break;
            }
        }

        if (player.checkLevelUp()) {
            player.levelUp();
        }
    }

    class Location {
        String name;
        String description;
        int dangerLevel;
        List<String> features;
        boolean hasTown;

        public Location(String name, String description, int dangerLevel, boolean hasTown) {
            this.name = name;
            this.description = description;
            this.dangerLevel = dangerLevel;
            this.features = new ArrayList<>();
            this.hasTown = hasTown;
        }

        public void addFeature(String feature) {
            features.add(feature);
        }
    }
}