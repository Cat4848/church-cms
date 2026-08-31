import globalStyles from "../../css/global.module.css";
import MandatoryField from "../MandatoryField/MandatoryField.tsx";
import styles from "./CreateOrUpdateHymnForm.module.css";
import { type CreateOrUpdateHymnPayload } from "../../domain/Hymn.ts";
import type { Author } from "../../domain/Author.ts";
import type { HymnBook } from "../../domain/HymnBook.ts";
import type { Topic } from "../../domain/Topic.ts";
import type { Label } from "../../domain/Label.ts";
import type { Dispatch, SetStateAction } from "react";

interface Props {
  hymnId?: number;
  formData: CreateOrUpdateHymnPayload;
  setFormData: (
    key: keyof CreateOrUpdateHymnPayload,
    value: CreateOrUpdateHymnPayload[keyof CreateOrUpdateHymnPayload],
  ) => void;
  setFormDataRaw: Dispatch<SetStateAction<CreateOrUpdateHymnPayload>>;
  authors: Author[];
  hymnBooks: HymnBook[];
  topics: Topic[];
  labels: Label[];
}

export enum FormHelpers {
  Reset = "Reset",
}

const CreateOrUpdateHymnForm = (props: Props) => {
  return (
    <>
      <div className={globalStyles["input-group-grid-two-columns"]}>
        <div className={globalStyles["input-group"]}>
          <label htmlFor="author-id">
            <MandatoryField /> Author
          </label>
          <select id="author-id" onChange={(e) => props.setFormData("authorId", Number(e.target.value))}>
            {props.authors.map((author) => (
              <option
                key={author.authorId}
                value={author.authorId}
                selected={author.authorId === props.formData.authorId}
              >
                {author.name}
              </option>
            ))}
          </select>
        </div>

        <div className={`${globalStyles["input-group"]} ${styles["align-right"]}`}>
          <div className={`${globalStyles["input-group"]}`}>
            <label htmlFor="author-extras">Author Extras</label>
            <input
              id="author-extras"
              value={props.formData.authorExtras}
              onChange={(e) => {
                if (e.target.value === "") {
                  props.setFormDataRaw({ ...props.formData, authorExtras: undefined });
                } else {
                  props.setFormData("authorExtras", e.target.value);
                }
              }}
            />
          </div>
        </div>
      </div>

      <div className={globalStyles["input-group-grid-two-columns"]}>
        <div className={globalStyles["input-group"]}>
          <label htmlFor="hymn-book">Hymn Book</label>
          <select
            id="hymn-book"
            onChange={(e) => {
              if (e.target.value === FormHelpers.Reset) {
                props.setFormDataRaw({ ...props.formData, hymnBookId: undefined });
              } else {
                props.setFormData("hymnBookId", Number(e.target.value));
              }
            }}
          >
            <option key="select-value-hymn-book" value={FormHelpers.Reset}>
              -- Select --
            </option>
            {props.hymnBooks.map((hymnBook) => (
              <option
                key={hymnBook.hymnBookId}
                selected={hymnBook.hymnBookId === props.formData.hymnBookId}
                value={hymnBook.hymnBookId}
              >
                {hymnBook.name}
              </option>
            ))}
          </select>
        </div>

        <div className={`${globalStyles["input-group"]} ${styles["align-right"]}`}>
          <div className={globalStyles["input-group"]}>
            <label id="hymn-title">
              <MandatoryField /> Title
            </label>
            <input
              id="hymn-title"
              value={props.formData.title}
              onChange={(e) => props.setFormData("title", e.target.value)}
            />
          </div>
        </div>
      </div>

      <div className={globalStyles["input-group-grid-two-columns"]}>
        <div className={globalStyles["input-group"]}>
          <label htmlFor="topic">Topic</label>
          <select
            id="topic"
            onChange={(e) => {
              if (e.target.value === FormHelpers.Reset) {
                props.setFormDataRaw({ ...props.formData, topicId: undefined });
              } else {
                props.setFormData("topicId", Number(e.target.value));
              }
            }}
          >
            <option key="select-value-topic" value={FormHelpers.Reset}>
              -- Select --
            </option>
            {props.topics.map((topic) => (
              <option key={topic.topicId} value={topic.topicId} selected={topic.topicId === props.formData.topicId}>
                {topic.name}
              </option>
            ))}
          </select>
        </div>

        <div className={`${globalStyles["input-group"]} ${styles["align-right"]}`}>
          <div className={globalStyles["input-group"]}>
            <label htmlFor="number-in-hymn-book">Number in Hymn Book</label>
            <input
              id="number-in-hymn-book"
              type="number"
              step="1"
              min="0"
              value={props.formData.numberInHymnBook ?? ""}
              onChange={(e) => {
                if (Number(e.target.value) === 0) {
                  props.setFormDataRaw({ ...props.formData, numberInHymnBook: undefined });
                } else {
                  props.setFormData("numberInHymnBook", Number(e.target.value));
                }
              }}
            />
          </div>
        </div>
      </div>

      <div className={globalStyles["input-group-grid-two-columns"]}>
        <div className={globalStyles["input-group"]}>
          <label htmlFor="label">Label</label>
          <select
            id="label"
            onChange={(e) => {
              if (e.target.value === FormHelpers.Reset) {
                props.setFormDataRaw({ ...props.formData, labelId: undefined });
              } else {
                props.setFormData("labelId", Number(e.target.value));
              }
            }}
          >
            <option key="select-value-hymn-topic" value={FormHelpers.Reset}>
              -- Select --
            </option>
            {props.labels.map((label) => (
              <option key={label.labelId} value={label.labelId} selected={label.labelId === props.formData.labelId}>
                {label.name}
              </option>
            ))}
          </select>
        </div>
      </div>

      <div className={`${globalStyles["input-group"]} ${styles["lyrics-input-group"]}`}>
        <label htmlFor="hymn-lyrics">
          <MandatoryField /> Lyrics
        </label>
        <textarea
          id="hymn-lyrics"
          value={props.formData.lyrics}
          onChange={(e) => props.setFormData("lyrics", e.target.value)}
        />
      </div>
    </>
  );
};

export default CreateOrUpdateHymnForm;
