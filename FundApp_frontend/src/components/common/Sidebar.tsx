import { 
    BarChart2, 
    DollarSign, 
    Heart, 
    Menu,
    Landmark,
    Sun,
    Moon,
    Globe,
    Settings,
    ChevronDown,
    ChevronRight
} from "lucide-react";
import { useState } from "react";
import { AnimatePresence, motion } from "framer-motion";
import { Link } from "react-router-dom";
import { useTheme } from '../../contexts/ThemeContext';
import { useLanguage } from '../../contexts/LanguageContext';
import { useTranslation } from 'react-i18next';
import './Sidebar.css';

// Define a type for the sidebar item
interface SidebarItem {
  nameKey: string;
  icon: React.ComponentType<{ size?: string | number; style?: React.CSSProperties }>;
  color: string;
  href: string;
}

const Sidebar: React.FC = () => {
  const [isSidebarOpen, setIsSidebarOpen] = useState(true);
  const [isSettingsOpen, setIsSettingsOpen] = useState(false);
  const { theme, toggleTheme } = useTheme();
  const { language, toggleLanguage } = useLanguage();
  const { t } = useTranslation();

  const SIDEBAR_ITEMS: SidebarItem[] = [
    { nameKey: "sidebar.funds", icon: Landmark, color: "#8B5CF6", href: "/funds" },
    { nameKey: "sidebar.fundsHistoricalData", icon: BarChart2, color: "#6366f1", href: "/history"},
    { nameKey: "sidebar.favorites", icon: Heart, color: "#8B5CF6", href: "/favorites" },
    { nameKey: "sidebar.fundDetail", icon: DollarSign, color: "#EC4899", href: "/fundDetail" },
  ];

  const toggleSettings = () => {
    setIsSettingsOpen(!isSettingsOpen);
  };

  return (
    <motion.div
      className="sidebar"
      style={{ width: isSidebarOpen ? '256px' : '80px' }}
      animate={{ width: isSidebarOpen ? 256 : 80 }}
    >
      <div className="sidebar-container">
        <div className="sidebar-top">
          <motion.button
            whileHover={{ scale: 1.1 }}
            whileTap={{ scale: 0.9 }}
            onClick={() => setIsSidebarOpen(!isSidebarOpen)}
            className="menu-button"
          >
            <Menu size={24} />
          </motion.button>
        </div>

        <nav>
          {SIDEBAR_ITEMS.map((item) => (
            <Link key={item.href} to={item.href}>
              <motion.div className="nav-item">
                <item.icon size={20} style={{ color: item.color, minWidth: "20px" }} />
                <AnimatePresence>
                  {isSidebarOpen && (
                    <motion.span
                      className="nav-item-text"
                      initial={{ opacity: 0, width: 0 }}
                      animate={{ opacity: 1, width: "auto" }}
                      exit={{ opacity: 0, width: 0 }}
                      transition={{ duration: 0.2, delay: 0.3 }}
                    >
                      {t(item.nameKey)}
                    </motion.span>
                  )}
                </AnimatePresence>
              </motion.div>
            </Link>
          ))}
          
          {/* Settings Menu */}
          <div className="nav-item settings-menu-item" onClick={toggleSettings}>
            <div className="nav-item-content">
              <Settings size={20} style={{ color: "#4B5563", minWidth: "20px" }} />
              <AnimatePresence>
                {isSidebarOpen && (
                  <motion.span
                    className="nav-item-text"
                    initial={{ opacity: 0, width: 0 }}
                    animate={{ opacity: 1, width: "auto" }}
                    exit={{ opacity: 0, width: 0 }}
                    transition={{ duration: 0.2, delay: 0.3 }}
                  >
                    {t('sidebar.settings')}
                  </motion.span>
                )}
              </AnimatePresence>
            </div>
            {isSidebarOpen && (
              isSettingsOpen ? (
                <ChevronDown size={16} className="settings-chevron" />
              ) : (
                <ChevronRight size={16} className="settings-chevron" />
              )
            )}
          </div>
          
          {/* Settings Submenu - Vertical */}
          <AnimatePresence>
            {isSettingsOpen && isSidebarOpen && (
              <motion.div 
                className="settings-submenu-vertical"
                initial={{ opacity: 0, height: 0 }}
                animate={{ opacity: 1, height: "auto" }}
                exit={{ opacity: 0, height: 0 }}
                transition={{ duration: 0.2 }}
              >
                {/* Theme Toggle */}
                <div className="submenu-item">
                  {theme === 'dark' ? (
                    <Sun size={18} style={{ color: "#F59E0B" }} />
                  ) : (
                    <Moon size={18} style={{ color: "#6366F1" }} />
                  )}
                  <button onClick={toggleTheme} className="submenu-button">
                    {theme === 'dark' ? t('theme.light') : t('theme.dark')}
                  </button>
                </div>
                
                {/* Language Toggle */}
                <div className="submenu-item">
                  <Globe size={18} style={{ color: "#10B981" }} />
                  <button onClick={toggleLanguage} className="submenu-button">
                    {language === 'en' ? t('language.turkish') : t('language.english')}
                  </button>
                </div>
              </motion.div>
            )}
          </AnimatePresence>
        </nav>
      </div>
    </motion.div>
  );
};

export default Sidebar;
  