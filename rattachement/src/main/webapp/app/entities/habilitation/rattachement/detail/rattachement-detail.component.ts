import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRattachement } from '../rattachement.model';

@Component({
  selector: 'jhi-rattachement-detail',
  templateUrl: './rattachement-detail.component.html',
})
export class RattachementDetailComponent implements OnInit {
  rattachement: IRattachement | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rattachement }) => {
      this.rattachement = rattachement;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
