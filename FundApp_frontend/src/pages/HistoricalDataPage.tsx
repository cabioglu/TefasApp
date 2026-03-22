import { motion } from "framer-motion";
import Header from "../components/common/Header";
import DataTable from "../components/historicalData/DataTable";
import { useTranslation } from "react-i18next";

const HistoricalDataPage: React.FC = () => {
  const { t } = useTranslation();
  
  return (
    <div className="flex-1 overflow-auto relative z-10">
    <Header title={t("sidebar.fundsHistoricalData")} />
  
    <main className="w-full max-w-screen-2xl mx-auto py-3 px-4 sm:px-6 lg:px-8">
      {/* STATS */}
      <motion.div
        className="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-4 mb-8"
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 1 }}
      >
        {/* Add your stats content here */}
      </motion.div>
  
      <DataTable />
  
      {/* CHARTS */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        {/* Add your charts or visualizations here */}
      </div>
    </main>
  </div>
  );
};

export default HistoricalDataPage;
