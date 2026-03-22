import {useEffect, useState, ChangeEvent } from "react";
import {useAllHistoryData} from "../../hooks/useApiCall"
import { motion } from "framer-motion";
import { Search } from "lucide-react";
import { useTranslation } from "react-i18next";
import './DataTable.css';

interface DataHistoryItemDTO {
  historyId: number;
  code: string;
  fundName: string;
  date: Date; // Alternatively, you can use Date if you plan to manipulate dates
  unitPrice: number;
  totalUnits: number;
  quantity: number;
  totalValue: number;
}


const DataTable: React.FC = () => {
  const { t } = useTranslation();
  const [searchTerm, setSearchTerm] = useState<string>("");
  const [HistoryDataFunds, setHistoryDataFunds] = useState<DataHistoryItemDTO[]>([]);
  const [filteredHistoryDataFunds, setFilteredHistoryDataFunds] = useState<DataHistoryItemDTO[]>([]);
 
  const { callApi } = useAllHistoryData();
  
  
  
  const fetchHistoryData = async () => {
    try {
      const data = await callApi(); // Trigger the API call
      setHistoryDataFunds(data); // Update the state with the response
      setFilteredHistoryDataFunds(data);
      console.log(data);
    } catch (err) {
      console.error('Error fetching history data:', err);
    }
  };

  useEffect(() => {
    fetchHistoryData(); // Automatically fetch data on component mount
  }, []); // Empty dependency array ensures it runs only once

  const handleSearch = (e: ChangeEvent<HTMLInputElement>): void => {
    const term = e.target.value.toLowerCase();
    setSearchTerm(term);

    // Filter logic
    const filtered = HistoryDataFunds.filter(
      (historyDataFund) =>
        historyDataFund.code.toLowerCase().includes(term) || historyDataFund.fundName.toLowerCase().includes(term)
    );
    setFilteredHistoryDataFunds(filtered);
  };

  return (
    <motion.div
      className="data-table-container"
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay: 0.2 }}
    >
      <div className="data-table-header">
        <h2 className="data-table-title">{t("historicalData.tableTitle")}</h2>
        <div className="search-container">
          <input
            type="text"
            placeholder={t("historicalData.searchPlaceholder")}
            className="search-input"
            value={searchTerm}
            onChange={handleSearch}
          />
          <Search className="search-icon" size={18} />
        </div>
      </div>

      <table className="table">
        <thead>
          <tr>
            <th>{t("historicalData.code")}</th>
            <th>{t("historicalData.fundName")}</th>
            <th>{t("historicalData.date")}</th>
            <th>{t("historicalData.unitPrice")}</th>
            <th>{t("historicalData.totalUnit")}</th>
            <th>{t("historicalData.quantity")}</th>
            <th>{t("historicalData.totalValue")}</th>
            <th>{t("historicalData.actions")}</th>
          </tr>
        </thead>
        <tbody>
          {filteredHistoryDataFunds.map((fund) => (
            <motion.tr
              key={fund.historyId}
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              transition={{ duration: 0.3 }}
            >
              <td>
                <div className="fund-code-container">
                  <div className="fund-code-avatar">
                    {fund.code}
                  </div>
                </div>
              </td>
              <td>
                <div className="fund-name">{fund.fundName}</div>
              </td>
              <td>{new Intl.DateTimeFormat('en-GB').format(new Date(fund.date))}</td>
              <td>{fund.unitPrice}</td>
              <td>{fund.totalUnits}</td>
              <td>{fund.quantity}</td>
              <td>{fund.totalValue}</td>
              <td>
                <button className="action-button edit-button">{t("historicalData.edit")}</button>
                <button className="action-button delete-button">{t("historicalData.delete")}</button>
              </td>
            </motion.tr>
          ))}
        </tbody>
      </table>
    </motion.div>
  );
};

export default DataTable;
