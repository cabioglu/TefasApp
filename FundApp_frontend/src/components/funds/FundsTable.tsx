import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useGetAllFunds } from "../../hooks/useApiCall";
import { Table, Tag, Input, Button } from "antd";
import { CheckOutlined, CloseOutlined, SearchOutlined } from "@ant-design/icons";
import type { ColumnsType, TablePaginationConfig } from "antd/es/table";
import { useTranslation } from "react-i18next";
import "./FundsTable.css";

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
  fundTitleTypeName: string;
}

interface Fund {
  id: number;
  code: string;
  fundName: string;
  fundFounder: FundFounder;
  umbrellaFundType: UmbrellaFundType;
  fundTitles: FundTitle[];
  processedTefas: boolean;
}

interface PerformanceDTO {
  oneDay: number;
  oneWeek: number;
  oneMonth: number;
  threeMonth: number;
  sixMonth: number;
  oneYear: number;
  threeYear: number;
  fiveYear: number;
}

interface FundWithPerformance{
  fund : Fund;
  performanceDTO : PerformanceDTO;
}

const FundsTable: React.FC = () => {
  const { t } = useTranslation();
  const [funds, setFunds] = useState<FundWithPerformance[]>([]);
  const [filteredFunds, setFilteredFunds] = useState<FundWithPerformance[]>([]);
  const [inputFilteredFunds, setInputFilteredFunds] = useState<FundWithPerformance[]>([]);
  const [searchTerm, setSearchTerm] = useState<string>("");
  const [pagination, setPagination] = useState<TablePaginationConfig>({
    current: 1,
    pageSize: 10,
    total: 0,
  });
  const navigate = useNavigate();
  const { callApi } = useGetAllFunds();

  const fetchFundsData = async () => {
    try {
      const data = await callApi();
      setFunds(data);
      setFilteredFunds(data); // Initialize filtered data
      setInputFilteredFunds(data);
    } catch (err) {
      console.error("Error fetching data:", err);
    }
  };

  useEffect(() => {
    fetchFundsData();
  }, []);

  const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value.toLowerCase();
    setSearchTerm(value);

    const filtered = inputFilteredFunds.filter(
      (fund) =>
        fund.fund.code.toLowerCase().includes(value) ||
        fund.fund.fundName.toLowerCase().includes(value)
    );
    setFilteredFunds(filtered);
    //setInputFilteredFunds(filtered);
    setPagination((prev) => ({ ...prev, current: 1, total: filtered.length }));
  };

  const handleTableChange = (
    newPagination: TablePaginationConfig,
    filters: Record<string, any>,
    sorter: any
  ) => {
    setPagination(newPagination); // Update pagination

    const sortedData = [...funds].sort((a, b) => {
      if (!sorter.order) return 0; // No sorting applied
      const direction = sorter.order === "ascend" ? 1 : -1;
      return (a[sorter.field] || "").toString().localeCompare((b[sorter.field] || "").toString()) * direction;
    });

    const filteredData = sortedData.filter((record) => {
      let matchesFilters = true;

      if (filters.fundFounder) {
        matchesFilters = matchesFilters && filters.fundFounder.includes(record.fund.fundFounder?.fundFounderName);
      }
      if (filters.umbrellaFundType) {
        matchesFilters = matchesFilters && filters.umbrellaFundType.includes(record.fund.umbrellaFundType?.umbrellaFundTypeName);
      }
      if (filters.processedTefas) {
        matchesFilters = matchesFilters && filters.processedTefas.includes(record.fund.processedTefas);
      }

      return matchesFilters;
    });

    setFilteredFunds(filteredData);
    setInputFilteredFunds(filteredData);
    //setPagination((prev) => ({ ...prev, current: 1, total: filteredData.length }));
  };

  
  const fundFounderFilters = Array.from(
    new Set(funds.map((fund) => fund.fund.fundFounder?.fundFounderName).filter(Boolean))
  ).map((name) => ({ text: name, value: name }));

  const umbrellaFundTypeFilters = Array.from(
    new Set(funds.map((fund) => fund.fund.umbrellaFundType?.umbrellaFundTypeName).filter(Boolean))
  ).map((type) => ({ text: type, value: type }));

  const columns: ColumnsType<FundWithPerformance> = [
    {
      title: t("fundsTable.code"),
      dataIndex: ["fund", "code"],
      key: "code",
      sorter: true,
      fixed: 'left',
      render: (code: string) => <Tag color="blue">{code}</Tag>,
    },
    /*
    {
      title: t("fundsTable.fundName"),
      dataIndex: ["fund", "fundName"],
      key: "fundName",
      fixed: 'left',
      sorter: true,
    },
    {
      title: t("fundsTable.fundFounder"),
      dataIndex: ["fund", "fundFounder", "fundFounderName"],
      key: "fundFounder",
      filters: fundFounderFilters,
      filterMultiple: true,
      render: (name: string) => name || t("common.notAvailable"),
    }, */
    {
      title: t("fundsTable.umbrellaFundType"),
      dataIndex: ["fund", "umbrellaFundType", "umbrellaFundTypeName"],
      key: "umbrellaFundType",
      filters: umbrellaFundTypeFilters,
      filterMultiple: true,
      render: (type: string) => type || t("common.notAvailable"),
    },
    {
      title: t("fundsTable.processed"),
      dataIndex: ["fund", "processedTefas"],
      key: "processedTefas",
      filters: [
        { text: t("fundsTable.processedTrue"), value: true },
        { text: t("fundsTable.processedFalse"), value: false },
      ],
      render: (processed: boolean) =>
        processed ? (
          <CheckOutlined style={{ color: "green" }} />
        ) : (
          <CloseOutlined style={{ color: "red" }} />
        ),
    },
    {
      title: t("fundsTable.daily"),
      dataIndex: ["performanceDTO", "oneDay"],
      key: "oneDay",
      sorter: (a, b) => a.performanceDTO.oneDay - b.performanceDTO.oneDay,
      render: (type: number) => { if (typeof type === 'number') { return type.toFixed(4); } else { return t("common.notAvailable"); } },
    },
    {
      title: t("fundsTable.oneWeek"),
      dataIndex: ["performanceDTO", "oneWeek"],
      key: "oneWeek",
      sorter: (a, b) => a.performanceDTO.oneWeek - b.performanceDTO.oneWeek,
      render: (type: number) => { if (typeof type === 'number') { return type.toFixed(4); } else { return t("common.notAvailable"); } },
    },
    {
      title: t("fundsTable.oneMonth"),
      dataIndex: ["performanceDTO", "oneMonth"],
      key: "oneMonth",
      sorter: (a, b) => a.performanceDTO.oneMonth - b.performanceDTO.oneMonth,
      render: (type: number) => { if (typeof type === 'number') { return type.toFixed(4); } else { return t("common.notAvailable"); } },
    },
    {
      title: t("fundsTable.threeMonth"),
      dataIndex: ["performanceDTO", "threeMonth"],
      key: "threeMonth",
      sorter: (a, b) => a.performanceDTO.threeMonth - b.performanceDTO.threeMonth,
      render: (type: number) => { if (typeof type === 'number') { return type.toFixed(4); } else { return t("common.notAvailable"); } },
    },
    {
      title: t("fundsTable.sixMonth"),
      dataIndex: ["performanceDTO", "sixMonth"],
      key: "sixMonth",
      sorter: (a, b) => a.performanceDTO.sixMonth - b.performanceDTO.sixMonth,
      render: (type: number) => { if (typeof type === 'number') { return type.toFixed(4); } else { return t("common.notAvailable"); } },
    },
    {
      title: t("fundsTable.oneYear"),
      dataIndex: ["performanceDTO", "oneYear"],
      key: "oneYear",
      sorter: (a, b) => a.performanceDTO.oneYear - b.performanceDTO.oneYear,
      render: (type: number) => { if (typeof type === 'number') { return type.toFixed(4); } else { return t("common.notAvailable"); } },
    },
    {
      title: t("fundsTable.threeYear"),
      dataIndex: ["performanceDTO", "threeYear"],
      key: "threeYear",
      sorter: (a, b) => a.performanceDTO.threeYear - b.performanceDTO.threeYear,
      render: (type: number) => { if (typeof type === 'number') { return type.toFixed(4); } else { return t("common.notAvailable"); } },
    },
    {
      title: t("fundsTable.fiveYear"),
      dataIndex: ["performanceDTO", "fiveYear"],
      key: "fiveYear",
      sorter: (a, b) => a.performanceDTO.fiveYear - b.performanceDTO.fiveYear,
      render: (type: number) => { if (typeof type === 'number') { return type.toFixed(4); } else { return t("common.notAvailable"); } },
    },
    {
      title: t("fundsTable.actions"),
      key: "actions",
      align: "center",
      render: (_: any, record: FundWithPerformance) => (
        <div style={{ display: "flex" }}>
          <Button
            type="link"
            onClick={() => navigate(`/fundDetail/${record.fund.id}`)}
            style={{ marginRight: 0 }}
          >
            {t("fundsTable.detail")}
          </Button>
          <Button type="link" style={{ color: "orange" }}>
            {t("fundsTable.addFavorites")}
          </Button>
        </div>
      ),
    },
  ];


  return (
    <div className="funds-table-container">
      <Input
        placeholder={t("fundsTable.searchPlaceholder")}
        prefix={<SearchOutlined />}
        value={searchTerm}
        onChange={handleSearch}
        allowClear
        style={{ marginBottom: 16, width: "300px" }}
      />
      <Table
        columns={columns}
        dataSource={filteredFunds}
        rowKey="id"
        size="small"
        className="custom-table"
        scroll={{ x: 'max-content' }}
        pagination={pagination}
        onChange={handleTableChange}
      />
    </div>
  );
};

export default FundsTable;
