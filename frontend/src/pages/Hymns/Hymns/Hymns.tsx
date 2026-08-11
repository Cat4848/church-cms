import { useGetAllHymnsQuery, useCreateHymnMutation, useUpdateHymnMutation } from "../../../store/api/hymnsApi.ts";
import Loading from "../../../components/Loading/Loading.tsx";
import { ErrorFallback } from "../../../components/ErrorBoundary/ErrorBoundary.tsx";
import NoContent from "../../../components/NoContent/NoContent.tsx";
import globalStyles from "../../../css/global.module.css";
import Hymn from "../Hymn/Hymn.tsx";
import { useGetAllAuthorsQuery } from "../../../store/api/authorsApi.ts";
import { useGetAllHymnBooksQuery } from "../../../store/api/hymnBooksApi.ts";
import { useGetAllTopicsQuery } from "../../../store/api/topicsApi.ts";
import { useGetAllLabelsQuery } from "../../../store/api/labelsApi.ts";
import type { Author } from "../../../domain/Author.ts";
import type { HymnBook } from "../../../domain/HymnBook.ts";
import type { Topic } from "../../../domain/Topic.ts";
import type { Label } from "../../../domain/Label.ts";
import SearchAndCreate from "../../../components/SearchAndCreate/SearchAndCreate.tsx";
import { useState } from "react";

