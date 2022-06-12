import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { HabilitationService } from '../service/habilitation.service';
import { IHabilitation, Habilitation } from '../habilitation.model';
import { IFonction } from 'app/entities/habilitation/fonction/fonction.model';
import { FonctionService } from 'app/entities/habilitation/fonction/service/fonction.service';

import { HabilitationUpdateComponent } from './habilitation-update.component';

describe('Habilitation Management Update Component', () => {
  let comp: HabilitationUpdateComponent;
  let fixture: ComponentFixture<HabilitationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let habilitationService: HabilitationService;
  let fonctionService: FonctionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [HabilitationUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(HabilitationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HabilitationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    habilitationService = TestBed.inject(HabilitationService);
    fonctionService = TestBed.inject(FonctionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Fonction query and add missing value', () => {
      const habilitation: IHabilitation = { id: 456 };
      const fonction: IFonction = { id: 56798 };
      habilitation.fonction = fonction;

      const fonctionCollection: IFonction[] = [{ id: 52102 }];
      jest.spyOn(fonctionService, 'query').mockReturnValue(of(new HttpResponse({ body: fonctionCollection })));
      const additionalFonctions = [fonction];
      const expectedCollection: IFonction[] = [...additionalFonctions, ...fonctionCollection];
      jest.spyOn(fonctionService, 'addFonctionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ habilitation });
      comp.ngOnInit();

      expect(fonctionService.query).toHaveBeenCalled();
      expect(fonctionService.addFonctionToCollectionIfMissing).toHaveBeenCalledWith(fonctionCollection, ...additionalFonctions);
      expect(comp.fonctionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const habilitation: IHabilitation = { id: 456 };
      const fonction: IFonction = { id: 55635 };
      habilitation.fonction = fonction;

      activatedRoute.data = of({ habilitation });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(habilitation));
      expect(comp.fonctionsSharedCollection).toContain(fonction);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Habilitation>>();
      const habilitation = { id: 123 };
      jest.spyOn(habilitationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ habilitation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: habilitation }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(habilitationService.update).toHaveBeenCalledWith(habilitation);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Habilitation>>();
      const habilitation = new Habilitation();
      jest.spyOn(habilitationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ habilitation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: habilitation }));
      saveSubject.complete();

      // THEN
      expect(habilitationService.create).toHaveBeenCalledWith(habilitation);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Habilitation>>();
      const habilitation = { id: 123 };
      jest.spyOn(habilitationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ habilitation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(habilitationService.update).toHaveBeenCalledWith(habilitation);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackFonctionById', () => {
      it('Should return tracked Fonction primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFonctionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
