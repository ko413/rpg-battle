package creature.character;

import creature.Creature;
import weapon.Weapon;

public class SuperHero extends Hero {

    public SuperHero(String name, int hp, Weapon weapon) {
        super(name, hp, weapon);
    }

    public String transform() {
        this.setHp(this.getHp() - 30);
        return this.getName() + "はスーパーヒーローになった！\nしかし力を解放した代償を受ける！";
    }

    @Override
    public String attack(Creature target) {
        int damage = (int)(this.getWeapon().getDamage() * 2.5);
        target.setHp(target.getHp() - damage);
        return this.getName() + "は" + this.getWeapon().getName() + this.getWeapon().attackMessage()
                + "\n" + target.getName() + "に" + damage + "のダメージを与えた！";
    }
}