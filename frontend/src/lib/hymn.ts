import type { CreateOrUpdateHymnPayload } from "../domain/Hymn.ts";

export const isHymnValid = (formData: CreateOrUpdateHymnPayload): boolean => {
  // includes 0 and undefined
  if (!formData.authorId) return false;
  if (formData.authorExtras && formData.authorExtras.length > 200) return false;
  if (!formData.title || formData.title.length < 3 || formData.title.length > 100) return false;
  if (!formData.lyrics || formData.lyrics.length < 3 || formData.lyrics.length > 21000) return false;
  if (formData.hymnBookId && formData.hymnBookId < 1) return false;
  if (formData.numberInHymnBook && formData.numberInHymnBook < 0) return false;
  if (formData.topicId && formData.topicId < 1) return false;
  if (formData.labelId && formData.labelId < 1) return false;
  return true;
};
