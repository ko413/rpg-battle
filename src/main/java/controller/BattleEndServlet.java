package controller;

import java.io.IOException;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

/** 結果画面：勝敗と最後の戦闘ログを表示し、セッションを破棄する */
@WebServlet("/end")
public class BattleEndServlet extends HttpServlet {

    @Override
    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Boolean victory = (session == null) ? null : (Boolean) session.getAttribute("victory");

        // 勝敗が記録されていない（リロード・直接アクセス）場合はタイトルへ戻す
        if (victory == null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        // 決着がついた行動のログも結果画面で見せる
        ArrayList<String> messages = (ArrayList<String>) session.getAttribute("messages");
        request.setAttribute("victory", victory);
        request.setAttribute("lastMessages", messages == null ? new ArrayList<String>() : new ArrayList<>(messages));

        session.invalidate(); // 表示に必要な値はリクエストに移したので、ここで破棄してよい
        request.getRequestDispatcher("/WEB-INF/jsp/end.jsp").forward(request, response);
    }
}
