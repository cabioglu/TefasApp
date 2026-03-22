import React, { useEffect, useState, useMemo } from "react";
import { useFundById, useTCMBUSDTRY } from "../hooks/useApiCall";
import { useParams } from "react-router-dom";
import { motion } from "framer-motion";
import { Tabs, Statistic, Row, Col, Card, Typography, Button, Tooltip } from "antd";
import { PieChartIcon, BarChart3Icon, UsersIcon } from "lucide-react";
import DetailPriceLineChart from "../components/fundDetail/DetailPriceLineChart";
import { useTranslation } from "react-i18next";
import './FundDetailPage.css';
import FundInfoCard from '../components/FundInfoCard';
import { format, subDays, subMonths, subYears } from 'date-fns';
import { tr, enUS } from 'date-fns/locale';

const { Title } = Typography;
const { TabPane } = Tabs;

interface FundFounder {
  id: number;
  fundFounderName: string;
}

interface UmbrellaFundType {
  id: number;
  umbrellaFundTypeName: string;
}

interface FundTitle {
  id: number;
  fundTitleName: string;
}

interface DataHistoryItemDTO {
  historyId: number;
  code: string;
  fundName: string;
  date: Date;
  unitPrice: number;
  totalUnits: number;
  quantity: number;
  totalValue: number;
}

interface MonthlyDataItem {
  month: string;
  value: number;
  normalizedHeight?: number;
}

interface Fund {
  id: number;
  code: string;
  fundName: string;
  fundFounder: FundFounder;
  umbrellaFundType: UmbrellaFundType;
  fundTitles: FundTitle[];
  dataHistory: DataHistoryItemDTO[];
  processedTefas: boolean;
}

