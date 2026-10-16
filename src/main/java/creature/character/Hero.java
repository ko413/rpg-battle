package creature.character;

import creature.Character;
import creature.Creature;
import weapon.Sword;
import weapon.Weapon;
public class Hero extends Character {
    public Hero(String name, int hp, Weapon weapon) { super(name, hp, weapon); }
    public Hero(String name, int hp) { this(name, hp, new Sword()); }
    @Override
    public String attack(Creature target) {
        int damage = this.getWeapon().getDamage();
        target.setHp(target.getHp() - damage);
        String message = this.getName() + "は" + this.getWeapon().getName() + this.getWeapon().attackMessage()
                + "\n" + target.getName() + "に" + damage + "のダメージを与えた！";
        return message;
    }
}