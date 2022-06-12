import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRattachement } from '../rattachement.model';
import { RattachementService } from '../service/rattachement.service';

@Component({
  templateUrl: './rattachement-delete-dialog.component.html',
})
export class RattachementDeleteDialogComponent {
  rattachement?: IRattachement;

  constructor(protected rattachementService: RattachementService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rattachementService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
