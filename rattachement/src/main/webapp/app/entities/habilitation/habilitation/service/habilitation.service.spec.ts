import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IHabilitation, Habilitation } from '../habilitation.model';

import { HabilitationService } from './habilitation.service';

describe('Habilitation Service', () => {
  let service: HabilitationService;
  let httpMock: HttpTestingController;
  let elemDefault: IHabilitation;
  let expectedResult: IHabilitation | IHabilitation[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HabilitationService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      compte: 'AAAAAAA',
      entreprise: 0,
      dateMaj: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateMaj: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Habilitation', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateMaj: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateMaj: currentDate,
        },
        returnedFromService
      );

      service.create(new Habilitation()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Habilitation', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          compte: 'BBBBBB',
          entreprise: 1,
          dateMaj: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateMaj: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Habilitation', () => {
      const patchObject = Object.assign(
        {
          compte: 'BBBBBB',
          entreprise: 1,
          dateMaj: currentDate.format(DATE_TIME_FORMAT),
        },
        new Habilitation()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateMaj: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Habilitation', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          compte: 'BBBBBB',
          entreprise: 1,
          dateMaj: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateMaj: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Habilitation', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addHabilitationToCollectionIfMissing', () => {
      it('should add a Habilitation to an empty array', () => {
        const habilitation: IHabilitation = { id: 123 };
        expectedResult = service.addHabilitationToCollectionIfMissing([], habilitation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(habilitation);
      });

      it('should not add a Habilitation to an array that contains it', () => {
        const habilitation: IHabilitation = { id: 123 };
        const habilitationCollection: IHabilitation[] = [
          {
            ...habilitation,
          },
          { id: 456 },
        ];
        expectedResult = service.addHabilitationToCollectionIfMissing(habilitationCollection, habilitation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Habilitation to an array that doesn't contain it", () => {
        const habilitation: IHabilitation = { id: 123 };
        const habilitationCollection: IHabilitation[] = [{ id: 456 }];
        expectedResult = service.addHabilitationToCollectionIfMissing(habilitationCollection, habilitation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(habilitation);
      });

      it('should add only unique Habilitation to an array', () => {
        const habilitationArray: IHabilitation[] = [{ id: 123 }, { id: 456 }, { id: 14832 }];
        const habilitationCollection: IHabilitation[] = [{ id: 123 }];
        expectedResult = service.addHabilitationToCollectionIfMissing(habilitationCollection, ...habilitationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const habilitation: IHabilitation = { id: 123 };
        const habilitation2: IHabilitation = { id: 456 };
        expectedResult = service.addHabilitationToCollectionIfMissing([], habilitation, habilitation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(habilitation);
        expect(expectedResult).toContain(habilitation2);
      });

      it('should accept null and undefined values', () => {
        const habilitation: IHabilitation = { id: 123 };
        expectedResult = service.addHabilitationToCollectionIfMissing([], null, habilitation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(habilitation);
      });

      it('should return initial array if no Habilitation is added', () => {
        const habilitationCollection: IHabilitation[] = [{ id: 123 }];
        expectedResult = service.addHabilitationToCollectionIfMissing(habilitationCollection, undefined, null);
        expect(expectedResult).toEqual(habilitationCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
