package com.mycompany.turnbased_rpg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.ObjectInputStream;
import java.io.IOException;

public abstract class Hero implements Serializable {
    private static final long serialVersionUID = 1L;

    protected transient Random random;
    public int hp;
    public int maxHP;
    public int minDmg;
    public int maxDmg;
    public int mana;
    public int maxMana;
    public int gold;
    public int xp = 0;
    public int level = 1;
    public int xpToLevel = 50;
    protected String[] attackNames;
    private int shoutCooldown = 0;
    private String[] shouts = {"Unrelenting Force"};
    private static final int SHOUT_COOLDOWN = 3;
    private List<Faction> factions = new ArrayList<>();

    public class Equipment implements Serializable {
        public String weapon;
        public String armor;
        public String accessory;
        public int weaponBonus;
        public int armorBonus;

        public Equipment() {
            weapon = "Fists";
            armor = "Clothes";
            accessory = "None";
            weaponBonus = 0;
            armorBonus = 0;
        }
    }

    public Equipment equipment = new Equipment();

    public class InventoryItem implements Serializable {
        String name;
        int quantity;
        float weight;

        public InventoryItem(String name, float weight) {
            this.name = name;
            this.weight = weight;
            this.quantity = 1;
        }
    }

    private List<InventoryItem> inventory = new ArrayList<>();
    private float carryingCapacity = 100.0f;

    public String[] weaponTable = {
        "Iron Sword", "Steel Sword", "Mithril Sword", "Elven Sword", "Glass Sword", "Daedric Sword",
        "Hunting Bow", "Longbow", "Composite Bow", "Elven Bow", "Glass Bow", "Daedric Bow",
        "Fire Staff", "Ice Wand", "Staff of Fireballs", "Staff of Ice Storms", "Staff of Healing",
        "Wand of Lightning", "Orb of Elements",
        "Warhammer", "Battleaxe", "Mace", "Flail",
        "Dragonbone Sword", "Dawnbreaker", "Chillrend", "Dragonbane", "Dragonbone Bow",
        "Ebony Dagger"
    };

    public String[] itemTable = {
        "Health Potion", "Mana Potion", "Strength Potion", "Agility Potion", "Intelligence Potion",
        "Leather Armor", "Chainmail", "Plate Armor", "Robe of Protection", "Cloak of Shadows",
        "Lockpick", "Soul Gem", "Scroll of Teleportation", "Amulet of Health",
        "Dragonbone Armor", "Potion of Ultimate Healing", "Amulet of Talos", "Ring of Hircine",
        "Archmage Robes", "Elven Armor", "Dragonscale Armor", "Nightshade Cloak", "Holy Shroud"
    };

    public Hero(Random random) {
        this.random = random != null ? random : new Random();
        this.gold = 50;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.random = new Random();
    }

    public abstract String getClassName();
    protected abstract List<String> getAllowedWeapons();
    protected abstract List<String> getAllowedArmors();

    public boolean equipItem(String itemName) {
        List<String> allowedWeapons = getAllowedWeapons();
        List<String> allowedArmors = getAllowedArmors();
        for (InventoryItem item : inventory) {
            if (item.name.equalsIgnoreCase(itemName)) {
                if (allowedWeapons.contains(itemName)) {
                    equipment.weapon = itemName;
                    equipment.weaponBonus = calculateWeaponBonus(itemName);
                    System.out.println("Equipped " + itemName + " (+" + equipment.weaponBonus + " damage)");
                    return true;
                } else if (allowedArmors.contains(itemName)) {
                    equipment.armor = itemName;
                    equipment.armorBonus = calculateArmorBonus(itemName);
                    System.out.println("Equipped " + itemName + " (+" + equipment.armorBonus + " defense)");
                    return true;
                }
            }
        }
        System.out.println("Cannot equip " + itemName + ": not compatible with " + getClassName());
        return false;
    }

