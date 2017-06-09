import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';
import {Vehicle} from "./app.component";

@Injectable()
export class DocumentService{

  constructor(private http: Http){
    console.log('Document service initialized...')
  }

  getDocumentsProgress(){
    return this.http.get('http://localhost:8080/documents/progress')
      .map((res:Response)=>res.json());
  }

  getConvertedDocuments(){
    return this.http.get('http://localhost:8080/documents/converted')
      .map((res:Response)=>res.json());
  }

  getVehicles(): Promise<Vehicle[]>{
    return this.http.get('http://localhost:8080/vehicles')
      .toPromise()
      .then(response=>response.json() as Vehicle[])
      .catch(this.handleError);
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }



}


