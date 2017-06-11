package com.borovyksv.controller;

import com.borovyksv.mongo.pojo.ConvertedDocument;
import com.borovyksv.mongo.pojo.DocumentWithTextPages;
import com.borovyksv.mongo.pojo.ProgressStatus;
import com.borovyksv.mongo.repository.ConvertedDocumentRepository;
import com.borovyksv.mongo.repository.DocumentWithTextPagesRepository;
import com.borovyksv.mongo.repository.ProgressStatusRepository;
import com.borovyksv.service.FileService;
import com.borovyksv.sql.VehicleRepository;
import com.borovyksv.sql.vehicle.FrontendVehicle;
import com.borovyksv.sql.vehicle.Info;
import com.borovyksv.sql.vehicle.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class MainController {

  @Autowired
  ProgressStatusRepository progressStatusRepository;
  @Autowired
  ConvertedDocumentRepository convertedDocumentRepository;
  @Autowired
  DocumentWithTextPagesRepository documentWithTextPagesRepository;
  @Autowired
  VehicleRepository vehicleRepository;

  @Autowired
  FileService fileService;


  @RequestMapping(value = "/documents/progress", method = RequestMethod.GET)
  public List<ProgressStatus> progressStatus() {
    return progressStatusRepository.findAll();
  }

  @RequestMapping(value = "/documents/converted", method = RequestMethod.GET)
  public List<ConvertedDocument> convertedDocuments() {
    return convertedDocumentRepository.findAll();
  }

  @RequestMapping(value = "/vehicles", method = RequestMethod.GET)
  public List<FrontendVehicle> getVehicles(){
    List<Vehicle> allAndGroup = vehicleRepository.findAllAndGroup();

    List<FrontendVehicle> results = new ArrayList<>();
    for (Vehicle vehicle : allAndGroup) {
      FrontendVehicle fv = new FrontendVehicle();
      fv.setVendor(vehicle.getModel_make());
      fv.setModels(Arrays.asList(vehicle.getModel_name().split(", ")));
      results.add(fv);
    }
    return results;
  }

  @RequestMapping(value = "/documents/store", method = RequestMethod.POST, consumes = "multipart/form-data")
  public String handleFileUpload(@RequestPart("file") MultipartFile file, @RequestPart("info") Info info) {

    DocumentWithTextPages doc = new DocumentWithTextPages();
    doc.setName(file.getOriginalFilename());
    doc.setVendor(info.getVendor());
    doc.setType(info.getType());
    doc.setModel(info.getModel());
    doc.setYear(info.getYear());
    doc.setOptions(info.getOptions());


    DocumentWithTextPages saved = documentWithTextPagesRepository.save(doc);
    System.out.println(saved);
    String id = saved.getId();

    String result = "upload error";
    if (fileService.store(file, id)) {
      result = file.getOriginalFilename()+" upload successful";
    }
    return "{\"status\":\"" +result+"\"}";

  }




}
