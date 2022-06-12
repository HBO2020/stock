import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RattachementService } from '../service/rattachement.service';
import { IRattachement, Rattachement } from '../rattachement.model';

import { RattachementUpdateComponent } from './rattachement-update.component';

describe('Rattachement Management Update Component', () => {
  let comp: RattachementUpdateComponent;
  let fixture: ComponentFixture<RattachementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rattachementService: RattachementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RattachementUpdateComponent],
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
      .overrideTemplate(RattachementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RattachementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rattachementService = TestBed.inject(RattachementService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const rattachement: IRattachement = { id: 456 };

      activatedRoute.data = of({ rattachement });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(rattachement));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Rattachement>>();
      const rattachement = { id: 123 };
      jest.spyOn(rattachementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rattachement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rattachement }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(rattachementService.update).toHaveBeenCalledWith(rattachement);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Rattachement>>();
      const rattachement = new Rattachement();
      jest.spyOn(rattachementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rattachement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rattachement }));
      saveSubject.complete();

      // THEN
      expect(rattachementService.create).toHaveBeenCalledWith(rattachement);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Rattachement>>();
      const rattachement = { id: 123 };
      jest.spyOn(rattachementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rattachement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rattachementService.update).toHaveBeenCalledWith(rattachement);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
