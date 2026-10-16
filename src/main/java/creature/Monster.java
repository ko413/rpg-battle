package creature;

public abstract class Monster implements Creature {
    private String name;
    private int hp;
    private int maxHp; // HPバー表示用（初期HP）
    private char suffix;

    public Monster(String name, char suffix, int hp) {
        this.name = name;
        this.suffix = suffix;
        this.hp = hp;
        this.maxHp = hp;
    }

    public String run() { return "「" + this.getName() + "は逃げ出した」"; }

    public String die() { return "「" + this.getName() + "を倒した！」"; }

    @Override
    public final boolean isAlive() { return this.hp > 0; }

    @Override
    public String showStatus() { return this.getName() + " (HP: " + this.getHp() + ")"; }

    @Override
    public abstract String attack(Creature target);

    @Override
    public String getName() { return this.name + this.suffix; }

    public void setName(String name) { this.name = name; }

    @Override
    public int getHp() { return this.hp; }

    @Override
    public void setHp(int hp) { this.hp = (hp < 0) ? 0 : hp; }

    public int getMaxHp() { return this.maxHp; }
}
