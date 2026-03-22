import React from "react";
import { motion } from "framer-motion";
import { useTranslation } from "react-i18next";
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts";

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

interface RechartsAreaChartProps {
  dataHistory: DataHistoryItemDTO[];
}

const DetailPriceLineChart: React.FC<RechartsAreaChartProps> = ({
  dataHistory,
}) => {
  const { t } = useTranslation();

  // Sort by date
  const sortedData = [...dataHistory].sort((a, b) => {
    const dateA = new Date(a.date).getTime();
    const dateB = new Date(b.date).getTime();
    return dateA - dateB;
  });
  
  // Format data for Recharts
  const formattedData = sortedData.map((item) => ({
    date: new Date(item.date).toLocaleDateString("en-GB"), // Format dates as DD/MM/YYYY
    unitPrice: item.unitPrice,
  }));

  const minValue = Math.min(...formattedData.map((item) => item.unitPrice));
  return (
    <motion.div
      className="card p-6"
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay: 0.4 }}
    >
      <h2 className="text-xl font-semibold mb-2">{t("charts.unitPrice")}</h2>

      <div style={{ width: "100%", height: 300 }}>
        <ResponsiveContainer>
          <LineChart
            data={formattedData}
            margin={{ top: 10, right: 30, left: 80, bottom: 30 }}
          >
            <CartesianGrid
              strokeDasharray="3 3"
              stroke="var(--color-border)"
            />
            <XAxis
              dataKey="date"
              tick={{ fill: "var(--color-text-secondary)" }}
              axisLine={{ stroke: "var(--color-border)" }}
              tickLine={{ stroke: "var(--color-border)" }}
              label={{ value: t("charts.date"), position: "bottom", fill: "var(--color-text-primary)" }}
            />
            <YAxis
              tick={{ fill: "var(--color-text-secondary)" }}
              axisLine={{ stroke: "var(--color-border)" }}
              tickLine={{ stroke: "var(--color-border)" }}
              label={{
                value: t("charts.unitPrice"),
                angle: -90,
                position: "left",
                fill: "var(--color-text-primary)",
              }}
              domain={[minValue, "auto"]}
            />
            <Tooltip
              contentStyle={{
                backgroundColor: "var(--color-surface)",
                border: "1px solid var(--color-border)",
                borderRadius: "8px",
              }}
              labelStyle={{ color: "var(--color-text-primary)" }}
              itemStyle={{ color: "var(--color-text-primary)" }}
            />
            <Legend
              wrapperStyle={{ color: "var(--color-text-primary)" }}
              iconType="circle"
              verticalAlign="top"
            />
            <Line
              type="monotone"
              dataKey="unitPrice"
              stroke="var(--color-purple-400)"
              activeDot={{ r: 8 }}
              name={t("charts.unitPrice")}
            />
          </LineChart>
        </ResponsiveContainer>
      </div>
    </motion.div>
  );
};

export default DetailPriceLineChart;