const FundDetail: React.FC = () => {
  const { t, i18n } = useTranslation();
  const { fundId } = useParams<{ fundId: string }>();
  const [fund, setFund] = useState<Fund | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [activePeriod, setActivePeriod] = useState<string>("1W");
  const [performanceMetrics, setPerformanceMetrics] = useState<{[key: string]: number}>({});
  const [fundValueMonthlyData, setFundValueMonthlyData] = useState<MonthlyDataItem[]>([]);
  const [investorCountMonthlyData, setInvestorCountMonthlyData] = useState<MonthlyDataItem[]>([]);
  const [volumeMonthlyData, setVolumeMonthlyData] = useState<MonthlyDataItem[]>([]);
  const [priceHistoryData, setPriceHistoryData] = useState<{label: string, color: string}[]>([]);
  const [statsData, setStatsData] = useState<{totalValue: string, investorCount: string, volumeValue: string}>({
    totalValue: "",
    investorCount: "",
    volumeValue: ""
  });

  const { callApi } = useFundById(fundId!);
  const { callApi: fetchUSDTRY } = useTCMBUSDTRY("01.01.2025","03.01.2025");

  // Locale seçimi için helper fonksiyon
  const getLocale = () => {
    return i18n.language.startsWith('tr') ? tr : enUS;
  };

  // Calculate performance metrics from historical data
  const calculatePerformanceMetrics = (dataHistory: DataHistoryItemDTO[]) => {
    if (!dataHistory || dataHistory.length === 0) return {};
    
    // Sort by date in ascending order
    const sortedData = [...dataHistory].sort((a, b) => 
      new Date(a.date).getTime() - new Date(b.date).getTime()
    );
    
    // Get the latest date available in data
    const latestDateEntry = sortedData[sortedData.length - 1];
    const latestDate = new Date(latestDateEntry.date);
    const latestPrice = latestDateEntry.unitPrice;
    
    const metrics: {[key: string]: number} = {};
    
    // Function to get data for exact date or closest to it
    const getDataForDate = (targetDate: Date): DataHistoryItemDTO | null => {
      // Try to find exact match first
      const exactMatch = sortedData.find(item => {
        const itemDate = new Date(item.date);
        return itemDate.getFullYear() === targetDate.getFullYear() &&
               itemDate.getMonth() === targetDate.getMonth() &&
               itemDate.getDate() === targetDate.getDate();
      });
      
      if (exactMatch) return exactMatch;
      
      // If no exact match, find the closest earlier date
      return sortedData.reduce((closest, current) => {
        const currentDate = new Date(current.date);
        if (currentDate <= targetDate && (!closest || currentDate > new Date(closest.date))) {
          return current;
        }
        return closest;
      }, null as DataHistoryItemDTO | null);
    };
    
    // Find the price 1 week ago from the latest date
    const oneWeekAgo = subDays(latestDate, 7);
    const oneWeekData = getDataForDate(oneWeekAgo);
    
    if (oneWeekData) {
      metrics["1W"] = ((latestPrice - oneWeekData.unitPrice) / oneWeekData.unitPrice) * 100;
    }
    
    // Find the price 1 month ago from the latest date
    const oneMonthAgo = subMonths(latestDate, 1);
    const oneMonthData = getDataForDate(oneMonthAgo);
    
    if (oneMonthData) {
      metrics["1M"] = ((latestPrice - oneMonthData.unitPrice) / oneMonthData.unitPrice) * 100;
    }
    
    // Find the price 3 months ago from the latest date
    const threeMonthsAgo = subMonths(latestDate, 3);
    const threeMonthData = getDataForDate(threeMonthsAgo);
    
    if (threeMonthData) {
      metrics["3M"] = ((latestPrice - threeMonthData.unitPrice) / threeMonthData.unitPrice) * 100;
    }
    
    // Find the price 6 months ago from the latest date
    const sixMonthsAgo = subMonths(latestDate, 6);
    const sixMonthData = getDataForDate(sixMonthsAgo);
    
    if (sixMonthData) {
      metrics["6M"] = ((latestPrice - sixMonthData.unitPrice) / sixMonthData.unitPrice) * 100;
    }
    
    // Find the price 1 year ago from the latest date
    const oneYearAgo = subYears(latestDate, 1);
    const oneYearData = getDataForDate(oneYearAgo);
    
    if (oneYearData) {
      metrics["1Y"] = ((latestPrice - oneYearData.unitPrice) / oneYearData.unitPrice) * 100;
    }
    
    // Calculate Year-to-Date (YTD) performance - from January 1st of current year
    const currentYear = latestDate.getFullYear();
    const startOfYear = new Date(currentYear, 0, 1); // January 1st of current year
    const ytdData = getDataForDate(startOfYear);
    
    if (ytdData) {
      metrics["YTD"] = ((latestPrice - ytdData.unitPrice) / ytdData.unitPrice) * 100;
    }
    
    // First record to calculate since inception return
    const firstData = sortedData[0];
    metrics["5Y"] = ((latestPrice - firstData.unitPrice) / firstData.unitPrice) * 100;
    
    return metrics;
  };
  
  // Calculate monthly data for the bar charts
  const calculateMonthlyData = (dataHistory: DataHistoryItemDTO[]) => {
    if (!dataHistory || dataHistory.length === 0) return {
      fundValueData: [],
      investorCountData: [],
      volumeData: []
    };
    
    // Get current locale
    const currentLocale = getLocale();
    
    // Sort data by date in ascending order
    const sortedData = [...dataHistory].sort((a, b) => 
      new Date(a.date).getTime() - new Date(b.date).getTime()
    );
    
    // Group data by month-year and find the last entry for each month
    const monthlyLastEntries: { [key: string]: DataHistoryItemDTO } = {};
    
    sortedData.forEach(item => {
      const date = new Date(item.date);
      // Format as YYYY-MM to ensure proper chronological grouping
      const monthYearKey = `${date.getFullYear()}-${date.getMonth() + 1}`;
      
      // Always replace with the latest entry for this month
      monthlyLastEntries[monthYearKey] = item;
    });
    
    // Convert the last entries to an array and sort by date
    const lastEntriesArray = Object.values(monthlyLastEntries)
      .sort((a, b) => new Date(a.date).getTime() - new Date(b.date).getTime());
    
    // Get the last 6 months of data
    const lastSixMonthsData = lastEntriesArray.slice(-6);
    
    // Extract the metrics from the last entries with localized month names
    const fundValueData = lastSixMonthsData.map(item => ({
      month: format(new Date(item.date), 'MMM', { locale: currentLocale }), // Localized month names
      value: item.totalValue,
      rawDate: new Date(item.date) // Keep the date for sorting if needed
    }));
    
    const investorCountData = lastSixMonthsData.map(item => ({
      month: format(new Date(item.date), 'MMM', { locale: currentLocale }),
      value: item.quantity, // Using quantity as investor count
      rawDate: new Date(item.date)
    }));
    
    const volumeData = lastSixMonthsData.map(item => ({
      month: format(new Date(item.date), 'MMM', { locale: currentLocale }),
      value: item.totalValue / item.quantity * item.unitPrice, // Approximation of trading volume
      rawDate: new Date(item.date)
    }));
    
    // Normalize function to maintain the same visual approach
    const normalizeData = (data: { month: string, value: number, rawDate: Date }[]) => {
      if (data.length === 0) return [];
      
      // Normalize values
      const values = data.map(item => item.value);
      const minValue = Math.min(...values);
      const maxValue = Math.max(...values);
      
      if (maxValue > minValue) {
        return data.map(item => ({
          month: item.month,
          value: item.value,
          normalizedHeight: 20 + (item.value - minValue) / (maxValue - minValue) * 80
        }));
      }
      
      return data.map(item => ({
        month: item.month,
        value: item.value,
        normalizedHeight: 60
      }));
    };
    
    // Return the normalized data for each metric
    return {
      fundValueData: normalizeData(fundValueData),
      investorCountData: normalizeData(investorCountData),
      volumeData: normalizeData(volumeData)
    };
  };
  
  // Calculate price history for the bottom chart
  const calculatePriceHistory = (dataHistory: DataHistoryItemDTO[]) => {
    if (!dataHistory || dataHistory.length === 0) return [];
    
    // Sort by date in descending order to get most recent first
    const sortedData = [...dataHistory]
      .sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime())
      .slice(0, 6); // Get last 6 entries
    
    // Colors for the bars
    const colors = [
      "var(--color-beige)", 
      "var(--color-coral)",
      "var(--color-yellow)",
      "var(--color-orange)",
      "var(--color-teal)",
      "var(--color-lightblue)"
    ];
    
    return sortedData.map((item, index) => ({
      label: `${item.unitPrice.toFixed(0)} ₺`,
      color: colors[index % colors.length]
    }));
  };
  
  // Calculate statistics
  const calculateStats = (fund: Fund) => {
    if (!fund || !fund.dataHistory || fund.dataHistory.length === 0) {
      return {
        totalValue: t("common.notAvailable"),
        investorCount: t("common.notAvailable"),
        volumeValue: t("common.notAvailable")
      };
    }
    
    // Get the latest data entry
    const latestData = [...fund.dataHistory]
      .sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime())[0];
    
    // Format total value with Turkish currency format
    const totalValue = new Intl.NumberFormat('tr-TR', {
      maximumFractionDigits: 0
    }).format(latestData.totalValue) + " ₺";
    
    // For investor count, we'll use a calculation based on total units
    // This is just an example - in a real app, you might have actual investor count data
    const investorCount = Math.floor(latestData.quantity).toString();
    
    // Calculate volume as average daily trading volume over last month
    const oneMonthAgo = subMonths(new Date(latestData.date), 1);
    const recentData = fund.dataHistory.filter(item => 
      new Date(item.date) >= oneMonthAgo && new Date(item.date) <= new Date(latestData.date)
    );
    
    const averageDailyVolume = recentData.reduce((sum, item) => sum + item.totalValue, 0) / 
      (recentData.length || 1);
    
    const volumeValue = new Intl.NumberFormat('tr-TR', {
      maximumFractionDigits: 0
    }).format(averageDailyVolume) + " ₺";
    
    return {
      totalValue,
      investorCount,
      volumeValue
    };
  };

  // Filter chart data based on the selected period
  const filteredChartData = useMemo(() => {
    if (!fund || !fund.dataHistory || fund.dataHistory.length === 0) return [];
    
    // Sort data in ascending order by date
    const sortedData = [...fund.dataHistory].sort((a, b) => 
      new Date(a.date).getTime() - new Date(b.date).getTime()
    );
    
    const latestDate = new Date(sortedData[sortedData.length - 1].date);
    let startDate: Date;
    
    // Determine the start date based on the active period
    switch (activePeriod) {
      case '1W':
        startDate = subDays(latestDate, 7);
        break;
      case '1M':
        startDate = subMonths(latestDate, 1);
        break;
      case '3M':
        startDate = subMonths(latestDate, 3);
        break;
      case '6M':
        startDate = subMonths(latestDate, 6);
        break;
      case '1Y':
        startDate = subYears(latestDate, 1);
        break;
      case 'YTD':
        startDate = new Date(latestDate.getFullYear(), 0, 1); // January 1st of current year
        break;
      case '5Y':
      default:
        return sortedData; // Show all data for "since inception" or fallback
    }
    
    // Filter data based on the start date
    return sortedData.filter(item => new Date(item.date) >= startDate);
  }, [fund, activePeriod]);

  useEffect(() => {
    const getFundDetail = async () => {
      try {
        setLoading(true);
        const response = await callApi(fundId);
        setFund(response);
        
        // Calculate metrics from real data
        if (response && response.dataHistory) {
          const metrics = calculatePerformanceMetrics(response.dataHistory);
          setPerformanceMetrics(metrics);
          
          const { fundValueData, investorCountData, volumeData } = calculateMonthlyData(response.dataHistory);
          setFundValueMonthlyData(fundValueData);
          setInvestorCountMonthlyData(investorCountData);
          setVolumeMonthlyData(volumeData);
          
          const priceHistoryChartData = calculatePriceHistory(response.dataHistory);
          setPriceHistoryData(priceHistoryChartData);
          
          const calculatedStats = calculateStats(response);
          setStatsData(calculatedStats);
        }
      } catch (err) {
        console.error("Error fetching fund data:", err);
      } finally {
        setLoading(false);
      }
    };

    const getTCMBUSDTRY = async () => {
      try {
        const response = await fetchUSDTRY("01-01-2025","03-01-2025");
        console.log(response);
      } catch (err) {
        console.error("Error fetching USDT/TRY data:", err);
      }
    };

    getFundDetail();
    getTCMBUSDTRY();
  }, [fundId]);

  // Dil değiştiğinde aylık verileri yeniden hesapla
  useEffect(() => {
    if (fund && fund.dataHistory) {
      const { fundValueData, investorCountData, volumeData } = calculateMonthlyData(fund.dataHistory);
      setFundValueMonthlyData(fundValueData);
      setInvestorCountMonthlyData(investorCountData);
      setVolumeMonthlyData(volumeData);
    }
  }, [i18n.language, fund]);

  if (loading) {
    return (
      <div className="loading-container">
        <p>{t("fundDetail.loading")}</p>
      </div>
    );
  }

  if (!fund) {
    return (
      <div className="loading-container">
        <p>{t("fundDetail.noData")}</p>
      </div>
    );
  }
  
  // Determine if performance is positive or negative
  const isPositivePerformance = (value: number) => value >= 0;

  return (
    <div className="fund-detail-container">
      <main className="fund-detail-main">
        <motion.div
          className="fund-header"
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.3 }}
        >
           <FundInfoCard fund={fund} />
        </motion.div>

        <Tabs defaultActiveKey="overview" className="fund-tabs">
          <TabPane tab={t("fundDetail.overview")} key="overview">
            {/* Performance Metrics Section */}
            <Card className="performance-card">
              <Title level={5}>{t("fundDetail.performanceChart")}</Title>
              <div className="performance-metrics">
                {Object.entries(performanceMetrics).map(([period, value]) => (
                  <div 
                    key={period} 
                    className={`metric-item ${activePeriod === period ? 'active' : ''}`}
                    onClick={() => setActivePeriod(period)}
                  >
                    <div className="period-label">{period === "YTD" ? t("fundDetail.ytd") : period}</div>
                    <div className={`performance-value ${isPositivePerformance(value) ? 'positive' : 'negative'}`}>
                      % {value.toFixed(2)}
                    </div>
                  </div>
                ))}
              </div>
              
              {/* Main Chart */}
              <div className="fund-main-chart">
                <DetailPriceLineChart dataHistory={filteredChartData} />
              </div>
            </Card>

            {/* Statistics Cards */}
            <Row gutter={[16, 16]} className="statistics-row">
              <Col xs={24} md={8}>
                <Card className="stats-card">
                  <Statistic 
                    title={t("fundDetail.totalFundValue")}
                    value={statsData.totalValue}
                    prefix={<PieChartIcon size={16} />}
                  />
                </Card>
              </Col>
              <Col xs={24} md={8}>
                <Card className="stats-card">
                  <Statistic 
                    title={t("fundDetail.investorCount")}
                    value={statsData.investorCount}
                    prefix={<UsersIcon size={16} />}
                  />
                </Card>
              </Col>
              <Col xs={24} md={8}>
                <Card className="stats-card">
                  <Statistic 
                    title={t("fundDetail.volumeValue")}
                    value={statsData.volumeValue}
                    prefix={<BarChart3Icon size={16} />}
                  />
                </Card>
              </Col>
            </Row>

            {/* Monthly Bar Charts */}
            <Row gutter={[16, 16]} className="chart-row">
              <Col xs={24} md={8}>
                <Card className="monthly-chart-card">
                  <Title level={5}>{t("fundDetail.totalFundValueChart")}</Title>
                  <div className="monthly-chart">
                    {fundValueMonthlyData.map((item, index) => (
                      <div key={index} className="monthly-bar-container">
                        <Tooltip 
                          title={new Intl.NumberFormat('tr-TR', { maximumFractionDigits: 0 }).format(item.value) + " ₺"}
                          color={isPositivePerformance(item.value - (fundValueMonthlyData[index-1]?.value || item.value)) ? '#4caf50' : '#f44336'}
                        >
                          <div 
                            className="monthly-bar" 
                            style={{ 
                              height: `${item.normalizedHeight || 40}px`,
                              backgroundColor: isPositivePerformance(item.value - (fundValueMonthlyData[index-1]?.value || item.value)) ? '#4caf50' : '#f44336'
                            }}
                          ></div>
                        </Tooltip>
                        <div className="monthly-label">{item.month}</div>
                      </div>
                    ))}
                  </div>
                </Card>
              </Col>
              <Col xs={24} md={8}>
                <Card className="monthly-chart-card">
                  <Title level={5}>{t("fundDetail.investorCountChart")}</Title>
                  <div className="monthly-chart">
                    {investorCountMonthlyData.map((item, index) => (
                      <div key={index} className="monthly-bar-container">
                        <Tooltip 
                          title={new Intl.NumberFormat('tr-TR', { maximumFractionDigits: 0 }).format(item.value)}
                          color={isPositivePerformance(item.value - (investorCountMonthlyData[index-1]?.value || item.value)) ? '#4caf50' : '#f44336'}
                        >
                          <div 
                            className="monthly-bar" 
                            style={{ 
                              height: `${item.normalizedHeight || 40}px`,
                              backgroundColor: isPositivePerformance(item.value - (investorCountMonthlyData[index-1]?.value || item.value)) ? '#4caf50' : '#f44336'
                            }}
                          ></div>
                        </Tooltip>
                        <div className="monthly-label">{item.month}</div>
                      </div>
                    ))}
                  </div>
                </Card>
              </Col>
              <Col xs={24} md={8}>
                <Card className="monthly-chart-card">
                  <Title level={5}>{t("fundDetail.volumeValueChart")}</Title>
                  <div className="monthly-chart">
                    {volumeMonthlyData.map((item, index) => (
                      <div key={index} className="monthly-bar-container">
                        <Tooltip 
                          title={new Intl.NumberFormat('tr-TR', { maximumFractionDigits: 0 }).format(item.value) + " ₺"}
                          color={isPositivePerformance(item.value - (volumeMonthlyData[index-1]?.value || item.value)) ? '#4caf50' : '#f44336'}
                        >
                          <div 
                            className="monthly-bar" 
                            style={{ 
                              height: `${item.normalizedHeight || 40}px`,
                              backgroundColor: isPositivePerformance(item.value - (volumeMonthlyData[index-1]?.value || item.value)) ? '#4caf50' : '#f44336'
                            }}
                          ></div>
                        </Tooltip>
                        <div className="monthly-label">{item.month}</div>
                      </div>
                    ))}
                  </div>
                </Card>
              </Col>
            </Row>

            {/* Fund History Comparison */}
            <Card className="price-history-card">
              <div className="price-history-header">
                <Title level={5}>{t("fundDetail.priceHistory")}</Title>
                <div className="period-selectors">
                  <Button type="primary" size="small">1 Ay</Button>
                  <Button size="small">3 Ay</Button>
                  <Button size="small">6 Ay</Button>
                </div>
              </div>
              <div className="price-history-chart">
                {priceHistoryData.map((item, index) => (
                  <div key={index} className="price-history-bar-container">
                    <div className="price-history-bar" style={{ 
                      height: `${80 + Math.random() * 40}px`,
                      backgroundColor: item.color 
                    }}></div>
                    <div className="price-history-label">{item.label}</div>
                  </div>
                ))}
              </div>
            </Card>
           
          </TabPane>
          <TabPane tab={t("fundDetail.analysis")} key="analysis">
            <Card>
              <p>{t("fundDetail.analysisContent")}</p>
            </Card>
          </TabPane>
        </Tabs>
      </main>
    </div>
  );
};

export default FundDetail;
