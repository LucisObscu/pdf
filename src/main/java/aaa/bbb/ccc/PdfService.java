package aaa.bbb.ccc;

import org.springframework.web.multipart.MultipartFile;

public interface PdfService {
	public String txtWrite(MultipartFile BookFile,String line);
	public String pdfWrite(MultipartFile mFile);
	public String check(MultipartFile BookFile,String line)throws Exception;
}
