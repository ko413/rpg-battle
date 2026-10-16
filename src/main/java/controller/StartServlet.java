package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import creature.Character;
import creature.Monster;
import creature.character.*;
import creature.monster.*;
import weapon.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

/** ゲーム開始：パーティと敵を生成してセッションに保存し、開始画面を表示する */
@WebServlet("/start")
public class StartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // --- ゲームの準備 ---
        ArrayList<Character> party = new ArrayList<>();
        party.add(new Hero("勇者", 120, new Sword()));
        party.add(new Wizard("魔法使い", 80, 25, new Wand()));
        party.add(new Thief("盗賊", 90, new Dagger()));

        ArrayList<Monster> monsters = new ArrayList<>();
        Random rand = new Random();
        int matangoCount = 0, goblinCount = 0, slimeCount = 0;

        for (int i = 0; i < 5; i++) {
            switch (rand.nextInt(3)) {
                case 0 -> monsters.add(new Matango((char) ('A' + matangoCount++), 45));
                case 1 -> monsters.add(new Goblin((char) ('A' + goblinCount++), 60));
                default -> monsters.add(new Slime((char) ('A' + slimeCount++), 30));
            }
        }

        // --- ゲームの状態をサーバーに保存 ---
        HttpSession session = request.getSession();
        session.setAttribute("party", party);
        session.setAttribute("monsters", monsters);
        session.setAttribute("messages", new ArrayList<String>());
        session.setAttribute("history", new ArrayList<String>()); // 戦闘ログの履歴
        session.setAttribute("playerIndex", 0);

        // --- 表示はJSPに任せる ---
        request.setAttribute("party", party);
        request.setAttribute("monsters", monsters);
        request.getRequestDispatcher("/WEB-INF/jsp/hello.jsp").forward(request, response);
    }
}
