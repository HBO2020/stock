import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HabilitationComponent } from '../list/habilitation.component';
import { HabilitationDetailComponent } from '../detail/habilitation-detail.component';
import { HabilitationUpdateComponent } from '../update/habilitation-update.component';
import { HabilitationRoutingResolveService } from './habilitation-routing-resolve.service';

const habilitationRoute: Routes = [
  {
    path: '',
    component: HabilitationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HabilitationDetailComponent,
    resolve: {
      habilitation: HabilitationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HabilitationUpdateComponent,
    resolve: {
      habilitation: HabilitationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HabilitationUpdateComponent,
    resolve: {
      habilitation: HabilitationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(habilitationRoute)],
  exports: [RouterModule],
})
export class HabilitationRoutingModule {}