    public boolean useItem(String itemName) {
        for (InventoryItem item : inventory) {
            if (item.name.equalsIgnoreCase(itemName)) {
                switch (itemName.toLowerCase()) {
                    case "health potion":
                        hp = Math.min(hp + 50, maxHP);
                        System.out.println("Used Health Potion, restored 50 HP!");
                        break;
                    case "mana potion":
                        mana = Math.min(mana + 50, maxMana);
                        System.out.println("Used Mana Potion, restored 50 Mana!");
                        break;
                    case "potion of ultimate healing":
                        hp = maxHP;
                        System.out.println("Used Potion of Ultimate Healing, fully restored HP!");
                        break;
                    case "amulet of talos":
                        shoutCooldown = Math.max(0, shoutCooldown - 1);
                        System.out.println("Used Amulet of Talos, reduced shout cooldown by 1!");
                        break;
                    default:
                        System.out.println("Cannot use " + itemName + "!");
                        return false;
                }
                item.quantity--;
                if (item.quantity == 0) {
                    inventory.remove(item);
                }
                return true;
            }
        }
        System.out.println("Item not found in inventory!");
        return false;
    }

    private int calculateWeaponBonus(String weapon) {
        if (weapon.contains("Iron")) return 3;
        if (weapon.contains("Steel")) return 5;
        if (weapon.contains("Mithril")) return 8;
        if (weapon.contains("Elven")) return 10;
        if (weapon.contains("Glass")) return 12;
        if (weapon.contains("Daedric")) return 15;
        if (weapon.contains("Hunting Bow") || weapon.contains("Longbow")) return 5;
        if (weapon.contains("Composite Bow")) return 7;
        if (weapon.contains("Staff") || weapon.contains("Wand")) return 7;
        if (weapon.contains("Orb")) return 10;
        if (weapon.contains("Mace") || weapon.contains("Flail")) return 6;
        if (weapon.contains("Dragonbone")) return 18;
        if (weapon.contains("Dawnbreaker")) return 20;
        if (weapon.contains("Chillrend")) return 15;
        if (weapon.contains("Dragonbane")) return 15;
        if (weapon.contains("Ebony")) return 14;
        return 0;
    }

    private int calculateArmorBonus(String armor) {
        if (armor.contains("Leather")) return 3;
        if (armor.contains("Chainmail")) return 6;
        if (armor.contains("Plate")) return 10;
        if (armor.contains("Robe")) return 4;
        if (armor.contains("Cloak")) return 5;
        if (armor.contains("Dragonbone")) return 15;
        if (armor.contains("Dragonscale")) return 12;
        if (armor.contains("Elven")) return 8;
        if (armor.contains("Nightshade")) return 7;
        if (armor.contains("Archmage")) return 6;
        if (armor.contains("Holy Shroud")) return 5;
        return 0;
    }

    public boolean addItem(String itemName, float weight) {
        if (getCurrentWeight() + weight > carryingCapacity) {
            System.out.println("Inventory full!");
            return false;
        }
        for (InventoryItem item : inventory) {
            if (item.name.equals(itemName)) {
                item.quantity++;
                return true;
            }
        }
        inventory.add(new InventoryItem(itemName, weight));
        return true;
    }

    public float getCurrentWeight() {
        float total = 0;
        for (InventoryItem item : inventory) {
            total += item.weight * item.quantity;
        }
        return total;
    }

    public void showInventory() {
        System.out.println("\n=== Inventory (" + getCurrentWeight() + "/" + carryingCapacity + ") ===");
        for (InventoryItem item : inventory) {
            System.out.println("- " + item.name + " x" + item.quantity + " (" + item.weight + " lbs each)");
        }
        System.out.println("\nEquipped:");
        System.out.println("- Weapon: " + equipment.weapon + " (+" + equipment.weaponBonus + ")");
        System.out.println("- Armor: " + equipment.armor + " (+" + equipment.armorBonus + ")");
    }

    public void showAttacks() {
        for (int i = 0; i < attackNames.length; i++) {
            System.out.println((i + 1) + ". " + attackNames[i]);
        }
    }

    public void addGold(int amount) {
        gold += amount;
        System.out.println("You received " + amount + " gold. Total gold: " + gold);
    }

    public boolean spendGold(int amount) {
        if (gold >= amount) {
            gold -= amount;
            System.out.println("You spent " + amount + " gold. Remaining gold: " + gold);
            return true;
        } else {
            System.out.println("Not enough gold!");
            return false;
        }
    }

