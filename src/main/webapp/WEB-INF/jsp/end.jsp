<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>戦闘終了</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<main class="container">
    <section class="panel result ${victory ? 'win' : 'lose'}">
        <%-- 画像は /img/victory.png, /img/defeat.png（なければ表示しない） --%>
        <span class="portrait" data-initial="${victory ? '勝' : '敗'}">
            <img src="${pageContext.request.contextPath}/img/${victory ? 'victory' : 'defeat'}.png" alt=""
                 onerror="this.parentNode.classList.add('no-img');this.remove()">
        </span>
        <h2>戦闘終了</h2>
        <c:choose>
            <c:when test="${victory}"><p>敵を全て倒した！勇者達は勝利した！</p></c:when>
            <c:otherwise><p>味方パーティは全滅してしまった…</p></c:otherwise>
        </c:choose>
        <c:if test="${not empty lastMessages}">
            <div class="log-box result-log">
                <c:forEach var="msg" items="${lastMessages}">
                    <p class="log-item"><c:out value="${msg}"/></p>
                </c:forEach>
            </div>
        </c:if>
        <div class="actions">
            <a class="btn" href="${pageContext.request.contextPath}/start">もう一度遊ぶ</a>
        </div>
    </section>
</main>
</body>
</html>
