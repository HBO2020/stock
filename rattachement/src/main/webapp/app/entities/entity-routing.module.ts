import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'habilitation',
        data: { pageTitle: 'rattachementApp.habilitationHabilitation.home.title' },
        loadChildren: () => import('./habilitation/habilitation/habilitation.module').then(m => m.HabilitationHabilitationModule),
      },
      {
        path: 'fonction',
        data: { pageTitle: 'rattachementApp.habilitationFonction.home.title' },
        loadChildren: () => import('./habilitation/fonction/fonction.module').then(m => m.HabilitationFonctionModule),
      },
      {
        path: 'rattachement',
        data: { pageTitle: 'rattachementApp.habilitationRattachement.home.title' },
        loadChildren: () => import('./habilitation/rattachement/rattachement.module').then(m => m.HabilitationRattachementModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
