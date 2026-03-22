import React, { useState } from "react";
import { Form, Input, Button, Card, message } from "antd";
import { UserOutlined, LockOutlined } from "@ant-design/icons";
import { useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { motion } from "framer-motion";
import "./LoginPage.css";

type LoginFormValues = {
  username: string;
  password: string;
};

const LoginPage: React.FC = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const [loading, setLoading] = useState<boolean>(false);
  const [form] = Form.useForm();

  const onFinish = async (values: LoginFormValues) => {
    setLoading(true);
    try {
      // TODO: Replace with actual API call
      // Simulate API call
      await new Promise((resolve) => setTimeout(resolve, 1000));
      
      // For demo purposes, accept any credentials
      // In production, validate against your backend
      console.log("Login attempt:", values);
      
      // Store authentication token (mock)
      localStorage.setItem("authToken", "mock-token");
      localStorage.setItem("isAuthenticated", "true");
      
      message.success(t("login.success"));
      navigate("/funds");
    } catch (error) {
      message.error(t("login.error"));
      console.error("Login error:", error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <div className="bg-gradient" />
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
        className="login-content"
      >
        <Card className="login-card">
          <div className="login-header">
            <h1 className="login-title">{t("login.title")}</h1>
            <p className="login-subtitle">{t("login.subtitle")}</p>
          </div>
          
          <Form
            form={form}
            name="login"
            onFinish={onFinish}
            autoComplete="off"
            layout="vertical"
            size="large"
          >
            <Form.Item
              name="username"
              rules={[
                { required: true, message: t("login.usernameRequired") },
                { min: 3, message: t("login.usernameMinLength") },
              ]}
            >
              <Input
                prefix={<UserOutlined />}
                placeholder={t("login.usernamePlaceholder")}
              />
            </Form.Item>

            <Form.Item
              name="password"
              rules={[
                { required: true, message: t("login.passwordRequired") },
                { min: 6, message: t("login.passwordMinLength") },
              ]}
            >
              <Input.Password
                prefix={<LockOutlined />}
                placeholder={t("login.passwordPlaceholder")}
              />
            </Form.Item>

            <Form.Item>
              <Button
                type="primary"
                htmlType="submit"
                loading={loading}
                block
                className="login-button"
              >
                {t("login.submit")}
              </Button>
            </Form.Item>
          </Form>
        </Card>
      </motion.div>
    </div>
  );
};

export default LoginPage;

