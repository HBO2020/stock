import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IRattachement, Rattachement } from '../rattachement.model';
import { RattachementService } from '../service/rattachement.service';
import { Status } from 'app/entities/enumerations/status.model';

@Component({
  selector: 'jhi-rattachement-update',
  templateUrl: './rattachement-update.component.html',
})
export class RattachementUpdateComponent implements OnInit {
  isSaving = false;
  statusValues = Object.keys(Status);

  editForm = this.fb.group({
    id: [],
    idDemande: [null, [Validators.required]],
    compte: [null, [Validators.required]],
    status: [null, [Validators.required]],
    descriptionRole: [],
    dateCreation: [],
    dateMaj: [],
  });

  constructor(protected rattachementService: RattachementService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rattachement }) => {
      if (rattachement.id === undefined) {
        const today = dayjs().startOf('day');
        rattachement.dateCreation = today;
        rattachement.dateMaj = today;
      }

      this.updateForm(rattachement);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rattachement = this.createFromForm();
    if (rattachement.id !== undefined) {
      this.subscribeToSaveResponse(this.rattachementService.update(rattachement));
    } else {
      this.subscribeToSaveResponse(this.rattachementService.create(rattachement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRattachement>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(rattachement: IRattachement): void {
    this.editForm.patchValue({
      id: rattachement.id,
      idDemande: rattachement.idDemande,
      compte: rattachement.compte,
      status: rattachement.status,
      descriptionRole: rattachement.descriptionRole,
      dateCreation: rattachement.dateCreation ? rattachement.dateCreation.format(DATE_TIME_FORMAT) : null,
      dateMaj: rattachement.dateMaj ? rattachement.dateMaj.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IRattachement {
    return {
      ...new Rattachement(),
      id: this.editForm.get(['id'])!.value,
      idDemande: this.editForm.get(['idDemande'])!.value,
      compte: this.editForm.get(['compte'])!.value,
      status: this.editForm.get(['status'])!.value,
      descriptionRole: this.editForm.get(['descriptionRole'])!.value,
      dateCreation: this.editForm.get(['dateCreation'])!.value
        ? dayjs(this.editForm.get(['dateCreation'])!.value, DATE_TIME_FORMAT)
        : undefined,
      dateMaj: this.editForm.get(['dateMaj'])!.value ? dayjs(this.editForm.get(['dateMaj'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
