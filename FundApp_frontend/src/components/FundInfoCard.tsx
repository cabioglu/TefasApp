import React from 'react';
import { Card, Row, Col, Typography } from 'antd';
import { useTranslation } from 'react-i18next';
import { StarIcon } from 'lucide-react';

const { Title, Text } = Typography;

interface IFundInfoCardProps {
  fund: {
    code: string;
    fundName: string;
    fundFounder?: {
      fundFounderName: string;
    };
    umbrellaFundType?: {
      umbrellaFundTypeName: string;
    };
    processedTefas: boolean;
  };
}

const FundInfoCard: React.FC<IFundInfoCardProps> = ({ fund }) => {
  const { t } = useTranslation();

  return (
    <>
      <div className="fund-title-section">
        <div className="fund-logo">
          <img 
            src="/images/fund-logo.png" 
            alt={fund.code} 
            onError={(e) => {
              const target = e.target as HTMLImageElement;
              target.src = `https://placehold.co/40x40/2466bf/white?text=${fund.code}`;
            }} 
          />
        </div>
        <div className="fund-title-info">
          <Title level={3} className="text-white">{fund.code} <StarIcon className="star-icon" size={18} /></Title>
          <Text type="secondary" className="text-white">{fund.fundName}</Text>
        </div>
      </div>
      
      <Card className="fund-info-card">
        <Row gutter={16}>
          <Col span={12}>
            <div className="fund-detail-info-item">
              <strong>{t("fundDetail.fundCode")}</strong> {fund.code}
            </div>
            <div className="fund-detail-info-item">
              <strong>{t("fundDetail.founder")}</strong> {fund.fundFounder?.fundFounderName || t("common.notAvailable")}
            </div>
          </Col>
          <Col span={12}>
            <div className="fund-detail-info-item">
              <strong>{t("fundDetail.umbrellaFundType")}</strong>{" "}
              {fund.umbrellaFundType?.umbrellaFundTypeName || t("common.notAvailable")}
            </div>
            <div className="fund-detail-info-item">
              <strong>{t("fundDetail.processed")}</strong>{" "}
              {fund.processedTefas ? (
                <span className="processed">{t("fundDetail.yes")}</span>
              ) : (
                <span className="not-processed">{t("fundDetail.no")}</span>
              )}
            </div>
          </Col>
        </Row>
      </Card>
    </>
  );
};

export default FundInfoCard; 