<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@include file= "../layout/header.jsp"  %>


<div class="container">

<form method="post" class="was-validated">
  <div class="form-group">
    <input type="text" class="form-control" id="title" placeholder="Enter title">

  </div>
 <div class="form-group">
  <textarea class="form-control summernote" rows="5" id="content"></textarea>
</div>

</form>

  <button id="btn_save" class="btn btn-primary">글쓰기 완료</button>

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