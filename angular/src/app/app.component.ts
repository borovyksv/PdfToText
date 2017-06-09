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
  documents: Document[];
  converted: Converted[];

  // Form
  vehicles: Vehicle[];

  selectedVehicle:Vehicle=new Vehicle("",[""]);


  submitted = false;

  onSubmit() { this.submitted = true; }

  changeSelectedVehicle(value:string) {
      for (let i in this.vehicles) {
        if (this.vehicles[i].vendor == value) {
          this.selectedVehicle = this.vehicles[i];
          break;
        }
      }
  }

  get diagnostic() { return JSON.stringify(this.selectedVehicle); }
  // end Form

  // upload a file
  fileList:FileList;

  fileChange(event) {
    this.fileList = event.target.files;
  }
  uploadPdf() {
    console.log("upload PDF")
    if (this.fileList.length > 0) {
      let file:File = this.fileList[0];
      let formData:FormData = new FormData();

      formData.append('file', file, file.name);
      formData.append('info', new Blob([JSON.stringify(this.selectedVehicle)],
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

export class Document{
  docName: string;
  id: number;
  imagesProgress: number;
  pagesProgress: number;
  textProgress: number;
  errors: Array<string>;
}

export class Converted{
  id: string;
  name: string;
  numberOfPages: number
}
