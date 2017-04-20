import {Component} from '@angular/core';
import {PostsService} from './posts.service'
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [PostsService]
})
export class AppComponent {
  title = 'app works!';
  page: Page;

  constructor(private postsService:PostsService) {
    this.postsService.getPage().subscribe(page => {
      console.log(page);
      this.page = page;
    })
  }
}

export class Page{
  id: number;
  text: string;
}
