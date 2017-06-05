package com.borovyksv.mongo;

import com.borovyksv.mongo.pojo.DocumentWithTextPages;
import com.borovyksv.mongo.pojo.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DocumentAdapter {
  public DocumentAdapter() {
  }

  public static DocumentWithTextPages getDocumentFromMap(String fileName, Map<Integer, String> textPages) {
    DocumentWithTextPages document = new DocumentWithTextPages();
    List<Page> pageList = new ArrayList<>();


    String cuttedFileName = fileName.substring(0, fileName.lastIndexOf('.'));
    document.setName(cuttedFileName);

    for (Map.Entry<Integer, String> entry : textPages.entrySet()) {
      Page page = new Page();
      page.setId(entry.getKey());
      page.setText(entry.getValue());
      pageList.add(page);
    }

    document.setPages(pageList);
    return document;
  }

  public static List<Page> getBookmarkPages(Map<Integer, String> bookmarkPages) {
    List<Page> pageList = new ArrayList<>();
    for (Map.Entry<Integer, String> entry : bookmarkPages.entrySet()) {
      Page page = new Page();
      page.setId(entry.getKey());
      page.setText(entry.getValue());
      pageList.add(page);
    }
    return pageList;
  }

}
