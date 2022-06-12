import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IFonction, Fonction } from '../fonction.model';
import { FonctionService } from '../service/fonction.service';

@Component({
  selector: 'jhi-fonction-update',
  templateUrl: './fonction-update.component.html',
})
export class FonctionUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    idFonction: [null, [Validators.required]],
    libelle: [],
    description: [],
    pictogramme: [],
  });

  constructor(protected fonctionService: FonctionService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fonction }) => {
      this.updateForm(fonction);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fonction = this.createFromForm();
    if (fonction.id !== undefined) {
      this.subscribeToSaveResponse(this.fonctionService.update(fonction));
    } else {
      this.subscribeToSaveResponse(this.fonctionService.create(fonction));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFonction>>): void {
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

  protected updateForm(fonction: IFonction): void {
    this.editForm.patchValue({
      id: fonction.id,
      idFonction: fonction.idFonction,
      libelle: fonction.libelle,
      description: fonction.description,
      pictogramme: fonction.pictogramme,
    });
  }

  protected createFromForm(): IFonction {
    return {
      ...new Fonction(),
      id: this.editForm.get(['id'])!.value,
      idFonction: this.editForm.get(['idFonction'])!.value,
      libelle: this.editForm.get(['libelle'])!.value,
      description: this.editForm.get(['description'])!.value,
      pictogramme: this.editForm.get(['pictogramme'])!.value,
    };
  }
}
