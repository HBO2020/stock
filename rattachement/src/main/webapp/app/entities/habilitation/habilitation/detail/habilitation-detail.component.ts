import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHabilitation } from '../habilitation.model';

@Component({
  selector: 'jhi-habilitation-detail',
  templateUrl: './habilitation-detail.component.html',
})
export class HabilitationDetailComponent implements OnInit {
  habilitation: IHabilitation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ habilitation }) => {
      this.habilitation = habilitation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
