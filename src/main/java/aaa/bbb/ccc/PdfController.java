package aaa.bbb.ccc;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;



@Controller
public class PdfController {
	
	@Autowired
	PdfService pdfService;
//	@RequestMapping(value="/write.bbs",method=RequestMethod.GET)
//	public String write()throws IOException{	
//		
//		BufferedReader br = new BufferedReader(new FileReader("D:/temp/gggg.txt"));
//		int lineSize=3 , field=3;
//		String bimil = UUID.randomUUID().toString();
//		try {
//			
//		    StringBuilder sb = new StringBuilder();
//		    String line = br.readLine();
//		    boolean bool=false;
//		    if(bool) {
//		    while (line != null) {
//		        sb.append(line);
//		        sb.append("\n");
//		        line = br.readLine();
//		    }
//		    }else{
//		    
//		    while (line != null) {
//		    	sb.append(line);
//		        line = br.readLine();
//		    }
//		    // txt \n 전부삭제 String Builder sb 담기
//		    System.out.println("+++++++++++"+sb);
//		    
//		    int a=(int)(sb.length()/lineSize);
////		    int a=(int)(Math.ceil(sb.length()/(double)3));
//		    System.out.println("+++++++++++"+a);
//		    
//		    int k=0;
//		    for(int x=1;x<=a;x++){
//		    	//1 2 3 
//		    	sb.insert((int)(k+(x*lineSize)), "\n");
//		    	k++;
//		    }
//		    //30자마다 /n 넣기
//		    }
//		    System.out.println("+++++++++++"+sb);
//		    String parag[]= sb.toString().split("\n");
//		    
//		    // 30자마다 /n 기중으로 잘라서 스트링 배열에 넣기(/n 삭제됨)
//		    for(int i=0;i<parag.length;i++) {
//		    	parag[i]+="\r\n";
//		    }
//		    // 30자마다 엔터값 넣기
//		    
//		    for(int i=0;i<parag.length;i++) {
//		    	if((i+1)%field==0){
//		    	parag[i]=parag[i]+bimil;
//		    	}
//		    }
//		    
//		    String ppp="";
//		    for(int i=0;i<parag.length;i++) {
//		    	ppp=ppp+parag[i];
//		    }
//		    
//		    String parags[] = ppp.toString().split(bimil);
//
//		    for (int i=0; i<parags.length;i++) {
//		        File file = new File("D:/temp/Paragraph_"+i+".txt");
//		        FileWriter writer = new FileWriter(file, true);
//		        PrintWriter output = new PrintWriter(writer);
//		        output.print(parags[i]);
//		        output.close();
//		        br.close();
//		    }
//		} finally {
//		    br.close();
//		}
//
//		return "writeForm"; 
//	}
	
	@RequestMapping(value="/write.bbs",method=RequestMethod.GET)
	public String text(){
		
		return "writeForm";
	}
	
	
	
	@RequestMapping(value="/write.bbs",method=RequestMethod.POST)
	public String write(String bookTitle,String bookContent,
						@RequestPart("BookFile") MultipartFile mFile){		
		pdfService.writePdf(mFile);
		return "wrtie";
	}
	
}
