import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IHabilitation } from '../habilitation.model';
import { HabilitationService } from '../service/habilitation.service';

@Component({
  templateUrl: './habilitation-delete-dialog.component.html',
})
export class HabilitationDeleteDialogComponent {
  habilitation?: IHabilitation;

  constructor(protected habilitationService: HabilitationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.habilitationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
