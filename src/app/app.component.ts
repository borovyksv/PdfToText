import {Component} from '@angular/core';
import {DocumentService} from './documents.service'
import {Observable} from 'rxjs/Rx';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [DocumentService]
})
export class AppComponent {
  documents: Document[];
  converted: Converted[];

  constructor(private documentService:DocumentService) {
    Observable.interval(2000).subscribe(x => {
      this.documentService.getDocumentsProgress().subscribe(documents => {
        return this.documents = documents;
      });
      this.documentService.getConvertedDocuments().subscribe(converted => {
        return this.converted = converted;
      });

    })
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
