<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@include file= "../layout/header.jsp"  %>


<div class="container">

<form  class="was-validated">
	<input type="hidden" id="id" value="${board.id}"/>
  <div class="form-group">
    <input type="text" value="${board.title}" class="form-control" id="title" placeholder="Enter title">

  </div>
 <div class="form-group">
  <textarea class="form-control summernote" rows="5" id="content">${board.content}</textarea>
</div>

</form>

  <button id="btn_update" class="btn btn-primary">글수정 완료</button>

  <br>
  </div>
  


</body>


<script>
      $('.summernote').summernote({
        placeholder: 'Hello Bootstrap 4',
        tabsize: 2,
        height: 300
      });
    </script>
 <script src="/js/board.js"></script>
  <%@include file= "../layout/footer.jsp"  %>
</html>