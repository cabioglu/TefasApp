import React, { useEffect } from "react";
import FundsPage from "./pages/FundsPage";
import FundDetailPage from "./pages/FundDetailPage";
import HistoricalDataPage from './pages/HistoricalDataPage';
import LoginPage from "./pages/LoginPage";
import ProtectedLayout from "./components/common/ProtectedLayout";
import { Route, Routes, Navigate, useLocation } from "react-router-dom";
import { useLanguage } from "./contexts/LanguageContext";

const ProtectedRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const isAuthenticated = localStorage.getItem("isAuthenticated") === "true";
  const location = useLocation();

  if (!isAuthenticated) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  return <ProtectedLayout>{children}</ProtectedLayout>;
};

function App() {
  const { language } = useLanguage();

  useEffect(() => {
    document.documentElement.setAttribute('lang', language);
  }, [language]);

  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route
        path="/"
        element={
          <ProtectedRoute>
            <Navigate to="/funds" replace />
          </ProtectedRoute>
        }
      />
      <Route
        path="/funds"
        element={
          <ProtectedRoute>
            <FundsPage />
          </ProtectedRoute>
        }
      />
      <Route
        path="/history"
        element={
          <ProtectedRoute>
            <HistoricalDataPage />
          </ProtectedRoute>
        }
      />
      <Route
        path="/fundDetail/:fundId"
        element={
          <ProtectedRoute>
            <FundDetailPage />
          </ProtectedRoute>
        }
      />
    </Routes>
  );
}

export default App;
