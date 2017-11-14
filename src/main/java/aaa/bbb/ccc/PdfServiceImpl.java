package aaa.bbb.ccc;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

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

	@Override
	public String check(PdfFileDto fileDto)throws Exception {
//		Files.probeContentType()
		String error=null;
		int line=fileDto.getLine();
			if(line==1||line==2){
				error=txtWrite(fileDto.getBookFile(),line,
						fileDto.getNumOfOneLine(),fileDto.getLineOfOnePage());
			}else{
				error=pdfWrite(fileDto.getBookFile());
			}
		

		return error;
	}


	public String txtWrite(MultipartFile bookFile,int lineNum,int numLine,int linePage){
		try {
		String stord="D:/temp/";
		String oldFileName=bookFile.getOriginalFilename();
		//BufferedReader br=null;
		BufferedReader br=null;
		File destinationFile =null;
		int numOfOneLine = 0;	
		int lineOfOnePage = 0;
		String destinationDir = "D:/temp/Converted_txt/";
		System.out.println("aaaaaaaaaaaaaaaaaaaa"+oldFileName);
		destinationFile = new File(destinationDir + oldFileName+"/"+ oldFileName);
	if (destinationFile.isDirectory()) {

		
	}else {
		destinationFile.mkdirs();
	}
//	Writer myWriter= new BufferedWriter(new OutputStreamWriter(
//		    new FileOutputStream(oldFileName), "UTF-8"));
//		try {
//		    myWriter.write(destinationDir + oldFileName+"/"+ oldFileName);
//		} catch(Exception e){
//		e.printStackTrace();
//		}
		
		bookFile.transferTo(new File(destinationDir + oldFileName+"/"+ oldFileName));
		//br = new BufferedReader(new FileReader(destinationDir + oldFileName+"/"+ oldFileName)); //Read .txt file
		br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(destinationDir + oldFileName+"/"+ oldFileName)),"euc-kr"));
		if(numLine==0) {
			numOfOneLine=30;
		}else {
			numOfOneLine = numLine;	// Number of one line
		}
		if(linePage==0) {
			lineOfOnePage=30;
		}else {
			lineOfOnePage = linePage;	// Number of lines of One page
		}
		
		int page = 1;	// For count pages
		int numOfEnter;	// For search the number of Enter
		StringBuilder sb = new StringBuilder();
		String line = br.readLine(); // Read one line
		
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
				page = write(page,sb,oldFileName); // Write and return the number of page for increase
			}
					
		} // Exit when line is not exist
		
		write(page,sb,oldFileName); // Write the left text 
		br.close();
		}catch (Exception e) {
//			return "500";
			e.printStackTrace();
		}
		return null;
	}
	public static int write(int page, StringBuilder sb,String oldFileName) throws Exception{
		if(sb.toString().equals("\r\n")) {System.out.println("\\r\\n");return page;}
		if(sb.toString().equals("")) {System.out.println("space");return page;}
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:/temp/Converted_txt/"+oldFileName+"/"+ page + oldFileName)),"euc-kr"));
		String withoutLastEnter = sb.toString().substring(0, sb.toString().lastIndexOf("\r\n"));
//		bw.write(withoutLastEnter);
		Writer wit=bw;
		wit.write(withoutLastEnter);
		bw.flush();
		bw.close();
		System.out.println("sb : " + withoutLastEnter);
		sb.setLength(0);
		page++;
		return page;
	}
	
	public String pdfWrite(MultipartFile mFile) {
		File destinationFile =null;
		String sourceDir = "D:/temp/"; // Pdf files are read from this folder
		String destinationDir = "D:/temp/Converted_PdfFiles_to_Image/"; // converted images from pdf document are
																		// saved here
		String oldFileName = mFile.getOriginalFilename();
		try {
			
			
			mFile.transferTo(new File(sourceDir+oldFileName));
			Long startTime = System.currentTimeMillis();
			System.out.println(startTime);

			File sourceFile = new File(sourceDir + oldFileName);
			//							D:/temp/+gameOfThrone.pdf
			destinationFile = new File(destinationDir + oldFileName);
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
			File file = new File(sourceDir + oldFileName);
			file.delete();
			destinationFile.delete();
			return "500";
		}
		return null;
	}
	
	public void fileImg(MultipartFile fileImg, String fileName, String stordName) {
		File file =new File("");
		if(!file.exists()) {
			file.mkdirs();
		}
		try {
			fileImg.transferTo(new File(""));
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
}
