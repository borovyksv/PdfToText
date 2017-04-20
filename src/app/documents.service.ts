import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import 'rxjs/add/operator/map';

@Injectable()
export class DocumentService{

  constructor(private http: Http){
    console.log('Document service initialized...')
  }

  getDocuments(){
    return this.http.get('http://localhost:8080/documents')
      .map((res:Response)=>res.json());
  }

}


