package com.mycompany.turnbased_rpg;
import java.io.*;
import java.util.*;

class Color {
    public static final String RESET = "\u001B[0m";
    public static final String WHITE = "\u001B[37m";
    public static final String GREEN = "\u001B[32m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String GRAY = "\u001B[90m";
    
    private static final boolean USE_ANSI = System.console() != null && System.getenv("TERM") != null;
    
    public static String colorize(String text, String color) {
        if (!USE_ANSI) return text;
        return color + text + RESET;
    }
}

public class TurnBased_RPG {
    Scanner scan = new Scanner(System.in);

    Hero player;
    Enemy enemy;

    Stack<Integer> lastPlayerHP = new Stack<>();
    Stack<Integer> lastEnemyHP = new Stack<>();

    int gameTimer = 2;
    boolean questActive = false;
    String currentQuest = "";
    List<String> questLog = new ArrayList<>();
    List<String> completedQuests = new ArrayList<>();

    String equippedWeapon = "Basic Sword";
    String equippedArmor = "Cloth Armor";

    private Map<String, Location> worldMap;
    private Random random = new Random();
    private List<Faction> availableFactions;

    public static void main(String[] args) {
        TurnBased_RPG game = new TurnBased_RPG();
        game.displayMainMenu();
    }

    public TurnBased_RPG() {
        initializeWorld();
        initializeFactions();
    }

    private void initializeFactions() {
        availableFactions = new ArrayList<>();
        availableFactions.add(new Faction("Companions"));
        availableFactions.add(new Faction("Thieves Guild"));
        availableFactions.add(new Faction("Dark Brotherhood"));
        availableFactions.add(new Faction("College of Winterhold"));
        availableFactions.add(new Faction("Imperial Legion"));
    }

