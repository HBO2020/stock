import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RattachementComponent } from './list/rattachement.component';
import { RattachementDetailComponent } from './detail/rattachement-detail.component';
import { RattachementUpdateComponent } from './update/rattachement-update.component';
import { RattachementDeleteDialogComponent } from './delete/rattachement-delete-dialog.component';
import { RattachementRoutingModule } from './route/rattachement-routing.module';

@NgModule({
  imports: [SharedModule, RattachementRoutingModule],
  declarations: [RattachementComponent, RattachementDetailComponent, RattachementUpdateComponent, RattachementDeleteDialogComponent],
  entryComponents: [RattachementDeleteDialogComponent],
})
export class HabilitationRattachementModule {}
