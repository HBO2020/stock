import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Status } from 'app/entities/enumerations/status.model';
import { IRattachement, Rattachement } from '../rattachement.model';

import { RattachementService } from './rattachement.service';

describe('Rattachement Service', () => {
  let service: RattachementService;
  let httpMock: HttpTestingController;
  let elemDefault: IRattachement;
  let expectedResult: IRattachement | IRattachement[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RattachementService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      idDemande: 'AAAAAAA',
      compte: 'AAAAAAA',
      status: Status.DEMANDE,
      descriptionRole: 'AAAAAAA',
      dateCreation: currentDate,
      dateMaj: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateCreation: currentDate.format(DATE_TIME_FORMAT),
          dateMaj: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Rattachement', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateCreation: currentDate.format(DATE_TIME_FORMAT),
          dateMaj: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateCreation: currentDate,
          dateMaj: currentDate,
        },
        returnedFromService
      );

      service.create(new Rattachement()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Rattachement', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idDemande: 'BBBBBB',
          compte: 'BBBBBB',
          status: 'BBBBBB',
          descriptionRole: 'BBBBBB',
          dateCreation: currentDate.format(DATE_TIME_FORMAT),
          dateMaj: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateCreation: currentDate,
          dateMaj: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Rattachement', () => {
      const patchObject = Object.assign(
        {
          compte: 'BBBBBB',
          dateMaj: currentDate.format(DATE_TIME_FORMAT),
        },
        new Rattachement()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateCreation: currentDate,
          dateMaj: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Rattachement', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idDemande: 'BBBBBB',
          compte: 'BBBBBB',
          status: 'BBBBBB',
          descriptionRole: 'BBBBBB',
          dateCreation: currentDate.format(DATE_TIME_FORMAT),
          dateMaj: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateCreation: currentDate,
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

    it('should delete a Rattachement', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRattachementToCollectionIfMissing', () => {
      it('should add a Rattachement to an empty array', () => {
        const rattachement: IRattachement = { id: 123 };
        expectedResult = service.addRattachementToCollectionIfMissing([], rattachement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rattachement);
      });

      it('should not add a Rattachement to an array that contains it', () => {
        const rattachement: IRattachement = { id: 123 };
        const rattachementCollection: IRattachement[] = [
          {
            ...rattachement,
          },
          { id: 456 },
        ];
        expectedResult = service.addRattachementToCollectionIfMissing(rattachementCollection, rattachement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Rattachement to an array that doesn't contain it", () => {
        const rattachement: IRattachement = { id: 123 };
        const rattachementCollection: IRattachement[] = [{ id: 456 }];
        expectedResult = service.addRattachementToCollectionIfMissing(rattachementCollection, rattachement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rattachement);
      });

      it('should add only unique Rattachement to an array', () => {
        const rattachementArray: IRattachement[] = [{ id: 123 }, { id: 456 }, { id: 36992 }];
        const rattachementCollection: IRattachement[] = [{ id: 123 }];
        expectedResult = service.addRattachementToCollectionIfMissing(rattachementCollection, ...rattachementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rattachement: IRattachement = { id: 123 };
        const rattachement2: IRattachement = { id: 456 };
        expectedResult = service.addRattachementToCollectionIfMissing([], rattachement, rattachement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rattachement);
        expect(expectedResult).toContain(rattachement2);
      });

      it('should accept null and undefined values', () => {
        const rattachement: IRattachement = { id: 123 };
        expectedResult = service.addRattachementToCollectionIfMissing([], null, rattachement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rattachement);
      });

      it('should return initial array if no Rattachement is added', () => {
        const rattachementCollection: IRattachement[] = [{ id: 123 }];
        expectedResult = service.addRattachementToCollectionIfMissing(rattachementCollection, undefined, null);
        expect(expectedResult).toEqual(rattachementCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
