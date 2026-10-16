<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%-- 敵パーティ。selectable=true のときは、敵カードを攻撃対象のラジオボタンとして使う。
     立ち絵は /img/<種別名>.png。なければ頭文字を表示する。 --%>
<c:set var="imgBase" value="${pageContext.request.contextPath}/img"/>
<section class="panel" id="enemy-panel">
    <h2 class="panel-title">敵パーティ</h2>
    <div class="enemy-grid">
        <c:forEach var="mon" items="${monsters}" varStatus="st">
            <c:set var="kind" value="${mon['class'].simpleName}"/>
            <c:set var="pct" value="${mon.hp * 100 / mon.maxHp}"/>
            <c:choose>
                <c:when test="${pct > 50}"><c:set var="level" value="ok"/></c:when>
                <c:when test="${pct > 25}"><c:set var="level" value="warn"/></c:when>
                <c:otherwise><c:set var="level" value="danger"/></c:otherwise>
            </c:choose>
            <label class="enemy">
                <c:if test="${selectable}">
                    <input type="radio" name="targetIndex" form="action-form" value="${st.index}" ${st.first ? 'checked' : ''}>
                </c:if>
                <span class="enemy-card">
                    <span class="portrait lg" data-initial="${fn:substring(mon.name, 0, 1)}">
                        <img src="${imgBase}/${fn:toLowerCase(kind)}.png" alt=""
                             onerror="this.parentNode.classList.add('no-img');this.remove()">
                    </span>
                    <span class="enemy-name"><c:out value="${mon.name}"/></span>
                    <span class="bar" role="progressbar" aria-label="HP"
                          aria-valuemin="0" aria-valuemax="${mon.maxHp}" aria-valuenow="${mon.hp}">
                        <span class="bar-fill ${level}" style="width:${pct}%"></span>
                    </span>
                    <span class="hp-num">HP ${mon.hp} / ${mon.maxHp}</span>
                </span>
            </label>
        </c:forEach>
    </div>
</section>
