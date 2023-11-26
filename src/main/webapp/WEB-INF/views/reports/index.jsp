<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actTop" value="${ForwardConst.ACT_TOP.getValue()}" />
<c:set var="actEmp" value="${ForwardConst.ACT_EMP.getValue()}" />
<c:set var="actRep" value="${ForwardConst.ACT_REP.getValue()}" />

<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commNew" value="${ForwardConst.CMD_NEW.getValue()}" />

<c:import url="../layout/app.jsp">
    <c:param name="content">

        <h2>日報管理システムへようこそ</h2>

        <h2>いよいよ最終段階です</h2>




    </c:param>
</c:import>
