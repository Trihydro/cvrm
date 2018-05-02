import { CvptPage } from './app.po';

describe('cvpt App', function() {
  let page: CvptPage;

  beforeEach(() => {
    page = new CvptPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