    private void initializeWorld() {
        worldMap = new HashMap<>();

        // Original Locations
        Location whiterun = new Location("Whiterun Plains",
            "A vast, grassy plain with rolling hills, home to farmers and the mighty Companions.", 1, true,
            new String[]{"Wolf", "Bandit", "Sabre Cat"});
        whiterun.addFeature("Companions Hall");
        whiterun.addFeature("Marketplace");
        worldMap.put("Whiterun Plains", whiterun);

        Location riften = new Location("Riften Woods",
            "A dense forest shrouded in mist, hiding thieves and smugglers.", 2, true,
            new String[]{"Bandit", "Spriggan", "Bear"});
        riften.addFeature("Thieves Guild Hideout");
        riften.addFeature("Mistveil Keep");
        worldMap.put("Riften Woods", riften);

        Location solitude = new Location("Solitude Cliffs",
            "Towering cliffs overlooking the sea, home to the Imperial Legion and wealthy nobles.", 3, true,
            new String[]{"Forsworn", "Troll", "Bandit"});
        solitude.addFeature("Blue Palace");
        solitude.addFeature("Docks");
        worldMap.put("Solitude Cliffs", solitude);

        Location winterhold = new Location("Winterhold Tundra",
            "A frozen wasteland where mages study ancient secrets at the College.", 4, false,
            new String[]{"Ice Wraith", "Frost Troll", "Hagraven"});
        winterhold.addFeature("College of Winterhold");
        winterhold.addFeature("Frozen Ruins");
        worldMap.put("Winterhold Tundra", winterhold);

        Location markarth = new Location("Markarth Ruins",
            "Ancient Dwemer ruins carved into mountains, plagued by Falmer and dark magic.", 5, false,
            new String[]{"Falmer", "Draugr", "Dragon"});
        markarth.addFeature("Dwemer Museum");
        markarth.addFeature("Dark Brotherhood Sanctuary");
        worldMap.put("Markarth Ruins", markarth);

        // New Locations
        Location dawnstar = new Location("Dawnstar Coast",
            "A chilly coastal town battered by icy winds, known for its hardy fishermen.", 2, true,
            new String[]{"Horker", "Bandit", "Ice Wolf"});
        dawnstar.addFeature("Dawnstar Harbor");
        dawnstar.addFeature("Iron-Breaker Mine");
        worldMap.put("Dawnstar Coast", dawnstar);

        Location falkreath = new Location("Falkreath Forest",
            "A lush forest with towering pines, haunted by spirits and bandits.", 2, true,
            new String[]{"Bandit", "Spriggan", "Bear"});
        falkreath.addFeature("Falkreath Graveyard");
        falkreath.addFeature("Jarl's Longhouse");
        worldMap.put("Falkreath Forest", falkreath);

        Location morthal = new Location("Morthal Marshes",
            "A foggy swamp filled with eerie lights and dangerous creatures.", 3, true,
            new String[]{"Mudcrab", "Swamp Troll", "Will-o'-Wisp"});
        morthal.addFeature("Moorside Inn");
        morthal.addFeature("Highmoon Hall");
        worldMap.put("Morthal Marshes", morthal);

        Location windhelm = new Location("Windhelm Snowfields",
            "A snow-covered expanse surrounding the ancient city of the Stormcloaks.", 3, true,
            new String[]{"Ice Wraith", "Snow Bear", "Bandit"});
        windhelm.addFeature("Palace of the Kings");
        windhelm.addFeature("Candlehearth Hall");
        worldMap.put("Windhelm Snowfields", windhelm);

        Location riverwood = new Location("Riverwood Valley",
            "A peaceful valley with a rushing river, home to lumberjacks and traders.", 1, true,
            new String[]{"Wolf", "Bandit", "Mudcrab"});
        riverwood.addFeature("Sleeping Giant Inn");
        riverwood.addFeature("Riverwood Trader");
        worldMap.put("Riverwood Valley", riverwood);

        Location bleakFalls = new Location("Bleak Falls Barrow",
            "An ancient Nordic tomb filled with traps and undead Draugr.", 4, false,
            new String[]{"Draugr", "Skeleton", "Dragon Priest"});
        bleakFalls.addFeature("Ancient Altar");
        bleakFalls.addFeature("Word Wall");
        worldMap.put("Bleak Falls Barrow", bleakFalls);

        Location highHrothgar = new Location("High Hrothgar",
            "The sacred mountain monastery of the Greybeards, shrouded in mist.", 5, false,
            new String[]{"Frost Troll", "Ice Wraith", "Snow Sabre Cat"});
        highHrothgar.addFeature("Greybeard Sanctum");
        highHrothgar.addFeature("Meditation Chamber");
        worldMap.put("High Hrothgar", highHrothgar);

        Location ivarstead = new Location("Ivarstead Village",
            "A small settlement at the base of the Throat of the World.", 1, true,
            new String[]{"Wolf", "Bandit", "Bear"});
        ivarstead.addFeature("Vilemyr Inn");
        ivarstead.addFeature("Riftweald Farm");
        worldMap.put("Ivarstead Village", ivarstead);

        Location dragonBridge = new Location("Dragon Bridge",
            "A strategic crossing with a stone bridge shaped like a dragon.", 2, true,
            new String[]{"Bandit", "Sabre Cat", "Troll"});
        dragonBridge.addFeature("Four Shields Tavern");
        dragonBridge.addFeature("Dragon Bridge Lumber Camp");
        worldMap.put("Dragon Bridge", dragonBridge);

        Location karthwasten = new Location("Karthwasten Hills",
            "Rugged hills rich with silver mines, contested by Forsworn.", 3, true,
            new String[]{"Forsworn", "Bandit", "Hagraven"});
        karthwasten.addFeature("Sanuarach Mine");
        karthwasten.addFeature("Karthwasten Hall");
        worldMap.put("Karthwasten Hills", karthwasten);

        Location rorikstead = new Location("Rorikstead Fields",
            "Fertile farmlands known for bountiful harvests and peaceful folk.", 1, true,
            new String[]{"Wolf", "Bandit", "Fox"});
        rorikstead.addFeature("Frostfruit Inn");
        rorikstead.addFeature("Cowflop Farm");
        worldMap.put("Rorikstead Fields", rorikstead);

        Location helgen = new Location("Helgen Ruins",
            "A destroyed fortress recently ravaged by a dragon attack.", 3, false,
            new String[]{"Bandit", "Draugr", "Skeleton"});
        helgen.addFeature("Burned Keep");
        helgen.addFeature("Hidden Escape Tunnel");
        worldMap.put("Helgen Ruins", helgen);

        Location blackreach = new Location("Blackreach",
            "A vast underground cavern lit by glowing mushrooms and Dwemer ruins.", 5, false,
            new String[]{"Falmer", "Chaurus", "Dwemer Automaton"});
        blackreach.addFeature("Tower of Mzark");
        blackreach.addFeature("Silent City");
        worldMap.put("Blackreach", blackreach);

        Location soulCairn = new Location("Soul Cairn",
            "A desolate plane of Oblivion filled with lost souls and necrotic energy.", 5, false,
            new String[]{"Boneman", "Mistman", "Wrathman"});
        soulCairn.addFeature("Soul Well");
        soulCairn.addFeature("Boneyard");
        worldMap.put("Soul Cairn", soulCairn);

        Location forgottenVale = new Location("Forgotten Vale",
            "A hidden glacial valley home to ancient Falmer temples.", 4, false,
            new String[]{"Falmer", "Frostbite Spider", "Ice Wraith"});
        forgottenVale.addFeature("Auriel's Shrine");
        forgottenVale.addFeature("Frozen Lake");
        worldMap.put("Forgotten Vale", forgottenVale);

        Location eastmarch = new Location("Eastmarch Hot Springs",
            "Steaming geothermal pools surrounded by volcanic rock.", 3, false,
            new String[]{"Horker", "Troll", "Ash Spawn"});
        eastmarch.addFeature("Sulfur Pools");
        eastmarch.addFeature("Ancient Cairn");
        worldMap.put("Eastmarch Hot Springs", eastmarch);

        Location hjaalmarch = new Location("Hjaalmarch Bog",
            "A treacherous wetland teeming with dangerous wildlife.", 3, false,
            new String[]{"Mudcrab", "Swamp Troll", "Chaurus"});
        hjaalmarch.addFeature("Sunken Ruins");
        hjaalmarch.addFeature("Bog Beacon");
        worldMap.put("Hjaalmarch Bog", hjaalmarch);

        Location pale = new Location("The Pale",
            "A stark, snowy landscape with scattered Nordic ruins.", 2, false,
            new String[]{"Snow Wolf", "Ice Wraith", "Skeleton"});
        pale.addFeature("Frostmere Crypt");
        pale.addFeature("Ancient Watchtower");
        worldMap.put("The Pale", pale);

        Location bleakCoast = new Location("Bleak Coast",
            "A frozen shoreline littered with shipwrecks and ice floes.", 3, false,
            new String[]{"Horker", "Ice Wolf", "Frost Troll"});
        bleakCoast.addFeature("Wreck of the Winter War");
        bleakCoast.addFeature("Ice Cave");
        worldMap.put("Bleak Coast", bleakCoast);

        Location volkihar = new Location("Castle Volkihar",
            "A foreboding vampire stronghold on a remote island.", 5, false,
            new String[]{"Vampire", "Death Hound", "Gargoyle"});
        volkihar.addFeature("Volkihar Cathedral");
        volkihar.addFeature("Bloodstone Chalice");
        worldMap.put("Castle Volkihar", volkihar);

        Location ravenRock = new Location("Raven Rock",
            "A Dunmer colony on the ash-covered island of Solstheim.", 4, true,
            new String[]{"Ash Spawn", "Riekling", "Netch"});
        ravenRock.addFeature("Redoran Council Hall");
        ravenRock.addFeature("The Retching Netch");
        worldMap.put("Raven Rock", ravenRock);

        Location telMithryn = new Location("Tel Mithryn",
            "A Telvanni wizard tower surrounded by ash and fungal growths.", 4, false,
            new String[]{"Ash Spawn", "Spriggan", "Burnt Spriggan"});
        telMithryn.addFeature("Telvanni Tower");
        telMithryn.addFeature("Silt Strider Stable");
        worldMap.put("Tel Mithryn", telMithryn);

        Location skaalVillage = new Location("Skaal Village",
            "A small Nordic settlement on Solstheim, devoted to the All-Maker.", 2, true,
            new String[]{"Wolf", "Bear", "Riekling"});
        skaalVillage.addFeature("Shaman's Hut");
        skaalVillage.addFeature("Greathall");
        worldMap.put("Skaal Village", skaalVillage);

        Location thirskMeadHall = new Location("Thirsk Mead Hall",
            "A warrior lodge on Solstheim, recently reclaimed from Rieklings.", 3, true,
            new String[]{"Riekling", "Bear", "Troll"});
        thirskMeadHall.addFeature("Mead Hall");
        thirskMeadHall.addFeature("Hunter's Camp");
        worldMap.put("Thirsk Mead Hall", thirskMeadHall);

        Location apocrypha = new Location("Apocrypha",
            "The otherworldly realm of Hermaeus Mora, filled with forbidden knowledge.", 5, false,
            new String[]{"Seeker", "Lurker", "Daedra"});
        apocrypha.addFeature("Black Book Archive");
        apocrypha.addFeature("Forbidden Library");
        worldMap.put("Apocrypha", apocrypha);
    }

