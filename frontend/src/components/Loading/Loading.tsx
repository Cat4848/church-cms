const Loading = () => {
  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignContent: "center",
        height: "100vh",
        width: "100vw",
      }}
    >
      <img
        src="../../../public/loading-spinner.svg"
        alt="Loading spinner"
        style={{ height: "5vh", display: "block" }}
      />
    </div>
  );
};

export default Loading;
