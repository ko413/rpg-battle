package creature.monster;

import creature.Creature;
import creature.Monster;

public class Goblin extends Monster {
    public Goblin(char suffix, int hp) {
        super("ゴブリン", suffix, hp);
    }

    @Override
    public String attack(Creature target) {
        int damage = 8;
        target.setHp(target.getHp() - damage);
        return this.getName() + "はナイフで切りつけた！\n" + target.getName() + "に" + damage + "のダメージを与えた！";
    }
}