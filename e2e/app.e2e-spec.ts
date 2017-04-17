import { PdfToTextPage } from './app.po';

describe('pdf-to-text App', function() {
  let page: PdfToTextPage;

  beforeEach(() => {
    page = new PdfToTextPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
