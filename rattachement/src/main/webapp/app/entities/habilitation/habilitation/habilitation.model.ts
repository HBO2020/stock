import dayjs from 'dayjs/esm';
import { IFonction } from 'app/entities/habilitation/fonction/fonction.model';

export interface IHabilitation {
  id?: number;
  compte?: string;
  entreprise?: number | null;
  dateMaj?: dayjs.Dayjs | null;
  fonction?: IFonction;
}

export class Habilitation implements IHabilitation {
  constructor(
    public id?: number,
    public compte?: string,
    public entreprise?: number | null,
    public dateMaj?: dayjs.Dayjs | null,
    public fonction?: IFonction
  ) {}
}

export function getHabilitationIdentifier(habilitation: IHabilitation): number | undefined {
  return habilitation.id;
}
