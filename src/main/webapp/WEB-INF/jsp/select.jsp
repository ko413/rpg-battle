<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Action Select</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<main class="container">
    <h1 class="sr-only">RPG Battle</h1>

    <%-- 敵カードを攻撃対象の選択に使う --%>
    <c:set var="selectable" value="true" scope="request"/>

    <%-- PC：上段＝味方｜敵、下段＝コマンド｜ログ ／ スマホ：敵→味方→コマンド→ログ --%>
    <div class="battle-layout">

        <jsp:include page="/WEB-INF/jsp/fragments/party.jsp"/>
        <jsp:include page="/WEB-INF/jsp/fragments/enemies.jsp"/>

        <form class="panel command-panel" id="action-form" action="${pageContext.request.contextPath}/battle" method="post">
            <h2 class="panel-title"><c:out value="${currentCharacter.name}"/>のコマンド</h2>

            <c:choose>
                <c:when test="${actionType == 'SUPERHERO'}">
                    <p class="note">スーパーヒーローは攻撃するしかない！（剣で25ダメージ）</p>
                    <input type="hidden" name="actionChoice" value="1">
                </c:when>
                <c:when test="${actionType == 'HERO'}">
                    <fieldset>
                        <legend class="sr-only">どうする？</legend>
                        <div class="choice-list">
                            <label class="choice">
                                <input type="radio" name="actionChoice" value="1" data-target="true" checked>
                                <span>攻撃<small>剣で切りつける ／ 10ダメージ</small></span>
                            </label>
                            <label class="choice">
                                <input type="radio" name="actionChoice" value="2" data-target="false">
                                <span>スーパーヒーローになる<small>HP-30 ／ 以後の攻撃が25ダメージ（2.5倍）</small></span>
                            </label>
                        </div>
                    </fieldset>
                </c:when>
                <c:when test="${actionType == 'WIZARD'}">
                    <fieldset>
                        <legend class="sr-only">どうする？</legend>
                        <div class="choice-list">
                            <label class="choice">
                                <input type="radio" name="actionChoice" value="1" data-target="true" checked>
                                <span>攻撃(石)<small>3ダメージ</small></span>
                            </label>
                            <label class="choice">
                                <input type="radio" name="actionChoice" value="2" data-target="true">
                                <span>魔法攻撃<small>MP5消費 ／ 15ダメージ</small></span>
                            </label>
                        </div>
                    </fieldset>
                </c:when>
                <c:when test="${actionType == 'THIEF'}">
                    <fieldset>
                        <legend class="sr-only">どうする？</legend>
                        <div class="choice-list">
                            <label class="choice">
                                <input type="radio" name="actionChoice" value="1" data-target="true" checked>
                                <span>攻撃<small>素早く2回攻撃 ／ 6×2＝12ダメージ</small></span>
                            </label>
                            <label class="choice">
                                <input type="radio" name="actionChoice" value="2" data-target="false">
                                <span>守る<small>次の敵の攻撃を1回無効化</small></span>
                            </label>
                        </div>
                    </fieldset>
                </c:when>
            </c:choose>

            <button class="btn btn-block" type="submit">決定</button>
        </form>

        <%-- 戦闘ログ：直近3件が見える高さで、上にスクロールすると履歴を読める --%>
        <section class="panel log-panel">
            <h2 class="panel-title">戦闘ログ</h2>
            <div class="log-box" id="log" tabindex="0" aria-live="polite">
                <c:if test="${empty history}">
                    <p class="log-empty">行動を選んでください。</p>
                </c:if>
                <c:forEach var="msg" items="${history}">
                    <p class="log-item${fn:startsWith(msg, '---') ? ' sep' : ''}"><c:out value="${msg}"/></p>
                </c:forEach>
            </div>
        </section>

    </div>
</main>
<script src="${pageContext.request.contextPath}/js/battle.js"></script>
</body>
</html>