const Hymns = () => {
  const { data: hymns, error: getAllHymnsError, isLoading } = useGetAllHymnsQuery();
  const { data: authors, isLoading: isAuthorsLoading, error: authorsError } = useGetAllAuthorsQuery();
  const { data: hymnBooks, isLoading: isHymnBooksError, error: hymnBooksError } = useGetAllHymnBooksQuery();
  const { data: topics, isLoading: isTopicsLoading, error: topicsError } = useGetAllTopicsQuery();
  const { data: labels, isLoading: isLabelsLoading, error: labelsError } = useGetAllLabelsQuery();

  const [searchHymnTerm, setSearchHymnTerm] = useState("");

  const [authorId, setAuthorId] = useState(0);
  const [authorExtras, setAuthorExtras] = useState("");
  const [hymnTitle, setHymnTitle] = useState("");
  const [hymnLyrics, setHymnLyrics] = useState("");
  const [hymnBookId, setHymnBookId] = useState(0);
  const [numberInHymnBook, setNumberInHymnBook] = useState(0);
  const [topicId, setTopicId] = useState(0);
  const [labelId, setLabelId] = useState(0);

  // todo: add loading for all entities
  if (isLoading || isAuthorsLoading || isHymnBooksError || isTopicsLoading || isLabelsLoading) {
    return <Loading />;
  }
  if (!hymns || getAllHymnsError) {
    return <ErrorFallback error={getAllHymnsError ? getAllHymnsError.toString() : "Error getting Hymns"} />;
  }
  if (!authors || authorsError) {
    return <ErrorFallback error={authorsError ? authorsError.toString() : "Error getting Authors"} />;
  }
  if (!hymnBooks || hymnBooksError) {
    return <ErrorFallback error={hymnBooksError ? hymnBooksError.toString() : "Error getting Hymn Books"} />;
  }
  if (!topics || topicsError) {
    return <ErrorFallback error={topicsError ? topicsError.toString() : "Error getting Topics"} />;
  }
  if (!labels || labelsError) {
    return <ErrorFallback error={labelsError ? labelsError.toString() : "Error getting Labels"} />;
  }
  // todo: add the errors for creating and updating + all other entities
  if (hymns && !hymns.length) {
    return <NoContent entity="Hymns" />;
  }

  if (authors && !authors.length) {
    return <NoContent entity="Authors" />;
  }
  if (hymnBooks && !hymnBooks.length) {
    return <NoContent entity="HymnBooks" />;
  }
  if (topics && !topics.length) {
    return <NoContent entity="Topics" />;
  }
  if (labels && !labels.length) {
    return <NoContent entity="Labels" />;
  }

  const handleEditSearchTerm = (searchTerm: string) => {
    setSearchHymnTerm(searchTerm);
  };

  const handleCreateHymn = () => {};
  const handleResetHymnFormState = () => {};

  return (
    <div>
      <SearchAndCreate
        searchTerm={searchHymnTerm}
        onChange={handleEditSearchTerm}
        entity="Hymn"
        isCreateButtonDisabled={false}
        isCreateFormValid={true}
        onCreate={handleCreateHymn}
        resetCreateState={handleResetHymnFormState}
      >
        <div className={globalStyles["flex-box-column-center-gap-1"]}>
          {/*<div className={globalStyles["flex-box-center-gap-1"]}>*/}
          <div className={globalStyles["input-group-two-columns"]}>
            <div className={globalStyles["input-group"]}>
              <label htmlFor="author-id">Author</label>
              <select id="author-id" onChange={(e) => setAuthorId(Number(e.target.value))}>
                {authors.map((author) => (
                  <option value={author.authorId}>{author.name}</option>
                ))}
              </select>
            </div>

            <div className={globalStyles["input-group"]}>
              <label htmlFor="author-extras">Author Extras</label>
              <input id="author-extras" value={authorExtras} onChange={(e) => setAuthorExtras(e.target.value)} />
            </div>
          </div>

          <div className={globalStyles["input-group-two-columns"]}>
            <div className={globalStyles["input-group"]}>
              <label id="hymn-title">Title</label>
              <input id="hymn-title" value={hymnTitle} onChange={(e) => setHymnTitle(e.target.value)} />
            </div>

            <div className={globalStyles["input-group"]}>
              <label htmlFor="hymn-book">Hymn Book</label>
              <select id="hymn-book" onChange={(e) => setHymnBookId(Number(e.target.value))}>
                {hymnBooks.map((hymnBook) => (
                  <option value={hymnBook.hymnBookId}>{hymnBook.name}</option>
                ))}
              </select>
            </div>
          </div>

          <div className={globalStyles["input-group-two-columns"]}>
            <div className={globalStyles["input-group"]}>
              <label htmlFor="number-in-hymn-book">Number in Hymn Book</label>
              <input
                id="number-in-hymn-book"
                value={numberInHymnBook}
                onChange={(e) => setNumberInHymnBook(Number(e.target.value))}
              />
            </div>

            <div className={globalStyles["input-group"]}>
              <label htmlFor="topic">Topic</label>
              <select id="topic" onChange={(e) => setTopicId(Number(e.target.value))}>
                {topics.map((topic) => (
                  <option value={topic.topicId}>{topic.name}</option>
                ))}
              </select>
            </div>
          </div>

          <div className={globalStyles["input-group"]}>
            <label htmlFor="label">Label</label>
            <option id="label" onChange={(e) => setLabelId(Number(e.target.value))}>
              {labels.map((label) => (
                <option value={label.labelId}>{label.name}</option>
              ))}
            </option>
          </div>

          <div className={globalStyles["input-group"]}>
            <label htmlFor="hymn-lyrics">Lyrics</label>
            <textarea id="hymn-lyrics" value={hymnLyrics} onChange={(e) => setHymnLyrics(e.target.value)} />
          </div>
          {/*</div>*/}
        </div>
      </SearchAndCreate>
      <div className={globalStyles["grid-two-columns"]}>
        {hymns &&
          hymns.length &&
          hymns.map((hymn) => {
            const author: Author | undefined = authors.find((a) => a.authorId === hymn.authorId);
            if (!author) {
              return <NoContent entity="Authors" />;
            }

            const hymnBook: HymnBook | undefined = hymnBooks.find((h) => h.hymnBookId === hymn.hymnBookId);
            if (!hymnBook) {
              return <NoContent entity="Hymn Book" />;
            }

            const topic: Topic | undefined = topics.find((t) => t.topicId === hymn.topicId);
            if (!topic) {
              return <NoContent entity="Topic" />;
            }

            const label: Label | undefined = labels.find((l) => l.labelId === hymn.labelId);
            if (!label) {
              return <NoContent entity="Label" />;
            }
            return (
              <Hymn
                hymnId={hymn.hymnId}
                authorName={author.name}
                authorExtras={hymn.authorExtras}
                title={hymn.title}
                lyrics={hymn.lyrics}
                hymnBookName={hymnBook.name}
                numberInHymnBook={hymn.numberInHymnBook}
                topicName={topic.name}
                labelName={label.name}
              />
            );
          })}
      </div>
    </div>
  );
};

export default Hymns;
