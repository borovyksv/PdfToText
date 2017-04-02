//import com.sun.pdfview.PDFFile;
//import com.sun.pdfview.PDFPage;
//
//import javax.imageio.ImageIO;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.awt.image.ColorModel;
//import java.awt.image.PixelGrabber;
//import java.io.File;
//import java.io.IOException;
//import java.io.RandomAccessFile;
//import java.nio.ByteBuffer;
//import java.nio.channels.FileChannel;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.atomic.AtomicInteger;
//
//public class ImageMain {
//    public static void setup() throws IOException {
//        // load a pdf from a byte buffer
//        File file = new File("D:\\pdf\\Acura.pdf");
//        RandomAccessFile raf = new RandomAccessFile(file, "r");
//        FileChannel channel = raf.getChannel();
//        ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
//        final PDFFile pdffile = new PDFFile(buf);
//        int numPgs = pdffile.getNumPages();
//
//        ExecutorService executorService = Executors.newFixedThreadPool(4);
//
//        AtomicInteger pageCounter = new AtomicInteger(0);
//
//        for (int i = 0; i < numPgs; i++) {
//            executorService.execute(() -> {
//                try {
//                    int currentPage = pageCounter.incrementAndGet();
//                    // draw the first page to an image
//                    PDFPage page = pdffile.getPage(currentPage);
//                    // get the width and height for the doc at the default zoom
//                    Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(), (int) page.getBBox().getHeight());
//                    // generate the image
//                    Image img = page.getImage(rect.width, rect.height, // width & height
//                            rect, // clip rect
//                            null, // null for the ImageObserver
//                            true, // fill background with white
//                            true // block until drawing is done
//                    );
//                    // save it as a file
//                    BufferedImage bImg = toBufferedImage(img);
//                    File yourImageFile = new File("page_" + currentPage + ".png");
//                    ImageIO.write(bImg, "png", yourImageFile);
//                    System.out.println(yourImageFile.toString()+" saved");
//                    bImg.flush();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//        }
//    }
//
//    // This method returns a buffered image with the contents of an image
//    public static BufferedImage toBufferedImage(Image image) {
//        if (image instanceof BufferedImage) {
//            return (BufferedImage) image;
//        }
//        // This code ensures that all the pixels in the image are loaded
//        image = new ImageIcon(image).getImage();
//        // Determine if the image has transparent pixels; for this method's
//        // implementation, see e661 Determining If an Image Has Transparent
//        // Pixels
//        boolean hasAlpha = hasAlpha(image);
//        // Create a buffered image with a format that's compatible with the
//        // screen
//        BufferedImage bimage = null;
//        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        try {
//            // Determine the type of transparency of the new buffered image
//            int transparency = Transparency.OPAQUE;
//            if (hasAlpha) {
//                transparency = Transparency.BITMASK;
//            }
//            // Create the buffered image
//            GraphicsDevice gs = ge.getDefaultScreenDevice();
//            GraphicsConfiguration gc = gs.getDefaultConfiguration();
//            bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
//        } catch (HeadlessException e) {
//            // The system does not have a screen
//        }
//        if (bimage == null) {
//            // Create a buffered image using the default color model
//            int type = BufferedImage.TYPE_INT_RGB;
//            if (hasAlpha) {
//                type = BufferedImage.TYPE_INT_ARGB;
//            }
//            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
//        }
//        // Copy image to buffered image
//        Graphics g = bimage.createGraphics();
//        // Paint the image onto the buffered image
//        g.drawImage(image, 0, 0, null);
//        g.dispose();
//        return bimage;
//    }
//
//    public static boolean hasAlpha(Image image) {
//        // If buffered image, the color model is readily available
//        if (image instanceof BufferedImage) {
//            BufferedImage bimage = (BufferedImage) image;
//            return bimage.getColorModel().hasAlpha();
//        }
//        // Use a pixel grabber to retrieve the image's color model;
//        // grabbing a single pixel is usually sufficient
//        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
//        try {
//            pg.grabPixels();
//        } catch (InterruptedException e) {
//        }
//        // Get the image's color model
//        ColorModel cm = pg.getColorModel();
//        return cm.hasAlpha();
//    }
//
//    public static void main(final String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                try {
//                    ImageMain.setup();
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        });
//    }
//}