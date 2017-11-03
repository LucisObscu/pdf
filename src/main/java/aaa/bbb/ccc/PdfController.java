package aaa.bbb.ccc;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PdfController {

	@RequestMapping(value="/write.bbs",method=RequestMethod.GET)
	public String write()throws IOException{	
		
		BufferedReader br = new BufferedReader(new FileReader("D:/temp/gggg.txt"));
		int lineSize=20 , field=10;
		String bimil = UUID.randomUUID().toString();
		try {
			
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    boolean bool=true;
		    if(bool) {
		    while (line != null) {
		        sb.append(line);
		        sb.append("\n");
		        line = br.readLine();
		    }
		    }else{
		    
		    while (line != null) {
		    	sb.append(line);
		        line = br.readLine();
		    }
		    // txt \n 전부삭제 String Builder sb 담기
		    System.out.println("+++++++++++"+sb);
		    
		    int a=(int)(sb.length()/lineSize);
//		    int a=(int)(Math.ceil(sb.length()/(double)3));
		    System.out.println("+++++++++++"+a);
		    
		    int k=0;
		    for(int x=1;x<=a;x++){
		    	//1 2 3 
		    	sb.insert((int)(k+(x*lineSize)), "\n");
		    	k++;
		    }
		    //30자마다 /n 넣기
		    }
		    System.out.println("+++++++++++"+sb);
		    String parag[]= sb.toString().split("\n");
		    
		    // 30자마다 /n 기중으로 잘라서 스트링 배열에 넣기(/n 삭제됨)
		    for(int i=0;i<parag.length;i++) {
		    	parag[i]+="\r\n";
		    }
		    // 30자마다 엔터값 넣기
		    
		    for(int i=0;i<parag.length;i++) {
		    	if((i+1)%field==0){
		    	parag[i]=parag[i]+bimil;
		    	}
		    }
		    
		    String ppp="";
		    for(int i=0;i<parag.length;i++) {
		    	ppp=ppp+parag[i];
		    }
		    
		    String parags[] = ppp.toString().split(bimil);

		    for (int i=0; i<parags.length;i++) {
		        File file = new File("D:/temp/Paragraph_"+i+".txt");
		        FileWriter writer = new FileWriter(file, true);
		        PrintWriter output = new PrintWriter(writer);
		        output.print(parags[i]);
		        output.close();
		        br.close();
		    }
		} finally {
		    br.close();
		}

		return "writeForm"; 
	}
	
	@RequestMapping(value="/write.bbs",method=RequestMethod.POST)
	public String write(@RequestPart("fname") MultipartFile mFile){		
		try {
			String sourceDir = "D:/temp/"; // Pdf files are read from this folder
			String destinationDir = "D:/temp/Converted_PdfFiles_to_Image/"; // converted images from pdf document are
																			// saved here
			
			String oldFileName = mFile.getOriginalFilename();
			mFile.transferTo(new File(sourceDir+oldFileName));
			Long startTime = System.currentTimeMillis();
			System.out.println(startTime);

			File sourceFile = new File(sourceDir + oldFileName);
			//							D:/temp/+gameOfThrone.pdf
			File destinationFile = new File(destinationDir + oldFileName);
				//						D:/temp/Converted_PdfFiles_to_Image/+gameOfThrone.pdf
			if (!destinationFile.exists()) {
				destinationFile.mkdirs();
				System.out.println("Folder Created -> " + destinationFile.getAbsolutePath());
			}
			if (sourceFile.exists()) {
				
				System.out.println("Images copied to Folder: " + destinationFile.getName());
				PDDocument document = PDDocument.load(sourceFile);
//				PdfImageCounter counter = new PdfImageCounter();
//				counter.countImagesWithProcessor(document);
				PDFRenderer pdfRenderer = new PDFRenderer(document);
				int pageCounter = 0;
				String fileName = sourceFile.getName().replace(".pdf", "");
				System.out.println("Total files to be converted -> " + document.getPages().getCount());
				
				int pageNumber = 1;
				for (PDPage page : document.getPages())	{
					// 기존의 방식
/*				    // note that the page number parameter is zero based
				    BufferedImage bim = pdfRenderer.renderImageWithDPI(pageCounter, 300, ImageType.RGB);
				    File outputfile = new File(
							destinationDir + oldFileName + "/" + fileName + "_" + pageNumber + ".png");
					
				    // suffix in filename will be used as the file format
				    ImageIO.write(bim, "png", outputfile);
*/
					//새로운 방식 <훨씬 빠른듯>
					
					BufferedImage bim = pdfRenderer.renderImageWithDPI(pageCounter, 300, ImageType.RGB);
					
//					byte[] imageByte = ((DataBufferByte)bim.getData().getDataBuffer()).getData();
					
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write( bim, "jpg", baos );
					baos.flush();
					byte[] imageInByte = baos.toByteArray();
					baos.close();
//					DataBufferInt imageByte = (DataBufferInt)bim.getData().getDataBuffer();
//					int[] imageInt = imageByte.getData();
				    FileOutputStream fos3 = new FileOutputStream(new File(
							destinationDir + oldFileName + "/" + fileName + "_" + pageNumber + ".png"));
					BufferedOutputStream bos3 = new BufferedOutputStream(fos3);
//					bos3.write(imageByte);
					bos3.write(imageInByte);
					bos3.close();
					fos3.close();
					
					
					
					
					
					
					
					PDFTextStripper reader = new PDFTextStripper();
					reader.setStartPage(pageNumber);
					reader.setEndPage(pageNumber);
					String pageText = reader.getText(document);
					new File(destinationDir + oldFileName + "/" + pageNumber).mkdir();
					FileOutputStream fos = new FileOutputStream(
							new File(destinationDir + oldFileName + "/" + pageNumber + "/"+fileName + ".txt"));

					BufferedOutputStream bos = new BufferedOutputStream(fos);
					bos.write(pageText.getBytes());
					System.out.println("TextCreated" + destinationDir + pageNumber + "/" + fileName + ".txt");
					bos.close();
					fos.close();
					
					
					PDResources pdResources = page.getResources();
					int imageCount = 1;
			        for (COSName cosName : pdResources.getXObjectNames()) {
			            PDXObject xobject = pdResources.getXObject(cosName);
			            if (xobject instanceof PDImageXObject) {
			            	//기존의 방식
			            	/*
			            	BufferedImage bi = ((PDImageXObject) xobject).getImage();
			            	String imageDest =destinationDir + oldFileName + "/" + pageNumber + "/" + fileName
									+ "_" + imageCount + ".png";
			            	System.out.println("Image created as " + imageDest);
			            	File file = new File(imageDest);
			            	ImageIO.write(bi, "png", file);
			            	*/
			            	
			            	//새로운 방식 <훨씬 빠른듯>
							
							
//							byte[] imageByte = ((DataBufferByte)bim.getData().getDataBuffer()).getData();
			            	BufferedImage bi = ((PDImageXObject) xobject).getImage();
							ByteArrayOutputStream baossa = new ByteArrayOutputStream();
							ImageIO.write( bi, "jpg", baossa );
							baossa.flush();
							byte[] imageInByteSa = baossa.toByteArray();
							baossa.close();
//							DataBufferInt imageByte = (DataBufferInt)bim.getData().getDataBuffer();
//							int[] imageInt = imageByte.getData();
							String imageDest4 =destinationDir + oldFileName + "/" + pageNumber + "/" + fileName
									+ "_" + imageCount + ".png";
						    FileOutputStream fos4 = new FileOutputStream(new File(imageDest4));
							BufferedOutputStream bos4 = new BufferedOutputStream(fos4);
//							bos3.write(imageByte);
							bos4.write(imageInByteSa);
							bos4.close();
							fos4.close();
			            	
			            	
			            	
			            	
			            	
			            	
			            	
			            	
			                imageCount++;
			                if (imageCount % 100 == 0) {
			                    System.out.println("Found image: #" + imageCount);
			                }
			            }
			        }
			        
			       
			        
					/*PDResources pdResources = page.getResources();
					int totalImages = 1;
					Map pageImages = pdResources.getImages();
					if (pageImages != null) {
						Iterator imageIter = pageImages.keySet().iterator();
						while (imageIter.hasNext()) {
							String key = (String) imageIter.next();
							PDImageXObject pdxObjectImage = (PDImageXObject) pageImages.get(key);
							pdxObjectImage.(destinationDir + oldFileName + "/" + pageNumber + "/" + fileName
									+ "_" + totalImages);
							totalImages++;
						}
					}*/
			        System.out.println("pageNumber : " + pageNumber);
					pageNumber++;
					pageCounter++;
				}
				document.close();
				
				System.out.println("걸린시간 : " + (System.currentTimeMillis() - startTime));
				System.out.println("Converted Images are saved at -> " + destinationFile.getAbsolutePath());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "wrtie";
	}
	
}
