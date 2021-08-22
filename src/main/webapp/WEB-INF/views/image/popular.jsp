<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp"%>

<!--인기 게시글-->
<main class="popular">
	<div class="exploreContainer">

		<!--인기게시글 갤러리(GRID배치)-->
		<div class="popular-gallery">

			<c:forEach var="image" items="${images}">
				<div class="p-img-box">
				<c:choose>
						  <c:when test = "${image.ffmpegPath eq null}">
					<a href="/user/${image.user.id}"> <img src="/upload/${image.postImageUrl}" />
					</a>
					</c:when>
					 <c:when test = "${image.ffmpegPath ne null}">
					 <a href="/user/${image.user.id}"> <img src="/upload/${image.ffmpegPath}" />
					 </a>
					 </c:when>
						</c:choose>
				</div>
			</c:forEach>

		</div>

	</div>
</main>

<%@ include file="../layout/footer.jsp"%>

