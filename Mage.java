import java.util.Random;

public class Mage extends Character {
    private int attackRange; // max distance mage can attack from
    private int movementRange; //max step mage can move

    // constructor
    public Mage(String name) {
        super(
                name,
                100,   // mana
                25,   // damage
                80,   // health
                1,    // level
                0     // position (left side)
        );

        // has long attack range & low movement
        this.attackRange = 1000;
        this.movementRange = 8;
    }

    // mage movement
    @Override
    public void move() {
        Random rand = new Random();
        // moves randomly
        int movement = rand.nextInt(movementRange * 2 + 1) - movementRange;
        position += movement;

        //  doesn't let mage move to negative positions
        if (position < 0) {
            position = 0;
        }

        // mage regenerates mana every turn
        mana += 5;
        if (mana > 100) mana = 100;
    }

    // mage attacks
    @Override
    public void attack(Character target) {
        Random rand = new Random();

        // checks if target is in range
        if (Math.abs(position - target.position) > attackRange) {
            return; // target is too far
        }
        // can't attack without enough mana
        if (mana < 10) {
            return;
        }
        // dice roll determines whether the spell hits
        int diceRoll = rand.nextInt(20) + 1;

        // spell hit with random damage
        if (diceRoll >= 7) {
            int damageDealt = rand.nextInt(16) + 25;
            target.health -= damageDealt;

            mana -= 10;
        } else {
            // spell missed but mana is consumed
            mana -= 10;
        }
    }
}
