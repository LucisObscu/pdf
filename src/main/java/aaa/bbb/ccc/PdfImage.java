package aaa.bbb.ccc;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.apache.pdfbox.util.PDFTextStripper;

@SuppressWarnings("unchecked")
public class PdfImage implements Runnable {
//	 <dependency>
//	 <groupId>org.apache.pdfbox</groupId>
//	 <artifactId>pdfbox</artifactId>
//	 <version>1.8.13</version>
//	 </dependency>

	public static void main(String[] args) {
		PdfImage myrun = new PdfImage();
		
		Thread t = new Thread(myrun); // ������ myrun ��ü�� �μ��� ������ ����
		t.start();
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try {
			String sourceDir = "D:/temp/"; // Pdf files are read from this folder
			String destinationDir = "D:/temp/Converted_PdfFiles_to_Image/"; // converted images from pdf document are
																			// saved here
			String oldFileName = "gameOfThrone.pdf";
			Long startTime = System.currentTimeMillis();
			System.out.println(startTime);

			File sourceFile = new File(sourceDir + oldFileName);
			//							D:/temp/+gameOfThrone.pdf
			File destinationFile = new File(destinationDir + oldFileName);
				//						D:/temp/Converted_PdfFiles_to_Image/+gameOfThrone.pdf
			if (!destinationFile.exists()) {
				destinationFile.mkdir();
				System.out.println("Folder Created -> " + destinationFile.getAbsolutePath());
			}
			if (sourceFile.exists()) {
				System.out.println("Images copied to Folder: " + destinationFile.getName());
				PDDocument document = PDDocument.load(sourceDir + oldFileName);
				List<PDPage> list = document.getDocumentCatalog().getAllPages();
				System.out.println("Total files to be converted -> " + list.size());

				String fileName = sourceFile.getName().replace(".pdf", "");
				//  gameOfThrone
				int pageNumber = 1;
				for (PDPage page : list) {
					int resolution = 288;
					BufferedImage image = page.convertToImage(BufferedImage.TYPE_INT_RGB, resolution);
					File outputfile = new File(
							destinationDir + oldFileName + "/" + fileName + "_" + pageNumber + ".png");
//					D:/temp/Converted_PdfFiles_to_Image/+gameOfThrone.pdf+/+gameOfThrone+_+1+.png
					System.out.println("Image Created -> " + outputfile.getName());
					ImageIO.write(image, "png", outputfile);

					PDFTextStripper reader = new PDFTextStripper();
					reader.setStartPage(pageNumber);
					reader.setEndPage(pageNumber);
					String pageText = reader.getText(document);
					new File(destinationDir + oldFileName + "/" + pageNumber).mkdir();
					FileOutputStream fos = new FileOutputStream(
							new File(destinationDir + oldFileName + "/" + pageNumber + "/temptext1.txt"));

					BufferedOutputStream bos = new BufferedOutputStream(fos);
					bos.write(pageText.getBytes());
					System.out.println("TextCreated" + destinationDir + pageNumber + "/" + fileName + ".txt");
					bos.close();
					fos.close();

					PDResources pdResources = page.getResources();

					int totalImages = 1;
					Map pageImages = pdResources.getImages();
					if (pageImages != null) {

						Iterator imageIter = pageImages.keySet().iterator();
						while (imageIter.hasNext()) {
							String key = (String) imageIter.next();
							PDXObjectImage pdxObjectImage = (PDXObjectImage) pageImages.get(key);
							pdxObjectImage.write2file(destinationDir + oldFileName + "/" + pageNumber + "/" + fileName
									+ "_" + totalImages);
							totalImages++;
						}
					}

					pageNumber++;
				}

				/*
				 * List allPages = document.getDocumentCatalog().getAllPages(); for( int i=0;
				 * i<allPages.size(); i++ ) { System.out.println("Processing page "+i); PDPage
				 * page = (PDPage)allPages.get(i);
				 * 
				 * int resolution = 72; PDFTextStripper pts = new PDFTextStripper();
				 * BufferedImage image = page.convertToImage(BufferedImage.TYPE_INT_RGB,
				 * resolution);
				 * 
				 * PDStream contents = page.getContents(); if( contents != null ) {
				 * pts.processStream( page, page.findResources(), page.getContents().getStream()
				 * ); }
				 * 
				 * 
				 * }
				 * 
				 */
				System.out.println("�ɸ��ð� : " + (System.currentTimeMillis() - startTime));
				document.close();
				System.out.println("Converted Images are saved at -> " + destinationFile.getAbsolutePath());
			} else {
				System.err.println(sourceFile.getName() + " File not exists");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}
	
	
	
	
}