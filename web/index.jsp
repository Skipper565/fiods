<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${empty step}">
  <c:set var="step" value="step1" />
</c:if>
<html>
  <head>
    <title>FIODS</title>
  </head>
  <body>
  <h1>
    <c:out value="${io}" />
  </h1>
  <p>FIODS</p>
  <c:out value="${error}" />
  <form method="post" action="/index/${step}" enctype="multipart/form-data">
    <c:if test="${step == 'step1'}">
      <input type="file" name="file" />
    </c:if>
    <c:if test="${step == 'step2'}">
      <c:out value="${path}" />
      <input type="hidden" value="${list}" />
      <select name="table">
        <c:forEach items="${tables}" var="table">
          <option value="${table}">${table}</option>
        </c:forEach>
      </select>
    </c:if>
    <c:if test="${step == 'step3'}">
      <input type="text" name="value" />
    </c:if>
    <c:if test="${step == 'step4'}">
      <c:if test="${empty rows}">No results found!</c:if>
      <c:forEach items="${rows}" var="row">
        <c:out value="${row.getTextContent()}" /><br />
      </c:forEach>
    </c:if>
    <input type="submit" name="submit_button" value="Dalej" />
  </form>

  </body>
</html>
