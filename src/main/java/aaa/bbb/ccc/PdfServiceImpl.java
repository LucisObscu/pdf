package aaa.bbb.ccc;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;

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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PdfServiceImpl implements PdfService{
	
	
	public void fileFilter() {
		
	}
	
	
	
	
	@Override
	public void check(MultipartFile BookFile) {
		String originalName=BookFile.getOriginalFilename();
		String formatName = originalName.substring(originalName.lastIndexOf(".")+1);
		if(formatName.equalsIgnoreCase("txt")) {
			System.out.println("++++++++++++++"+"txt:"+formatName);
		}else if(formatName.equalsIgnoreCase("pdf")) {
			pdfWrite(BookFile);
		}else{
			
		}
	}




	@Override
	public void txtWrite() {
		try {
			
		
		BufferedReader br = new BufferedReader(new FileReader("D:/temp/gggg.txt")); //Read .txt file
		int numOfOneLine = 3;	// Number of one line
		int lineOfOnePage = 3;	// Number of lines of One page
		int page = 1;	// For count pages
		int numOfEnter;	// For search the number of Enter
		StringBuilder sb = new StringBuilder();
		String line = br.readLine(); // Read one line
		
		while (line != null) {
	        sb.append(line);
	        sb.append("\n");
	        line = br.readLine();
		}
		while (line != null) { // While line is exist
			if (line.length() <= numOfOneLine) { 
				sb.append(line + "\r\n");
				line = br.readLine();	// Read next line when current line is lesser than numOfOneLine 
			} else {
				sb.append(line.substring(0, numOfOneLine) + "\r\n");
				line = line.substring(numOfOneLine); // Cut line and reunite
				
			}
			numOfEnter = StringUtils.countOccurrencesOf(sb.toString(), "\r\n");
			if(numOfEnter >= lineOfOnePage) {
				page = write(page,sb); // Write and return the number of page for increase
			}
					
		} // Exit when line is not exist
		write(page,sb); // Write the left text 
		br.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static int write(int page, StringBuilder sb) throws Exception{
		if(sb.toString().equals("\r\n")) {System.out.println("\\r\\n");return page;}
		if(sb.toString().equals("")) {System.out.println("space");return page;}
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("d:/temp/gggg" + page + ".txt")));
		String withoutLastEnter = sb.toString().substring(0, sb.toString().lastIndexOf("\r\n"));
		bw.write(withoutLastEnter);
		bw.flush();
		bw.close();
		System.out.println("while¾È sb : " + withoutLastEnter);
		sb.setLength(0);
		page++;
		return page;
	}
	
	public void pdfWrite(MultipartFile mFile) {
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
	}
	
}
