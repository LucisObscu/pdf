package aaa.bbb.ccc;

import org.springframework.web.multipart.MultipartFile;

public interface PdfService {
	public void write();
	public void writePdf(MultipartFile mFile);
}
