package aaa.bbb.ccc;

import org.springframework.web.multipart.MultipartFile;

public interface PdfService {
	public void txtWrite();
	public void pdfWrite(MultipartFile mFile);
	public void check(MultipartFile BookFile);
}
