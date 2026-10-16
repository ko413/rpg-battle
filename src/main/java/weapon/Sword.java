package weapon;
public class Sword extends Weapon {
    public Sword() { super("剣", 10); }
    @Override
    public String attackMessage() { return "で切りつけた！"; }
}