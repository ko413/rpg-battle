package creature.monster;

import creature.Creature;
import creature.Monster;

public final class Slime extends Monster {
    public Slime(char suffix, int hp) {
        super("スライム", suffix, hp);
    }

    @Override
    public String attack(Creature target) {
        int damage = 5;
        target.setHp(target.getHp() - damage);
        return this.getName() + "は体当たり攻撃！\n" + target.getName() + "に" + damage + "のダメージを与えた！";
    }
}