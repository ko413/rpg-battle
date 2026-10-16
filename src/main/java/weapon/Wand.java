package weapon;
public class Wand extends Weapon {
    public Wand() {
        super("魔法の杖", 15);
        this.setCost(5);
    }
    @Override
    public String attackMessage() { return "から魔法を放った！"; }
}