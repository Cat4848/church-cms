import { type JSX } from "react";
import * as React from "react";
import styles from "./styles.module.css";

interface Props {
  fallback: JSX.Element;
  children: JSX.Element;
}

interface State {
  hasError: boolean;
}

export class ErrorBoundary extends React.Component<Props, State> {
  constructor(props: Props, state: State) {
    super(props, state);
    this.state = { hasError: false };
  }

  static getDerivedStateFromError() {
    return { hasError: true };
  }

  componentDidCatch(/*error: Error, errorInfo: React.ErrorInfo*/) {
    // TODO send the error to my logging service
  }

  render() {
    if (this.state.hasError) {
      return this.props.fallback;
    }
    return this.props.children;
  }
}

export const Fallback = () => {
  return <div className={styles.errorBoundary}>Oops, sorry, something went wrong.</div>;
};
