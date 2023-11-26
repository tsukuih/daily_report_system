<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="constants.AttributeConst" %>
<%@ page import="constants.ForwardConst" %>

<%-- "Employee"をaction変数に代入。"Employee"は、従業員情報を表示する画面に遷移するためのアクション名として使用 --%>
<c:set var="action" value="${ForwardConst.ACT_EMP.getValue()}" />

<%-- 変数commIdxに"index"を代入。 --%>
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />

<%--エラーがある場合、全てのエラーメッセージを表示する --%>
<c:if test="${errors != null}">
    <div id="flush_error">
        入力内容にエラーがあります。<br>
        <c:forEach var="error" items="${errors}">
            ・<c:out value="${error}" /><br>
        </c:forEach>
    </div>
</c:if>

<%-- 社員番号を入力するためのフォームを作成 --%>
<label for="${AttributeConst.EMP_CODE.getValue()}">社員番号</label><br>
<input type="text" name="${AttributeConst.EMP_CODE.getValue()}" id="${AttributeConst.EMP_CODE.getValue()}" value="${employee.code}">
<br><br>

<%-- 氏名を入力するためのフォームを作成 --%>
<label for="${AttributeConst.EMP_NAME.getValue()}">氏名</label><br>
<input type="text" name="${AttributeConst.EMP_NAME.getValue()}" id="${AttributeConst.EMP_NAME.getValue()}" value="${employee.name}">
<br><br>

<%-- パスワードを入力するためのフォームを作成 --%>
<label for="${AttributeConst.EMP_PASS.getValue()}">パスワード</label><br>
<input type="password" name="${AttributeConst.EMP_PASS.getValue()}" id="${AttributeConst.EMP_PASS.getValue()}">
<br><br>

<%-- 権限を入力するためのセレクトボックスを作成 --%>
<label for="${AttributeConst.EMP_ADMIN_FLG.getValue()}">権限</label><br />
<select name="${AttributeConst.EMP_ADMIN_FLG.getValue()}" id="${AttributeConst.EMP_ADMIN_FLG.getValue()}">
    <option value="${AttributeConst.ROLE_GENERAL.getIntegerValue()}"<c:if test="${employee.adminFlag == AttributeConst.ROLE_GENERAL.getIntegerValue()}"> selected</c:if>>一般</option>
    <option value="${AttributeConst.ROLE_ADMIN.getIntegerValue()}"<c:if test="${employee.adminFlag == AttributeConst.ROLE_ADMIN.getIntegerValue()}"> selected</c:if>>管理者</option>
</select>
<br><br>

<input type="hidden" name="${AttributeConst.EMP_ID.getValue()}" value="${employee.id}">
<input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
<button type="submit">投稿</button>

