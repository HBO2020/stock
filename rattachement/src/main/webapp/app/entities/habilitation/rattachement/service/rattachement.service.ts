import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRattachement, getRattachementIdentifier } from '../rattachement.model';

export type EntityResponseType = HttpResponse<IRattachement>;
export type EntityArrayResponseType = HttpResponse<IRattachement[]>;

@Injectable({ providedIn: 'root' })
export class RattachementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rattachements', 'habilitation');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(rattachement: IRattachement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rattachement);
    return this.http
      .post<IRattachement>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(rattachement: IRattachement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rattachement);
    return this.http
      .put<IRattachement>(`${this.resourceUrl}/${getRattachementIdentifier(rattachement) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(rattachement: IRattachement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rattachement);
    return this.http
      .patch<IRattachement>(`${this.resourceUrl}/${getRattachementIdentifier(rattachement) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRattachement>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRattachement[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRattachementToCollectionIfMissing(
    rattachementCollection: IRattachement[],
    ...rattachementsToCheck: (IRattachement | null | undefined)[]
  ): IRattachement[] {
    const rattachements: IRattachement[] = rattachementsToCheck.filter(isPresent);
    if (rattachements.length > 0) {
      const rattachementCollectionIdentifiers = rattachementCollection.map(
        rattachementItem => getRattachementIdentifier(rattachementItem)!
      );
      const rattachementsToAdd = rattachements.filter(rattachementItem => {
        const rattachementIdentifier = getRattachementIdentifier(rattachementItem);
        if (rattachementIdentifier == null || rattachementCollectionIdentifiers.includes(rattachementIdentifier)) {
          return false;
        }
        rattachementCollectionIdentifiers.push(rattachementIdentifier);
        return true;
      });
      return [...rattachementsToAdd, ...rattachementCollection];
    }
    return rattachementCollection;
  }

  protected convertDateFromClient(rattachement: IRattachement): IRattachement {
    return Object.assign({}, rattachement, {
      dateCreation: rattachement.dateCreation?.isValid() ? rattachement.dateCreation.toJSON() : undefined,
      dateMaj: rattachement.dateMaj?.isValid() ? rattachement.dateMaj.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateCreation = res.body.dateCreation ? dayjs(res.body.dateCreation) : undefined;
      res.body.dateMaj = res.body.dateMaj ? dayjs(res.body.dateMaj) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((rattachement: IRattachement) => {
        rattachement.dateCreation = rattachement.dateCreation ? dayjs(rattachement.dateCreation) : undefined;
        rattachement.dateMaj = rattachement.dateMaj ? dayjs(rattachement.dateMaj) : undefined;
      });
    }
    return res;
  }
}
