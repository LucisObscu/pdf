package aaa.bbb.ccc;

import org.springframework.web.multipart.MultipartFile;

public class PdfFileDto {
	private MultipartFile BookFile;
	private MultipartFile BookImg;
	private String line;
	
	public MultipartFile getBookFile() {
		return BookFile;
	}
	public void setBookFile(MultipartFile bookFile) {
		BookFile = bookFile;
	}
	public MultipartFile getBookImg() {
		return BookImg;
	}
	public void setBookImg(MultipartFile bookImg) {
		BookImg = bookImg;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	
	
}
