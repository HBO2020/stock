import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHabilitation, getHabilitationIdentifier } from '../habilitation.model';

export type EntityResponseType = HttpResponse<IHabilitation>;
export type EntityArrayResponseType = HttpResponse<IHabilitation[]>;

@Injectable({ providedIn: 'root' })
export class HabilitationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/habilitations', 'habilitation');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(habilitation: IHabilitation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(habilitation);
    return this.http
      .post<IHabilitation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(habilitation: IHabilitation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(habilitation);
    return this.http
      .put<IHabilitation>(`${this.resourceUrl}/${getHabilitationIdentifier(habilitation) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(habilitation: IHabilitation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(habilitation);
    return this.http
      .patch<IHabilitation>(`${this.resourceUrl}/${getHabilitationIdentifier(habilitation) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IHabilitation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IHabilitation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addHabilitationToCollectionIfMissing(
    habilitationCollection: IHabilitation[],
    ...habilitationsToCheck: (IHabilitation | null | undefined)[]
  ): IHabilitation[] {
    const habilitations: IHabilitation[] = habilitationsToCheck.filter(isPresent);
    if (habilitations.length > 0) {
      const habilitationCollectionIdentifiers = habilitationCollection.map(
        habilitationItem => getHabilitationIdentifier(habilitationItem)!
      );
      const habilitationsToAdd = habilitations.filter(habilitationItem => {
        const habilitationIdentifier = getHabilitationIdentifier(habilitationItem);
        if (habilitationIdentifier == null || habilitationCollectionIdentifiers.includes(habilitationIdentifier)) {
          return false;
        }
        habilitationCollectionIdentifiers.push(habilitationIdentifier);
        return true;
      });
      return [...habilitationsToAdd, ...habilitationCollection];
    }
    return habilitationCollection;
  }

  protected convertDateFromClient(habilitation: IHabilitation): IHabilitation {
    return Object.assign({}, habilitation, {
      dateMaj: habilitation.dateMaj?.isValid() ? habilitation.dateMaj.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateMaj = res.body.dateMaj ? dayjs(res.body.dateMaj) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((habilitation: IHabilitation) => {
        habilitation.dateMaj = habilitation.dateMaj ? dayjs(habilitation.dateMaj) : undefined;
      });
    }
    return res;
  }
}
