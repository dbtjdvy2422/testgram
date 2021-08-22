<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@include file= "layout/header.jsp"  %>


<div class="container">

<c:forEach var="board" items="${boards.content}">
  <div class="card m-5">
    <div class="card-body">
      <h4 class="card-title">${board.title}</h4>
      <p class="card-text">${board.content}</p>
      <a href="/board/${board.id}" class="btn btn-primary">상세 보기</a>
    </div>
  </div>
  </c:forEach>
  
  <ul class="pagination justify-content-center">
  <c:choose>
  <c:when test = "${boards.first}">
    <li class="page-item disabled"><a class="page-link" href="?page=${boards.number-1}">Previous</a></li>
  </c:when>
  <c:otherwise>
      <li class="page-item"><a class="page-link" href="?page=${boards.number-1}">Previous</a></li>
  </c:otherwise>
  </c:choose>
  
    <c:choose>
  <c:when test = "${boards.last}">
      <li class="page-item disabled"><a class="page-link" href="?page=${boards.number+1}">Next</a></li>
  </c:when>
  <c:otherwise>
      <li class="page-"><a class="page-link" href="?page=${boards.number+1}">Next</a></li>
  </c:otherwise>
  </c:choose>
</ul>

  <br>
  </div>

  <%@include file= "layout/footer.jsp"  %>

 <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>