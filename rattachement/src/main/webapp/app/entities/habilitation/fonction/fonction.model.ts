export interface IFonction {
  id?: number;
  idFonction?: string;
  libelle?: string | null;
  description?: string | null;
  pictogramme?: string | null;
}

export class Fonction implements IFonction {
  constructor(
    public id?: number,
    public idFonction?: string,
    public libelle?: string | null,
    public description?: string | null,
    public pictogramme?: string | null
  ) {}
}

export function getFonctionIdentifier(fonction: IFonction): number | undefined {
  return fonction.id;
}
