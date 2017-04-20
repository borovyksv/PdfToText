import {Component} from '@angular/core';
import {DocumentService} from './documents.service'
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [DocumentService]
})
export class AppComponent {
  title = 'app works!';
  documents: Document[];

  constructor(private documentService:DocumentService) {
    this.documentService.getDocuments().subscribe(documents => {
      this.documents = documents;
    })
  }
}

export class Document{
  docName: string;
  id: number;
  imagesProgress: number;
  pagesProgress: number;
  textProgress: number;
}
