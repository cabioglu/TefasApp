import { ReactNode } from "react";
import Sidebar from "./Sidebar";

type ProtectedLayoutProps = {
  children: ReactNode;
};

const ProtectedLayout: React.FC<ProtectedLayoutProps> = ({ children }) => {
  return (
    <div className="app-container">
      {/* Background */}
      <div className="bg-gradient" />

      <Sidebar />

      {children}
    </div>
  );
};

export default ProtectedLayout;

