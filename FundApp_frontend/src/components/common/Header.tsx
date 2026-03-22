import React from "react";
import { useTranslation } from "react-i18next";
import './Header.css';

interface HeaderProps {
  title: string;
}

const Header: React.FC<HeaderProps> = ({ title }) => {
  const { t } = useTranslation();

  // Map the title to the appropriate translation key
  const getTitleKey = (title: string) => {
    switch (title) {
      case "Funds":
        return "header.funds";
      case "Fund Historical Data":
        return "header.fundHistoricalData";
      default:
        return title; // If no mapping found, use the original title
    }
  };

  return (
    <header className="header">
      <div className="header-container">
        <h1 className="header-title">{t(getTitleKey(title))}</h1>
      </div>
    </header>
  );
};

export default Header;
