package com.mycompany.mahipus_pokemon;

import java.util.*;

public class Mahipus_Pokemon {

    static class Move {
        String name;
        int damage;

        public Move(String name, int damage) {
            this.name = name;
            this.damage = damage;
        }
    }

    static class Pokemon {
        String name;
            int maxHp;
                int currentHp;
        List<Move> moves;

        public Pokemon(String name, int maxHp, List<Move> moves) {
            this.name = name;
                this.maxHp = maxHp;
                this.currentHp = maxHp;
            this.moves = moves;
        }

        public boolean isFainted() {
            return currentHp <= 0;
        }

        public void takeDamage(int dmg) {
            currentHp -= dmg;
            if (currentHp < 0) currentHp = 0;
        }

        public String getStatus() {
            return name + " HP: " + currentHp + "/" + maxHp;
        }
    }

    static class Battle {
        Pokemon playerPokemon;
            Pokemon opponentPokemon;
            Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        public Battle(Pokemon playerPokemon, Pokemon opponentPokemon) {
            this.playerPokemon = playerPokemon;
            this.opponentPokemon = opponentPokemon;
        }

        public void start() {
            System.out.println("A wild " + opponentPokemon.name + " appeared!");
            System.out.println("Go! " + playerPokemon.name + "!\n");

            boolean playerTurn = true;

            while (!playerPokemon.isFainted() && !opponentPokemon.isFainted()) {
                if (playerTurn) {
                    System.out.println(playerPokemon.getStatus());
                    System.out.println(opponentPokemon.getStatus());
                    System.out.println("\nYour turn! Choose a move:");

                    for (int i = 0; i < playerPokemon.moves.size(); i++) {
                        Move m = playerPokemon.moves.get(i);
                        System.out.printf("%d) %s (Damage: %d)\n", i + 1, m.name, m.damage);
                    }

                    int choice = 0;
                    while (true) {
                        System.out.print("Enter move number: ");
                        String input = scanner.nextLine();
                        try {
                            choice = Integer.parseInt(input);
                            if (choice < 1 || choice > playerPokemon.moves.size()) {
                                System.out.println("Invalid choice, try again.");
                            } else {
                                break;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input, please enter a number.");
                        }
                    }

                    Move selectedMove = playerPokemon.moves.get(choice - 1);
                    System.out.println(playerPokemon.name + " uses " + selectedMove.name + "!");
                    opponentPokemon.takeDamage(selectedMove.damage);
                    if (opponentPokemon.isFainted()) {
                        System.out.println("The opponent's " + opponentPokemon.name + " fainted! You win!");
                        break;
                    } else {
                        System.out.println(opponentPokemon.getStatus());
                    }
                } else {
                    Move oppMove = opponentPokemon.moves.get(random.nextInt(opponentPokemon.moves.size()));
                    System.out.println("\nOpponent's turn...");
                    System.out.println(opponentPokemon.name + " uses " + oppMove.name + "!");
                    playerPokemon.takeDamage(oppMove.damage);
                    if (playerPokemon.isFainted()) {
                        System.out.println(playerPokemon.name + " fainted! You lost the battle.");
                        break;
                    } else {
                        System.out.println(playerPokemon.getStatus());
                    }
                }

                playerTurn = !playerTurn;
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {
        
        Move thunderShock = new Move("Thunder Shock", 20);
        Move quickAttack = new Move("Quick Attack", 15);
        Move ironTail = new Move("Iron Tail", 25);
        Move electroBall = new Move("Electro Ball", 30);

        Move ember = new Move("Ember", 25);
        Move scratch = new Move("Scratch", 15);
        Move flamethrower = new Move("Flamethrower", 30);
        Move fireSpin = new Move("Fire Spin", 35);

        Move vineWhip = new Move("Vine Whip", 20);
        Move tackle = new Move("Tackle", 15);
        Move razorLeaf = new Move("Razor Leaf", 25);
        Move seedBomb = new Move("Seed Bomb", 30);

        Move waterGun = new Move("Water Gun", 20);
        Move bubble = new Move("Bubble", 15);
        Move hydroPump = new Move("Hydro Pump", 35);
        Move bite = new Move("Bite", 25);

        Move gust = new Move("Gust", 20);
        Move swift = new Move("Swift", 25);
        Move wingAttack = new Move("Wing Attack", 30);
        Move peck = new Move("Peck", 15);

        Move shadowClaw = new Move("Shadow Claw", 30);
        Move darkPulse = new Move("Dark Pulse", 35);
        Move hex = new Move("Hex", 25);
        Move nightmare = new Move("Nightmare", 20);

        Move doubleKick = new Move("Double Kick", 25);
        Move rockThrow = new Move("Rock Throw", 25);
        Move crossChop = new Move("Cross Chop", 35);
        Move karateChop = new Move("Karate Chop", 30);

       
        Pokemon pikachu = new Pokemon("Pikachu", 100, Arrays.asList(thunderShock, quickAttack, ironTail, electroBall));
        Pokemon charmander = new Pokemon("Charmander", 110, Arrays.asList(ember, scratch, flamethrower, fireSpin));
        Pokemon bulbasaur = new Pokemon("Bulbasaur", 120, Arrays.asList(vineWhip, tackle, razorLeaf, seedBomb));
        Pokemon squirtle = new Pokemon("Squirtle", 130, Arrays.asList(waterGun, bubble, hydroPump, bite));
        Pokemon pidgey = new Pokemon("Pidgey", 90, Arrays.asList(gust, swift, wingAttack, peck));
        Pokemon gengar = new Pokemon("Gengar", 140, Arrays.asList(shadowClaw, darkPulse, hex, nightmare));
        Pokemon eevee = new Pokemon("Eevee", 115, Arrays.asList(tackle, quickAttack, swift, bite));
        Pokemon onix = new Pokemon("Onix", 150, Arrays.asList(tackle, rockThrow, bite, swift));
        Pokemon dragonite = new Pokemon("Dragonite", 160, Arrays.asList(crossChop, bite, wingAttack, thunderShock));
        Pokemon mewtwo = new Pokemon("Mewtwo", 180, Arrays.asList(thunderShock, swift, shadowClaw, darkPulse));
        Pokemon snorlax = new Pokemon("Snorlax", 200, Arrays.asList(tackle, crossChop, bite, karateChop));
        Pokemon jigglypuff = new Pokemon("Jigglypuff", 100, Arrays.asList(scratch, tackle, bite, quickAttack));
        Pokemon alakazam = new Pokemon("Alakazam", 130, Arrays.asList(shadowClaw, swift, crossChop, darkPulse));
        Pokemon flareon = new Pokemon("Flareon", 120, Arrays.asList(flamethrower, quickAttack, bite, fireSpin));
        Pokemon vaporeon = new Pokemon("Vaporeon", 140, Arrays.asList(hydroPump, bite, quickAttack, waterGun));
        Pokemon jolteon = new Pokemon("Jolteon", 110, Arrays.asList(thunderShock, quickAttack, doubleKick, electroBall));
        Pokemon gyarados = new Pokemon("Gyarados", 170, Arrays.asList(bite, hydroPump, crossChop, tackle));
        Pokemon magikarp = new Pokemon("Magikarp", 50, Arrays.asList(tackle, bite, quickAttack, swimSplash()));
        Pokemon lapras = new Pokemon("Lapras", 160, Arrays.asList(waterGun, hydroPump, bite, thunderShock));
        Pokemon machamp = new Pokemon("Machamp", 190, Arrays.asList(crossChop, karateChop, tackle, bite));
        Pokemon articuno = new Pokemon("Articuno", 150, Arrays.asList(iceBeam(), gust, swift, peck));
        Pokemon zapdos = new Pokemon("Zapdos", 150, Arrays.asList(thunderShock, swift, quickAttack, gust));
        Pokemon moltres = new Pokemon("Moltres", 150, Arrays.asList(flamethrower, wingAttack, peck, fireSpin));
        Pokemon snivy = new Pokemon("Snivy", 110, Arrays.asList(vineWhip, tackle, razorLeaf, quickAttack));
        Pokemon tepig = new Pokemon("Tepig", 120, Arrays.asList(ember, tackle, fireSpin, scratch));
        Pokemon oshawott = new Pokemon("Oshawott", 120, Arrays.asList(waterGun, tackle, bite, quickAttack));

        List<Pokemon> available = Arrays.asList(
                pikachu, charmander, bulbasaur, squirtle, pidgey, gengar, eevee, onix, dragonite,
                mewtwo, snorlax, jigglypuff, alakazam, flareon, vaporeon, jolteon, gyarados,
                magikarp, lapras, machamp, articuno, zapdos, moltres, snivy, tepig, oshawott
        );

        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose your Pokémon:");

        for (int i = 0; i < available.size(); i++) {
            System.out.println((i + 1) + ") " + available.get(i).name);
        }

        int choice = 0;
        while (true) {
            System.out.print("Enter number: ");
            String input = scanner.nextLine();
            try {
                choice = Integer.parseInt(input);
                if (choice < 1 || choice > available.size()) {
                    System.out.println("Invalid choice, pick a number from the list.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a number.");
            }
        }

        Pokemon playerPokemon = available.get(choice - 1);

        Random random = new Random();
        Pokemon opponentPokemon;
        do {
            opponentPokemon = available.get(random.nextInt(available.size()));
        } while (opponentPokemon == playerPokemon);

        Battle battle = new Battle(playerPokemon, opponentPokemon);
        battle.start();

        scanner.close();
    }

    static Move iceBeam() { return new Move("Ice Beam", 35); }
    static Move swimSplash() { return new Move("Splash", 0); }

}
