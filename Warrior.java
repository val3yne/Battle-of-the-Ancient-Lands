import java.util.Random;

public class Warrior extends Character {
    private int attackRange;  // max distance warrior can attack
    private int movementRange; // max distance warrior can move

    // constructor
    public Warrior(String name) {
        super(name,
                50, //mana
                 40, // damage
                120, //health
                1, // level
                0); // starting position (right side)

        // close combat & high movement
        this.attackRange = 25;
        this.movementRange = 15;
    }

    // warrior movement
    @Override
    public void move() {
        Random rand = new Random();

        // generates random movement
        int movement = rand.nextInt(movementRange * 2 + 1) - movementRange;
        position += movement;

        // doesn't allow negative positions
        if (position < 0) {
            position = 0;
        }
    }

    // warrior attacks
    @Override
    public void attack(Character target) {
        Random rand = new Random();

        // calculates distance
        int distance = Math.abs(this.position - target.getPosition());

        // if target is out of range warrior can't attack
        if (distance > attackRange) {
            return;
        }
        // can't attack without enough mana
        if (mana < 5) {
            return;
        }

        // dice roll determines whether the attack hits
        int diceRoll = rand.nextInt(20) + 1;

        // attack hit with random damage
        if (diceRoll >= 5) {
            int damageDealt = rand.nextInt(14) + 15;
            target.health -= damageDealt;


            mana -= 5;
        } else {
            // attack missed but mana is consumed
            mana -= 5;
        }
    }
}
