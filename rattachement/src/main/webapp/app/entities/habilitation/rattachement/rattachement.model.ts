import dayjs from 'dayjs/esm';
import { Status } from 'app/entities/enumerations/status.model';

export interface IRattachement {
  id?: number;
  idDemande?: string;
  compte?: string;
  status?: Status;
  descriptionRole?: string | null;
  dateCreation?: dayjs.Dayjs | null;
  dateMaj?: dayjs.Dayjs | null;
}

export class Rattachement implements IRattachement {
  constructor(
    public id?: number,
    public idDemande?: string,
    public compte?: string,
    public status?: Status,
    public descriptionRole?: string | null,
    public dateCreation?: dayjs.Dayjs | null,
    public dateMaj?: dayjs.Dayjs | null
  ) {}
}

export function getRattachementIdentifier(rattachement: IRattachement): number | undefined {
  return rattachement.id;
}
