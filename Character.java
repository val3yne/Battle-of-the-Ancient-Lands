public abstract class Character {

    // each character must implement
    public abstract void move();
    public abstract void attack(Character target);

    // common attributes
    protected String name;
    protected int mana;
    protected int damage;
    protected int health;
    protected int level;
    protected int position;


    // constructor
    public Character(String name, int mana, int damage, int health, int level, int position) {
        this.name = name;
        this.mana = mana;
        this.damage = damage;
        this.health = health;
        this.level = level;
        this.position = position;

    }

    // getters

    public int getMana() {
        return mana;
    }

    public int getDamage() {     // damage is randomly generated
        return damage;
    }

    public int getHealth() {
        return health;
    }

    public int getLevel() {
        return level;
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public void setHealth(int health) {
        this.health = health;
    }

}


