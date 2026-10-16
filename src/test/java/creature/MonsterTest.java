package creature;

import creature.character.*;
import creature.monster.*;
import weapon.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * モンスター（ゴブリン・お化けキノコ・スライム）のテスト。
 */
class MonsterTest {

    private Hero newHero() {
        return new Hero("勇者", 120, new Sword());
    }

    @Test
    @DisplayName("モンスターごとの攻撃ダメージ：ゴブリン8・お化けキノコ6・スライム5")
    void eachMonsterDealsItsOwnDamage() {
        Hero hero = newHero();

        new Goblin('A', 60).attack(hero);
        assertEquals(112, hero.getHp());

        new Matango('A', 45).attack(hero);
        assertEquals(106, hero.getHp());

        new Slime('A', 30).attack(hero);
        assertEquals(101, hero.getHp());
    }

    @Test
    @DisplayName("モンスター名には種類ごとの識別記号(A, B...)が付く")
    void monsterNameIncludesSuffix() {
        assertEquals("ゴブリンA", new Goblin('A', 60).getName());
        assertEquals("スライムB", new Slime('B', 30).getName());
        assertEquals("お化けキノコC", new Matango('C', 45).getName());
    }

    @Test
    @DisplayName("モンスターのHPも0未満にならず、0で死亡扱いになる")
    void monsterHpNeverGoesBelowZero() {
        Slime slime = new Slime('A', 30);

        slime.setHp(-10);

        assertEquals(0, slime.getHp());
        assertFalse(slime.isAlive());
    }

    @Test
    @DisplayName("倒した・逃げ出したときのメッセージに識別記号付きの名前が使われる")
    void dieAndRunMessagesUseFullName() {
        Slime slime = new Slime('A', 30);

        assertTrue(slime.die().contains("スライムA"));
        assertTrue(slime.run().contains("スライムA"));
    }

    @Test
    @DisplayName("モンスターの攻撃メッセージにHTMLタグ(<br>)が含まれない")
    void monsterMessagesContainNoHtmlTags() {
        Hero hero = newHero();

        assertFalse(new Goblin('A', 60).attack(hero).contains("<br>"));
        assertFalse(new Matango('A', 45).attack(hero).contains("<br>"));
        assertFalse(new Slime('A', 30).attack(hero).contains("<br>"));
    }

    @Test
    @DisplayName("モンスターの最大HPは初期HPのままで、ダメージを受けても変わらない")
    void monsterMaxHpStaysAfterDamage() {
        Slime slime = new Slime('A', 30);

        slime.setHp(10);

        assertEquals(10, slime.getHp());
        assertEquals(30, slime.getMaxHp());
    }
}
