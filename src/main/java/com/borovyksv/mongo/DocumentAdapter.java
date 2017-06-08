package com.borovyksv.mongo;

import com.borovyksv.mongo.pojo.Page;

import java.util.*;

public class DocumentAdapter {
  public DocumentAdapter() {
  }

//  public static DocumentWithTextPages getDocumentFromMap(String fileName, Map<Integer, String> textPages) {
//    DocumentWithTextPages document = new DocumentWithTextPages();
//
//    String cuttedFileName = fileName.substring(0, fileName.lastIndexOf('.'));
//    document.setName(cuttedFileName);
//
//    List<Page> pageList = getPageListFromMap(textPages);
//
//    document.setPages(pageList);
//    return document;
//  }

  public static List<Page> getPageListFromMap(Map<Integer, String> bookmarkPages) {
    List<Page> pageList = new ArrayList<>();
    for (Map.Entry<Integer, String> entry : bookmarkPages.entrySet()) {
      Page page = new Page();
      page.setPageNum(entry.getKey());
      page.setText(entry.getValue());
      pageList.add(page);
    }
    return pageList;
  }

  public static List<Page> getBookmarkListFromMap(Map<String, Integer> bookmarkPages) {
    List<Page> pageList = new ArrayList<>();
    for (Map.Entry<String, Integer> entry : bookmarkPages.entrySet()) {
      Page page = new Page();
      page.setPageNum(entry.getValue());
      page.setText(entry.getKey());
      pageList.add(page);
    }
    Collections.sort(pageList, (o1, o2) -> Integer.compare(o1.getPageNum(), o2.getPageNum()));
    return pageList;

  }
}
