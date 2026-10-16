package creature.monster;

import creature.Creature;
import creature.Monster;

public class Matango extends Monster {
    public Matango(char suffix, int hp) {
        super("お化けキノコ", suffix, hp);
    }

    @Override
    public String attack(Creature target) {
        int damage = 6;
        target.setHp(target.getHp() - damage);
        return this.getName() + "は体当たり攻撃！\n" + target.getName() + "に" + damage + "のダメージを与えた！";
    }
}