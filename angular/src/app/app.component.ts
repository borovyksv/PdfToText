import {Component, OnInit} from '@angular/core';
import {DocumentService} from './documents.service'
import {Observable} from 'rxjs/Observable';
import {Http, RequestOptions, Headers} from "@angular/http";
import 'rxjs/add/observable/interval';
import 'rxjs/add/operator/toPromise';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [DocumentService]
})
export class AppComponent implements OnInit {

  // Form
  options = '';
  years = [2018];
  selectedYear:number;

  changeSelectedYear(value:number) {
    this.selectedYear = value;
  }

  types = ['owner manual',
    'repair',
    'miscellaneous'];
  selectedType = '';

  changeSelectedType(value:string) {
    this.selectedType = value;
  }


  // autocomplete
  // - vehicle
  vehicleQuery = '';
  vehicleFilteredList = [];
  vehicles:Vehicle[];


  filterVehicles() {
    if (this.vehicleQuery !== "") {
      this.vehicleFilteredList = this.vehicles.filter(function (el) {
        return el.vendor.toLowerCase().indexOf(this.vehicleQuery.toLowerCase()) > -1;
      }.bind(this));
    } else {
      this.vehicleFilteredList = [];
    }
  }

  selectVehicle(item) {
    this.vehicleQuery = item;
    this.vehicleFilteredList = [];
    this.changeSelectedVehicle(item);
  }

  // - model
  modelQuery = '';
  modelFilteredList = [];
  selectedVehicle:Vehicle = new Vehicle("", [""]);
  models = [];


  filterModels() {
    if (this.modelQuery !== "") {
      this.modelFilteredList = this.models.filter(function (el) {
        return el.toLowerCase().indexOf(this.modelQuery.toLowerCase()) > -1;
      }.bind(this));
    } else {
      this.modelFilteredList = [];
    }
  }

  selectModel(item) {
    this.modelQuery = item;
    this.modelFilteredList = [];
  }

  changeSelectedVehicle(value:string) {
    for (let i in this.vehicles) {
      if (this.vehicles[i].vendor == value) {
        this.selectedVehicle = this.vehicles[i];
        this.models = this.vehicles[i].models;
        break;
      }
    }
  }


  // end autocomplete

  documents:Document[];
  converted:Converted[];

  // upload a file
  fileList:FileList;

  fileChange(event) {
    this.fileList = event.target.files;
  }

  // get diagnostic() { return JSON.stringify(this.fileList); }

  uploadPdf() {
    console.log("upload PDF")
    if (this.fileList.length > 0) {
      let file:File = this.fileList[0];
      let formData:FormData = new FormData();
      let info:Form = new Form(
        this.vehicleQuery,
        [this.modelQuery],
        [this.selectedYear],
        this.selectedType,
        [this.options]);

      // todo:remove
      console.log(info);

      formData.append('file', file, file.name);
      formData.append('info', new Blob([JSON.stringify(info)],
        {
          type: "application/json"
        }));

      let headers = new Headers();
      headers.append('Accept', 'application/json');
      let options = new RequestOptions({headers: headers});
      this.http.post("http://localhost:8080/documents/store", formData, options)
        .toPromise()
        .then(res => console.log(res.json()))
        .catch(error => console.log(error));
    }
    this.vehicleQuery = '';
    this.modelQuery = '';
    this.selectedYear = null;
    this.selectedType = '';
    this.options = '';
    this.fileList = null;
  }

  // end upload a file


  constructor(private http:Http, private documentService:DocumentService) {
    //noinspection TypeScriptUnresolvedFunction
    Observable.interval(2000).subscribe(x => {
      this.documentService.getDocumentsProgress().subscribe(documents => {
        return this.documents = documents;
      });
      this.documentService.getConvertedDocuments().subscribe(converted => {
        return this.converted = converted;
      });

    })
  }

  ngOnInit():void {
    this.documentService.getVehicles().then(vehicles => {
      this.vehicles = vehicles;
      this.selectedVehicle = vehicles[0];
    });
    this.initYears();
  }

  initYears():void {
    for (let i = this.years[0] - 1; i >= 1800; i--) {
      this.years.push(i);
    }
  }

}

export class Form {
  vendor:string;
  model:string[];
  year:number[];
  type:string;
  options:string[];

  constructor(vendor:string, model:string[], year:number[], type:string, options:string[]) {
    this.vendor = vendor;
    this.model = model;
    this.year = year;
    this.type = type;
    this.options = options;
  }
}

export class Vehicle {
  vendor:string;
  models:string[];

  constructor(vendor:string, models:string[]) {
    this.vendor = vendor;
    this.models = models;
  }
}

export class Document {
  docName:string;
  id:number;
  imagesProgress:number;
  pagesProgress:number;
  textProgress:number;
  errors:Array<string>;
}

export class Converted {
  id:string;
  name:string;
  numberOfPages:number
}
