import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRattachement, Rattachement } from '../rattachement.model';
import { RattachementService } from '../service/rattachement.service';

@Injectable({ providedIn: 'root' })
export class RattachementRoutingResolveService implements Resolve<IRattachement> {
  constructor(protected service: RattachementService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRattachement> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((rattachement: HttpResponse<Rattachement>) => {
          if (rattachement.body) {
            return of(rattachement.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Rattachement());
  }
}
