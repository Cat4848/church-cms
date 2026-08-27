interface Props {
  entity: string;
}

const NoContent = ({ entity }: Props) => {
  return <h2>{`No ${entity} here yet.`}</h2>;
};

export default NoContent;
