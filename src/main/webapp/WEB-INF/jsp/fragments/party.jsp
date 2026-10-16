<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%-- 味方パーティ。行動中のキャラ（currentCharacter）をハイライトする。
     MPバーは、MPを消費する行動を持つ魔法使いのみ表示する。 --%>
<c:set var="imgBase" value="${pageContext.request.contextPath}/img"/>
<section class="panel party-panel">
    <h2 class="panel-title">味方パーティ</h2>
    <c:forEach var="ch" items="${party}">
        <c:set var="kind" value="${ch['class'].simpleName}"/>
        <c:set var="pct" value="${ch.hp * 100 / ch.maxHp}"/>
        <c:choose>
            <c:when test="${pct > 50}"><c:set var="level" value="ok"/></c:when>
            <c:when test="${pct > 25}"><c:set var="level" value="warn"/></c:when>
            <c:otherwise><c:set var="level" value="danger"/></c:otherwise>
        </c:choose>
        <div class="member${ch == currentCharacter ? ' active' : ''}">
            <span class="portrait" data-initial="${fn:substring(ch.name, 0, 1)}">
                <img src="${imgBase}/${fn:toLowerCase(kind)}.png" alt=""
                     onerror="this.parentNode.classList.add('no-img');this.remove()">
            </span>
            <div class="member-body">
                <div class="member-head">
                    <span class="member-name">
                        <c:out value="${ch.name}"/>
                        <c:if test="${kind == 'Thief' && ch.guarding}"><span class="badge">ガード中</span></c:if>
                    </span>
                    <span class="hp-num">HP ${ch.hp} / ${ch.maxHp}</span>
                </div>
                <span class="bar" role="progressbar" aria-label="HP"
                      aria-valuemin="0" aria-valuemax="${ch.maxHp}" aria-valuenow="${ch.hp}">
                    <span class="bar-fill ${level}" style="width:${pct}%"></span>
                </span>
                <c:if test="${kind == 'Wizard'}">
                    <div class="mp-row">
                        <span class="bar" role="progressbar" aria-label="MP"
                              aria-valuemin="0" aria-valuemax="${ch.maxMp}" aria-valuenow="${ch.mp}">
                            <span class="bar-fill mp" style="width:${ch.mp * 100 / ch.maxMp}%"></span>
                        </span>
                        <span class="hp-num">MP ${ch.mp} / ${ch.maxMp}</span>
                    </div>
                </c:if>
            </div>
        </div>
    </c:forEach>
</section>
