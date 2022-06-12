import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHabilitation, Habilitation } from '../habilitation.model';
import { HabilitationService } from '../service/habilitation.service';

@Injectable({ providedIn: 'root' })
export class HabilitationRoutingResolveService implements Resolve<IHabilitation> {
  constructor(protected service: HabilitationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHabilitation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((habilitation: HttpResponse<Habilitation>) => {
          if (habilitation.body) {
            return of(habilitation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Habilitation());
  }
}
