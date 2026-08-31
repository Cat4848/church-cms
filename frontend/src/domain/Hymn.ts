export interface Hymn {
  hymnId: number;
  authorId: number;
  authorExtras?: string;
  title: string;
  lyrics: string;
  hymnBookId?: number;
  numberInHymnBook?: number;
  topicId?: number;
  labelId?: number;
}

export interface CreateOrUpdateHymnPayload {
  authorId: number;
  authorExtras?: string;
  title: string;
  lyrics: string;
  hymnBookId?: number;
  numberInHymnBook?: number;
  topicId?: number;
  labelId?: number;
}