    public void displayMainMenu() {
        while (true) {
            System.out.println("===================================");
            System.out.println("  Relicborne: Odyssey of Nikolaos"  );
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
    System.out.println("1. Knight: Nikolaos Ironmark The Dragonborn");
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
    currentQuest = "Defeat a Bandit in Whiterun Plains.";
    questLog.add(currentQuest);
    inGameMenu();
}

    private void inGameMenu() {
        while (true) {
            System.out.println("\n===== In-Game Menu =====");
            System.out.println("1. Travel");
            System.out.println("2. Manage Inventory and Equipment");
            System.out.println("3. View Gold");
            System.out.println("4. View Quest Log");
            System.out.println("5. View Player Stats");
            System.out.println("6. Rest at Inn");
            System.out.println("7. Faction Menu");
            System.out.println("8. Save Game");
            System.out.println("9. Open Main Menu");
            System.out.print("Choose an option (1-9): ");
            String input = scan.nextLine().trim();

            switch (input) {
                case "1": travel(); break;
                case "2": manageInventoryAndEquipment(); break;
                case "3": viewGold(); break;
                case "4": viewQuestLog(); break;
                case "5": viewPlayerStats(); break;
                case "6": restAtInn(); break;
                case "7": factionMenu(); break;
                case "8": saveGame(); break;
                case "9": System.out.println("Returning to Main Menu..."); return;
                default: System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void manageInventoryAndEquipment() {
        while (true) {
            System.out.println("\n===== Inventory and Equipment Management =====");
            System.out.println("1. View Inventory");
            System.out.println("2. View Equipment");
            System.out.println("3. Equip or Use Item");
            System.out.println("4. Back to Main Menu");
            System.out.print("Choose an option (1-4): ");
            String input = scan.nextLine().trim();

            switch (input) {
                case "1": viewInventory(); break;
                case "2": viewEquipment(); break;
                case "3": equipOrUseItem(); break;
                case "4": return;
                default: System.out.println("Invalid option. Try again.");
            }
        }
    }



    
    private void factionMenu() {
        System.out.println("\nFaction Menu:");
        System.out.println("1. Join a faction");
        System.out.println("2. View faction status");
        System.out.println("3. Do a faction quest");
        System.out.println("4. Back");
        int choice = getChoice(1, 4);
        switch (choice) {
            case 1:
                joinFaction();
                break;
            case 2:
                viewFactions();
                break;
            case 3:
                doFactionQuest();
                break;
            case 4:
                break;
        }
    }

    private void joinFaction() {
        System.out.println("\nAvailable Factions:");
        int count = 0;
        for (int i = 0; i < availableFactions.size(); i++) {
            Faction faction = availableFactions.get(i);
            if (!player.isInFaction(faction.getName())) {
                System.out.println((count + 1) + ". " + faction.getName());
                count++;
            }
        }
        if (count == 0) {
            System.out.println("No factions available to join.");
            return;
        }
        System.out.println((count + 1) + ". Cancel");
        int choice = getChoice(1, count + 1);
        if (choice <= count) {
            int factionIdx = 0;
            for (Faction faction : availableFactions) {
                if (!player.isInFaction(faction.getName())) {
                    if (factionIdx == choice - 1) {
                        player.joinFaction(faction);
                        return;
                    }
                    factionIdx++;
                }
            }
        }
    }

    private void viewFactions() {
        System.out.println("\nYour Factions:");
        if (player.getFactions().isEmpty()) {
            System.out.println("You are not a member of any factions.");
        } else {
            for (Faction faction : player.getFactions()) {
                System.out.println(faction.getName() + " (Reputation: " + faction.getReputation() + ")");
            }
        }
    }

    private void doFactionQuest() {
        if (player.getFactions().isEmpty()) {
            System.out.println("You must join a faction first!");
            return;
        }
        System.out.println("\nChoose a faction to undertake a quest for:");
        List<Faction> playerFactions = player.getFactions();
        for (int i = 0; i < playerFactions.size(); i++) {
            System.out.println((i + 1) + ". " + playerFactions.get(i).getName());
        }
        System.out.println((playerFactions.size() + 1) + ". Cancel");
        int choice = getChoice(1, playerFactions.size() + 1);
        if (choice <= playerFactions.size()) {
            Faction faction = playerFactions.get(choice - 1);
            String quest = getFactionQuest(faction.getName());
            System.out.println("You undertake a quest for the " + faction.getName() + ": " + quest);
            player.addFactionReputation(faction.getName(), 50);
            player.addGold(100);
            player.addXP(50);
            questLog.add(quest);
            completedQuests.add(quest);
        }
    }

    private String getFactionQuest(String factionName) {
        switch (factionName) {
            case "Companions":
                return "Clear a bandit camp near Whiterun Plains.";
            case "Thieves Guild":
                return "Steal a valuable artifact from a noble in Riften Woods.";
            case "Dark Brotherhood":
                return "Assassinate a corrupt merchant in Solitude Cliffs.";
            case "College of Winterhold":
                return "Retrieve an ancient tome from a ruin in Winterhold Tundra.";
            case "Imperial Legion":
                return "Defend a supply caravan near Solitude Cliffs.";
            default:
                return "No quest available.";
        }
    }

    private int getChoice(int min, int max) {
        while (true) {
            try {
                int choice = Integer.parseInt(scan.nextLine().trim());
                if (choice >= min && choice <= max) {
                    return choice;
                }
                System.out.println("Please enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    public void travel() {
        System.out.println("\n=== Available Locations ===");
        int i = 1;
        List<String> locations = new ArrayList<>(worldMap.keySet());
        for (String name : locations) {
            Location loc = worldMap.get(name);
            System.out.println(i + ". " + name + " (Danger: " + loc.dangerLevel + "/5)");
            System.out.println("   " + loc.description);
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
            encounterNPC(loc);
        }

        int encounters = 1 + random.nextInt(loc.dangerLevel);
        for (int i = 0; i < encounters; i++) {
            if (random.nextFloat() < 0.6f) {
                generateEncounter(loc);
            } else {
                generateDiscovery(loc);
            }
        }
    }

    private void generateEncounter(Location loc) {
    if (player == null) {
        System.out.println("Error: No player selected. Please start a new game.");
        return;
    }
    enemy = new Enemy(Enemy.Tier.values()[random.nextInt(3)], player.level);
    enemy.changeName(loc.enemyPool[random.nextInt(loc.enemyPool.length)]);
    String color = enemy.getTier() == Enemy.Tier.WEAK ? Color.GRAY :
                   enemy.getTier() == Enemy.Tier.NORMAL ? Color.YELLOW : Color.RED;
    System.out.println("\nYou encounter a " + Color.colorize(enemy.getDisplayName(), color) + " in " + loc.name + "!");
    encounter();
}

    private void encounter() {
    if (player == null || enemy == null) {
        System.out.println("Error: Combat cannot start. Player or enemy is missing.");
        return;
    }
    List<String> combatLog = new ArrayList<>();
    combatLog.add("Combat starts!");

    while (player.hp > 0 && enemy.isAlive()) {
        player.applyPassiveEffects();

        displayCombatStatus(player, enemy, combatLog);

        System.out.println("\nYour turn! Choose an action:");
        System.out.println("1. Attack");
        System.out.println("2. Use Skill");
        System.out.println("3. Use Shout");
        System.out.println("4. Flee");
        int choice = getChoice(1, 4);

        boolean dodgeAttack = checkDodge(player);

        if (choice == 1) {
            performPlayerAttack(player, enemy, combatLog);
        } else if (choice == 2) {
            performPlayerSkill(player, enemy, combatLog);
        } else if (choice == 3) {
            performPlayerShout(player, enemy, combatLog);
        } else {
            if (random.nextInt(100) < 50) {
                combatLog.add(player.getClassName() + " flees from battle!");
                System.out.println("You fled from the battle!");
                return;
            } else {
                combatLog.add(player.getClassName() + " fails to flee!");
                System.out.println("You failed to flee!");
            }
        }

        if (player instanceof Rook && random.nextInt(100) < 15) {
            int counterDmg = player.minDmg + random.nextInt(player.maxDmg - player.minDmg + 1);
            combatLog.add("Rook counterattacks for " + counterDmg + " damage!");
            System.out.println("Rook counterattacks for " + counterDmg + " damage!");
            enemy.receiveDamage(counterDmg);
        }

        player.decrementCooldowns();

        if (!enemy.isAlive()) {
            handleVictory(player, combatLog, enemy);
            break;
        }

        if (dodgeAttack || enemy.stunnedForNextTurn) {
            combatLog.add("Enemy is stunned or you dodged, so they skip their turn!");
            System.out.println("Enemy is stunned or you dodged, so they skip their turn!");
            enemy.stunnedForNextTurn = false;
        } else {
            if (player instanceof Assasin && ((Assasin)player).smokeBombActive) {
                combatLog.add("Enemy's attack is weakened by Smoke Bomb!");
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
            combatLog.add("Defeat! You have been slain by the " + enemy.getCurrentName() + "...");
            System.out.println("You have been defeated by the " + enemy.getCurrentName() + "...");
            break;
        }
    }

    if (player.checkLevelUp()) {
        player.levelUp();
        combatLog.add(player.getClassName() + " levels up to " + player.level + "!");
    }

    System.out.println("\n=== Combat Log Summary ===");
    combatLog.forEach(System.out::println);
}

    private boolean checkDodge(Hero player) {
    if (player instanceof Archer && random.nextInt(100) < 20) {
        System.out.println("Archer dodges the enemy’s next attack!");
        return true;
    } else if (player instanceof Assasin && random.nextInt(100) < 10) {
        System.out.println("Assassin dodges the enemy’s next attack!");
        return true;
    }
    return false;
}

    private void performPlayerAttack(Hero player, Enemy enemy, List<String> combatLog) {
        int damage = player.minDmg + random.nextInt(player.maxDmg - player.minDmg + 1);
        boolean isCritical = random.nextInt(100) < 15; // 15% crit chance
        if (isCritical) {
            damage *= 2;
            combatLog.add(player.getClassName() + " lands a critical hit for " + damage + " damage!");
            System.out.println("Critical hit! You deal " + damage + " damage!");
        } else {
            combatLog.add(player.getClassName() + " attacks for " + damage + " damage!");
            System.out.println("You attack for " + damage + " damage!");
        }
        enemy.receiveDamage(damage);
    }

    private void performPlayerSkill(Hero player, Enemy enemy, List<String> combatLog) {
        player.showAttacks();
        System.out.print("Choose a skill (1-" + player.attackNames.length + "): ");
        int skillChoice = getChoice(1, player.attackNames.length);
        String skillName = player.attackNames[skillChoice - 1];

        player.useSkill(skillChoice - 1, enemy);
        combatLog.add(player.getClassName() + " uses " + skillName);
    }

    private void performPlayerShout(Hero player, Enemy enemy, List<String> combatLog) {
        player.showShouts();
        System.out.print("Choose a shout (1-" + player.getShouts().length + "): ");
        int shoutChoice = getChoice(1, player.getShouts().length);
        String shoutName = player.getShouts()[shoutChoice - 1];

        player.useShout(shoutChoice - 1, enemy);
        combatLog.add(player.getClassName() + " shouts " + shoutName);
    }

 private void handleVictory(Hero player, List<String> combatLog, Enemy enemy) {
    player.addGold(20);
    player.addXP(20);
    combatLog.add("You gain 20 gold and 20 XP.");
    String color = enemy.getTier() == Enemy.Tier.WEAK ? Color.GRAY :
                   enemy.getTier() == Enemy.Tier.NORMAL ? Color.YELLOW : Color.RED;
    combatLog.add("You defeated the " + Color.colorize(enemy.getDisplayName(), color) + "!");
    if (questActive && currentQuest.contains("Defeat a Bandit") && enemy.getCurrentName().equals("Bandit")) {
        combatLog.add("Quest completed: " + currentQuest);
        System.out.println("Quest completed: " + currentQuest);
        completedQuests.add(currentQuest);
        questLog.remove(currentQuest);
        questActive = false;
        currentQuest = "";
    } else if (questActive && currentQuest.contains("Lost Relic") && random.nextInt(100) < 10) {
        combatLog.add("You found a Lost Relic! Quest completed!");
        System.out.println("You found a Lost Relic! Quest completed!");
        completedQuests.add(currentQuest);
        questLog.remove(currentQuest);
        questActive = false;
        currentQuest = "";
        player.addGold(50);
        combatLog.add("You gain 50 gold for completing the quest.");
    }
}

  private void displayCombatStatus(Hero player, Enemy enemy, List<String> combatLog) {
    System.out.println("\n=== Combat Status ===");
    
    int maxBarLength = 20;
    int hpPerChar = Math.max(1, player.maxHP / maxBarLength);
    int manaPerChar = Math.max(1, player.maxMana / maxBarLength);
    
    int playerHpBars = Math.max(0, player.hp / hpPerChar);
    int playerManaBars = Math.max(0, player.mana / manaPerChar);
    
    String playerHpBar = "=".repeat(playerHpBars) + " ".repeat(Math.max(0, maxBarLength - playerHpBars));
    String playerManaBar = "*".repeat(playerManaBars) + " ".repeat(Math.max(0, maxBarLength - playerManaBars));
    
    System.out.printf("%s | Level: %d%n", player.getClassName(), player.level);
    System.out.printf("HP: %d/%d [%s]%n", player.hp, player.maxHP, playerHpBar);
    System.out.printf("Mana: %d/%d [%s]%n", player.mana, player.maxMana, playerManaBar);
    
    int enemyHpBars = Math.max(0, enemy.hp / hpPerChar);
    String enemyHpBar = "=".repeat(enemyHpBars) + " ".repeat(Math.max(0, maxBarLength - enemyHpBars));
    String color = enemy.getTier() == Enemy.Tier.WEAK ? Color.GRAY :
                   enemy.getTier() == Enemy.Tier.NORMAL ? Color.YELLOW : Color.RED;
    System.out.printf("%s | Level: %d%n", Color.colorize(enemy.getDisplayName(), color), enemy.level);
    System.out.printf("HP: %d/%d [%s]%n", enemy.hp, enemy.maxHP, enemyHpBar);
    
    System.out.println("Recent actions:");
    int start = Math.max(0, combatLog.size() - 3);
    for (int i = start; i < combatLog.size(); i++) {
        System.out.println("- " + combatLog.get(i));
    }
}
    
    private String getItemRarity(String itemName) {
    if (itemName.contains("Dragonbone") || itemName.contains("Dawnbreaker") ||
        itemName.contains("Chillrend") || itemName.contains("Dragonbane") ||
        itemName.contains("Archmage") || itemName.contains("Daedric")) {
        return Color.PURPLE; // Legendary
    } else if (itemName.contains("Elven") || itemName.contains("Glass") ||
               itemName.contains("Greater") || itemName.contains("Major") ||
               itemName.contains("Cloak of Shadows") || itemName.contains("Nightshade") ||
               itemName.contains("Orb of Elements")) {
        return Color.BLUE; // Rare
    } else if (itemName.contains("Steel") || itemName.contains("Mithril") ||
               itemName.contains("Leather") || itemName.contains("Mana") ||
               itemName.contains("Chainmail") || itemName.contains("Composite") ||
               itemName.contains("Longbow")) {
        return Color.GREEN; // Uncommon
    } else {
        return Color.WHITE; // Common
    }
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
        String[] loot = {"gold", "potion", "weapon", "armor", "food", "misc"};
        String found = loot[random.nextInt(loot.length)];

        if (found.equals("gold")) {
            int amount = 10 + random.nextInt(20 * loc.dangerLevel);
            player.addGold(amount);
            System.out.println("You found " + amount + " gold!");
        } else if (found.equals("potion")) {
            String[] potions = {
                "Health Potion", "Mana Potion", "Greater Health Potion", "Major Health Potion",
                "Minor Health Potion", "Greater Mana Potion", "Major Mana Potion", "Minor Mana Potion",
                "Antidote Potion", "Fire Resistance Potion", "Frost Resistance Potion",
                "Poison Resistance Potion", "Healing Elixir", "Potion of Ultimate Healing"
            };
            String potion = potions[random.nextInt(potions.length)];
            player.addItem(potion, 0.5f);
            System.out.println("You found a " + Color.colorize(potion, getItemRarity(potion)) + "!");
        } else if (found.equals("weapon")) {
            String[] classWeapons = player instanceof Knight ? new String[] {
                "Iron Sword", "Steel Sword", "Mithril Sword", "Elven Sword", "Glass Sword",
                "Daedric Sword", "Dragonbone Sword", "Dawnbreaker", "Chillrend", "Dragonbane"
            } :
            player instanceof Mage ? new String[] {
                "Fire Staff", "Ice Wand", "Staff of Fireballs", "Staff of Ice Storms",
                "Staff of Healing", "Wand of Lightning", "Orb of Elements"
            } :
            player instanceof Archer ? new String[] {
                "Hunting Bow", "Longbow", "Composite Bow", "Elven Bow", "Glass Bow",
                "Daedric Bow", "Dragonbone Bow"
            } :
            player instanceof Rook ? new String[] {
                "Warhammer", "Battleaxe", "Mace", "Flail"
            } :
            player instanceof Assasin ? new String[] {
                "Iron Dagger", "Daedric Dagger", "Mithril Dagger", "Elven Dagger", "Glass Dagger",
                "Daedric Dagger", "Ebony Dagger"
            } :
            new String[] {
                "Staff of Healing", "Holy Scepter", "Divine Mace"
            };
            String weapon = classWeapons[random.nextInt(classWeapons.length)];
            player.addItem(weapon, 2.0f);
            System.out.println("You found a " + Color.colorize(weapon, getItemRarity(weapon)) + "!");
        } else if (found.equals("armor")) {
            String[] classArmors = player instanceof Knight ? new String[] {
                "Plate Armor", "Dragonbone Armor"
            } :
            player instanceof Mage ? new String[] {
                "Robe of Protection", "Archmage Robes"
            } :
            player instanceof Archer ? new String[] {
                "Leather Armor", "Elven Armor"
            } :
            player instanceof Rook ? new String[] {
                "Chainmail", "Dragonscale Armor"
            } :
            player instanceof Assasin ? new String[] {
                "Cloak of Shadows", "Nightshade Cloak"
            } :
            new String[] {
                "Robe of Protection", "Holy Shroud"
            };
            String armor = classArmors[random.nextInt(classArmors.length)];
            player.addItem(armor, 3.0f);
            System.out.println("You found a " + Color.colorize(armor, getItemRarity(armor)) + "!");
        } else if (found.equals("food")) {
            String[] foods = {
                "Apple", "Bread Loaf", "Cheese Wheel", "Roasted Meat", "Vegetable Stew"
            };
            String food = foods[random.nextInt(foods.length)];
            player.addItem(food, 0.4f);
            System.out.println("You found a " + Color.colorize(food, getItemRarity(food)) + "!");
        } else if (found.equals("misc")) {
            String[] misc = {"Torch", "Map of the Realm", "Ancient Coin", "Silver Ring", "Amulet of Talos"};
            String item = misc[random.nextInt(misc.length)];
            player.addItem(item, 0.3f);
            System.out.println("You found a " + Color.colorize(item, getItemRarity(item)) + "!");
        }
    }
}

   private void encounterNPC(Location loc) {
    System.out.println("\nYou enter the town of " + loc.name + "...");

    int npcCount = 4 + random.nextInt(2);
    List<Enemy> npcs = new ArrayList<>();
    List<String> npcNames = new ArrayList<>();
    LinkedList<String> availableNames = new LinkedList<>();
    Enemy dummyEnemy = new Enemy();
    availableNames.addAll(dummyEnemy.enemyNames);

    for (int i = 0; i < npcCount && !availableNames.isEmpty(); i++) {
        Enemy npc = new Enemy();
        String uniqueName = availableNames.remove(random.nextInt(availableNames.size()));
        npc.changeName(uniqueName);
        npc.setHostile(random.nextFloat() < 0.2f);
        npcs.add(npc);
        String color = npc.isHostile() ? Color.RED : Color.GREEN;
        npcNames.add(Color.colorize(npc.getDisplayName(), color));
        System.out.println((i + 1) + ". " + npcNames.get(i));
    }

    while (true) {
        System.out.println("\nOptions:");
        for (int i = 0; i < npcs.size(); i++) {
            System.out.println((i + 1) + ". Interact with " + npcNames.get(i));
        }
        System.out.println((npcs.size() + 1) + ". Continue Exploring");
        System.out.println((npcs.size() + 2) + ". Leave Town");
        System.out.print("Choose an option (1-" + (npcs.size() + 2) + "): ");
        String input = scan.nextLine().trim();

        try {
            int choice = Integer.parseInt(input);
            if (choice >= 1 && choice <= npcs.size()) {
                Enemy npc = npcs.get(choice - 1);
                if (npc.isHostile()) {
                    interactWithHostileNPC(npc, loc.name);
                } else if (npc.isDocile()) {
                    System.out.println("This NPC is friendly and cannot be fought.");
                    interactWithDocileNPC(npc.getCurrentName(), loc);
                } else {
                    System.out.println("This NPC's status is unclear. Try another.");
                }
            } else if (choice == npcs.size() + 1) {
                continueExploring(loc);
            } else if (choice == npcs.size() + 2) {
                System.out.println("You leave the town.");
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }
}

    private void continueExploring(Location loc) {
        System.out.println("\nYou continue exploring " + loc.name + "...");
        if (random.nextFloat() < 0.5f) {
            generateRandomNPCEncounter(loc);
        } else {
            System.out.println("You find nothing of interest.");
        }
    }

   private void generateRandomNPCEncounter(Location loc) {
    if (player == null) {
        System.out.println("Error: No player selected. Please start a new game.");
        return;
    }
    Enemy npc = new Enemy(Enemy.Tier.values()[random.nextInt(3)], player.level);
    npc.setHostile(random.nextFloat() < 0.3f);
    String color = npc.isHostile() ? Color.RED : Color.GREEN;
    System.out.println("\nYou encounter a " + Color.colorize(npc.getDisplayName(), color) + " while exploring " + loc.name + "!");
    if (npc.isHostile()) {
        interactWithHostileNPC(npc, loc.name);
    } else if (npc.isDocile()) {
        System.out.println("This NPC is friendly and cannot be fought.");
        interactWithDocileNPC(npc.getCurrentName(), loc);
    } else {
        System.out.println("This NPC's status is unclear. You avoid them.");
    }
}

    private void interactWithHostileNPC(Enemy npc, String location) {
    if (!npc.isHostile()) {
        System.out.println(npc.getCurrentName() + " is not hostile. You cannot fight them.");
        interactWithDocileNPC(npc.getCurrentName(), worldMap.get(location));
        return;
    }
    System.out.println(Color.colorize(npc.getDisplayName(), Color.RED) + " attacks!");
    this.enemy = npc;
    encounter();
}

    private void interactWithDocileNPC(String npcName, Location loc) {
    System.out.println("You meet a friendly NPC: " + Color.colorize(npcName, Color.GREEN) + "!");
    while (true) {
        System.out.println("\nWhat would you like to do with " + Color.colorize(npcName, Color.GREEN) + "?");
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
            System.out.println("You leave " + Color.colorize(npcName, Color.GREEN) + ".");
            break;
        } else {
            System.out.println("Invalid option. Try again.");
        }
    }
}

    private void tradeWithNPC(String npcName) {
    while (true) {
        System.out.println("\nTrading with " + Color.colorize(npcName, Color.GREEN) + ":");
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
            System.out.println("You stop trading with " + Color.colorize(npcName, Color.GREEN) + ".");
            break;
        } else {
            System.out.println("Invalid option. Try again.");
        }
    }
}

    private void buyItem(String npcName) {
    System.out.println("Items available for purchase from " + Color.colorize(npcName, Color.GREEN) + ":");
    String[] items;
    int[] prices;
    if (player instanceof Knight) {
        items = new String[]{
            "Iron Sword", "Steel Sword", "Mithril Sword", "Elven Sword", "Glass Sword",
            "Daedric Sword", "Dragonbone Sword", "Dawnbreaker", "Chillrend", "Dragonbane",
            "Plate Armor", "Dragonbone Armor",
            "Health Potion", "Potion of Ultimate Healing", "Amulet of Talos"
        };
        prices = new int[]{10, 15, 20, 25, 30, 35, 40, 45, 42, 40,
                           20, 35,
                           5, 15, 10};
    } else if (player instanceof Mage) {
        items = new String[]{
            "Fire Staff", "Ice Wand", "Staff of Fireballs", "Staff of Ice Storms",
            "Staff of Healing", "Wand of Lightning", "Orb of Elements",
            "Robe of Protection", "Archmage Robes",
            "Mana Potion", "Potion of Ultimate Healing", "Amulet of Talos"
        };
        prices = new int[]{15, 20, 25, 30, 35, 40, 45,
                           15, 25,
                           7, 15, 10};
    } else if (player instanceof Archer) {
        items = new String[]{
            "Hunting Bow", "Longbow", "Composite Bow", "Elven Bow", "Glass Bow",
            "Daedric Bow", "Dragonbone Bow",
            "Leather Armor", "Elven Armor",
            "Health Potion", "Potion of Ultimate Healing", "Amulet of Talos"
        };
        prices = new int[]{10, 15, 20, 25, 30, 35, 40,
                           15, 20,
                           5, 15, 10};
    } else if (player instanceof Rook) {
        items = new String[]{
            "Warhammer", "Battleaxe", "Mace", "Flail",
            "Chainmail", "Dragonscale Armor",
            "Health Potion", "Potion of Ultimate Healing", "Amulet of Talos"
        };
        prices = new int[]{15, 20, 25, 30,
                           20, 32,
                           5, 15, 10};
    } else if (player instanceof Assasin) {
        items = new String[]{
            "Iron Dagger", "Steel Dagger", "Mithril Dagger", "Elven Dagger", "Glass Dagger",
            "Daedric Dagger", "Ebony Dagger",
            "Cloak of Shadows", "Nightshade Cloak",
            "Health Potion", "Potion of Ultimate Healing", "Amulet of Talos"
        };
        prices = new int[]{10, 15, 20, 25, 30, 35, 38,
                           15, 20,
                           5, 15, 10};
    } else {
        items = new String[]{
            "Staff of Healing", "Holy Scepter", "Divine Mace",
            "Robe of Protection", "Holy Shroud",
            "Health Potion", "Mana Potion", "Potion of Ultimate Healing", "Amulet of Talos"
        };
        prices = new int[]{25, 30, 35,
                           15, 20,
                           5, 7, 15, 10};
    }

    for (int i = 0; i < items.length; i++) {
        System.out.println((i + 1) + ". " + Color.colorize(items[i], getItemRarity(items[i])) + " - " + prices[i] + " Gold");
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
                float weight = items[idx].contains("Potion") || items[idx].contains("Elixir") ? 0.5f :
                              items[idx].contains("Armor") || items[idx].contains("Robes") ||
                              items[idx].contains("Cloak") || items[idx].contains("Shroud") ? 3.0f :
                              items[idx].contains("Amulet") ? 0.2f : 2.0f;
                player.addItem(items[idx], weight);
                System.out.println("You bought a " + Color.colorize(items[idx], getItemRarity(items[idx])) + "!");
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
            if (item.name.contains("Potion") || item.name.contains("Elixir")) {
                value = item.name.contains("Ultimate") ? 10 : 3;
            } else if (item.name.contains("Amulet")) {
                value = 5;
            } else if (item.name.contains("Sword") || item.name.contains("Bow") ||
                       item.name.contains("Staff") || item.name.contains("Dagger") ||
                       item.name.contains("Scepter") || item.name.contains("Mace")) {
                value = item.name.contains("Dragonbone") || item.name.contains("Dawnbreaker") ||
                        item.name.contains("Chillrend") || item.name.contains("Dragonbane") ? 20 : 10;
            } else if (item.name.contains("Armor") || item.name.contains("Robes") ||
                       item.name.contains("Cloak") || item.name.contains("Shroud")) {
                value = item.name.contains("Dragonbone") || item.name.contains("Dragonscale") ? 25 : 15;
            }
            player.addGold(value);
            System.out.println("You sold " + Color.colorize(item.name, getItemRarity(item.name)) + " for " + value + " gold!");
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
            case "Whiterun Plains":
                dialogue = "The plains are home to the Companions. Bandits roam freely, so stay alert.";
                break;
            case "Riften Woods":
                dialogue = "Riften’s woods hide the Thieves Guild. Watch for Spriggans in the mist.";
                break;
            case "Solitude Cliffs":
                dialogue = "Solitude is the seat of the Imperial Legion. Forsworn rebels lurk nearby.";
                break;
            case "Winterhold Tundra":
                dialogue = "The College of Winterhold trains mages here. Ice Wraiths haunt the tundra.";
                break;
            case "Markarth Ruins":
                dialogue = "Markarth’s ruins are filled with Falmer and ancient Dwemer secrets.";
                break;
            case "Dawnstar Coast":
                dialogue = "Dawnstar’s fishermen brave icy waters, but Horkers are a constant threat.";
                break;
            case "Falkreath Forest":
                dialogue = "Falkreath’s forest is peaceful, but spirits linger in the graveyard.";
                break;
            case "Morthal Marshes":
                dialogue = "Morthal’s swamps hide strange lights and dangerous trolls.";
                break;
            case "Windhelm Snowfields":
                dialogue = "Windhelm is the Stormcloak stronghold, but snow bears roam nearby.";
                break;
            case "Riverwood Valley":
                dialogue = "Riverwood is quiet, but wolves prowl the valley at night.";
                break;
            case "Bleak Falls Barrow":
                dialogue = "Bleak Falls is an ancient tomb. Beware the Draugr within.";
                break;
            case "High Hrothgar":
                dialogue = "The Greybeards meditate here. Only the worthy climb the 7,000 steps.";
                break;
            case "Ivarstead Village":
                dialogue = "Ivarstead is a stop for pilgrims heading to High Hrothgar.";
                break;
            case "Dragon Bridge":
                dialogue = "Dragon Bridge is a key trade route, but bandits often ambush travelers.";
                break;
            case "Karthwasten Hills":
                dialogue = "Karthwasten’s mines are rich, but Forsworn claim these hills.";
                break;
            case "Rorikstead Fields":
                dialogue = "Rorikstead’s farms thrive, but wolves threaten the livestock.";
                break;
            case "Helgen Ruins":
                dialogue = "Helgen was destroyed by a dragon. Only bandits remain now.";
                break;
            case "Blackreach":
                dialogue = "Blackreach’s caverns hide Dwemer secrets and Falmer ambushes.";
                break;
            case "Soul Cairn":
                dialogue = "The Soul Cairn traps lost souls. Beware the undead guardians.";
                break;
            case "Forgotten Vale":
                dialogue = "The Forgotten Vale holds ancient Falmer temples and icy dangers.";
                break;
            case "Eastmarch Hot Springs":
                dialogue = "Eastmarch’s hot springs are warm, but ash creatures roam here.";
                break;
            case "Hjaalmarch Bog":
                dialogue = "Hjaalmarch’s bogs are treacherous, filled with trolls and insects.";
                break;
            case "The Pale":
                dialogue = "The Pale’s snowy wastes hide ancient ruins and restless spirits.";
                break;
            case "Bleak Coast":
                dialogue = "The Bleak Coast’s shipwrecks attract scavengers and ice creatures.";
                break;
            case "Castle Volkihar":
                dialogue = "Castle Volkihar is a vampire lair. Only the brave or foolish enter.";
                break;
            case "Raven Rock":
                dialogue = "Raven Rock is a Dunmer outpost, but ash creatures threaten it.";
                break;
            case "Tel Mithryn":
                dialogue = "Tel Mithryn’s wizards study dangerous magic amid ash and fungi.";
                break;
            case "Skaal Village":
                dialogue = "The Skaal honor the All-Maker, but Rieklings raid their lands.";
                break;
            case "Thirsk Mead Hall":
                dialogue = "Thirsk’s warriors reclaimed this hall from Rieklings. It’s still contested.";
                break;
            case "Apocrypha":
                dialogue = "Apocrypha is Hermaeus Mora’s realm. Knowledge comes at a price here.";
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
        System.out.println("=== Inventory ===");
        if (player.getInventory().isEmpty()) {
            System.out.println("Your inventory is empty!");
        } else {
            for (int i = 0; i < player.getInventory().size(); i++) {
                Hero.InventoryItem item = player.getInventory().get(i);
                System.out.println((i + 1) + ". " + Color.colorize(item.name, getItemRarity(item.name)) +
                                   " (Quantity: " + item.quantity + ", Weight: " + item.weight + ")");
            }
        }
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

    private void equipOrUseItem() {
        if (player != null) {
            player.showInventory();
            System.out.print("Choose an item to equip or use (1-" + player.getInventory().size() + ") or 'cancel': ");
            String itemChoice = scan.nextLine().trim();
            if (itemChoice.equalsIgnoreCase("cancel")) {
                return;
            }
            try {
                int idx = Integer.parseInt(itemChoice) - 1;
                if (idx >= 0 && idx < player.getInventory().size()) {
                    Hero.InventoryItem item = player.getInventory().get(idx);
                    if (!player.equipItem(item.name)) {
                        player.useItem(item.name);
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
            out.writeObject(player.getInventory());
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
        player.getInventory().clear(); // Clear existing inventory to avoid duplicates
        player.getInventory().addAll((List<Hero.InventoryItem>) in.readObject());
        equippedWeapon = (String) in.readObject();
        equippedArmor = (String) in.readObject();
        questLog = (List<String>) in.readObject();
        completedQuests = (List<String>) in.readObject();
        System.out.println("Game loaded successfully!");
    } catch (Exception e) {
        System.out.println("Failed to load game.");
        e.printStackTrace();
    }
}

    class Location {
        String name;
        String description;
        int dangerLevel;
        List<String> features;
        boolean hasTown;
        String[] enemyPool;

        public Location(String name, String description, int dangerLevel, boolean hasTown, String[] enemyPool) {
            this.name = name;
            this.description = description;
            this.dangerLevel = dangerLevel;
            this.features = new ArrayList<>();
            this.hasTown = hasTown;
            this.enemyPool = enemyPool;
        }

        public void addFeature(String feature) {
            features.add(feature);
        }
    }
}