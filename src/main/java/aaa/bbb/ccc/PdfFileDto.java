package aaa.bbb.ccc;

import org.springframework.web.multipart.MultipartFile;

public class PdfFileDto {
	private MultipartFile BookFile;
	private MultipartFile BookImg;
	private int line;
	private int numOfOneLine;
	private int lineOfOnePage;
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
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	public int getNumOfOneLine() {
		return numOfOneLine;
	}
	public void setNumOfOneLine(int numOfOneLine) {
		this.numOfOneLine = numOfOneLine;
	}
	public int getLineOfOnePage() {
		return lineOfOnePage;
	}
	public void setLineOfOnePage(int lineOfOnePage) {
		this.lineOfOnePage = lineOfOnePage;
	}
	
	
	
	
}
