import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IHabilitation, Habilitation } from '../habilitation.model';
import { HabilitationService } from '../service/habilitation.service';
import { IFonction } from 'app/entities/habilitation/fonction/fonction.model';
import { FonctionService } from 'app/entities/habilitation/fonction/service/fonction.service';

@Component({
  selector: 'jhi-habilitation-update',
  templateUrl: './habilitation-update.component.html',
})
export class HabilitationUpdateComponent implements OnInit {
  isSaving = false;

  fonctionsSharedCollection: IFonction[] = [];

  editForm = this.fb.group({
    id: [],
    compte: [null, [Validators.required]],
    entreprise: [],
    dateMaj: [],
    fonction: [null, Validators.required],
  });

  constructor(
    protected habilitationService: HabilitationService,
    protected fonctionService: FonctionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ habilitation }) => {
      if (habilitation.id === undefined) {
        const today = dayjs().startOf('day');
        habilitation.dateMaj = today;
      }

      this.updateForm(habilitation);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const habilitation = this.createFromForm();
    if (habilitation.id !== undefined) {
      this.subscribeToSaveResponse(this.habilitationService.update(habilitation));
    } else {
      this.subscribeToSaveResponse(this.habilitationService.create(habilitation));
    }
  }

  trackFonctionById(_index: number, item: IFonction): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHabilitation>>): void {
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

  protected updateForm(habilitation: IHabilitation): void {
    this.editForm.patchValue({
      id: habilitation.id,
      compte: habilitation.compte,
      entreprise: habilitation.entreprise,
      dateMaj: habilitation.dateMaj ? habilitation.dateMaj.format(DATE_TIME_FORMAT) : null,
      fonction: habilitation.fonction,
    });

    this.fonctionsSharedCollection = this.fonctionService.addFonctionToCollectionIfMissing(
      this.fonctionsSharedCollection,
      habilitation.fonction
    );
  }

  protected loadRelationshipsOptions(): void {
    this.fonctionService
      .query()
      .pipe(map((res: HttpResponse<IFonction[]>) => res.body ?? []))
      .pipe(
        map((fonctions: IFonction[]) =>
          this.fonctionService.addFonctionToCollectionIfMissing(fonctions, this.editForm.get('fonction')!.value)
        )
      )
      .subscribe((fonctions: IFonction[]) => (this.fonctionsSharedCollection = fonctions));
  }

  protected createFromForm(): IHabilitation {
    return {
      ...new Habilitation(),
      id: this.editForm.get(['id'])!.value,
      compte: this.editForm.get(['compte'])!.value,
      entreprise: this.editForm.get(['entreprise'])!.value,
      dateMaj: this.editForm.get(['dateMaj'])!.value ? dayjs(this.editForm.get(['dateMaj'])!.value, DATE_TIME_FORMAT) : undefined,
      fonction: this.editForm.get(['fonction'])!.value,
    };
  }
}
