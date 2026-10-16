package creature;

import weapon.Weapon;

public abstract class Character implements Creature {
    private String name;
    private int hp;
    private int maxHp; // HPバー表示用（初期HP）
    private Weapon weapon;

    public Character(String name, int hp, Weapon weapon) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.weapon = weapon;
    }

    public String die() { return "「" + this.getName() + "は死んでしまった！」"; }

    @Override
    public final boolean isAlive() { return this.hp > 0; }

    @Override
    public String showStatus() { return this.getName() + " (HP: " + this.getHp() + ")"; }

    @Override
    public abstract String attack(Creature target);

    @Override
    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    @Override
    public int getHp() { return this.hp; }

    @Override
    public void setHp(int hp) { this.hp = (hp < 0) ? 0 : hp; }

    public int getMaxHp() { return this.maxHp; }

    public void setMaxHp(int maxHp) { this.maxHp = maxHp; }

    public Weapon getWeapon() { return this.weapon; }

    public void setWeapon(Weapon weapon) { this.weapon = weapon; }
}
