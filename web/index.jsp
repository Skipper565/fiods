<%@ page import="org.w3c.dom.NodeList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${empty step}">
  <c:set var="step" value="step1" />
</c:if>
<!DOCTYPE html>
<html>
  <head>
      <meta charset="utf-8" />
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <link href="/css/bootstrap.min.css" rel="stylesheet">
      <link href="/css/template.css" rel="stylesheet">
    <title>FIODS</title>
  </head>
  <body>

      <nav class="navbar navbar-inverse navbar-fixed-top">
          <div class="container">
              <div class="navbar-header">
                  <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                      <span class="sr-only">Toggle navigation</span>
                      <span class="icon-bar"></span>
                      <span class="icon-bar"></span>
                      <span class="icon-bar"></span>
                  </button>
                  <a class="navbar-brand" href="#">FIODS</a>
              </div>
              <div id="navbar" class="collapse navbar-collapse">
                  <ul class="nav navbar-nav">
                      <li class="active"><a href="#">Home</a></li>
                      <li><a href="#about">Github</a></li>
                      <li><a href="#contact">Wiki</a></li>
                  </ul>
              </div><!--/.nav-collapse -->
          </div>
      </nav>

      <div class="container">
          <div class="starter-template">
                  <h1>
                    Find in Open Document Spreadsheet [fiods]
                  </h1>
                  <p class="lead"> A PB138 project, allowing the user to search an ODS file</p>
                  <c:out value="${error}" />
                  <form method="post" action="/index/${step}" enctype="multipart/form-data">
                      <fieldset class="form-group">


                                <c:if test="${step == 'step1'}">
                                    <label for="exampleInputFile">Hello. Please choose a file you want to search.</label>
                                    <input type="file" name="file"  class="form-control-file" id="exampleInputFile"/>
                                </c:if>


                                <c:if test="${step == 'step2'}">
                                  <c:out value="${path}" />
                                  <input type="hidden" value="${list}" />
                                    <label for="Select">Please select one of the sheets in your document.</label>
                                    <select name="table" class="form-control" id="Select">
                                        <c:forEach items="${tables}" var="table">
                                          <option value="${table}">${table}</option>
                                        </c:forEach>
                                  </select>
                                </c:if>


                                <c:if test="${step == 'step3'}">
                                    <label for="searchString">Please type your query in the field below. All records containing that string will be returned</label>
                                    <input type="text" id="searchString" class="form-control" name="value" />
                                </c:if>

                                <c:if test="${step == 'step4'}">
                                    <table class="table table-hover">
                                  <c:if test="${empty rows}">No results found!</c:if>
                                      <c:forEach items="${rows}" var="row">
                                          <tr>
                                          <c:forEach items="${row}" var="field">
                                              <td>
                                              <c:out value="${field}"/>
                                              </td>
                                          </c:forEach>
                                          </tr>
                                        <%--<c:out value="${row.getTextContent()}" /><br />--%>
                                      </c:forEach>
                                    </table>
                                </c:if>

                      </fieldset>
                        <c:if test="${step != 'step4'}">
                            <input type="submit"  class="btn btn-primary" name="submit_button" value="Next" />
                        </c:if>
                        <c:if test="${step == 'step4'}">
                            <input type="submit"  class="btn btn-primary btn-success" name="submit_button" value="Search Again" />
                        </c:if>

                  </form>
          </div>
      </div>
      <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
      <script>window.jQuery || document.write('<script src="../../assets/js/vendor/jquery.min.js"><\/script>')</script>
      <script src="../../dist/js/bootstrap.min.js"></script>
  </body>
</html>
