package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import creature.Character;
import creature.Monster;
import creature.character.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

/**
 * 1人分の行動を処理する。パーティ全員が行動し終えたら、続けて敵のターンも処理する。
 * 状態を変える処理なので POST で受け、処理後は画面表示用URLへリダイレクトする（PRGパターン）。
 */
@WebServlet("/battle")
public class BattleServlet extends HttpServlet {

    private static final int ACTION_ATTACK = 1;
    private static final int ACTION_SPECIAL = 2;
    private static final int RUN_AWAY_HP = 5; // このHP以下になったモンスターは逃げる

    private final Random rand = new Random();

    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        ArrayList<Character> party = (ArrayList<Character>) session.getAttribute("party");
        ArrayList<Monster> monsters = (ArrayList<Monster>) session.getAttribute("monsters");
        ArrayList<String> messages = (ArrayList<String>) session.getAttribute("messages");
        Integer playerIndex = (Integer) session.getAttribute("playerIndex");

        // セッション切れ・直接アクセス対策：状態がなければ最初の画面へ戻す
        if (party == null || monsters == null || messages == null || playerIndex == null) {
            response.sendRedirect(request.getContextPath() + "/start");
            return;
        }

        // 行動するキャラはクライアントから受け取らず、サーバー側の状態（セッション）を正とする
        Character current = party.get(playerIndex);
        int actionChoice = parseIntOrDefault(request.getParameter("actionChoice"), ACTION_ATTACK);

        // --- 特殊行動の処理（対象を選ばない行動） ---
        if (current instanceof Thief thief && actionChoice == ACTION_SPECIAL) {
            messages.add(thief.guard());
        } else if (current instanceof Hero && !(current instanceof SuperHero) && actionChoice == ACTION_SPECIAL) {
            SuperHero sh = new SuperHero(current.getName(), current.getHp(), current.getWeapon());
            sh.setMaxHp(current.getMaxHp()); // 変身してもHPバーの最大値は変えない
            messages.add(sh.transform());
            party.set(playerIndex, sh);
            current = sh;
        } else {
            // --- 攻撃行動の処理：対象の番号を検証してから使う ---
            int targetIndex = parseIntOrDefault(request.getParameter("targetIndex"), -1);
            if (targetIndex < 0 || targetIndex >= monsters.size()) {
                response.sendRedirect(request.getContextPath() + "/select"); // 不正な値なら行動を消費せず選び直し
                return;
            }
            Monster target = monsters.get(targetIndex);
            if (current instanceof Wizard wizard && actionChoice == ACTION_SPECIAL) {
                messages.add(wizard.magic(target));
            } else {
                messages.add(current.attack(target));
            }
        }

        removeDefeatedMonsters(monsters, messages);
        if (monsters.isEmpty()) {
            finish(session, request, response, true);
            return;
        }

        // 変身の代償でHPが尽きた場合は、その場でパーティから外す（番号は詰まるので進めない）
        int next = playerIndex + 1;
        if (!current.isAlive()) {
            messages.add(current.die());
            party.remove((int) playerIndex);
            next = playerIndex;
            if (party.isEmpty()) {
                finish(session, request, response, false);
                return;
            }
        }

        // --- 全員が行動したら敵のターン ---
        if (next >= party.size()) {
            enemyTurn(party, monsters, messages);
            if (party.isEmpty()) {
                finish(session, request, response, false);
                return;
            }
            next = 0;
        }
        session.setAttribute("playerIndex", next);
        response.sendRedirect(request.getContextPath() + "/select");
    }

    /** 倒れた・逃げ出したモンスターを一覧から外す */
    private void removeDefeatedMonsters(List<Monster> monsters, List<String> messages) {
        Iterator<Monster> it = monsters.iterator();
        while (it.hasNext()) {
            Monster m = it.next();
            if (!m.isAlive()) {
                messages.add(m.die());
                it.remove();
            } else if (m.getHp() <= RUN_AWAY_HP) {
                messages.add(m.run());
                it.remove();
            }
        }
    }

    /** 敵のターン：生きている味方だけを攻撃対象にし、倒れたらすぐパーティから外す */
    private void enemyTurn(List<Character> party, List<Monster> monsters, List<String> messages) {
        messages.add("--- 敵のターン ---");
        for (Monster m : monsters) {
            if (party.isEmpty()) break;
            Character target = party.get(rand.nextInt(party.size()));

            boolean guarded = target instanceof Thief t && t.isGuarding();
            messages.add(m.attack(target));
            if (guarded) {
                messages.add("しかし、" + target.getName() + "は攻撃を回避し、ダメージが入らなかった！");
            }

            if (!target.isAlive()) {
                messages.add(target.die());
                party.remove(target); // 同じターン内で、倒れたキャラが再び狙われないようにする
            }
        }
        messages.add("--- 味方のターン ---");
    }

    /** 勝敗をセッションに記録して結果画面へ */
    private void finish(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                        boolean victory) throws IOException {
        session.setAttribute("victory", victory);
        response.sendRedirect(request.getContextPath() + "/end");
    }

    private static int parseIntOrDefault(String value, int defaultValue) {
        if (value == null) return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
