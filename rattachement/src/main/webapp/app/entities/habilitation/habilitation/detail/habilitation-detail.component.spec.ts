import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HabilitationDetailComponent } from './habilitation-detail.component';

describe('Habilitation Management Detail Component', () => {
  let comp: HabilitationDetailComponent;
  let fixture: ComponentFixture<HabilitationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HabilitationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ habilitation: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(HabilitationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(HabilitationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load habilitation on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.habilitation).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
