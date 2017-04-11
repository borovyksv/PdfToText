package com.borovyksv.util;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PDFConverterTest {
    private static String PATH_TO_TEST_FOLDER = "src/test/java/com/borovyksv/util/";
    private static String MOCK_FILENAME = "mock.pdf";
    private static int NUMBER_OF_PAGES = 10;

    private PDFConverter converter;

    @Before
    public void setUp() {
        File file = new File(PATH_TO_TEST_FOLDER + MOCK_FILENAME);
        converter = PDFConverterFactory.newPDFConverter(file.getAbsolutePath());
    }

    @Test
    public void testReturnFalseIfBookmarksNotExists() throws FileNotFoundException {
        boolean bool = converter.saveBookmarks();
        assertFalse(bool);
    }


    @Test
    public void testNumberOfConvertedPdfPages() {
        converter.savePagesFromPdf();
        int expected = NUMBER_OF_PAGES;
        int result = new File(PATH_TO_TEST_FOLDER + "/mock_parsed/PDF").listFiles().length;
        assertEquals(expected, result);
    }

    @Test
    public void testFormatOfConvertedPdfPages() {
        converter.savePagesFromPdf();
        String expectedFormat = ".pdf";

        String resultFileName = new File(PATH_TO_TEST_FOLDER + "/mock_parsed/PDF/").listFiles()[0].getName();
        String resultFormat = resultFileName.substring(resultFileName.lastIndexOf('.'));
        assertEquals(expectedFormat, resultFormat);
    }

    @Test
    public void testNumberOfConvertedImages() {
        converter.saveImagesAndTextFromPdf();
        int expected = NUMBER_OF_PAGES;

        int result = new File(PATH_TO_TEST_FOLDER + "/mock_parsed/IMG").listFiles().length;
        assertEquals(expected, result);
    }

    @Test
    public void testFormatOfConvertedImages() {
        converter.savePagesFromPdf();
        String expectedFormat = ".jpg";

        String resultFileName = new File(PATH_TO_TEST_FOLDER + "/mock_parsed/IMG/").listFiles()[0].getName();
        String resultFormat = resultFileName.substring(resultFileName.lastIndexOf('.'));
        assertEquals(expectedFormat, resultFormat);
    }

    @Test
    public void testNumberOfConvertedTextFiles() {
        converter.saveImagesAndTextFromPdf();
        int expected = NUMBER_OF_PAGES;

        int result = converter.getTextPages().size();
        assertEquals(expected, result);
    }





//    @AfterClass
//    public static void afterClass() {
//        try {
//            FileUtils.deleteDirectory(new File(PATH_TO_TEST_FOLDER + "/mock_parsed"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


}
