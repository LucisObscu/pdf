<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta charset="utf-8">
<title>글쓰기</title>
<script>
function fileCheck(frm) {   

  var file = frm.BookFile.value;
  var FileFilter = /\.(txt|pdf)$/i;
  var extArray = new Array(".txt", ".pdf");   
  var bSubmitCheck = false;
    
  if( !file ){ 
    alert( "파일을 선택하여 주세요!");
    return;
  }
  
  if( frm.BookFile.value.match(FileFilter))
  { 
    bSubmitCheck = true;
  }
  
  
  if (bSubmitCheck) {
     alert("파일 업로드를 시작합니다.");
     frm.submit(); 
   } else {
     alert("다음 파일만 업로드가 가능합니다.\n\n"  + (extArray.join("  ")) + "\n\n 업로드할 파일을 "
     + " 다시 선택하여 주세요.");
   }

}

function gkgkgkgk(){
	alert("안녕하신가");
}
</script>
</head>
<body>
<form action="/ccc/write.bbs" method="post"  enctype="multipart/form-data">
	<table border="2" width="200">  
		<tr>
 			 <td>글쓴이 :</td><td>human</td>
 		</tr>
 		<tr>	 
		 <td>제목 : </td><td><input type="txt" name="BookTitle"></td>			 
		</tr>
		<tr>
		  <td colspan="2"> <textarea cols="50" rows="20" name="Bookcontent" ></textarea></td>
	    </tr> 	    
	    <tr>
	      <td>첨부 : </td><td><input type="file" onload="gkgkgkgk()" name="BookFile"></td>
	    </tr>
	    <tr style="display: ">
	      <td>txt설정</td><td></td>
	    </tr>
	    <tr>
	      <td><input type="button" onclick="fileCheck(this.form)" value="글쓰기"></td>
	      <td><input type="reset" value="글쓰기취소"></td>	      	 
	    </tr>		
	</table>	
</form>
</body>
</html>