    public void joinFaction(Faction faction) {
        factions.add(faction);
        faction.joinFaction();
        faction.applyBenefits(this);
    }

    public boolean isInFaction(String factionName) {
        for (Faction faction : factions) {
            if (faction.getName().equalsIgnoreCase(factionName)) {
                return faction.isMember();
            }
        }
        return false;
    }

    public void addFactionReputation(String factionName, int amount) {
        for (Faction faction : factions) {
            if (faction.getName().equalsIgnoreCase(factionName)) {
                faction.addReputation(amount);
                return;
            }
        }
    }

    public List<Faction> getFactions() {
        return factions;
    }

    public void addXP(int amount) {
        xp += amount;
        System.out.println("You gained " + amount + " XP. Total XP: " + xp + "/" + xpToLevel);
    }

    public boolean checkLevelUp() {
        return xp >= xpToLevel;
    }

    public void levelUp() {
        while (xp >= xpToLevel) {
            xp -= xpToLevel;
            level++;
            maxHP += 20;
            maxMana += 10;
            minDmg += 3;
            maxDmg += 5;
            hp = maxHP;
            mana = maxMana;
            xpToLevel = (int) (xpToLevel * 1.4);
            System.out.println("You leveled up to level " + level + "!");
            System.out.println("Stats increased! HP and Mana restored!");
        }
    }

    public void receiveDamage(int dmg) {
        if (this instanceof Knight) {
            dmg = (int) (dmg * 0.9);
        }
        hp -= dmg;
        if (hp < 0) {
            hp = 0;
        }
        System.out.println(getClassName() + " takes " + dmg + " damage.");
    }

    public void decrementCooldowns() {
        if (shoutCooldown > 0) {
            shoutCooldown--;
        }
    }

    public void useSkill(int skillIdx, Enemy enemy) {
        double multiplier = getSkillMultiplier();
        int baseDmg = minDmg + random.nextInt(maxDmg - minDmg + 1);
        int damage = (int)(baseDmg * multiplier);
        System.out.println("You use " + attackNames[skillIdx] + " and deal " + damage + " damage!");
        enemy.receiveDamage(damage);
    }

    public void useShout(int shoutIdx, Enemy enemy) {
        if (shoutCooldown > 0) {
            System.out.println("Shout is on cooldown! (" + shoutCooldown + " turns remaining)");
            return;
        }
        if (shoutIdx >= 0 && shoutIdx < shouts.length) {
            String shout = shouts[shoutIdx];
            if (shout.equals("Unrelenting Force")) {
                int damage = 50 + random.nextInt(20);
                enemy.receiveDamage(damage);
                enemy.stunnedForNextTurn = true;
                System.out.println("You shout 'Fus Ro Dah!' dealing " + damage + " damage and stunning the enemy!");
                shoutCooldown = SHOUT_COOLDOWN;
            }
        } else {
            System.out.println("Invalid shout!");
        }
    }

    public void showShouts() {
        System.out.println("\nAvailable Shouts:");
        for (int i = 0; i < shouts.length; i++) {
            System.out.println((i + 1) + ". " + shouts[i]);
        }
    }

    public String[] getShouts() {
        return shouts;
    }

    public double getSkillMultiplier() {
        int tier = (level - 1) / 10 + 1;
        return 1.0 + 0.1 * (tier - 1);
    }

    public String getStatsString() {
        return
            "Class: " + getClassName() + "\n" +
            "Level: " + level + "\n" +
            "XP: " + xp + "/" + xpToLevel + "\n" +
            "HP: " + hp + "/" + maxHP + "\n" +
            "Mana: " + mana + "/" + maxMana + "\n" +
            "Attack: " + minDmg + " - " + maxDmg + "\n" +
            "Gold: " + gold;
    }

    public List<InventoryItem> getInventory() {
        return inventory;
    }

    public void applyPassiveEffects() {
        if (this instanceof Mage) {
            mana = Math.min(mana + (int)(maxMana * 0.1), maxMana);
            System.out.println("Mage regenerates " + (int)(maxMana * 0.1) + " mana.");
        }
    }
}