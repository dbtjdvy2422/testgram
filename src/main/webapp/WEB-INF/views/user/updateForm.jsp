<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@include file= "../layout/header.jsp"  %>


<form class="was-validated">
  <div class="form-group">
	<input type="hidden" id="id" value="${principal.user.id}"/>
    <label for="username">Username:</label>
    <input type="text" value="${principal.user.username}" class="form-control" id="username" placeholder="Enter username" name="username"readonly>
 </div>
 
<c:if test="${empty principal.user.oauth}">
<div class="form-group">
    <label for="password">Password:</label>
    <input type="password" class="form-control" id="password" placeholder="Enter password" name="password">

  </div>
</c:if>
  
  
  
  <div class="form-group">
    <label for="email">email:</label>
    <input type="email" class="form-control" id="email" placeholder="Enter email" name="email" readonly value="${principal.user.email}">

  </div>
  

</form>

<button id="btn_update" class="btn btn-primary">회원수정완료</button>

<script src="/js/user.js"></script>



  <%@include file= "../layout/footer.jsp"  %>


</body>
</html>