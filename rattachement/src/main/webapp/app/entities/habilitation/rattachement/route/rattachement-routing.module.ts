import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RattachementComponent } from '../list/rattachement.component';
import { RattachementDetailComponent } from '../detail/rattachement-detail.component';
import { RattachementUpdateComponent } from '../update/rattachement-update.component';
import { RattachementRoutingResolveService } from './rattachement-routing-resolve.service';

const rattachementRoute: Routes = [
  {
    path: '',
    component: RattachementComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RattachementDetailComponent,
    resolve: {
      rattachement: RattachementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RattachementUpdateComponent,
    resolve: {
      rattachement: RattachementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RattachementUpdateComponent,
    resolve: {
      rattachement: RattachementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rattachementRoute)],
  exports: [RouterModule],
})
export class RattachementRoutingModule {}
