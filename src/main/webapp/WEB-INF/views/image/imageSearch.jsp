<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp"%>


 
<main class="main">

	<section class="container"  id="searchList">
		<!--전체 리스트 시작-->
		<div class="div1">
	<input class="input1" type="text" name="keyword" id="keyword" placeholder="검색어 입력">
	<button class="button1" id="button1">검색</button>
</div>

		<article class="story-list" id="storyList">
		
		

			<!--스토리 아이템-->

			<!--스토리 아이템 끝-->

		</article>
	</section>
</main>


<script src="/js/imageSearch.js"></script>
</body>
</html>