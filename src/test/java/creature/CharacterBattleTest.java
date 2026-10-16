package creature;

import creature.character.*;
import creature.monster.*;
import weapon.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 味方キャラクター（勇者・スーパーヒーロー・盗賊・魔法使い）の戦闘ロジックのテスト。
 * 期待値は各クラス・武器クラスのダメージ設定から算出している。
 */
class CharacterBattleTest {

    private Slime newSlime() {
        return new Slime('A', 30);
    }

    private Goblin newGoblin() {
        return new Goblin('A', 60);
    }

    // ---------- 勇者 ----------

    @Test
    @DisplayName("勇者の攻撃：剣のダメージ(10)が敵のHPから引かれる")
    void heroAttackReducesHpBySwordDamage() {
        Hero hero = new Hero("勇者", 120, new Sword());
        Slime slime = newSlime();

        String message = hero.attack(slime);

        assertEquals(20, slime.getHp());
        assertTrue(message.contains("10のダメージ"));
    }

    // ---------- スーパーヒーロー ----------

    @Test
    @DisplayName("スーパーヒーローの攻撃：剣のダメージの2.5倍(25)が与えられる")
    void superHeroAttackDealsTwoPointFiveTimesDamage() {
        SuperHero superHero = new SuperHero("勇者", 120, new Sword());
        Slime slime = newSlime();

        superHero.attack(slime);

        assertEquals(5, slime.getHp());
    }

    @Test
    @DisplayName("変身すると代償としてHPが30減る")
    void transformCostsThirtyHp() {
        SuperHero superHero = new SuperHero("勇者", 120, new Sword());

        String message = superHero.transform();

        assertEquals(90, superHero.getHp());
        assertTrue(message.contains("スーパーヒーローになった"));
    }

    @Test
    @DisplayName("HPが30以下で変身すると死亡する（境界値：30は死亡、31は生存）")
    void transformAtLowHpBoundary() {
        SuperHero dying = new SuperHero("勇者", 30, new Sword());
        SuperHero surviving = new SuperHero("勇者", 31, new Sword());

        dying.transform();
        surviving.transform();

        assertFalse(dying.isAlive());
        assertEquals(0, dying.getHp());
        assertTrue(surviving.isAlive());
        assertEquals(1, surviving.getHp());
    }

    // ---------- 盗賊 ----------

    @Test
    @DisplayName("盗賊の攻撃：短剣のダメージの2倍(12)が与えられる")
    void thiefAttackDealsDoubleDamage() {
        Thief thief = new Thief("盗賊", 90, new Dagger());
        Goblin goblin = newGoblin();

        thief.attack(goblin);

        assertEquals(48, goblin.getHp());
    }

    @Test
    @DisplayName("盗賊のガード：1回目の攻撃は無効化され、2回目からダメージを受ける")
    void thiefGuardBlocksOnlyTheFirstAttack() {
        Thief thief = new Thief("盗賊", 90, new Dagger());
        Goblin goblin = newGoblin(); // 攻撃ダメージ8

        thief.guard();
        assertTrue(thief.isGuarding());

        goblin.attack(thief);
        assertEquals(90, thief.getHp(), "ガード中のダメージは無効");
        assertFalse(thief.isGuarding(), "ガードは1回で解除される");

        goblin.attack(thief);
        assertEquals(82, thief.getHp(), "ガード解除後は通常のダメージ");
    }

    // ---------- 魔法使い ----------

    @Test
    @DisplayName("魔法使いの通常攻撃：石を投げて3ダメージ")
    void wizardStoneAttackDealsThreeDamage() {
        Wizard wizard = new Wizard("魔法使い", 80, 25);
        Goblin goblin = newGoblin();

        String message = wizard.attack(goblin);

        assertEquals(57, goblin.getHp());
        assertTrue(message.contains("石を投げた"));
    }

    @Test
    @DisplayName("魔法攻撃：MPを5消費して15ダメージ")
    void wizardMagicConsumesMpAndDealsDamage() {
        Wizard wizard = new Wizard("魔法使い", 80, 25);
        Goblin goblin = newGoblin();

        wizard.magic(goblin);

        assertEquals(20, wizard.getMp());
        assertEquals(45, goblin.getHp());
    }

    @Test
    @DisplayName("MPが足りない場合は魔法が発動せず、ダメージもMP消費もない")
    void wizardMagicFailsWithoutEnoughMp() {
        Wizard wizard = new Wizard("魔法使い", 80, 4); // 必要MPは5
        Goblin goblin = newGoblin();

        String message = wizard.magic(goblin);

        assertEquals("MPが足りない！", message);
        assertEquals(4, wizard.getMp());
        assertEquals(60, goblin.getHp());
    }

    // ---------- 共通 ----------

    @Test
    @DisplayName("HPは0未満にならず、0で死亡扱いになる")
    void hpNeverGoesBelowZero() {
        Hero hero = new Hero("勇者", 120, new Sword());

        hero.setHp(-5);

        assertEquals(0, hero.getHp());
        assertFalse(hero.isAlive());
    }

    @Test
    @DisplayName("画面分離のリグレッション：行動メッセージにHTMLタグ(<br>)が含まれない")
    void messagesContainNoHtmlTags() {
        Goblin goblin = newGoblin();
        String[] messages = {
                new Hero("勇者", 120, new Sword()).attack(goblin),
                new SuperHero("勇者", 120, new Sword()).attack(goblin),
                new Thief("盗賊", 90, new Dagger()).attack(goblin),
                new Wizard("魔法使い", 80, 25).attack(goblin),
                new Wizard("魔法使い", 80, 25).magic(goblin),
                new SuperHero("勇者", 120, new Sword()).transform()
        };

        for (String message : messages) {
            assertFalse(message.contains("<br>"), "HTMLタグが残っている: " + message);
        }
    }

    @Test
    @DisplayName("最大HPは初期HPのままで、ダメージを受けても変わらない（HPバー表示用）")
    void maxHpStaysAfterDamage() {
        Hero hero = new Hero("勇者", 120, new Sword());

        newGoblin().attack(hero);

        assertEquals(112, hero.getHp());
        assertEquals(120, hero.getMaxHp());
    }

    @Test
    @DisplayName("魔法使いの最大MPは初期MPのままで、魔法を使っても変わらない（MPバー表示用）")
    void wizardMaxMpStaysAfterMagic() {
        Wizard wizard = new Wizard("魔法使い", 80, 25);

        wizard.magic(newGoblin());

        assertEquals(20, wizard.getMp());
        assertEquals(25, wizard.getMaxMp());
    }
}
