import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { HabilitationComponent } from './list/habilitation.component';
import { HabilitationDetailComponent } from './detail/habilitation-detail.component';
import { HabilitationUpdateComponent } from './update/habilitation-update.component';
import { HabilitationDeleteDialogComponent } from './delete/habilitation-delete-dialog.component';
import { HabilitationRoutingModule } from './route/habilitation-routing.module';

@NgModule({
  imports: [SharedModule, HabilitationRoutingModule],
  declarations: [HabilitationComponent, HabilitationDetailComponent, HabilitationUpdateComponent, HabilitationDeleteDialogComponent],
  entryComponents: [HabilitationDeleteDialogComponent],
})
export class HabilitationHabilitationModule {}
