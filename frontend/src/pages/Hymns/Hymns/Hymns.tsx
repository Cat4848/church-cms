import { useGetAllHymnsQuery, useCreateHymnMutation, useUpdateHymnMutation } from "../../../store/api/hymnsApi.ts";
import Loading from "../../../components/Loading/Loading.tsx";
import { ErrorFallback } from "../../../components/ErrorBoundary/ErrorBoundary.tsx";
import NoContent from "../../../components/NoContent/NoContent.tsx";
import globalStyles from "../../../css/global.module.css";
import hymn from "../Hymn/Hymn.tsx";
import Hymn from "../Hymn/Hymn.tsx";
import { useGetAllAuthorsQuery } from "../../../store/api/authorsApi.ts";
import { useGetAllHymnBooksQuery } from "../../../store/api/hymnBooksApi.ts";
import { useGetAllTopicsQuery } from "../../../store/api/topicsApi.ts";
import { useGetAllLabelsQuery } from "../../../store/api/labelsApi.ts";

const Hymns = () => {
  const { data: hymns, error: getAllHymnsError, isLoading } = useGetAllHymnsQuery();
  const { data: authors } = useGetAllAuthorsQuery();
  const { data: hymnBooks } = useGetAllHymnBooksQuery();
  const { data: topics } = useGetAllTopicsQuery();
  const { data: labels } = useGetAllLabelsQuery();

  // todo: add loading for all entities
  if (isLoading) {
    return <Loading />;
  }
  if (getAllHymnsError) {
    return <ErrorFallback error={getAllHymnsError.toString()} />;
  }
  // todo: add the errors for creating and updating + all other entities
  if (hymns && !hymns.length) {
    return <NoContent entity="Hymns" />;
  }

  return (
    <div className={globalStyles["grid-two-columns"]}>
      {hymns &&
        hymns.length &&
        hymns.map((hymn) => {
          return <Hymn />;
        })}
    </div>
  );
};

export default Hymns;
