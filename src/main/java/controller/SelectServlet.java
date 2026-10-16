package controller;

import java.io.IOException;
import java.util.ArrayList;
import creature.Character;
import creature.Monster;
import creature.character.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

/** 行動選択画面：今行動するキャラと戦況、戦闘ログを表示する */
@WebServlet("/select")
public class SelectServlet extends HttpServlet {

    private static final int HISTORY_LIMIT = 200;

    @Override
    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        ArrayList<Character> party = (ArrayList<Character>) session.getAttribute("party");
        ArrayList<Monster> monsters = (ArrayList<Monster>) session.getAttribute("monsters");
        ArrayList<String> messages = (ArrayList<String>) session.getAttribute("messages");
        Integer playerIndex = (Integer) session.getAttribute("playerIndex");

        // セッション切れ・直接アクセス対策：状態がなければ最初の画面へ戻す
        if (party == null || monsters == null || messages == null || playerIndex == null
                || party.isEmpty() || playerIndex >= party.size()) {
            response.sendRedirect(request.getContextPath() + "/start");
            return;
        }

        Character currentCharacter = party.get(playerIndex);

        // 職業ごとの選択肢をJSPが判断できるよう、種別だけを渡す
        String actionType;
        if (currentCharacter instanceof SuperHero) {
            actionType = "SUPERHERO";
        } else if (currentCharacter instanceof Hero) {
            actionType = "HERO";
        } else if (currentCharacter instanceof Wizard) {
            actionType = "WIZARD";
        } else if (currentCharacter instanceof Thief) {
            actionType = "THIEF";
        } else {
            actionType = "";
        }

        // 新しいメッセージを履歴へ移す（リロードしても消えないようセッションに保持）
        ArrayList<String> history = (ArrayList<String>) session.getAttribute("history");
        if (history == null) {
            history = new ArrayList<>();
            session.setAttribute("history", history);
        }
        history.addAll(messages);
        messages.clear();
        while (history.size() > HISTORY_LIMIT) {
            history.remove(0);
        }

        request.setAttribute("currentCharacter", currentCharacter);
        request.setAttribute("actionType", actionType);
        request.setAttribute("history", history);
        request.setAttribute("party", party);
        request.setAttribute("monsters", monsters);
        request.getRequestDispatcher("/WEB-INF/jsp/select.jsp").forward(request, response);
    }
}
