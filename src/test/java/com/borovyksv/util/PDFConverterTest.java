package com.borovyksv.util;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class PDFConverterTest {
    private static String PATH_TO_TEST_FOLDER = "src/test/java/com/borovyksv/util/";
    private static String MOCK_FILENAME = "mock.pdf";
    private static int NUMBER_OF_PAGES;

    private PDFConverter converter;

    @Before
    public void setUp() {
        File file = new File(PATH_TO_TEST_FOLDER + MOCK_FILENAME);
        System.out.println(file.getAbsolutePath());
        converter = PDFConverterFactory.newPDFConverter(file.getAbsolutePath());
        NUMBER_OF_PAGES = converter.getNumberOfPages();
    }

    @Test
    public void testReturnFalseIfBookmarksNotExists() throws FileNotFoundException {
        boolean bool = converter.saveBookmarks();
        assertFalse(bool);
    }


    @Test
    public void testNumberOfConvertedPdfPages() {
        converter.savePages();
        int expected = NUMBER_OF_PAGES;
        int result = new File(converter.getResultFolderPDF()).listFiles().length;
        assertEquals(expected, result);
    }

    @Test
    public void testFormatOfConvertedPdfPages() {
        converter.savePages();
        String expectedFormat = ".pdf";

        String resultFileName = new File(converter.getResultFolderPDF()).listFiles()[0].getName();
        String resultFormat = resultFileName.substring(resultFileName.lastIndexOf('.'));
        assertEquals(expectedFormat, resultFormat);
    }

    @Test
    public void testNumberOfConvertedImages() {
        converter.saveImages();
        int expected = NUMBER_OF_PAGES;

        int result = new File(converter.getResultFolderIMG()).listFiles().length;
        assertEquals(expected, result);
    }

    @Test
    public void testFormatOfConvertedImages() {
        converter.savePages();
        String expectedFormat = ".jpg";

        String resultFileName = new File(converter.getResultFolderIMG()).listFiles()[0].getName();
        String resultFormat = resultFileName.substring(resultFileName.lastIndexOf('.'));
        assertEquals(expectedFormat, resultFormat);
    }

    @Test
    public void testNumberOfConvertedTextFiles() {
        converter.saveImagesAndText();
        int expected = NUMBER_OF_PAGES;

        int result = converter.getTextPages().size();
        assertEquals(expected, result);
    }

    @Test
    public void testIsScannedPDF(){
        assertEquals(true, converter.isScanned());
    }





    @AfterClass
    public static void afterClass() {
        try {
            FileUtils.deleteDirectory(new File(PATH_TO_TEST_FOLDER + "/mock_parsed"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
