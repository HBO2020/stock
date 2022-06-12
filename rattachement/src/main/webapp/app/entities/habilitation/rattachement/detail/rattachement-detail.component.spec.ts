import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RattachementDetailComponent } from './rattachement-detail.component';

describe('Rattachement Management Detail Component', () => {
  let comp: RattachementDetailComponent;
  let fixture: ComponentFixture<RattachementDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RattachementDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ rattachement: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RattachementDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RattachementDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load rattachement on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.rattachement).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
