package pdfcr;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.StringTokenizer;

public class PDFCreater {
    public PDFCreater() {

    }

//    private void run() {
//
//    }

    public static void addPageNumbers(PDDocument document, String numberingFormat, int offset_X, int offset_Y) throws IOException {
        int page_counter = 1;
        for(PDPage page : document.getPages()){
            PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, false);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ITALIC, 10);
            PDRectangle pageSize = page.getMediaBox();
            float x = pageSize.getLowerLeftX();
            float y = pageSize.getLowerLeftY();
//            contentStream.newLineAtOffset(x+ offset_X, y-offset_Y);
            contentStream.newLineAtOffset(x+ pageSize.getWidth()-offset_X, y+offset_Y);
//            String text = MessageFormat.format(numberingFormat,page_counter);
            String text = page_counter+"";
            contentStream.showText(text);
            contentStream.endText();
            contentStream.close();
            ++page_counter;
        }
    }

    public static void main(String a[]) throws Exception{

//        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");

        Properties prop = new Properties();
        PDFCreater pc = new PDFCreater();



        prop.load(new FileInputStream(pc.getClass().getResource("../input.properties").getPath()));


//        String imgPath1 = "/home/jagannathan/yantra/y001.jpg";
//        String imgPath2 = "/home/jagannathan/yantra/y002.jpg";
        String imgPath = "";

        PDDocument document = new PDDocument();
//        InputStream in = new FileInputStream(imgPath);

        PDPage page = null;
        BufferedImage bimg = null;
        float width = 0;
        float height = 0;
        PDImageXObject img = null;
        PDPageContentStream contentStream = null;


        StringTokenizer st = new StringTokenizer(prop.getProperty("input_image"),",");


        while (st.hasMoreTokens()) {
            // First

            imgPath = st.nextToken();
            System.out.println("img path" + imgPath);
            bimg = ImageIO.read(new File(imgPath));
            width = bimg.getWidth();
            height = bimg.getHeight();


            page = new PDPage(new PDRectangle(width, height));
            document.addPage(page);


            img = PDImageXObject.createFromFile(imgPath, document) ; // new PDJpeg(document, new FileInputStream(someImage));
            contentStream = new PDPageContentStream(document, page);
            contentStream.drawImage(img, 0, 0);
            contentStream.close();

        }
//        addPageNumbers(document,"Page {0}",60,18);
        document.save(prop.getProperty("output_pdffile"));
        document.close();


    }
}
