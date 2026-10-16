<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>RPG Battle</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<main class="container">
    <header class="page-header">
        <h1>冒険が始まる！</h1>
        <p class="lead">${monsters.size()}体の敵が現れた！</p>
    </header>

    <div class="battle-layout">
        <jsp:include page="/WEB-INF/jsp/fragments/party.jsp"/>
        <jsp:include page="/WEB-INF/jsp/fragments/enemies.jsp"/>
    </div>

    <div class="actions">
        <a class="btn" href="${pageContext.request.contextPath}/select">戦闘開始</a>
    </div>
</main>
</body>
</html>
